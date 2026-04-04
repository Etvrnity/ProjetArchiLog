package clients.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client de retour.
 * Lit les lignes du serveur de façon dynamique pour gérer :
 * - Géronimo : question sur l'état du document (dégradé ou non)
 */
public class ClientRetour {
    private static final int PORT_RETOUR = 2002;

    public void launch(String host) {
        Socket socket = null;
        try {
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket(host, PORT_RETOUR);
            BufferedReader sin  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    sout = new PrintWriter(socket.getOutputStream(), true);

            System.out.println(sin.readLine());

            System.out.print(sin.readLine());
            sout.println(clavier.readLine());

            String line;
            while ((line = sin.readLine()) != null) {
                System.out.println(line);
                if (line.contains("(oui/non)") || line.trim().endsWith(": ")) {
                    sout.println(clavier.readLine());
                }
            }

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
