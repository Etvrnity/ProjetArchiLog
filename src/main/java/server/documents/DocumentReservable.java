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
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

public abstract class DocumentReservable implements Document {
    public static final long TWO_HOURS = 2 * 60 * 60 * 1000;
    private final int numero;
    private final String title;
    private Abonne abonne;
    private boolean borrowed;
    private boolean reserved;
    private Timer timer;
    private Date HourInTwoHours;

    public DocumentReservable(int numero, String title) {
        this.numero = numero;
        this.title = title;
        this.abonne = null;
        this.borrowed = false;
        this.reserved = false;
    }

    @Override
    public String idDoc() {
        return String.valueOf(numero);
    }

    public int getNumero() {
        return numero;
    }

    public String getTitle() {
        return title;
    }

    public Abonne getAbonne() {
        return abonne;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public Timer getTimer() {
        return timer;
    }

    /**
     * Réservation d'un document par un abonné.
     * Précondition : le document n'est ni emprunté ni réservé.
     *
     * @param ab abonné souhaitant réserver
     * @throws ReservationException si le document est déjà réservé ou emprunté
     */
    @Override
    public void reservation(Abonne ab) throws ReservationException {
        synchronized (this) {
            if (borrowed) {
                throw new ReservationException("Erreur : ce document est déjà emprunté");
            }
            if (reserved) {
                throw new ReservationException("Erreur : ce document est déjà réservé jusqu'à " + getHourEnd());
            }
            abonne = ab;
            reserved = true;
            HourInTwoHours = new Date(System.currentTimeMillis() + TWO_HOURS);
            timer = new Timer("Reservation for abonne n°" + ab.getNumber() + ", document n°" + this.numero);
            timer.schedule(new ReservationCanceler(this), TWO_HOURS);
        }
    }

    public String getHourEnd() {
        if (HourInTwoHours != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(HourInTwoHours);
        } else {
            return "";
        }
    }

    /**
     * Emprunt d'un document par un abonné.
     *
     * @param ab abonné souhaitant emprunter
     * @throws EmpruntException si le document est déjà emprunté ou réservé par un
     *                          autre
     */
    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        synchronized (this) {
            if (borrowed) {
                throw new EmpruntException("Erreur : ce document est déjà emprunté");
            } else if (reserved && (ab.getNumber() != abonne.getNumber())) {
                throw new DocumentReservedEmpruntException(this.getHourEnd());
            } else if (reserved && (ab.getNumber() == abonne.getNumber())) {
                borrowed = true;
                reserved = false;
            } else if (!reserved && abonne == null) {
                borrowed = true;
                abonne = ab;
            } else {
                throw new EmpruntException("Erreur : emprunt impossible");
            }
        }
    }

    /**
     * Retour d'un document ou annulation d'une réservation.
     *
     * @throws RetourException si le document n'est ni emprunté ni réservé
     */
    @Override
    public void retour() throws RetourException {
        synchronized (this) {
            if (!borrowed && !reserved) {
                throw new RetourException("Erreur : ce document n'est ni emprunté ni réservé");
            }
            borrowed = false;
            reserved = false;
            abonne = null;
        }
    }

    public void cancelReservation() {
        synchronized (this) {
            this.abonne = null;
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
