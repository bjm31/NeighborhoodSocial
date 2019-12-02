package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class SaveMessageServlet
 */
@WebServlet("/SaveMessage")
public class SaveMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	PrintWriter out;
	User user;
	HttpSession session;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name");
		String previous = request.getParameter("previous");
		String message = request.getParameter("message");
		
		
		out = response.getWriter();
		session = request.getSession(false);
		if (session == null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.forward(request, response);
		}
		user = (User) session.getAttribute("user");
	
		ObjectId to_id = DatabaseActions.getN_id2(name);	
	
		DatabaseActions.saveMessage(user.getN_id(), to_id, message);
		
		if(previous.equals("ViewNeighbor")) {
			previous = "NeighborList";
		}
		RequestDispatcher rd = request.getRequestDispatcher(previous);
		rd.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
