package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MakeMessage
 */
@WebServlet("/MakeMessage")
public class MakeMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	PrintWriter out;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out = response.getWriter();
		String name = request.getParameter("name");
		String previous = request.getParameter("previous");
		
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>Make Message</title>"
				+ "</head>"
				+ "<body>"
				+ "<p>Send a message to: " + name + "</p></br>"
				+ "<form action=\"SaveMessage\" method=\"POST\">\n" 
				+ "<textarea rows=\"25\" cols=\"50\" required placeholder=\"Enter text.\"\n" 
				+ "name=\"message\"></textarea>\n"
				+ "<input type=\"hidden\" name=\"name\" value=\"" + name + "\">"
				+ "<input type=\"hidden\" name=\"previous\" value=\"" + previous + "\">"
				+ "<input type=\"submit\" value=\"Send Message\" />\n" 
				+ "</form>");

		out.println("<form action=\"" + previous + "\"previous\")\">"
				+ "<input type=\"submit\" value=\"Go Back\">"
				+ "</form>"
				+ "</body>"
				+ "</html>");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
