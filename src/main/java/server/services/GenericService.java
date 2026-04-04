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
    }
    public Library getLibrary() {
        return library;
    }
    public void lancer() {
        new Thread(this).start();
    }
}
