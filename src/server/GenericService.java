package server;

import java.net.Socket;

public abstract class GenericService implements Runnable {
    Socket s;
    public void setSocket(Socket s) {
        this.s = s;
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
    public void lancer() {
        new Thread(this).start();
    }
}
