package mediatheque.model;

import mediatheque.exceptions.EmpruntException;

public class DVD extends DocumentBase {

    private static final int AGE_MINIMUM_ADULTE = 16;

    private final boolean adulte;

    public DVD(String id, String titre, boolean adulte) {
        super(id, titre);
        this.adulte = adulte;
    }

    public boolean isAdulte() {
        return adulte;
    }

    @Override
    protected void verifierEmprunt(Abonne ab) throws EmpruntException {
        if (adulte && ab.getAge() < AGE_MINIMUM_ADULTE) {
            throw new EmpruntException(
                "Vous devez avoir au moins " + AGE_MINIMUM_ADULTE
                + " ans pour emprunter le DVD « " + getTitre()
                + " » (votre âge : " + ab.getAge() + " ans).");
        }
    }

    @Override
    public String toString() {
        return super.toString() + (adulte ? " [+16]" : " [tout public]");
    }
}
