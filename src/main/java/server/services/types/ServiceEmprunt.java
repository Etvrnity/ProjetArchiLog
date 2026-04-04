package server.services.types;

import server.documents.Document;
import server.exceptions.BannedSubscriberException;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.EmpruntException;
import server.exceptions.SubscriberNotFoundException;
import server.services.GenericService;
import server.subscribers.Abonne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceEmprunt extends GenericService {
    public ServiceEmprunt(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service d'emprunt de documents de la médiathèque");

            try {
                out.println("Numéro d'abonné : ");
                String abonne = in.readLine();

                out.println("Numéro du document : ");
                String document = in.readLine();

                int abonneNumber = Integer.parseInt(abonne);
                int documentNumber = Integer.parseInt(document);

                Document doc = super.getLibrary().findDocumentByID(documentNumber);
                Abonne ab = super.getLibrary().findAbonneByID(abonneNumber);

                // --- Certification BretteSoft Géronimo ---
                if (ab.isBanned()) {
                    throw new BannedSubscriberException(ab.getBannedMessage());
                }

                doc.emprunt(ab);
                out.println("Document emprunté avec succès");

            } catch (NumberFormatException nbE) {
                out.println("Erreur : merci d'entrer un nombre");
            } catch (SubscriberNotFoundException | DocumentNotFoundException | EmpruntException e) {
                out.println(e.getMessage());
            }
            super.getClientSocket().close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getLocalizedMessage());
        }
    }
}
