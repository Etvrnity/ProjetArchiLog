package server.documents.types;

import server.documents.Document;
import server.subscribers.Subscriber;

public class DVD implements Document {
    private int numero;
    private String title;
    private Subscriber subscriber;
    private boolean borrowed;// == Emprunté
    private boolean adult;
    @Override
    public int numero() {
        return numero;
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
        assert(!borrowed && subscriber == null);//TODO verif ca

    }

    @Override
    public void empruntPar(Subscriber sub) {
        //TODO
    }

    @Override
    public void retour() {
        //TODO
        subscriber = null;
    }
}
