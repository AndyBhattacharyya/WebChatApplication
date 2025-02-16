import jakarta.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.List;

public class Chatroom {

    private List<User> users_chatroom = new ArrayList<>();
    public synchronized void addUserToChatRoom(User user){
        users_chatroom.add(user);
    }
    public synchronized void removeUserFromChatRoom(User user){
        users_chatroom.remove(user);
    }
    public synchronized void sendMessageToChatRoom(User user, String message){
        String chatUser = user.getUsername();
        String message_json = "{ \"user\":\"" +chatUser+"\", \"message\":\""+message+"\" }";
        for(User tmp_user: users_chatroom){
            tmp_user.writeMessageToStream(message_json);
            /*
            if(tmp_user!=user){
                //tmp_user.addMessageToQueue(message_json);
            }
             */

        }
    }







}
