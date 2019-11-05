package servlet;

import java.io.IOException;
import backend.User;
import backend.DatabaseActions;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Takes post info from MakePost.html and saves it to the DB
 */
@WebServlet("/Post")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String post; 
    private String postType;
    private String userName;
    private User user;
    HttpSession session;
    
   
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		session = request.getSession(false); //continue current session
		user = (User) session.getAttribute("user");
		userName = user.getUser();
		
		postType = request.getParameter("postType");
		post = request.getParameter("post");
		System.out.println("Post: " + post + "\npost type: " + postType);
		
		DatabaseActions.savePost(userName, postType, post);	
		DatabaseActions.viewPosts();
		response.sendRedirect("home.html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
