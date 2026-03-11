package mediatheque.server;

import mediatheque.model.Mediatheque;

import java.io.IOException;

/**
 * Point d'entrée de l'application serveur.
 * Lance les trois serveurs d'écoute sur les ports 2000, 2001 et 2002.
 */
public class AppServeur {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Médiathèque — Serveur principal");
        System.out.println("========================================");

        // Initialisation du catalogue (objets créés en dur)
        Mediatheque.getInstance().afficherCatalogue();

        try {
            ServeurReservation sr = new ServeurReservation();
            ServeurEmprunt     se = new ServeurEmprunt();
            ServeurRetour      st = new ServeurRetour();

            Thread tReservation = new Thread(sr, "Serveur-Reservation");
            Thread tEmprunt     = new Thread(se, "Serveur-Emprunt");
            Thread tRetour      = new Thread(st, "Serveur-Retour");

            tReservation.setDaemon(false);
            tEmprunt.setDaemon(false);
            tRetour.setDaemon(false);

            tReservation.start();
            tEmprunt.start();
            tRetour.start();

            System.out.println("\nServeurs démarrés. En attente de connexions...");
            System.out.println("  Réservation : port " + ServeurReservation.PORT);
            System.out.println("  Emprunt     : port " + ServeurEmprunt.PORT);
            System.out.println("  Retour      : port " + ServeurRetour.PORT);

        } catch (IOException e) {
            System.err.println("Impossible de démarrer un serveur : " + e.getMessage());
            System.exit(1);
        }
    }
}
