package server.documents;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import server.exceptions.DocumentReservedEmpruntException;
import server.exceptions.EmpruntException;
import server.exceptions.ReservationException;
import server.exceptions.RetourException;
import server.subscribers.Abonne;
import server.timertasks.ReservationCanceler;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class DocumentReservable implements Document {
    public static final long TWO_HOURS = 2 * 60 * 60 * 1000;

    private final int numero;
    private final String title;
    private Abonne abonne;
    private boolean borrowed;
    private boolean reserved;
    private Timer timer;
    private Date HourInTwoHours;

    private Date borrowDate;

    private final List<String> waitingEmails = new ArrayList<>();

    public DocumentReservable(int numero, String title) {
        this.numero = numero;
        this.title = title;
        this.abonne = null;
        this.borrowed = false;
        this.reserved = false;
        this.borrowDate = null;
    }


    @Override
    public String idDoc() { return String.valueOf(numero); }

    public int getNumero()      { return numero; }
    public String getTitle()    { return title; }
    public Abonne getAbonne()   { return abonne; }
    public boolean isBorrowed() { return borrowed; }
    public boolean isReserved() { return reserved; }
    public Date getBorrowDate() { return borrowDate; }

    /** Millisecondes restantes avant expiration de la réservation en cours. */
    public long getRemainingMs() {
        if (HourInTwoHours == null) return Long.MAX_VALUE;
        return HourInTwoHours.getTime() - System.currentTimeMillis();
    }

    public String getHourEnd() {
        if (HourInTwoHours != null)
            return new SimpleDateFormat("HH:mm").format(HourInTwoHours);
        return "";
    }


    @Override
    public synchronized void reservation(Abonne ab) throws ReservationException {
        if (borrowed)
            throw new ReservationException("Erreur : ce document est déjà emprunté");
        if (reserved)
            throw new ReservationException("Erreur : ce document est déjà réservé jusqu'à " + getHourEnd());

        abonne = ab;
        reserved = true;
        HourInTwoHours = new Date(System.currentTimeMillis() + TWO_HOURS);
        timer = new Timer("Reservation for abonne n°" + ab.getNumber() + ", document n°" + numero);
        timer.schedule(new ReservationCanceler(this), TWO_HOURS);
    }

    @Override
    public synchronized void emprunt(Abonne ab) throws EmpruntException {
        if (borrowed) {
            throw new EmpruntException("Erreur : ce document est déjà emprunté");
        } else if (reserved && ab.getNumber() != abonne.getNumber()) {
            throw new DocumentReservedEmpruntException(getHourEnd());
        } else if (reserved && ab.getNumber() == abonne.getNumber()) {
            borrowed = true;
            reserved = false;
            borrowDate = new Date();
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            HourInTwoHours = null;
        } else if (!reserved && abonne == null) {
            borrowed = true;
            abonne = ab;
            borrowDate = new Date();
        } else {
            throw new EmpruntException("Erreur : emprunt impossible");
        }
    }

    @Override
    public synchronized void retour() throws RetourException {
        if (!borrowed && !reserved)
            throw new RetourException("Erreur : ce document n'est ni emprunté ni réservé");

        borrowed = false;
        reserved = false;
        abonne = null;
        borrowDate = null;
        HourInTwoHours = null;

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        notifyWaitingSubscribers();
    }

    public synchronized void cancelReservation() {
        abonne = null;
        reserved = false;
        HourInTwoHours = null;

        notifyAll();

        notifyWaitingSubscribers();
    }

    public synchronized void addWaitingEmail(String email) {
        if (!waitingEmails.contains(email))
            waitingEmails.add(email);
    }

    private void notifyWaitingSubscribers() {
        if (waitingEmails.isEmpty()) return;
        String subject = "Document disponible : " + title;
        String body = "Bonjour,\n\nLe document \"" + title + "\" (n°" + numero
                + ") est maintenant disponible à la médiathèque.\n\nCordialement,\nLa médiathèque";
        for (String email : new ArrayList<>(waitingEmails)) {
            sendMailTo(email, subject, body);
        }
        waitingEmails.clear();
    }


    public void sendMailTo(String to, String subject, String body) {
        try {
            Dotenv dotenv = Dotenv.load();
            final String from     = dotenv.get("EMAIL_FROM");
            final String password = dotenv.get("PASSWORD_MAIL");

            Properties props = new Properties();
            props.put("mail.smtp.host",            dotenv.get("EMAIL_HOST"));
            props.put("mail.smtp.port",            dotenv.get("EMAIL_PORT"));
            props.put("mail.smtp.auth",            "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("[Mail] Envoyé à " + to + " — " + subject);
        } catch (MessagingException e) {
            System.err.println("[Mail] Erreur d'envoi : " + e.getLocalizedMessage());
        }
    }

    public void sendMail() {
        try {
            Dotenv dotenv = Dotenv.load();
            sendMailTo(
                    dotenv.get("EMAIL_TO"),
                    "Expiration de la réservation",
                    "La réservation du document n°" + numero + " (\"" + title + "\") est arrivée à expiration."
            );
        } catch (Exception e) {
            System.err.println("[Mail] Fichier .env manquant ou incomplet : " + e.getLocalizedMessage());
        }
    }
}
