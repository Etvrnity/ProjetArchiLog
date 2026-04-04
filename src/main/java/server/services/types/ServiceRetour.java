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
            BufferedReader in  = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter    out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service de retour de documents de la médiathèque");

            try {
                out.println("Numéro du document : ");
                String document = in.readLine();

                int documentNumber = Integer.parseInt(document);
                Document doc = super.getLibrary().findDocumentByID(documentNumber);

                Abonne borrower  = null;
                Date   borrowDate = null;

                if (doc instanceof DocumentReservable dr && dr.isBorrowed()) {
                    borrower  = dr.getAbonne();
                    borrowDate = dr.getBorrowDate();

                    out.println("Le document est-il endommagé ? (oui/non) : ");
                    String reponse = in.readLine();
                    boolean damaged = reponse != null && reponse.trim().equalsIgnoreCase("oui");

                    doc.retour();
                    out.println("Document restitué avec succès");

                    boolean lateReturn = borrowDate != null &&
                            (System.currentTimeMillis() - borrowDate.getTime()) > TWO_WEEKS_MS;

                    if (damaged || lateReturn) {
                        borrower.ban();
                        if (damaged && lateReturn) {
                            out.println("Avertissement : retard de plus de 2 semaines ET dégradation constatée !");
                        } else if (damaged) {
                            out.println("Avertissement : dégradation de document constatée !");
                        } else {
                            out.println("Avertissement : retour en retard de plus de 2 semaines !");
                        }
                        out.println(borrower.getName() + " est banni de la médiathèque pour 1 mois.");
                    }

                } else {
                    doc.retour();
                    out.println("Document restitué avec succès");
                }

            } catch (NumberFormatException e) {
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
