import jakarta.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.HashMap;

public class Chatroom {

    private HashMap<Cookie, User> users_chatroom = new HashMap<>();
    public synchronized void addUserToChatRoom(Cookie user){
        users_chatroom.put(user, new User(user));
    }
    public synchronized void removeUserFromChatRoom(Cookie user){
        users_chatroom.remove(user);
    }
    public synchronized void sendMessageToChatRoom(Cookie user, String message){
        String message_format = users_chatroom.get(user).getUsername() +"|"+message;
        for(Cookie key : users_chatroom.keySet()){
            if(key.equals(user)){
                continue;
            }
            users_chatroom.get(key).addMessageToQueue(message_format);
        }
    }
    public synchronized User getUserFromChatRoom(Cookie user){
        return users_chatroom.get(user);
    }







}
