package server.documents.types;

import server.documents.DocumentReservable;

public class Book extends DocumentReservable {

    private final int number_pages;

    public Book(int numero, String title, int number_pages) {
        super(numero, title);
        this.number_pages = number_pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "numero=" + idDoc() +
                ", title='" + super.getTitle() + '\'' +
                ", number_pages=" + number_pages +
                ", abonne=" + super.getAbonne() +
                ", borrowed=" + super.isBorrowed() +
                '}';
    }
}
