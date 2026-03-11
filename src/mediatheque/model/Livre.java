package mediatheque.model;

import mediatheque.exceptions.EmpruntException;

public class Livre extends DocumentBase {

    private final int nbPages;

    public Livre(String id, String titre, int nbPages) {
        super(id, titre);
        this.nbPages = nbPages;
    }

    public int getNbPages() {
        return nbPages;
    }

    @Override
    protected void verifierEmprunt(Abonne ab) throws EmpruntException {
        // Pas de restriction particulière pour les livres
    }

    @Override
    public String toString() {
        return super.toString() + " (" + nbPages + " pages)";
    }
}
