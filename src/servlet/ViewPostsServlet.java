package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import backend.DatabaseActions;

/**
 * Allows users to view posts stored on DB
 */
@WebServlet("/ViewPosts")
public class ViewPostsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] posts = DatabaseActions.viewPosts();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		
		//HTML output to page
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<title>View Posts</title>"
				+ "<style>"
				+ "#postList {text-align : center;"
				+ "			  padding-top: 3%;}"
				+ "#post {width : 40%;"
				+ "		  text-align : left;"
				+ "		  border-style : solid;}"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<h1 style=\"text-padding:center;\"><u>Posts</u></h1>"
				+ "<div id=\"postList\">");
		
		//Read string array full of posts
		for( int i = 0; i < posts.length; i++) {
			
			
			out.println("<div id=\"post\">" + posts[i] + "</div></br></br></br>");

		}
			out.println("</div>"
					+ "<form action=\"home.html\" method=\"GET\" id=\"button\">"
					+ "<input type=\"submit\" value=\"Home\"/>"
					+ "</form>"
					+ "</body>"
					+ "</html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
