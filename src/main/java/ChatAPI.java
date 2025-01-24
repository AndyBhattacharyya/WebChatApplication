import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChatAPI extends HttpServlet {

    private Chatroom globalchat = new Chatroom();
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        Handling polling requests from user
        * consider multithreading implications
        * understand that messages will be in format: "username|hey there whats up";
         */

        resp.setContentType("text/html");
        Cookie[] cookies = req.getCookies();
        Cookie usercookie = null;
        if(req.getCookies() != null) {
            for(int i = 0; i<cookies.length; i++) {
                if(cookies[i].getName().equals("user")) {
                    usercookie = cookies[i];
                }
            }
        }
        String message = "default| This is default message";
        PrintWriter out = resp.getWriter();
        if (usercookie != null) {
            message = globalchat.getUserFromChatRoom(usercookie).getMessageFromQueue();
            if(message==null)
                message = "default| This is default message";
        }
        out.println(message);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        2 types of post requests:
        (1) user addition to chatroom w/ redirect/ cookie impl
        (2) perhaps sending messages
         */

        String postType=req.getParameter("post_type");
        // (1) Processing
        if(postType!=null && postType.equals("register")) {
            String username = req.getParameter("username");
            /*
            Important functions to add:
            checkUserExistence()
            registerUser()
             */
            Cookie register_cookie = new Cookie("user",username);
            globalchat.addUserToChatRoom(register_cookie);
            resp.addCookie(register_cookie);
            resp.sendRedirect("/chatroom.html");
        }
        else{
            BufferedReader reader = req.getReader();
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String message = stringBuilder.toString();

            Cookie[] cookies = req.getCookies();
            Cookie usercookie = null;
            if(cookies != null) {
                for(int i = 0; i<cookies.length; i++) {
                    if(cookies[i].getName().equals("user")) {
                        usercookie = cookies[i];
                    }
                }
            }
            if(usercookie != null) {
                globalchat.sendMessageToChatRoom(usercookie, message);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");
            resp.setContentLength(0);

        }
    }
}
