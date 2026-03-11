package server.documents;

import server.exceptions.EmpruntException;
import server.exceptions.ReservationException;
import server.exceptions.RetourException;
import server.subscribers.Abonne;

public interface Document {
    String idDoc();

    // exception si déjà réservé ou emprunté
    void reservation(Abonne ab) throws ReservationException;

    // exception si réservé pour un autre abonné ou déjà emprunté
    void emprunt(Abonne ab) throws EmpruntException;

    // sert au retour d'un document ou à l'annulation d'une réservation
    void retour() throws RetourException;
}
