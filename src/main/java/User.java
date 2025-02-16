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
    private PrintWriter out;
    private Thread tmpUserThread;


    public User(String username) throws IOException {
        this.username = username;
        in = new PipedReader();
        bin = new BufferedReader(in);
        out = new PrintWriter(new PipedWriter(in));
        tmpUserThread = null;
    }

    public void interruptAndSwapThread(Thread newThread) throws InterruptedException{
        if(tmpUserThread!=null){
            tmpUserThread.interrupt();
        }
        tmpUserThread = newThread;
    }

    public String readMessage() throws IOException{
        return bin.readLine();
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

    public void writeMessageToStream(String message){
        out.println(message);
        out.flush();
    }
}
