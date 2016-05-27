package model;

import chatUtils.data.Chat;
import chatUtils.data.ChatMessage;
import chatUtils.data.Consts;
import chatUtils.data.UserData;
import chatUtils.net.Talker;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utils.net.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ClientManager implements Runnable {

    private final StreamHandler streamHandler;
    private final Set<Chat> chats;
    private UserData userData;

    public ClientManager(StreamHandler streamHandler, Set<Chat> chats) {
        
        this.streamHandler = streamHandler;
        this.chats = chats;
    }
    
    public void run() {
        
        this.streamHandler.init();
        userData = (UserData) this.streamHandler.pullFromStream();
        ExecutorService threadPool = Executors.newFixedThreadPool(Consts.TALKER_THREADS);
        Runnable writingThread = new Talker(streamHandler, new ChatMessage(this.userData.getUserName()));
        Runnable readingThread = new Talker(streamHandler);
        threadPool.submit(writingThread);
        threadPool.submit(readingThread);
    }
}