package server;

import server.documents.Document;
import server.documents.types.Book;
import server.documents.types.DVD;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.SubscriberNotFoundException;
import server.subscribers.Abonne;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Library {
    private final HashMap<Integer, Document> documents;
    private final HashMap<Integer, Abonne> abonnes;

    public Library() {
        documents = new HashMap<>();
        abonnes = new HashMap<>();
    }

    // Initialisation de la médiathèque avec des données en dur.

    public void initData() {
        // abonnés
        abonnes.put(1, new Abonne(1, "Alice Martin", new GregorianCalendar(1995, 3, 12).getTime()));
        abonnes.put(2, new Abonne(2, "Karim Foughali", new GregorianCalendar(2000, 7, 25).getTime()));
        abonnes.put(3, new Abonne(3, "Karima Mersad", new GregorianCalendar(2012, 11, 5).getTime()));
        abonnes.put(4, new Abonne(4, "Lukas Art", new GregorianCalendar(1988, 0, 30).getTime()));
        // livres
        documents.put(1, new Book(1, "Les Misérables", 1232));
        documents.put(2, new Book(2, "Le rouge et le noir", 96));
        documents.put(3, new Book(3, "Le Prince", 427));
        // DVDs
        documents.put(4, new DVD(4, "Mario et Luigi", false));
        documents.put(5, new DVD(5, "Gravity", false));
        documents.put(6, new DVD(6, "Pulp Fiction", true));
        documents.put(7, new DVD(7, "Fight Club", true));
    }

    public HashMap<Integer, Document> getDocuments() {
        return documents;
    }

    public HashMap<Integer, Abonne> getAbonnes() {
        return abonnes;
    }

    public Abonne findAbonneByID(int id) throws SubscriberNotFoundException {
        Abonne a = abonnes.get(id);
        if (a==null)
            throw new SubscriberNotFoundException();
        return a;
    }

    public Document findDocumentByID(int id) throws DocumentNotFoundException {
        Document d = documents.get(id);
        if (d==null)
            throw new DocumentNotFoundException();
        return d;
    }

    public void addAbonne(Abonne ab) {
        this.abonnes.put(ab.getNumber(), ab);
    }

    public void addDocument(Document doc) {
        this.documents.put(Integer.parseInt(doc.idDoc()), doc);
    }
}
