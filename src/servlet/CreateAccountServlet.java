package servlet;

import backend.DatabaseActions;
import backend.User;
import backend._PasswordManager;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Servlet implementation class CreateAccountServlet
 */
@WebServlet(urlPatterns = "/CreateAccount", initParams = @WebInitParam(name = "location", value = "$UPLOAD"))
public class CreateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAccountServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username 	= request.getParameter("username");
		String fname		= request.getParameter("fname");
		String lname		= request.getParameter("lname");
		String email		= request.getParameter("email");
		String photo		= request.getParameter("photo");
		char[] password1	= request.getParameter("password").toCharArray();
		char[] password2	= request.getParameter("password_confirm").toCharArray();
		
		if (!Arrays.equals(password1, password2)) {
			
			_PasswordManager.clear(password1);
			_PasswordManager.clear(password2);
			
			RequestDispatcher rd = request.getRequestDispatcher("badPasword.html");			//TODO  update with actual HTML page
			rd.forward(request, response);
		}
		
		else {
			User user = (User) request.getSession().getAttribute("user");
			String inviteCode = user.getInviteCode();
			DatabaseActions.createNewUser(inviteCode, username, fname, lname, email, photo);
			DatabaseActions.setNewPassword(username, password1);
			
			_PasswordManager.clear(password1);
			_PasswordManager.clear(password2);
			
			RequestDispatcher rd = request.getRequestDispatcher("/Home");			//TODO  update with actual HTML page
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
