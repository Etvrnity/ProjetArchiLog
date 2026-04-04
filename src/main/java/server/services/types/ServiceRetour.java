package server.services.types;

import server.documents.Document;
import server.documents.DocumentReservable;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.RetourException;
import server.services.GenericService;
import server.subscribers.Abonne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ServiceRetour extends GenericService {

    private static final long TWO_WEEKS_MS = 14L * 24 * 60 * 60 * 1000;

    public ServiceRetour(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service de retour de documents de la médiathèque");

            try {
                out.println("Numéro du document : ");
                String document = in.readLine();

                int documentNumber = Integer.parseInt(document);

                Document doc = super.getLibrary().findDocumentByID(documentNumber);

                // --- Certification BretteSoft Géronimo ---
                Abonne borrower = null;
                Date borrowDate = null;
                if (doc instanceof DocumentReservable dr) {
                    if (dr.isBorrowed()) {
                        borrower = dr.getAbonne();
                        borrowDate = dr.getBorrowDate();
                    }
                }
                doc.retour();
                out.println("Document restitué avec succès");

                // --- Certification BretteSoft© Géronimo ---
                if (borrower != null) {
                    if (borrowDate != null) {
                        long dureeMs = System.currentTimeMillis() - borrowDate.getTime();
                        if (dureeMs > TWO_WEEKS_MS) {
                            borrower.ban();
                            out.println("Avertissement : retour en retard de plus de 2 semaines !");
                            out.println(borrower.getName() + " est banni de la médiathèque pour 1 mois.");
                        }
                    }
                    out.println("Le document est-il endommagé ? (oui/non) : ");
                    String reponse = in.readLine();
                    if (reponse != null && reponse.trim().equalsIgnoreCase("oui")) {
                        borrower.ban();
                        out.println("Avertissement : dégradation de document constatée !");
                        out.println(borrower.getName() + " est banni de la médiathèque pour 1 mois.");
                    }
                }

            } catch (NumberFormatException nbE) {
                out.println("Erreur : merci d'entrer un nombre");
            } catch (DocumentNotFoundException | RetourException e) {
                out.println(e.getMessage());
            }
            super.getClientSocket().close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getLocalizedMessage());
        }
    }
}
