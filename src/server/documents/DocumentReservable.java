package server.documents;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import server.BDLink;
import server.exceptions.DocumentReservedEmpruntException;
import server.exceptions.EmpruntException;
import server.subscribers.Subscriber;
import server.timertasks.ReservationCanceler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

public abstract class DocumentReservable implements Document {

    public static final long TWO_HOURS = 2 * 60 * 60 * 1000;

    private final int numero;
    private final String title;
    private Subscriber subscriber;
    private boolean borrowed;
    private boolean reserved;
    private Timer timer;
    private Date HourInTwoHours;
    private BDLink bdLink;

    public DocumentReservable(int numero, String title, Subscriber subscriber, boolean borrowed, BDLink bdLink){
        this.numero = numero;
        this.title = title;
        this.subscriber = subscriber;
        this.borrowed = borrowed;
        this.reserved = false;
        this.bdLink = bdLink;
    }

    @Override
    public int numero() {
        return numero;
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

    public DocumentReservable getMe() {
        return this;
    }

    public Timer getTimer() {
        return timer;
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
     * reservationPour == reservationFor
     * Precondition : not borrowed and subscriber set to null
     * @param ab subscriber willing to book
     * @throws EmpruntException
     */
    @Override
    public void reservationPour(Subscriber ab) throws EmpruntException {
        synchronized (this) {
            if (borrowed || reserved) {
                throw new EmpruntException(true);
            }
            subscriber = ab;
            reserved = true;
            HourInTwoHours = new Date(System.currentTimeMillis() + TWO_HOURS);
            timer = new Timer("Reservation for subscriber n°" + ab.getNumber() + ", document n°" + this.numero);
            timer.schedule(new ReservationCanceler(this), TWO_HOURS);
        }
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
        synchronized (this) {
            if (borrowed) {
                throw new EmpruntException(false);
            } else if (!reserved && subscriber == null) {
                borrowed = true;
                subscriber = ab;
                this.bdLink.addBorrowToBD(ab.getNumber(), this.numero);
            } else if (reserved && (ab.getNumber() == subscriber.getNumber())) {
                borrowed = true;
                this.bdLink.addBorrowToBD(ab.getNumber(), this.numero);
            } else if (reserved && (ab.getNumber() != subscriber.getNumber())) {
                throw new DocumentReservedEmpruntException(this.getHourEnd());
            } else {
                throw new EmpruntException(false);
            }
        }
    }

    @Override
    public void retour() {
        synchronized (this) {
            if(borrowed || reserved){
                // cette situation est anormale, mais le prototype de retour()
                // dans l'interface Document ne permet pas de lever une exception
            }
            borrowed = false;
            reserved = false;
            subscriber = null;
            this.bdLink.removeBorrowFromBD(this.numero);
        }
    }

    public void cancelReservation(){
        synchronized (this) {
            this.subscriber = null;
            this.reserved = false;
            sendMail();
        }
    }

    public void sendMail() {

        Dotenv dotenv = Dotenv.load();
        String from = dotenv.get("EMAIL_FROM");
        String to = dotenv.get("EMAIL_TO");
        String password = dotenv.get("PASSWORD_MAIL");

        Properties props = new Properties();
        props.put("mail.smtp.host", dotenv.get("EMAIL_HOST"));
        props.put("mail.smtp.port", dotenv.get("EMAIL_PORT"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Expiration de la réservation");
            message.setText("La réservation du document n°" + this.numero + " est arrivé à expiration.");

            Transport.send(message);

        } catch (MessagingException e) {
            System.err.println("Erreur + " + e.getLocalizedMessage());
        }
    }
}
