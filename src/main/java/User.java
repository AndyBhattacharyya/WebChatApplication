import jakarta.servlet.http.Cookie;

import java.util.LinkedList;
import java.util.Queue;

public class User {

    private String username;
    private Queue<String> messages = new LinkedList<String>();

    public User(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    };

    public void addMessageToQueue(String message){
        messages.add(message);
    }
    public String getMessageFromQueue(){
        return messages.poll();
    }



}
