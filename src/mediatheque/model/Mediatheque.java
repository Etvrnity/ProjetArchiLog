package mediatheque.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton représentant la médiathèque.
 * Contient tous les abonnés et documents créés en dur au lancement.
 */
public class Mediatheque {

    private static final Mediatheque INSTANCE = new Mediatheque();

    private final Map<Integer, Abonne>  abonnes   = new HashMap<>();
    private final Map<String, Document> documents = new HashMap<>();

    private Mediatheque() {
        // -------- Abonnés --------
        ajouterAbonne(new Abonne(1,  "Dupont Alice",   LocalDate.of(1990,  3, 15)));
        ajouterAbonne(new Abonne(2,  "Martin Bob",     LocalDate.of(2010,  7, 22)));  // mineur
        ajouterAbonne(new Abonne(3,  "Bernard Claire", LocalDate.of(1985, 11,  5)));
        ajouterAbonne(new Abonne(4,  "Leroy Denis",    LocalDate.of(2008,  1, 30)));  // mineur
        ajouterAbonne(new Abonne(5,  "Petit Emma",     LocalDate.of(1995,  6,  8)));

        // -------- Livres --------
        ajouterDocument(new Livre("L001", "Le Petit Prince",           96));
        ajouterDocument(new Livre("L002", "Les Misérables",          1900));
        ajouterDocument(new Livre("L003", "Harry Potter T1",          309));
        ajouterDocument(new Livre("L004", "Clean Code",               431));
        ajouterDocument(new Livre("L005", "1984",                     328));

        // -------- DVDs --------
        ajouterDocument(new DVD("D001", "Inception",            true));   // +16
        ajouterDocument(new DVD("D002", "Le Roi Lion",          false));  // tout public
        ajouterDocument(new DVD("D003", "Pulp Fiction",         true));   // +16
        ajouterDocument(new DVD("D004", "Toy Story",            false));  // tout public
        ajouterDocument(new DVD("D005", "Interstellar",         true));   // +16
    }

    public static Mediatheque getInstance() {
        return INSTANCE;
    }

    private void ajouterAbonne(Abonne a) {
        abonnes.put(a.getNumero(), a);
    }

    private void ajouterDocument(Document d) {
        documents.put(d.idDoc(), d);
    }

    public Abonne getAbonne(int numero) {
        return abonnes.get(numero);
    }

    public Document getDocument(String id) {
        return documents.get(id);
    }

    public void afficherCatalogue() {
        System.out.println("=== Catalogue ===");
        documents.values().forEach(System.out::println);
        System.out.println("=== Abonnés ===");
        abonnes.values().forEach(System.out::println);
    }
}
