import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        
        chatMessage = new ChatMessage("Berta");
        socketHandler = new SocketHandler(Consts.PORT);
        socketHandler.accept();
        streamHandler = new StreamHandler(socketHandler.getSocket());
        streamHandler.init();
        ExecutorService threadPool = Executors.newFixedThreadPool(Consts.TALKER_THREADS);
        Runnable writingThread = new Talker(streamHandler, chatMessage);
        Runnable readingThread = new Talker(streamHandler);
        threadPool.submit(writingThread);
        threadPool.submit(readingThread);
    }
}
