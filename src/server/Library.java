package server;

import server.documents.Document;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.SubscriberNotFoundException;
import server.subscribers.Subscriber;

import java.util.ArrayList;

public class Library {
    private ArrayList<Document> documents;
    private ArrayList<Subscriber> subscribers;
    public Library(){
        documents = new ArrayList<>();
        subscribers = new ArrayList<>();
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public ArrayList<Subscriber> getSubscribers() {
        return subscribers;
    }

    public Subscriber findSubsciberFromID(int id) throws SubscriberNotFoundException {
        for (Subscriber s : subscribers)
            if(s.getNumber() == id)
                return s;
        throw new SubscriberNotFoundException();
    }

    public Document findDocumentFromID(int id) throws DocumentNotFoundException {
        for(Document d : documents)
            if (d.numero() == id)
                return d;
        throw new DocumentNotFoundException();
    }

    public void addSubsciber(Subscriber sub){
        this.subscribers.add(sub);
    }

    public void addDocument(Document doc){
        this.documents.add(doc);
    }

}
