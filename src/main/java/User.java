import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class User{

    private String username;
    private Queue<String> messages = new LinkedList<String>();
    ///implementation of stream based messaging
    private PipedReader in;
    private BufferedReader bin;
    private PrintWriter pin;
    private HttpServletResponse channelSSE;
    private Thread pushMessages;

    public User(String username) throws IOException {
        this.username = username;
        in = new PipedReader();
        bin = new BufferedReader(in);
        pin = new PrintWriter(new PipedWriter(in));
    }

    public void beginReadingMessages(HttpServletResponse resp) throws IOException, InterruptedException {
        PrintWriter out = resp.getWriter();
        String message;
        while((message=bin.readLine()) != null) {
            out.print(message+"\r\n");
            out.flush();
        }
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

    synchronized public void writeMessageToStream(String message){
        pin.println(message);
    }
}
