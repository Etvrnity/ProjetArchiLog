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

            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket(host, PORT_EMPRUNT);
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

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
