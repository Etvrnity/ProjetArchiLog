package server.documents.types;

import server.documents.DocumentReservable;
import server.exceptions.EmpruntException;
import server.exceptions.NotAdultEmpruntException;
import server.subscribers.Subscriber;

public class DVD extends DocumentReservable {

    private boolean adult;
    private static final int ADULT_AGE = 16;

    public DVD(int numero, String title, Subscriber subscriber, boolean borrowed, boolean adult) {
        super(numero, title, subscriber, borrowed);
        this.adult = adult;
    }

    @Override
    public void reservationPour(Subscriber ab) throws EmpruntException {
        if(adult && ab.getAge() < ADULT_AGE )
            throw new NotAdultEmpruntException();
        else
            super.reservationPour(ab);
    }

    @Override
    public void empruntPar(Subscriber ab) throws EmpruntException {
        if(adult && ab.getAge() < ADULT_AGE )
            throw new NotAdultEmpruntException();
        else
            super.empruntPar(ab);
    }

    @Override
    public String toString() {
        return "DVD{" +
                "numero=" + super.numero() +
                ", title='" + super.getTitle() + '\'' +
                ", subscriber=" + super.getSubscriber() +
                ", borrowed=" + super.isBorrowed() +
                ", adult=" + adult +
                '}';
    }
}
