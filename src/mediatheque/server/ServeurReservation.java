package mediatheque.server;

import mediatheque.exceptions.ReservationException;
import mediatheque.model.Abonne;
import mediatheque.model.Document;
import mediatheque.model.Mediatheque;

import java.io.*;
import java.net.*;

/**
 * Serveur d'écoute des demandes de réservation sur le port 2000.
 * Protocole : le client envoie "numeroAbonne;idDocument\n"
 * Le serveur répond par "OK\n" ou "ERREUR <message>\n".
 */
public class ServeurReservation implements Runnable {

    public static final int PORT = 2000;
    private final ServerSocket serverSocket;

    public ServeurReservation() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("[Réservation] Serveur démarré sur le port " + PORT);
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("[Réservation] Connexion de " + client.getInetAddress());
                new Thread(new TraitementReservation(client)).start();
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("[Réservation] Erreur accept : " + e.getMessage());
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Traitement d'une demande                                           //
    // ------------------------------------------------------------------ //

    private static class TraitementReservation implements Runnable {
        private final Socket socket;

        TraitementReservation(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)
            ) {
                String ligne = in.readLine();
                if (ligne == null) { return; }

                String[] parts = ligne.split(";");
                if (parts.length != 2) {
                    out.println("ERREUR Protocole invalide. Format attendu : numeroAbonne;idDocument");
                    return;
                }

                int    numAbonne;
                try {
                    numAbonne = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException e) {
                    out.println("ERREUR Numéro d'abonné invalide.");
                    return;
                }
                String idDoc = parts[1].trim();

                Mediatheque mth  = Mediatheque.getInstance();
                Abonne      ab   = mth.getAbonne(numAbonne);
                Document    doc  = mth.getDocument(idDoc);

                if (ab == null) {
                    out.println("ERREUR Abonné numéro " + numAbonne + " introuvable.");
                    return;
                }
                if (doc == null) {
                    out.println("ERREUR Document « " + idDoc + " » introuvable.");
                    return;
                }

                try {
                    doc.reservation(ab);
                    out.println("OK Réservation confirmée pour « " + doc.idDoc()
                        + " » au nom de " + ab.getNom()
                        + ". Vous avez 2h pour passer l'emprunter.");
                    System.out.println("[Réservation] OK — " + ab.getNom() + " / " + doc.idDoc());
                } catch (ReservationException e) {
                    out.println("ERREUR " + e.getMessage());
                    System.out.println("[Réservation] REFUS — " + e.getMessage());
                }

            } catch (IOException e) {
                System.err.println("[Réservation] Erreur traitement : " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
