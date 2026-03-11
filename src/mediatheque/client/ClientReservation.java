package mediatheque.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Client de réservation à distance (installé chez l'abonné).
 * Usage : java ClientReservation [host [port]]
 * Par défaut : host=localhost, port=2000
 */
public class ClientReservation {

    public static void main(String[] args) throws IOException {
        String host = (args.length > 0) ? args[0] : "localhost";
        int    port = (args.length > 1) ? Integer.parseInt(args[1]) : 2000;

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Client Réservation ===");
        System.out.print("Votre numéro d'abonné : ");
        String numAbonne = sc.nextLine().trim();

        System.out.print("Identifiant du document à réserver : ");
        String idDoc = sc.nextLine().trim();

        try (
            Socket     socket = new Socket(host, port);
            PrintWriter out   = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Envoi de la requête
            out.println(numAbonne + ";" + idDoc);

            // Lecture de la réponse
            String reponse = in.readLine();
            if (reponse == null) {
                System.out.println("Pas de réponse du serveur.");
            } else if (reponse.startsWith("OK")) {
                System.out.println("✔ " + reponse.substring(3));
            } else {
                System.out.println("✘ " + reponse.substring(7));
            }
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au serveur " + host + ":" + port);
        }
    }
}
