package servlet;


import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import backend.DatabaseActions;
import backend.PhotoScaler;
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
		if (session == null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.forward(request, response);
		}
		if (session == null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.forward(request, response);
		}
		userObj = (User) session.getAttribute("user");
		response.setContentType("text/html");
		out = response.getWriter();
		
		
		//make call to get user info to fill profile page
		String profileInfo[] = DatabaseActions.getProfile(userObj.getN_id());
		byte[] picture = DatabaseActions.getPicture(userObj.getN_id());
		

		
		//resize photo and convert to base64
		byte[] newBuffer = PhotoScaler.resizeByteArray(250, 250, picture);
		byte[] encoded = Base64.getEncoder().encode(newBuffer);
	
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Profile page</title>"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>"
				+ "<form action=\"UpdatePic\" method=\"POST\" enctype=\"multipart/form-data\">"
				+ "<input type=\"file\" class=\"file_field\" name=\"photo\" required>"
				+ "<input type=\"submit\" value=\"Change Picture\">"
				+ "</form>");
		
		/* [0] - name
		 * [1] - email
		 * [2] - photo name
		 * [3] - reward points
		 * [4] - local agent
		 * [5] - last login
		 * 
		 */
		for(int i = 0; i < profileInfo.length; i++) {
			
			if(i != 2 && i != 4 && i != 5) {

				out.println(profileInfo[i] + "</br></br>");
			}
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
