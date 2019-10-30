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

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("username");
		char[] pass = request.getParameter("password").toCharArray();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		if (DatabaseActions.login(user, pass)) {
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
