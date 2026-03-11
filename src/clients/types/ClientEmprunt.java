package clients.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientEmprunt {
    private static final int PORT_EMPRUNT = 2001;

    public void launch(String host) {
        Socket socket = null;
        try {
            // Cree le stream pour lire du texte à partir du clavier
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            // Cree une socket pour communiquer avec le service d'emprunt
            socket = new Socket(host, PORT_EMPRUNT);
            // Cree les streams pour lire et ecrire du texte dans cette socket
            BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);

            System.out.println(sin.readLine());

            System.out.print(sin.readLine());
            sout.println(clavier.readLine());
            System.out.print(sin.readLine());
            sout.println(clavier.readLine());

            System.out.print(sin.readLine());
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        // Refermer dans tous les cas la socket
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
