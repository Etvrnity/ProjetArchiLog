package server.services.types;

import server.documents.Document;
import server.documents.DocumentReservable;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.EmpruntException;
import server.exceptions.SubscriberNotFoundException;
import server.services.GenericService;
import server.subscribers.Subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceReservation extends GenericService {
    public ServiceReservation(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service de réservation de documents de la médiathèque");

            try {
                out.println("Numéro d'abonné : ");
                String subscriber = in.readLine();

                out.println("Numéro du document : ");
                String document = in.readLine();

                int subscriberNumber = Integer.parseInt(subscriber);
                int documentNumber = Integer.parseInt(document);

                Document doc = super.getLibrary().findDocumentFromID(documentNumber);
                Subscriber sub = super.getLibrary().findSubsciberFromID(subscriberNumber);

                doc.reservationPour(sub);
                out.println("Document réservé avec succès jusqu'à " + ((DocumentReservable) doc).getHourEnd());

            } catch (NumberFormatException nbE){
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
