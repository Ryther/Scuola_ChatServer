package model.clientManager;

import chatUtils.data.ChatMessage;
import java.util.Set;
import java.util.TreeSet;
import utils.net.StreamHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class Client implements Comparable {
    
    private final Set<String> chats;
    private final StreamHandler streamHandler;

    public Client(StreamHandler streamHandler) {
        
        this.chats = new TreeSet();
        this.streamHandler = streamHandler;
        this.streamHandler.init();
    }
    
    public void addChat(String chat) {
        
        this.chats.add(chat);
    }
    
    public boolean checkChat(String chat) {
        
        return this.chats.contains(chat);
    }
    
    public void pushToClient(ChatMessage chatMessage) {
        
        this.streamHandler.pushToStream(chatMessage);
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
