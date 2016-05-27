package model;

import chatUtils.data.Chat;
import chatUtils.data.Consts;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utils.net.SocketHandler;
import utils.net.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerMain {

    private static SocketHandler socketHandler;
    
    public static void main(String[] args) {
        
        ExecutorService threadPool = Executors.newFixedThreadPool(Consts.CLIENT_THREADS);
        
        socketHandler = new SocketHandler(Consts.PORT);
        Set<Chat> chats = new TreeSet();
        
        while (true) {
            socketHandler.accept();

            Runnable clientManager = new ClientManager(
                    new StreamHandler(socketHandler.getSocket()), chats);

            threadPool.submit(clientManager);
        }
    }
}
