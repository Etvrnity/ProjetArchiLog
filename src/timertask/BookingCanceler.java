package timertask;

import server.documents.DocumentReservable;

import java.util.TimerTask;

public class BookingCanceler extends TimerTask {
    private final DocumentReservable doc;

    public BookingCanceler(DocumentReservable doc) {
        this.doc = doc;
    }

    @Override
    public void run() {
        doc.cancelBooking();
    }
}
