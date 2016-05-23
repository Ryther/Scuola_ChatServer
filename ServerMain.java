package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ChatMessage;
import utils.Consts;
import utils.SocketHandler;
import utils.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerMain {

    private static SocketHandler socketHandler;
    private static StreamHandler streamHandler;
    private static ChatMessage chatMessage;
    
    public static void main(String[] args) {
        
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        socketHandler = new SocketHandler(Consts.PORT);
        socketHandler.accept();
        streamHandler = new StreamHandler(socketHandler.getSocket());
        streamHandler.init();
        chatMessage = (ChatMessage) streamHandler.pullFromStream();
        System.out.println(chatMessage.getDate() + " - [" + chatMessage.getUsername() + "] " + chatMessage.getMessage());
    }
}
