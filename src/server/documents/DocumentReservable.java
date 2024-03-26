package server.documents;

import server.exceptions.EmpruntException;
import server.subscribers.Subscriber;

public abstract class DocumentReservable implements Document {

    @Override
    public int numero() {
        return 0;
    }

    @Override
    public Subscriber emprunteur() {
        return null;
    }

    @Override
    public Subscriber reserveur() {
        return null;
    }

    @Override
    public void reservationPour(Subscriber ab) throws EmpruntException {

    }

    @Override
    public void empruntPar(Subscriber ab) throws EmpruntException {

    }

    @Override
    public void retour() {

    }
}
