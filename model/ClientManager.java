package model;

import chatUtils.data.Chat;
import chatUtils.data.ChatMessage;
import chatUtils.data.UserData;
import java.util.ArrayList;
import java.util.List;
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
        String userName = userData.getUserName();
        serverData.addUser(userData);
        List<Chat> chats = new ArrayList<Chat>(userData.getChats().values());
        for (Chat chat:chats) {
            if (!serverData.checkChat(chat.getChatName())) {
                
                serverData.addChat(chat);
            }
            serverData.addUserToChat(userData.getUserName(), chat.getChatName());
        }
        serverData.getUser(userName).setStreamHandler(streamHandler);
        
        List<UserData> usersList;
        while (true) {
            
            ChatMessage chatMessage = (ChatMessage) this.streamHandler.pullFromStream();
            usersList = new ArrayList<UserData>(this.serverData.getChat(chatMessage.getChatName()).getUsers().values());
            for (UserData chatUser:usersList) {
                
                chatUser.getStreamHandler().pushToStream(chatMessage);
            }
        }
    }
}