package servlet;

import backend.DatabaseActions;
import backend.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InviteServlet
 */
@WebServlet("/Invite")
public class InviteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	HttpSession session;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InviteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inviteCode = request.getParameter("inviteCode");
		if (DatabaseActions.validInvite(inviteCode)) {
			
			// add user info to session
			User user = new User(inviteCode);
			session = request.getSession();
			if (session == null) {
				RequestDispatcher rd = request.getRequestDispatcher("index.html");
				rd.forward(request, response);
			}
			session.setAttribute("user",  user);;
			
			RequestDispatcher rd = request.getRequestDispatcher("new_user.html");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = request.getRequestDispatcher("invalid_code.html");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
