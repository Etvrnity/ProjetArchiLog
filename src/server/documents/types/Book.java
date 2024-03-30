package server.documents.types;

import server.documents.DocumentReservable;
import server.subscribers.Subscriber;

public class Book extends DocumentReservable {

    private int number_pages;

    public Book(int numero, String title, int number_pages, Subscriber subscriber, boolean borrowed) {
        super(numero,title,subscriber,borrowed);
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
