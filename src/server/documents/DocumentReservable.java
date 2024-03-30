package server.documents;

import server.exceptions.EmpruntException;
import server.subscribers.Subscriber;
import timertask.BookingCanceler;

import java.util.Timer;

public abstract class DocumentReservable implements Document {

    private int numero;
    private String title;

    private Subscriber subscriber;
    private boolean borrowed;// == Emprunté
    private boolean booked;// == Réservé

    public DocumentReservable(int numero, String title, Subscriber subscriber, boolean borrowed){
        this.numero = numero;
        this.title = title;
        this.subscriber = subscriber;
        this.borrowed = borrowed;
        this.booked = false;
    }

    @Override
    public int numero() {
        return numero;
    }

    @Override
    public Subscriber emprunteur() {
        if(borrowed){
            return subscriber;
        }
        return null;
    }

    @Override
    public Subscriber reserveur() {
        if(!borrowed){
            return subscriber;
        }
        return null;
    }

    @Override
    public void reservationPour(Subscriber sub) throws EmpruntException {
        assert (!borrowed && subscriber == null);//TODO verif ca
        if (borrowed || booked) {
            throw new EmpruntException();
        }
        subscriber = sub;
        borrowed = true;

        long twoHours = 2*60*60*1000;
        Timer t = new Timer("Booking for sub nb " + sub.getNumber() + ", doc nb :" + this.numero);
        t.schedule(new BookingCanceler(this), twoHours);
    }

    @Override
    public void empruntPar(Subscriber ab) throws EmpruntException {
        //TODO
    }

    @Override
    public void retour() {
        //TODO
        subscriber = null;
    }

    public void cancelBooking(){
        this.subscriber = null;
        this.booked = false;
    }


    public String getTitle() {
        return title;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public boolean isBorrowed() {
        return borrowed;
    }
}
