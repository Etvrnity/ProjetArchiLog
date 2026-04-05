package server.services.types;

import server.documents.Document;
import server.documents.DocumentReservable;
import server.exceptions.BannedSubscriberException;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.ReservationException;
import server.exceptions.SubscriberNotFoundException;
import server.services.GenericService;
import server.subscribers.Abonne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceReservation extends GenericService {

    private static final long GRAND_CHAMAN_THRESHOLD_MS = 60_000L;

    public ServiceReservation(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader in  = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter    out = new PrintWriter(super.getSocket().getOutputStream(), true);
            out.println("Bonjour, bienvenue sur le service de réservation de documents de la médiathèque");

            try {
                out.println("Numéro d'abonné : ");
                String abonne = in.readLine();

                out.println("Numéro du document : ");
                String document = in.readLine();

                int abonneNumber   = Integer.parseInt(abonne);
                int documentNumber = Integer.parseInt(document);

                Document doc = super.getLibrary().findDocumentByID(documentNumber);
                Abonne   ab  = super.getLibrary().findAbonneByID(abonneNumber);

                if (ab.isBanned()) {
                    throw new BannedSubscriberException(ab.getBannedMessage());
                }

                if (doc instanceof DocumentReservable docRes) {
                    grandChamanWait(docRes, out);
                }

                try {
                    doc.reservation(ab);
                    out.println("Document réservé avec succès jusqu'à " + ((DocumentReservable) doc).getHourEnd());

                } catch (ReservationException e) {
                    boolean docEmprunte = (doc instanceof DocumentReservable dr) && dr.isBorrowed();
                    if (docEmprunte) {
                        out.println("Pas de chance : le document a été emprunté pendant votre attente. " +
                                "Vous avez bénéficié d'un concert céleste gratuit. " +
                                "Il aurait fallu faire une offrande plus importante au grand chaman.");
                    } else {
                        out.println(e.getMessage());
                    }

                    if (doc instanceof DocumentReservable docRes
                            && (docRes.isReserved() || docRes.isBorrowed())) {
                        sittingBullAlert(docRes, in, out);
                    }
                }

            } catch (NumberFormatException e) {
                out.println("Erreur : merci d'entrer un nombre");
            } catch (SubscriberNotFoundException | DocumentNotFoundException e) {
                out.println(e.getMessage());
            } catch (BannedSubscriberException e) {
                out.println(e.getMessage());
            }

            super.getClientSocket().close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getLocalizedMessage());
        }
    }

    private void grandChamanWait(DocumentReservable docRes, PrintWriter out) {
        synchronized (docRes) {
            long remaining = docRes.getRemainingMs();
            if (docRes.isReserved() && remaining > 0 && remaining <= GRAND_CHAMAN_THRESHOLD_MS) {
                out.println("♪ Le document sera disponible dans moins d'une minute, " +
                        "une musique céleste vous accompagne pendant l'attente... ♪");
                try {
                    docRes.wait(remaining + 1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                out.println("♪ La musique s'arrête. Tentative de réservation en cours... ♪");
            }
        }
    }

    private void sittingBullAlert(DocumentReservable docRes, BufferedReader in, PrintWriter out)
            throws IOException {
        out.println("Souhaitez-vous être alerté par email lors du retour de ce document ? (oui/non)");
        String rep = in.readLine();
        if (rep != null && rep.trim().equalsIgnoreCase("oui")) {
            out.println("Votre adresse email : ");
            String email = in.readLine();
            if (email != null && !email.isBlank()) {
                docRes.addWaitingEmail(email.trim());
                out.println("Vous serez notifié par email dès que \"" + docRes.getTitle() + "\" sera disponible.");
            } else {
                out.println("Adresse invalide, alerte non enregistrée.");
            }
        }
    }
}