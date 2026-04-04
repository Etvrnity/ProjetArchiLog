package server.timertasks;

import server.documents.DocumentReservable;

import java.util.TimerTask;

public class ReservationCanceler extends TimerTask {
    private final DocumentReservable doc;

    public ReservationCanceler(DocumentReservable doc) {
        this.doc = doc;
    }

    @Override
    public void run() {
        doc.cancelReservation();
    }
}
