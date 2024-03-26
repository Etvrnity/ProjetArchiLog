package timertask;

import server.documents.types.*;

import java.util.TimerTask;

public class BookingCanceler extends TimerTask {
    private DVD dvd;
    private Book book;

    public BookingCanceler(DVD dvd) {
        this.dvd = dvd;
    }

    public BookingCanceler(Book book){
        this.book = book;
    }

    @Override
    public void run() {
        if(dvd != null){
            dvd.cancelBooking();
        }
        else
            book.cancelBooking();
    }
}
