package server;

import server.services.GenericService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final ServerSocket listen_socket;
    private final Class<? extends GenericService> aClass;
    private Library library;

    Server(Class<? extends GenericService> aClass, Library library, int port) throws IOException {
        this.aClass = aClass;
        this.library = library;
        listen_socket = new ServerSocket(port);
    }

    public void run() {
        try {
            while(true) {
                Socket client = listen_socket.accept();
                GenericService s = aClass.getConstructor(Socket.class).newInstance(client);
                s.setSocket(client);
                s.setLibrary(library);
                s.lancer();
            }
        }
        catch (IOException e) {
            try {
                this.listen_socket.close();
            } catch (IOException ignored) {}
            System.err.println("Error on the listening port : "+e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
