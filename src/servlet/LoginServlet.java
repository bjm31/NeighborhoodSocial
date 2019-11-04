package servlet;

import backend.User;
import backend.DatabaseActions;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
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
