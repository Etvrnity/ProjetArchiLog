package server.documents.types;

import server.documents.Document;
import server.subscribers.Subscriber;

public class Book implements Document {

    private int numero;
    private String titre;
    private Subscriber subscriber;
    private boolean borrowed;



    @Override
    public int numero() {
        return 0;
    }

    @Override
    public Subscriber emprunteur() {
        if(borrowed){
            return subscriber;
        }
        return null;
    }

    @Override
    public Subscriber reserveur() {
        if(!borrowed){
            return subscriber;
        }
        return null;
    }

    @Override
    public void reservationPour(Subscriber sub) {

    }

    @Override
    public void empruntPar(Subscriber sub) {

    }

    @Override
    public void retour() {

    }
}
