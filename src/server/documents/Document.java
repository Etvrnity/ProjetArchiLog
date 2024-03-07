package server.documents;

import server.subscribers.Subscriber;

public interface Document {
    int numero();

    // return null si pas emprunté ou pas réservé
    Subscriber emprunteur() ; // Abonné qui a emprunté ce document
    Subscriber reserveur() ; // Abonné qui a réservé ce document

    void reservationPour(Subscriber sub) ;// precondition : ni réservé ni emprunté

    void empruntPar(Subscriber sub);// precondition : libre ou réservé par l’abonné qui vient emprunter

    void retour();// retour d’un document ou annulation d‘une réservation
}
