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
		PrintWriter out = response.getWriter();
		
		String user = request.getParameter("username").toLowerCase();
		char[] pass = request.getParameter("password").toCharArray();
		
		if (DatabaseActions.login(user, pass)) {
			
			//saves user info for the session
			ObjectId n_id = DatabaseActions.getN_id(user);
			userObj = new User(user, n_id);
			session.setAttribute("user", userObj);			
			
			RequestDispatcher rd = request.getRequestDispatcher("successfulLogin.html");	// TODO replace with eventual home page
			rd.forward(request, response);
		}
		else {

			out.print("Invalid Credentials");
			RequestDispatcher rd = request.getRequestDispatcher("index.html");				// TODO replace with invalid_credentials.html
			rd.include(request, response);
		
		}		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
}
