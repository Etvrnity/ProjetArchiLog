package mediatheque.server;

import mediatheque.exceptions.RetourException;
import mediatheque.model.Document;
import mediatheque.model.Mediatheque;

import java.io.*;
import java.net.*;

/**
 * Serveur d'écoute des demandes de retour sur le port 2002.
 * Protocole : le client envoie "idDocument\n"
 * Le serveur répond par "OK\n" ou "ERREUR <message>\n".
 */
public class ServeurRetour implements Runnable {

    public static final int PORT = 2002;
    private final ServerSocket serverSocket;

    public ServeurRetour() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("[Retour] Serveur démarré sur le port " + PORT);
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("[Retour] Connexion de " + client.getInetAddress());
                new Thread(new TraitementRetour(client)).start();
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("[Retour] Erreur accept : " + e.getMessage());
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Traitement d'une demande                                           //
    // ------------------------------------------------------------------ //

    private static class TraitementRetour implements Runnable {
        private final Socket socket;

        TraitementRetour(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)
            ) {
                String idDoc = in.readLine();
                if (idDoc == null) { return; }
                idDoc = idDoc.trim();

                Mediatheque mth = Mediatheque.getInstance();
                Document    doc = mth.getDocument(idDoc);

                if (doc == null) {
                    out.println("ERREUR Document « " + idDoc + " » introuvable.");
                    return;
                }

                try {
                    doc.retour();
                    out.println("OK Retour enregistré : « " + doc.idDoc() + " » est de nouveau disponible.");
                    System.out.println("[Retour] OK — " + doc.idDoc());
                } catch (RetourException e) {
                    out.println("ERREUR " + e.getMessage());
                    System.out.println("[Retour] REFUS — " + e.getMessage());
                }

            } catch (IOException e) {
                System.err.println("[Retour] Erreur traitement : " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
