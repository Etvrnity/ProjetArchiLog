package server.exceptions;

public class SubscriberNotFoundException extends Exception {
    public SubscriberNotFoundException() {
        super("Erreur : cet utilisateur n'existe pas");
    }
}
