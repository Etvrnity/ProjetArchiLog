package server.documents.types;

import server.documents.DocumentReservable;
import server.exceptions.EmpruntException;
import server.exceptions.NotAdultEmpruntException;
import server.exceptions.ReservationException;
import server.subscribers.Abonne;

public class DVD extends DocumentReservable {

    private final boolean adult;
    private static final int ADULT_AGE = 16;

    public DVD(int numero, String title, boolean adult) {
        super(numero, title);
        this.adult = adult;
    }

    @Override
    public void reservation(Abonne ab) throws ReservationException {
        if (adult && ab.getAge() < ADULT_AGE)
            throw new ReservationException("Erreur : vous n'avez pas l'âge pour réserver ce DVD");
        super.reservation(ab);
    }

    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        if (adult && ab.getAge() < ADULT_AGE)
            throw new NotAdultEmpruntException();
        super.emprunt(ab);
    }

    @Override
    public String toString() {
        return "DVD{" +
                "numero=" + super.idDoc() +
                ", title='" + super.getTitle() + '\'' +
                ", abonne=" + super.getAbonne() +
                ", borrowed=" + super.isBorrowed() +
                ", adult=" + adult +
                '}';
    }
}
