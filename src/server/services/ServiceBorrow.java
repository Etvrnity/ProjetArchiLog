package server.services;

import server.GenericService;
import server.Library;
import server.documents.Document;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.EmpruntException;
import server.exceptions.SubscriberNotFoundException;
import server.subscribers.Subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceBorrow extends GenericService {
    public ServiceBorrow(Socket s) {
        super(s);
    }
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service des emprunts et retours de la médiathèque");

            try {
                out.println("Numéro d'abonné : ");
                String subscriber = in.readLine();

                out.println("Numéro du document : ");
                String document = in.readLine();

                int subscriberNumber = Integer.parseInt(subscriber);
                int documentNumber = Integer.parseInt(document);

                Document doc = super.getLibrary().findDocumentFromID(documentNumber);
                Subscriber sub = super.getLibrary().findSubsciberFromID(subscriberNumber);

                doc.empruntPar(sub);
                out.println("Document emprunté avec succès");

            } catch (NumberFormatException nbE){
                out.println("Erreur : merci d'entrer un nombre.");
            } catch (DocumentNotFoundException e) {
                out.println("Erreur : ce document n'existe pas");
            } catch (SubscriberNotFoundException e) {
                out.println("Erreur : cet utilisateur n'existe pas");
            } catch (EmpruntException e) {
                out.println("Erreur : emprunt impossible");
            }
            super.getClientSocket().close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getLocalizedMessage());
        }
    }
}
