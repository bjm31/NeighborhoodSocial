package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import backend.DatabaseActions;
import java.util.Arrays;

/**
 * Servlet implementation class CreateAccountServlet
 */
@WebServlet("/CreateAccount")
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
			RequestDispatcher rd = request.getRequestDispatcher("badPasword.html");			//TODO  update with actual HTML page
			rd.forward(request, response);
		}
		
		DatabaseActions.createNewUser("aRandomString", username, fname, lname, email, photo); //TODO fix invite code String
		DatabaseActions.setNewPassword(username, password1);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
