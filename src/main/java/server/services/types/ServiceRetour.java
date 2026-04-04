package server.services.types;

import server.documents.Document;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.RetourException;
import server.services.GenericService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceRetour extends GenericService {
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

                doc.retour();
                out.println("Document restitué avec succès");

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
