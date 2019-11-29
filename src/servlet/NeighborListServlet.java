package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;

import backend.DatabaseActions;
import backend.PhotoScaler;
import backend.User;

/**
 * Servlet implementation class NeighborListServlet
 */
@WebServlet("/NeighborList")
public class NeighborListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	PrintWriter out;   
    User user;
    HttpSession session;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ObjectId[] ids = DatabaseActions.getNeighborList();
		String[] names = DatabaseActions.getAllNames();
		
		session = request.getSession();
		if (session == null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.forward(request, response);
		}
		user = (User) session.getAttribute("user");		
		ObjectId n_id = user.getN_id();
		
		int i = 0;
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out = response.getWriter();
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Neighbor List</title>"
				+ "</head>"
				+ "<body>"
				+ "<h1>Your Neighbors</h1></br>");
		
		//byte[] n_pic = null;
		for(String s : names) {

			if(n_id.compareTo(ids[i]) != 0) {
				
				byte[] n_pic = DatabaseActions.getPicture(ids[i]);
				n_pic = PhotoScaler.resizeByteArray(75, 75, n_pic);
				byte[] encoded = Base64.getEncoder().encode(n_pic);
				
				out.println("</br>" 
						+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>"
						+ s + "  "
						+ "<form action=\"ViewNeighbor\">"
						+ "<input type=\"hidden\" name=\"n_id\" value=\"" + ids[i] + "\">"
						+ "<input type=\"submit\" value=\"View Profile\">"
						+ "</form>"
						+ "</br>");
				
			}
			i++;
		}
		out.println("<form action=\"Home\">"
				+ "<input type=\"submit\" value=\"Home\">"
				+ "</form>"
				+ "</body>"
				+ "</html>");
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
