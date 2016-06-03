package model;

import chatUtils.data.ChatMessage;
import chatUtils.data.Consts;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;
import utils.net.SocketChannelHandler;
import utils.net.SocketHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerMain {

    private static SocketHandler socketHandler;
    private static SocketChannelHandler socketChannelHandler;
    
//    public static void main(String[] args) {
//        
//        ServerData serverData = new ServerData();
//        ExecutorService threadPool = Executors.newFixedThreadPool(Consts.CLIENT_THREADS);
//        
//        socketHandler = new SocketHandler(Consts.PORT);
//        
//        while (true) {
//            socketHandler.accept();
//
//            Runnable clientManager = new ClientManager(
//                    new StreamHandler(socketHandler.getSocket()), serverData);
//
//            threadPool.submit(clientManager);
//        }
//    }
    
    public static void main(String[] args) {
        
        socketChannelHandler = new SocketChannelHandler(Consts.PORT);
        
        while (true) {
            
            Set<SelectionKey> keySet = socketChannelHandler.select();
            Iterator<SelectionKey> selectionKeys = keySet.iterator();
            
            while (selectionKeys.hasNext()) {
                
                SelectionKey selectedKey = selectionKeys.next();
                if (selectedKey.isAcceptable()) {
                    
                    socketChannelHandler.accept();
                    
                } else if (selectedKey.isReadable()) {
                    
                    ChatMessage chatMessage = (ChatMessage) socketChannelHandler.pullFromStream(selectedKey);
                    System.out.println(chatMessage);
                }
            }
        }
    }
}
