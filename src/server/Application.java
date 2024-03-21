package server;

import server.documents.Document;
import server.services.ServiceBooking;
import server.services.ServiceBorrow;
import server.subscribers.Subscriber;

import java.io.IOException;
import java.util.ArrayList;

public class Application {
    private final static int PORT_BOOKING = 3000;
    private static final int PORT_BORROW = 4000;

    public static void main(String[] args) {
        ArrayList<Document> documents = new ArrayList<>();
        ArrayList<Subscriber> subscribers = new ArrayList<>();
        BDLink bdLink = new BDLink();
        bdLink.init_local_from_bd(subscribers,documents);

        for (Document d : documents){
            System.out.println(d);
        }
        for (Subscriber s : subscribers){
            System.out.println(s);
        }

        try {
            new Thread(new Server(ServiceBooking.class, PORT_BOOKING)).start();
            System.out.println("Serveur lance avec succès sur le port " + PORT_BOOKING);
            new Thread(new Server(ServiceBorrow.class, PORT_BORROW)).start();
            System.out.println("Serveur lance avec succès sur le port " + PORT_BORROW);
        } catch (IOException e) {
            System.err.println("Pb lors de la création du serveur : " +  e);
        }
    }
}
