package server.services;

import server.Library;

import java.net.Socket;

public abstract class GenericService implements Runnable {
    private Socket s;
    private Library library;
    public void setSocket(Socket s) {
        this.s = s;
    }

    public void setLibrary(Library lib){
        this.library = lib;
    }

    public Socket getClientSocket(){
        return s;
    }

    public GenericService(Socket s) {
        this.s = s;
    }
    public Socket getSocket(){
        return s;
    } // y a deux méthodes pour get s?

    public Library getLibrary() {
        return library;
    } // eske c une bonne idée? c'est pour l'utiliser dans les Services

    public void lancer() {
        new Thread(this).start();
    }
}
