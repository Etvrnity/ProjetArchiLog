package clients.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReservation {
    private final static int PORT_RESERVATION = 3000;
    public void launch(String host) {
        Socket socket = null;
        try {
            // Cree le stream pour lire du texte à partir du clavier (on pourrait aussi utiliser Scanner)
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            // Cree une socket pour communiquer avec le service se trouvant sur la machine host au port PORT
            socket = new Socket(host, PORT_RESERVATION);
            // Cree les streams pour lire et ecrire du texte dans cette socket
            BufferedReader sin = new BufferedReader (new InputStreamReader(socket.getInputStream ( )));
            PrintWriter sout = new PrintWriter (socket.getOutputStream ( ), true);

            String line;
            line = sin.readLine();
            System.out.println(line);

            System.out.print(sin.readLine());
            String nbSub = clavier.readLine();
            sout.println(nbSub);
            System.out.print(sin.readLine());
            sout.println(clavier.readLine());

            System.out.print(sin.readLine());
        }
        catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        // Refermer dans tous les cas la socket
        try {
            if (socket != null) {
                socket.close();
            }
        }
        catch (IOException ignored) { }
    }
}
