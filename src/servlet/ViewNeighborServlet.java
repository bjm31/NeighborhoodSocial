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
		ObjectId objId = new ObjectId(n_id);
		System.out.println(n_id);
		
		String[] profile = DatabaseActions.getProfile(objId);
		byte[] picture = DatabaseActions.getPicture(objId);
		byte[] encoded = Base64.getEncoder().encode(picture);
		
		for(String s : profile) {
			
			System.out.println(s);
		}

		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Profile page</title>"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>");
		
		for(int i = 0; i < profile.length; i++) {
			
			out.println(profile[i] + "</br></br>");
		}
		
		out.println("</body>"
				+ "</html>"
				+ "<form action=\"NeighborList\" method=\"POST\">"
				+ "<input type=\"submit\" value=\"Go Back\">"
				+ "</form>");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
