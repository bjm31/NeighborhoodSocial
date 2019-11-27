package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import backend.DatabaseActions;

/**
 * Servlet implementation class NeighborListServlet
 */
@WebServlet("/NeighborList")
public class NeighborListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    PrintWriter out;   

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ObjectId[] ids = DatabaseActions.getNeighborList();
		String[] names = DatabaseActions.getAllNames();
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out = response.getWriter();
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Neighbor List</title>"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<h1>Your Neighbors</h1></br>");
		
		for(String s : names) {
			out.println("</br>" + s + "  "
					+ "<button>View Profile</button>"
					+ "</br>");
			
		}
		out.println("</body>"
				+ "</html>");
		//TODO
		//Use Ids to get Name and profile info
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
