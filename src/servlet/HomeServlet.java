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
 * Allows users to view posts stored on DB
 */
@WebServlet("/Home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] posts = DatabaseActions.viewPosts();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String doctype = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		String displayName = "";
		String filterType = request.getParameter("filterType");
		String postType = "";

		//HTML output
		out.println(doctype + "<html>"
				+ "<head>"
				+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>"
				+ "<title>View Posts</title>"
				+ "<style>"
				+ "#postList {text-align : center;"
				+ "			  padding-top: 11%;}"
				+ "#post {width : 40%;"
				+ "		  text-align : left;"
				+ "		  border-style : solid;}"
				+ "#buttons{float : left;"
				+ "			width : 50%;"
				+ "			margin: 5px;}"
				+ "#button{display: inline-block;"
				+ "}"
				+ "#filter{padding-bottom: 20px;}"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<h1><u>Neighborhood Social</u></h1>"
				+ "</br>"
				+ "<div id=\"buttons\">"
				+ "<form action=\"MakePost.html\" method=\"GET\" id=\"button\">"
				+ "<input type=\"submit\" value=\"Make Post\"/>"
				+ "</form>"
				+ "<form action=\"ViewProfile\" method=\"GET\" id=\"button\" class=\"servlet.ViewProfileServlet\">"
				+ "<input type=\"submit\" value=\"View Profile\">"
				+ "</form>"
				+ "<form action=\"NeighborList\" method=\"GET\" id=\"button\">"
				+ "<input type=\"submit\" value=\"View Neighbors\">"
				+ "</form>"
				+ "<form action=\"ViewMessages\" method=\"GET\" id=\"button\">"
				+ "<input type=\"submit\" value=\"View Messages\">"
				+ "</form>"
				+ "<form action=\"Logout\" method=\"GET\" id=\"button\" class=\"servlet.Logout\">"   
				+ "<input type=\"submit\" value=\"Logout\">"
				+ "</form>"
				+ "<h2 style=\"text-padding:center;\">Neighbors' Posts:</h2>");
		
		if(filterType == null || filterType.compareTo("") == 0) {
			
			out.print("<form action=\"Home\" method=\"GET\" id=\"filter\" class=\"servlet.Home\" onchange=\"$('#filter').submit();\">"
				+ "Filter by: <select name=\"filterType\">\n" 
				+ "				<option value=\"\">View All</option>\n"  
				+ "				<option value=\"For Sale\">For Sale</option>\n" 
				+ "				<option value=\"Help Wanted\">Help Wanted</option>\n"
				+ "				<option value=\"Events\">Events</option>\n"
				+ "			 </select>"
				+ "</form>"	);
		}
		
		if(filterType != null && filterType.compareTo("For Sale") == 0) {		

			out.print("<form action=\"Home\" method=\"GET\" id=\"filter\" class=\"servlet.Home\" onchange=\"$('#filter').submit();\">"
				+ "Filter by: <select name=\"filterType\">\n"
				+ "				<option value=\"For Sale\">For Sale</option>\n" 
				+ "				<option value=\"Help Wanted\">Help Wanted</option>\n"
				+ "				<option value=\"Events\">Events</option>\n"
				+ "				<option value=\"\">View All</option>\n"  
				+ "			 </select>"
				+ "</form>"	);
		}
		
		if(filterType != null && filterType.compareTo("Help Wanted") == 0) {
			
			out.print("<form action=\"Home\" method=\"GET\" id=\"filter\" class=\"servlet.Home\" onchange=\"$('#filter').submit();\">"
				+ "Filter by: <select name=\"filterType\">\n" 
				+ "				<option value=\"Help Wanted\">Help Wanted</option>\n"
				+ "				<option value=\"For Sale\">For Sale</option>\n" 
				+ "				<option value=\"Events\">Events</option>\n"
				+ "				<option value=\"\">View All</option>\n"
				+ "			 </select>"
				+ "</form>"	);
		}
		
		if(filterType != null && filterType.compareTo("Events") == 0) {		
			
			out.print("<form action=\"Home\" method=\"GET\" id=\"filter\" class=\"servlet.Home\" onchange=\"$('#filter').submit();\">"
				+ "Filter by: <select name=\"filterType\">\n" 
				+ "				<option value=\"Events\">Events</option>\n"
				+ "				<option value=\"For Sale\">For Sale</option>\n" 
				+ "				<option value=\"Help Wanted\">Help Wanted</option>\n"
				+ "				<option value=\"\">View All</option>\n"
				+ "			 </select>"
				+ "</form>"	);
		}
				out.println("</div>"
				+ "<div id=\"postList\">");
				
		
		//Read string array full of posts
		//Display posts according to filter type
		ObjectId n_id;
		for( int i = posts.length - 1; i >= 0; i--) {
						
			String tokens[] = posts[i].split("\n");
			
			displayName = tokens[1];
			postType = tokens[5];

			n_id = new ObjectId(posts[i].substring(posts[i].indexOf('>') + 1, posts[i].indexOf('<', 1)));
			byte[] pic = DatabaseActions.getPicture(n_id);
			pic = PhotoScaler.resizeByteArray(75, 75, pic);
			byte[] encoded = Base64.getEncoder().encode(pic);
			
			if(filterType == null || filterType.compareTo("") == 0) {  //show all posts
				
				out.println("<div id=\"post\">"
						+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>"
						+  posts[i]
						+ "<a href=\" \">Reply</a>"
						+ "</div></br></br></br>");
			}
			if(filterType != null && filterType.compareTo("For Sale") == 0) {  //show For Sale posts
				
				if(postType.compareTo(filterType) == 0) {
					out.println("<div id=\"post\">"
							+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>" 
							+ posts[i]
							+ "<a href=\" \">Reply</a>"
							+ "</div></br></br></br>");
				}
			}
			if(filterType != null && filterType.compareTo("Help Wanted") == 0) {  //show Help Wanted posts
				
				if(postType.compareTo(filterType) == 0) {
					out.println("<div id=\"post\">"
							+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>" 
							+ posts[i]
							+ "<a href=\" \">Reply</a>"
							+ "</div></br></br></br>");
				}
			}
			if(filterType != null && filterType.compareTo("Events") == 0) {  //show Events posts
				
				if(postType.compareTo(filterType) == 0) {
					out.println("<div id=\"post\">" 
							+ "<img src =\"data:image/jpg;base64," + new String(encoded) +"\" alt=\"Image Not Found\"></br>" 
							+ posts[i]
							+ "<a href=\" \">Reply</a>"
							+ "</div></br></br></br>");
				}
			}

			
		}
			out.println("</div>"
					+ "</form>"
					+ "</body>"
					+ "</html>");
			

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
