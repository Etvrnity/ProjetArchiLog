package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private ServerSocket listen_socket;
    private Class<? extends GenericService> aClass;

    Server(Class<? extends GenericService> aClass, int port) throws IOException {
        this.aClass = aClass;
        listen_socket = new ServerSocket(port);
    }

    public void run() {
        try {
            while(true) {
                Socket client = listen_socket.accept();
                GenericService s = aClass.getConstructor(Socket.class).newInstance(client);
                s.setSocket(client);
                s.lancer();
            }
        }
        catch (IOException e) {
            try {this.listen_socket.close();} catch (IOException e1) {}
            System.err.println("Error on the listening port : "+e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
