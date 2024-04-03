package server.documents;

import server.exceptions.EmpruntException;
import server.subscribers.Subscriber;
import timertask.BookingCanceler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;



public abstract class DocumentReservable implements Document {

    public static final long TWO_HOURS = 2 * 60 * 60 * 1000;

    private int numero;
    private String title;
    private Subscriber subscriber;
    private boolean borrowed;// == Emprunté
    private boolean booked;// == Réservé
    private Timer t;
    private Date HourInTwoHours;

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

    /**
     * reservationPour == bookingFor
     * Precondition : not borrowed and subscriber set to null
     * @param ab subscriber willing to book
     * @throws EmpruntException
     */
    @Override
    public void reservationPour(Subscriber ab) throws EmpruntException {
        if (borrowed || booked) {
            throw new EmpruntException();
        }
        subscriber = ab;
        booked = true;
        HourInTwoHours = new Date(System.currentTimeMillis() + TWO_HOURS);
        t = new Timer("Booking for sub nb " + ab.getNumber() + ", doc nb :" + this.numero);
        t.schedule(new BookingCanceler(this), TWO_HOURS);
    }

    public String getHourEnd(){
        if (HourInTwoHours != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(HourInTwoHours);
        } else {
            return "";
        }
    }

    /**
     * empruntPar == borrowedBy
     * @param ab subscriber willing to borrow
     * @throws EmpruntException
     */
    @Override
    public void empruntPar(Subscriber ab) throws EmpruntException {
        if(borrowed) {
            throw new EmpruntException();
        } else if(!booked && subscriber == null){
            borrowed = true;
            subscriber = ab;
        } else if(booked && (ab.getNumber()== subscriber.getNumber())) {
             borrowed = true;
        } else {
            throw new EmpruntException();
        }
    }

    @Override
    public void retour() {
        borrowed = false;
        booked = false;
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
