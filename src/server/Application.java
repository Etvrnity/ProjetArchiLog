package server;

import server.documents.Document;
import server.services.types.ServiceReservation;
import server.services.types.ServiceEmprunt;
import server.services.types.ServiceRetour;
import server.subscribers.Abonne;

import java.io.IOException;

public class Application {
    private final static int PORT_RESERVATION = 2000;
    private static final int PORT_EMPRUNT = 2001;
    private static final int PORT_RETOUR = 2002;
    private static final boolean DEBUG = false;
    private static Library library;

    public static void main(String[] args) {
        library = new Library();
        library.initData();

        if (DEBUG) {
            System.out.println("=== initialisation de la base locale ===");
            System.out.println("== Abonnés ==");
            for (Abonne a : library.getAbonnes().values()) {
                System.out.println(a);
            }
            System.out.println("== Documents ==");
            for (Document d : library.getDocuments().values()) {
                System.out.println(d);
            }
            System.out.println("======================");
        }

        try {
            new Thread(new Server(ServiceReservation.class, library, PORT_RESERVATION)).start();
            System.out.println("Serveur de réservation lancé avec succès sur le port " + PORT_RESERVATION);
            new Thread(new Server(ServiceEmprunt.class, library, PORT_EMPRUNT)).start();
            System.out.println("Serveur d'emprunt lancé avec succès sur le port " + PORT_EMPRUNT);
            new Thread(new Server(ServiceRetour.class, library, PORT_RETOUR)).start();
            System.out.println("Serveur de retour lancé avec succès sur le port " + PORT_RETOUR);
        } catch (IOException e) {
            System.err.println("Pb lors de la création du serveur : " + e);
        }
    }
}