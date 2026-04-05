package tests;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Certification BretteSoft© Sitting Bull
 * Test d'envoi du mail de notification de disponibilité d'un document.
 *
 * Comment lancer :
 *  1. Crée un fichier .env à la racine du projet avec :
 *       EMAIL_FROM=ton_adresse@gmail.com
 *       EMAIL_TO=celthansdez@gmail.com
 *       PASSWORD_MAIL=ton_mot_de_passe_application
 *       EMAIL_HOST=smtp.gmail.com
 *       EMAIL_PORT=587
 *
 *  2. Si tu utilises Gmail : active "Mots de passe d'application" dans ton compte Google
 *     (Sécurité → Validation en 2 étapes → Mots de passe d'application)
 *     et utilise ce mot de passe généré dans PASSWORD_MAIL (pas ton vrai mdp).
 *
 *  3. Fais clic droit sur ce fichier dans IntelliJ → Run 'TestSendMail.main()'
 */
public class Testsendmail {

    public static void main(String[] args) {

        System.out.println("=== Certification BretteSoft© Sitting Bull ===");
        System.out.println("Envoi du mail de notification en cours...\n");

        try {
            Dotenv dotenv = Dotenv.load();

            final String from     = dotenv.get("EMAIL_FROM");
            final String to       = "celthansdez@gmail.com";
            final String password = dotenv.get("PASSWORD_MAIL");
            final String host     = dotenv.get("EMAIL_HOST");
            final String port     = dotenv.get("EMAIL_PORT");

            Properties props = new Properties();
            props.put("mail.smtp.host",            host);
            props.put("mail.smtp.port",            port);
            props.put("mail.smtp.auth",            "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            // Activation du debug SMTP (visible dans la console IntelliJ)
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Certification BretteSoft Sitting Bull - Document disponible : Pulp Fiction");

            String body =
                    "Bonjour,\n\n" +
                            "Vous avez demandé à être alerté par signaux de fumée lors du retour du document suivant :\n\n" +
                            "   Titre  : Pulp Fiction\n" +
                            "   N°     : 6\n" +
                            "   Type   : DVD (adulte)\n\n" +
                            "Ce document est désormais disponible à la médiathèque.\n" +
                            "Vous pouvez venir l'emprunter ou le réserver dès maintenant.\n\n" +
                            "Rappel : la réservation est valable 2 heures à partir de votre demande.\n\n" +
                            "Cordialement,\n" +
                            "La médiathèque — Système BretteSoft© Sitting Bull\n\n" +
                            "---\n" +
                            "Ceci est un message automatique, merci de ne pas y répondre.";

            message.setText(body);

            Transport.send(message);

            System.out.println("\n[SUCCES] Mail envoyé à : " + to);
            System.out.println("[SUCCES] Objet         : " + message.getSubject());

        } catch (Exception e) {
            System.err.println("\n[ERREUR] Envoi échoué : " + e.getMessage());
            System.err.println("Vérifiez votre fichier .env et vos identifiants SMTP.");
            e.printStackTrace();
        }
    }
}