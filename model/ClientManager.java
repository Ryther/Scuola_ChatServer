package model;

import chatUtils.data.ChatMessage;
import chatUtils.data.Consts;
import chatUtils.data.UserData;
import chatUtils.net.Talker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.data.ServerData;
import utils.net.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ClientManager implements Runnable {

    private final StreamHandler streamHandler;
    private final ServerData serverData;

    public ClientManager(StreamHandler streamHandler, ServerData serverData) {
        
        this.streamHandler = streamHandler;
        this.serverData = serverData;
    }
    
    public void run() {
        
        this.streamHandler.init();
        
        UserData userData;
        userData = (UserData) this.streamHandler.pullFromStream();
        serverData.addUser(userData);
        serverData.addUserToChat(userData.getUserName(), "Test");
        
        while (true) {
            
            ChatMessage chatMessage = (ChatMessage) this.streamHandler.pullFromStream();
            //for (UserData chatUsers:this.serverData.getChat(chatMessage.getChatName()).getUsers().entrySet())
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(Consts.TALKER_THREADS);
        Runnable writingThread = new Talker(streamHandler, new ChatMessage(this.userData.getUserName()));
        Runnable readingThread = new Talker(streamHandler);
        threadPool.submit(writingThread);
        threadPool.submit(readingThread);
    }
}