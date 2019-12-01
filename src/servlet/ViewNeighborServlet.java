package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import backend.DatabaseActions;
import backend.PhotoScaler;

/**
 * Servlet implementation class ViewNeighborServlet
 */
@WebServlet("/ViewNeighbor")
public class ViewNeighborServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PrintWriter out;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		out = response.getWriter();
		String n_id = request.getParameter("n_id");
		
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		System.out.println("ID: " + n_id);
		ObjectId objId = new ObjectId(n_id);

		
		String[] profile = DatabaseActions.getProfile(objId);
		byte[] picture = DatabaseActions.getPicture(objId);
		picture = PhotoScaler.resizeByteArray(200, 200, picture);
		byte[] encoded = Base64.getEncoder().encode(picture);

		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Profile page</title>"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>");
		
		/* [0] - name
		 * [1] - email
		 * [2] - photo name
		 * [3] - reward points
		 * [4] - local agent
		 * [5] - last login
		 */
		for(int i = 0; i < profile.length; i++) {
			
			if(i != 2 && i != 4) {
				out.println(profile[i] + "</br></br>");
			}
		}
		String name = profile[0].substring(profile[0].indexOf('>', 3) + 1);
		
		System.out.println("NAME: " + profile[0].substring(profile[0].indexOf('>', 3) + 1));
		out.println("<form action=\"MakeMessage\" method=\"POST\">"
				+ "<input type=\"hidden\" name=\"previous\" value=\"ViewNeighbor\">"
				+ "<input type=\"hidden\" name=\"name\" value=\"" + name + "\">"
				+ "<input type=\"submit\" value=\"Send Message\">"
				+ "</form>"
				+ "<form action=\"NeighborList\" method=\"POST\">"
				+ "<input type=\"submit\" value=\"Go Back\">"
				+ "</form>"
				+ "</body>"
				+ "</html>");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
