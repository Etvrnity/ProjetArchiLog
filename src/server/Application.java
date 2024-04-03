package server;

import server.documents.Document;
import server.services.ServiceBooking;
import server.services.ServiceBorrow;
import server.subscribers.Subscriber;

import java.io.IOException;

public class Application {
    private final static int PORT_BOOKING = 3000;
    private static final int PORT_BORROW = 4000;
    private static final boolean DEBUG = true;//TODO
    private static Library library;

    public static void main(String[] args) {
        library = new Library();

        BDLink bdLink = new BDLink();
        bdLink.init_local_from_bd(library);

        if (DEBUG) {
            for (Subscriber s : library.getSubscribers()){
                System.out.println(s);
            }
            for (Document d : library.getDocuments()){
                System.out.println(d);
            }
        }


        try {
            new Thread(new Server(ServiceBooking.class, library, PORT_BOOKING)).start();
            System.out.println("Serveur lance avec succès sur le port " + PORT_BOOKING);
            new Thread(new Server(ServiceBorrow.class, library, PORT_BORROW)).start();
            System.out.println("Serveur lance avec succès sur le port " + PORT_BORROW);
        } catch (IOException e) {
            System.err.println("Pb lors de la création du serveur : " +  e);
        }
    }
}