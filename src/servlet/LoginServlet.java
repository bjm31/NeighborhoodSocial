package servlet;

import backend.DatabaseActions;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/home")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("username");
		char[] pass = request.getParameter("password").toCharArray();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		if (DatabaseActions.login(user, pass)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/successfulLogin.html");
			dispatcher.forward(request, response);
		}
		else {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/loginError.html");
			rd.include(request, response);
		}		
	}	
	

}
