package server.services;

import server.GenericService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceBooking extends GenericService {
    public ServiceBooking(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service de réservation de documents de la médiathèque");

            try {
                out.println("Numéro d'abonné : ");
                int subscriberNumber = Integer.parseInt(in.readLine());

                out.println("Numéro du document : ");
                int documentNumber = Integer.parseInt(in.readLine());

                //TODO reservation



            } catch (NumberFormatException nbE){
                out.println("Erreur : merci d'entrer un nombre.");//TODO voir si on garde ca
                super.getClientSocket().close();
                return;
            }


        } catch (IOException e) {
            //TODO
        }
    }
}
