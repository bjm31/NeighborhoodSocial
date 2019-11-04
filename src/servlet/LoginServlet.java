package servlet;

import backend.User;
import backend.DatabaseActions;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import org.bson.types.ObjectId;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    HttpSession session;  
    User userObj;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Create a session
		session = request.getSession();
		
		response.setContentType("text/html");
		
		String user = request.getParameter("username");
		char[] pass = request.getParameter("password").toCharArray();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

 
		if (DatabaseActions.login(user, pass)) {
			
			//saves user info for the session
			userObj = new User(user, new String(pass));
			session.setAttribute("user", userObj);			
			
			RequestDispatcher rd = request.getRequestDispatcher("successfulLogin.html");
			ObjectId n_id = DatabaseActions.getN_id(user);
			Cookie cookie = new Cookie("sessionCookie", n_id.toString());
			cookie.setComment("This cookie stores the system userid");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			rd.forward(request, response);
		}
		else {

			out.print("Invalid Credentials");
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.include(request, response);
		
		}		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
}
