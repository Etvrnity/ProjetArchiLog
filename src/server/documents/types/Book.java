package server.documents.types;

import server.documents.Document;
import server.documents.DocumentReservable;
import server.exceptions.EmpruntException;
import server.subscribers.Subscriber;
import timertask.BookingCanceler;

import java.time.LocalDateTime;
import java.util.Timer;

public class Book extends DocumentReservable {

    private int numero;
    private String title;
    private int number_pages;
    private Subscriber subscriber;
    private boolean borrowed;
    private boolean booked;

    public Book(int numero, String title, int number_pages, Subscriber subscriber, boolean borrowed) {
        this.numero = numero;
        this.title = title;
        this.number_pages = number_pages;
        this.subscriber = subscriber;
        this.borrowed = borrowed;
    }


    @Override
    public int numero() {
        return  numero;
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
    public void empruntPar(Subscriber sub) throws EmpruntException {

    }

    @Override
    public void retour() {

    }

    @Override
    public String toString() {
        return "Book{" +
                "numero=" + numero +
                ", title='" + title + '\'' +
                ", number_pages=" + number_pages +
                ", subscriber=" + subscriber +
                ", borrowed=" + borrowed +
                '}';
    }

    public void cancelBooking() {
        this.subscriber = null;
        this.booked = false;
    }
}
