package mediatheque.server;

import mediatheque.exceptions.EmpruntException;
import mediatheque.model.Abonne;
import mediatheque.model.Document;
import mediatheque.model.Mediatheque;

import java.io.*;
import java.net.*;

/**
 * Serveur d'écoute des demandes d'emprunt sur le port 2001.
 * Protocole : le client envoie "numeroAbonne;idDocument\n"
 * Le serveur répond par "OK\n" ou "ERREUR <message>\n".
 */
public class ServeurEmprunt implements Runnable {

    public static final int PORT = 2001;
    private final ServerSocket serverSocket;

    public ServeurEmprunt() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("[Emprunt] Serveur démarré sur le port " + PORT);
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("[Emprunt] Connexion de " + client.getInetAddress());
                new Thread(new TraitementEmprunt(client)).start();
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("[Emprunt] Erreur accept : " + e.getMessage());
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Traitement d'une demande                                           //
    // ------------------------------------------------------------------ //

    private static class TraitementEmprunt implements Runnable {
        private final Socket socket;

        TraitementEmprunt(Socket socket) {
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

                int numAbonne;
                try {
                    numAbonne = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException e) {
                    out.println("ERREUR Numéro d'abonné invalide.");
                    return;
                }
                String idDoc = parts[1].trim();

                Mediatheque mth = Mediatheque.getInstance();
                Abonne      ab  = mth.getAbonne(numAbonne);
                Document    doc = mth.getDocument(idDoc);

                if (ab == null) {
                    out.println("ERREUR Abonné numéro " + numAbonne + " introuvable.");
                    return;
                }
                if (doc == null) {
                    out.println("ERREUR Document « " + idDoc + " » introuvable.");
                    return;
                }

                try {
                    doc.emprunt(ab);
                    out.println("OK Emprunt enregistré : « " + doc.idDoc()
                        + " » emprunté par " + ab.getNom() + ".");
                    System.out.println("[Emprunt] OK — " + ab.getNom() + " / " + doc.idDoc());
                } catch (EmpruntException e) {
                    out.println("ERREUR " + e.getMessage());
                    System.out.println("[Emprunt] REFUS — " + e.getMessage());
                }

            } catch (IOException e) {
                System.err.println("[Emprunt] Erreur traitement : " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
