package model;

import chatUtils.data.Chat;
import chatUtils.data.ChatMessage;
import chatUtils.data.UserData;
import java.util.ArrayList;
import java.util.List;
import model.data.ServerData;
import utils.net.SocketChannelHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ClientManager implements Runnable {

    private final SocketChannelHandler socketChannelHandler;
    private final ServerData serverData;
    private final Object object;

    public ClientManager(SocketChannelHandler socketChannelHandler, ServerData serverData, Object object) {
        
        this.socketChannelHandler = socketChannelHandler;
        this.serverData = serverData;
        this.object = object;
    }
    
    public void run() {
        
        if (this.object instanceof UserData) {
            UserData userData;
            userData = (UserData) this.object;
            String userName = userData.getUserName();
            serverData.addUser(userData);
            List<Chat> chats = new ArrayList<Chat>(userData.getChats().values());
            for (Chat chat:chats) {
                if (!serverData.checkChat(chat.getChatName())) {

                    serverData.addChat(chat);
                }
                serverData.addUserToChat(userData.getUserName(), chat.getChatName());
            }
            serverData.getUser(userName).setSocketChannelHandler(this.socketChannelHandler);
            
        } else if (this.object instanceof ChatMessage) {
        
            List<UserData> usersList;

            ChatMessage chatMessage = (ChatMessage) this.object;
            usersList = new ArrayList<UserData>(this.serverData.getChat(chatMessage.getChatName()).getUsers().values());
            for (UserData chatUser:usersList) {

                chatUser.getSocketChannelHandler().pushToChannel(chatMessage);
            }
        }
    }
}