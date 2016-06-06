package model;

import chatUtils.data.Consts;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.data.ServerData;
import utils.net.SocketChannelHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerMain {

    private static SocketChannelHandler socketChannelHandler;
    private static ServerData serverData;
    
    public static void main(String[] args) {
        
        socketChannelHandler = new SocketChannelHandler(Consts.PORT);
        ExecutorService poolThread = Executors.newFixedThreadPool(Consts.TALKER_THREADS);
        serverData = new ServerData();
        while (true) {
            
            Set<SelectionKey> keySet = socketChannelHandler.select();
            Iterator<SelectionKey> keyIterator = keySet.iterator();
            
            while (keyIterator.hasNext()) {
                
                SelectionKey selectedKey = keyIterator.next();
                if (selectedKey.isAcceptable()) {
                    
                    socketChannelHandler.accept();
                    
                } else if (selectedKey.isReadable()) {
                    
                    SocketChannelHandler tempSocketChannelHandler;
                    tempSocketChannelHandler = new SocketChannelHandler((SocketChannel) selectedKey.channel());
                    Runnable clientManager = new ClientManager(selectedKey, 
                            serverData, 
                            socketChannelHandler.pullFromChannel(selectedKey, Consts.BUFFER_DIMENSION));
                    poolThread.execute(clientManager);
                }
            }
            keyIterator.remove();
        }
    }
}
