package server.documents.types;

import server.documents.Document;
import server.subscribers.Subscriber;

public class Book implements Document {

    private int numero;
    private String titre;
    private Subscriber subscriber;
    private boolean borrowed;

    public Book(int numero, String title, int number_pages, Subscriber subscriber, boolean borrowed) {
        this.numero = numero;
        this.title = title;
        this.number_pages = number_pages;
        this.subscriber = subscriber;
        this.borrowed = borrowed;
    }


    @Override
    public int numero() {
        return 0;
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
    public void reservationPour(Subscriber sub) {

    }

    @Override
    public void empruntPar(Subscriber sub) {

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
}
