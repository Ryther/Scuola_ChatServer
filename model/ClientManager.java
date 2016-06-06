package model;

import chatUtils.data.Chat;
import chatUtils.data.ChatMessage;
import chatUtils.data.UserData;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import model.data.ServerData;
import utils.net.SocketChannelHandler;

/**
 *
 * @author Edoardo Zanoni
 */
public class ClientManager implements Runnable {

    private final SelectionKey selectionKey;
    private final ServerData serverData;
    private final Object object;

    public ClientManager(SelectionKey selectionKey, ServerData serverData, Object object) {
        
        this.selectionKey = selectionKey;
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
            serverData.getUser(userName).setSelectionKey(this.selectionKey);
            
            this.propagateNewUser(userData);
            
        } else if (this.object instanceof ChatMessage) {
        
            this.propagateMessage((ChatMessage) this.object);
        }
    }
    
    private void propagateMessage(ChatMessage chatMessage) {
        
        List<UserData> usersList;

        usersList = new ArrayList<UserData>(this.serverData.getChat(chatMessage.getChatName()).getUsers().values());
        for (UserData chatUser:usersList) {

            SocketChannel socketChannel = (SocketChannel) chatUser.getSelectionKey().channel();
            SocketChannelHandler.pushToChannel(socketChannel, chatMessage);
        }
    }
    
    private void propagateNewUser(UserData userData) {
        
        ChatMessage chatMessage = new ChatMessage(userData.getUserName() + " si è unito alla chat");
        chatMessage.setMessage("");
        chatMessage.setDateTime();
        
        List<UserData> usersList;
        List<Chat> chats = new ArrayList<Chat>(userData.getChats().values());
        for (Chat chat:chats) {
            usersList = new ArrayList<UserData>(this.serverData.getChat(chat.getChatName()).getUsers().values());
            for (UserData chatUser:usersList) {

                SocketChannel socketChannel = (SocketChannel) chatUser.getSelectionKey().channel();
                SocketChannelHandler.pushToChannel(socketChannel, chatMessage);
            }
        }
    }
}