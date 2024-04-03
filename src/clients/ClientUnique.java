package clients;

import clients.types.ClientBooking;
import clients.types.ClientBorrow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientUnique {
    private final static String HOST = "localhost";

    public static void main(String[] args) {
        // Cree le stream pour lire du texte à partir du clavier (on pourrait aussi utiliser Scanner)
        BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Quel service désirez-vous ? (réservation ou emprunt) : ");
            String service = clavier.readLine();
            if(service.equals("réservation")){
                new ClientBooking().launch(HOST, clavier);
            } else if (service.equals("emprunt")) {
                new ClientBorrow().launch(HOST, clavier);
            } else {
                System.err.println("Merci de rentrer un service parmi ceux proposés.");
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
