package server;

import server.documents.Document;
import server.services.types.ServiceReservation;
import server.services.types.ServiceBorrow;
import server.subscribers.Subscriber;

import java.io.IOException;

public class Application {
    private final static int PORT_RESERVATION = 3000;
    private static final int PORT_BORROW = 4000;
    private static final boolean DEBUG = false;
    private static Library library;

    public static void main(String[] args) {
        library = new Library();
        library.init_local_from_bd();

        if (DEBUG) {
            System.out.println("=== initialisation de la base locale ===");
            System.out.println("== Utilisateurs ==");
            for (Subscriber s : library.getSubscribers()){
                System.out.println(s);
            }
            System.out.println("== Documents ==");
            for (Document d : library.getDocuments()){
                System.out.println(d);
            }
            System.out.println("======================");
        }


        try {
            new Thread(new Server(ServiceReservation.class, library, PORT_RESERVATION)).start();
            System.out.println("Serveur lancé avec succès sur le port " + PORT_RESERVATION);
            new Thread(new Server(ServiceBorrow.class, library, PORT_BORROW)).start();
            System.out.println("Serveur lancé avec succès sur le port " + PORT_BORROW);
        } catch (IOException e) {
            System.err.println("Pb lors de la création du serveur : " +  e);
        }
    }
}