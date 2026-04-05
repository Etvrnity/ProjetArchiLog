package server.documents;

import server.exceptions.EmpruntException;
import server.exceptions.ReservationException;
import server.exceptions.RetourException;
import server.subscribers.Abonne;

public interface Document {
    String idDoc();

    void reservation(Abonne ab) throws ReservationException;

    void emprunt(Abonne ab) throws EmpruntException;

    void retour() throws RetourException;
}
