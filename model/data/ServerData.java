package model.data;

import chatUtils.data.Chat;
import chatUtils.data.UserData;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Edoardo Zanoni
 */
public class ServerData {
    
    private final ConcurrentHashMap<Integer, Chat> chats;
    private final ConcurrentHashMap<Integer, UserData> connectedUsers;

    public ServerData() {
    
        this.chats = new ConcurrentHashMap();
        this.connectedUsers = new ConcurrentHashMap();
    }
    
    public void addUser(UserData userData) {
        
        this.connectedUsers.put(userData.hashCode(), userData);
    }
    
    public void addChat(Chat chat) {
        
        this.chats.put(chat.hashCode(), chat);
    }
    
    public void addUserToChat(String userName, String chatName) {
        
        this.chats.get(chatName.hashCode()).
                addUser(this.connectedUsers.get(userName.hashCode()));
        this.connectedUsers.get(userName.hashCode()).
                addChat(this.chats.get(chatName.hashCode()));
    }
}
