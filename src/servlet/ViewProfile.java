package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import backend.DatabaseActions;
import backend.User;

/**
 * Allows user to view their profile
 */
@WebServlet("/ViewProfile")
public class ViewProfile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	HttpSession session;  
    User userObj;
    PrintWriter out;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		session = request.getSession(false);
		userObj = (User) session.getAttribute("user");
		response.setContentType("text/html");
		out = response.getWriter();
		
		String profileInfo[] = DatabaseActions.getProfile(userObj.getN_id());
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>"
				+ "<title>View Profile</title>"
				+ "</style>"
				+ "</head>"
				+ "<body>");
		
		for(int i = 0; i < profileInfo.length; i++) {
			
			out.println(profileInfo[i] + "</br></br>");
		}
		
		out.println("<form action=\"Home\" method=\"GET\" id=\"returnButton\" class=\"servlet.Home\">"
				+ "<input type=\"submit\" value=\"Go back\">"
				+ "</form>"
				+ "</body>"
				+ "</html>");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
