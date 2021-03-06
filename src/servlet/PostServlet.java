package servlet;

import java.io.IOException;
import backend.User;
import backend.DatabaseActions;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bson.types.ObjectId;


/**
 * Takes post info from MakePost.html and saves it to the DB
 */
@WebServlet("/Post")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private String post; 
    private String postType;
    private User user;
    HttpSession session;
        
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		session = request.getSession(false); //continue current session
		if (session == null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.forward(request, response);
		}
		user = (User) session.getAttribute("user");
		ObjectId n_id = user.getN_id();
		
		postType = request.getParameter("postType");
		post = request.getParameter("post");

		
		DatabaseActions.savePost(n_id, postType, post);	
		
		response.sendRedirect("Home");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
