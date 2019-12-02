package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;

import backend.DatabaseActions;
import backend.User;

/**
 * Servlet implementation class ViewMessagesServlet
 */
@WebServlet("/ViewMessages")

public class ViewMessagesServlet extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
    HttpSession session;
    User user;
    PrintWriter out;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		out = response.getWriter();
		session = request.getSession();
		user = (User) session.getAttribute("user");
		int count = 0;
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		String filterType = request.getParameter("filterType");
		ObjectId n_id = user.getN_id();
		String[][] msgs = DatabaseActions.getMessages(n_id); //[i][0] = From ID, [i][1] = TO ID, [i][2] = Message, [i][3] = Time Sent
		String name = DatabaseActions.getDisplayName(n_id);
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>"
				+ "<title>View Messages</title>"
				+ "</head>"
				+ "<body>"
				+ "<h1>Your Messages</h1></br>");

		if(filterType == null || filterType.compareTo("") == 0 || filterType.compareTo("received") == 0) {
			
			out.println("<form action=\"ViewMessages\" method=\"GET\" id=\"filter\" onchange=\"$('#filter').submit();\">"
					+ "Filter by: <select name=\"filterType\">\n"
					+ "				<option value=\"received\">Received</option>\n"
					+ "				<option value=\"sent\">Sent</option>\n" 
					+ "			 </select>"
					+ "</form>");
			
			for(int i = 0; i < msgs.length; i++) {

				
				if(msgs[i][1].equals(name)) {
					
					out.println("</br><b>From: " + msgs[i][0] + " at " + msgs[i][3] + "</b></br></br>");
					out.println(msgs[i][2] + "</br></br></br>");
					count++;
				}
			}
			
			if(count == 0) {
				out.println("No messages received.");
			}
		}
		
		if(filterType != null && filterType.compareTo("sent") == 0) {
			
			out.println("<form action=\"ViewMessages\" method=\"GET\" id=\"filter\" onchange=\"$('#filter').submit();\">"
					+ "Filter by: <select name=\"filterType\">\n"  
					+ "				<option value=\"sent\">Sent</option>\n" 
					+ "				<option value=\"received\">Received</option>\n"
					+ "			 </select>"
					+ "</form>");

			for(int i = 0; i < msgs.length; i++) {

				if(msgs[i][0].equals(name)) {
					
					out.println("</br><b>To: " + msgs[i][1] + " at " + msgs[i][3] + "</b></br></br>");
					out.println(msgs[i][2] + "</br></br></br>");
					count++;
				}
			}
			if(count == 0) {
				out.println("No messages sent.");
			}
		}
		
		out.println("</br><form action=\"Home\" method=\"GET\" id=\"returnButton\">"
				+ "<input type=\"submit\" value=\"Go back\">"
				+ "</form>"
				+ "</body>"
				+ "</html>");

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
