package clients;

import clients.types.ClientReservation;
import clients.types.ClientBorrow;

public class ClientUnique {
    private final static String HOST = "localhost";

    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("Merci de lancer le programme en précisant le service demandé (réservation/emprunt/retour)");
            return;
        }
        String service = args[0];
        if(service.equals("réservation")){
            new ClientReservation().launch(HOST);
        } else if (service.equals("emprunt") || service.equals("retour")) {
            new ClientBorrow().launch(HOST);
        } else {
            System.err.println("Merci de lancer le programme avec un service reconnu (réservation/emprunt/retour)");
        }
    }
}
