package clients.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReservation {
    private final static int PORT_RESERVATION = 2000;

    public void launch(String host) {
        Socket socket = null;
        try {
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket(host, PORT_RESERVATION);
            BufferedReader sin  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    sout = new PrintWriter(socket.getOutputStream(), true);

            System.out.println(sin.readLine());

            System.out.print(sin.readLine());
            sout.println(clavier.readLine());

            System.out.print(sin.readLine());
            sout.println(clavier.readLine());

            String line;
            while ((line = sin.readLine()) != null) {
                System.out.println(line);

                if (line.contains("(oui/non)")
                        || line.trim().endsWith(":")
                        || line.trim().endsWith(": ")) {
                    String rep = clavier.readLine();
                    sout.println(rep != null ? rep : "");
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
