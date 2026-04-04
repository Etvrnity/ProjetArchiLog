package server.exceptions;

public class NotAdultEmpruntException extends EmpruntException {
    public NotAdultEmpruntException() {
        super("Erreur : vous n’avez pas l’âge pour emprunter ce DVD");
    }
}
