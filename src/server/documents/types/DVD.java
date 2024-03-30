package server.documents.types;

import server.documents.DocumentReservable;
import server.subscribers.Subscriber;

public class DVD extends DocumentReservable {
    private boolean adult;

    public DVD(int numero, String title, Subscriber subscriber, boolean borrowed, boolean adult) {
        super(numero, title, subscriber, borrowed);
        this.adult = adult;
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
