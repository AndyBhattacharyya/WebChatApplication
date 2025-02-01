import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class ChatAPI extends HttpServlet {

    private Chatroom globalchat = new Chatroom();
    private final String COOKIE_NAME = "user";

    private User authorize(HttpServletRequest req){
        HttpSession session = req.getSession();
        return (User) session.getAttribute(COOKIE_NAME);
    }

    private void invalidSessionHandler(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/index.html");
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        Handling polling requests from user
        * consider multithreading implications
        * understand that messages will be in format: "username|hey there whats up";
         */
        HttpSession session = req.getSession(false);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        User user;
        if(session == null || (user=(User) session.getAttribute(COOKIE_NAME))==null){
            invalidSessionHandler(resp);
        }
        else{
            //polling has potential to return null message, perhaps utilize http content length=0 feature
            String message = user.getMessageFromQueue();
            if(message!=null)
                out.println(message);
            else
                resp.setContentLength(0);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        2 types of post requests:
        (1) user addition to chatroom w/ redirect/ cookie impl
        (2) perhaps sending messages
         */
        String postType=req.getParameter("post_type");
        // (1) Processing
        if("application/x-www-form-urlencoded".equalsIgnoreCase(req.getContentType()) && postType!=null) {
            /*
            (FIX) if the username contains spaces, storing directly into cookie w/ space results in an IllegalArgumentException
            (Workaround) obtain URL encoded strings
             */
            String username = req.getParameter("username");
            /*
            Important functions to add:
            checkUserExistence()
            registerUser()
             */

            /*
            Cookie register_cookie = new Cookie("user",username);
            globalchat.addUserToChatRoom(register_cookie);
            resp.addCookie(register_cookie);
            resp.sendRedirect("/chatroom.html");
             */
            HttpSession session = req.getSession(true);
            if(session.getAttribute(COOKIE_NAME)==null){
                User tmp = new User(username);
                session.setAttribute(COOKIE_NAME, tmp);
                globalchat.addUserToChatRoom(tmp);
            }
            resp.sendRedirect("/chatroom.html");
        }

        /*
        Figure out maximum message length allowed
         */
        else if("text/plain".equalsIgnoreCase(req.getContentType())) {
            BufferedReader reader = req.getReader();
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String message = stringBuilder.toString();
            //Before message sent, validate session
            User user;
            if((user=authorize(req))==null){
                invalidSessionHandler(resp);
            }
            else{
                globalchat.sendMessageToChatRoom(user, message);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/html");
                resp.setContentLength(0);
            }
        }
    }
}
