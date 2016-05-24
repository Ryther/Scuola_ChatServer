import net.SocketHandler;
import net.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerMain {

    private static SocketHandler socketHandler;
    private static StreamHandler streamHandler;
    private static ChatMessage chatMessage;
    
    public static void main(String[] args) {
        
        socketHandler = new SocketHandler(Consts.PORT);
        socketHandler.accept();
        streamHandler = new StreamHandler(socketHandler.getSocket());
        streamHandler.init();
        chatMessage = (ChatMessage) streamHandler.pullFromStream();
        System.out.println(chatMessage.getDate() + " - [" + chatMessage.getUsername() + "] " + chatMessage.getMessage());
    }
}
