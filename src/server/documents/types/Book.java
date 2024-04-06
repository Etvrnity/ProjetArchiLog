package server.documents.types;

import server.BDLink;
import server.documents.DocumentReservable;
import server.subscribers.Subscriber;

public class Book extends DocumentReservable {

    private final int number_pages;

    public Book(int numero, String title, int number_pages, Subscriber subscriber, boolean borrowed, BDLink bdLink) {
        super(numero,title,subscriber,borrowed, bdLink);
        this.number_pages = number_pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "numero=" + numero() +
                ", title='" + super.getTitle() + '\'' +
                ", number_pages=" + number_pages +
                ", subscriber=" + super.getSubscriber() +
                ", borrowed=" + super.isBorrowed() +
                '}';
    }
}
