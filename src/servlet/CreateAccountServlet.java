package servlet;

import backend.DatabaseActions;
import backend.User;
import backend._PasswordManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;

import java.util.Arrays;

/**
 * Servlet implementation class CreateAccountServlet
 */
@WebServlet(urlPatterns = "/CreateAccount", initParams = @WebInitParam(name = "location", value = "$UPLOAD"))
@MultipartConfig
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
		
		String username 	= request.getParameter("username").toLowerCase();
		String fname		= request.getParameter("fname");
		String lname		= request.getParameter("lname");
		String email		= request.getParameter("email");
		String photo		= Paths.get(request.getPart("photo").getSubmittedFileName()).getFileName().toString();
		
		
		byte[] buffer = new byte[16384];		
		InputStream fileContent = request.getPart("photo").getInputStream();
		buffer = IOUtils.toByteArray(fileContent);

		
		char[] password1	= request.getParameter("password").toCharArray();
		char[] password2	= request.getParameter("password_confirm").toCharArray();
		boolean goodUsername= true;
		boolean goodPassword= true;
		
		if (!Arrays.equals(password1, password2)) {
			goodPassword = false;
			_PasswordManager.clear(password1);
			_PasswordManager.clear(password2);

			RequestDispatcher rd = request.getRequestDispatcher("invalidPassword.html");
			rd.forward(request, response);
		}
		
		else {
			if (DatabaseActions.doesUsernameExist(username)) {
				goodUsername = false;

				RequestDispatcher rd = request.getRequestDispatcher("invalidUsername.html");
				rd.forward(request, response);
			}
		}
		
		if (goodPassword && goodUsername) {
			
			HttpSession session = request.getSession();
			if (session == null) {
				RequestDispatcher rd = request.getRequestDispatcher("index.html");
				rd.forward(request, response);
			}
			User user = (User) request.getSession().getAttribute("user");
			
			
			
			
			String inviteCode = user.getInviteCode();
			DatabaseActions.createNewUser(inviteCode, username, fname, lname, email, photo, buffer);
			DatabaseActions.setNewPassword(username, password1);
			
			ObjectId n_id = DatabaseActions.getN_id(username);
			user.setN_id(n_id);

			session.setAttribute("user", user);
			System.out.println("username: " + user.getUser());
			System.out.println("id: " + user.getN_id());

			
			
			_PasswordManager.clear(password1);
			_PasswordManager.clear(password2);
			
			RequestDispatcher rd = request.getRequestDispatcher("/Home");
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
