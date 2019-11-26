package servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
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
		
		
		//Get user session
		session = request.getSession(false);
		userObj = (User) session.getAttribute("user");
		response.setContentType("text/html");
		out = response.getWriter();
		
		//make call to get user info to fill profile page
		String profileInfo[] = DatabaseActions.getProfile(userObj.getN_id());
		byte[] picture = DatabaseActions.getPicture(userObj.getN_id());

		byte[] encoded = Base64.getEncoder().encode(picture);
		
		
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Profile page</title>"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>");
		
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
