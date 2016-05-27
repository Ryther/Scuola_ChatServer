package model;

import chatUtils.data.Chat;
import chatUtils.data.Consts;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.data.ServerData;
import utils.net.SocketHandler;
import utils.net.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerMain {

    private static SocketHandler socketHandler;
    
    public static void main(String[] args) {
        
        ServerData serverData = new ServerData();
        serverData.addChat(new Chat("Test"));
        ExecutorService threadPool = Executors.newFixedThreadPool(Consts.CLIENT_THREADS);
        
        socketHandler = new SocketHandler(Consts.PORT);
        
        while (true) {
            socketHandler.accept();

            Runnable clientManager = new ClientManager(
                    new StreamHandler(socketHandler.getSocket()), serverData);

            threadPool.submit(clientManager);
        }
    }
}
