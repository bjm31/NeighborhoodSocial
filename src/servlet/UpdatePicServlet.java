package servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import backend.DatabaseActions;
import backend.User;

/**
 * Servlet implementation class UpdatePicServlet
 */
@WebServlet("/UpdatePic")
@MultipartConfig
public class UpdatePicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	HttpSession session;
	User user;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		session = request.getSession();
		user = (User) session.getAttribute("user");
		
		byte[] buffer = new byte[16384];		
		InputStream fileContent = request.getPart("photo").getInputStream();
		buffer = IOUtils.toByteArray(fileContent);
		
		for(byte b : buffer) {
			
			System.out.print(b);
		}
		
		DatabaseActions.setPicture(user.getN_id(), buffer);
		
		RequestDispatcher rd = request.getRequestDispatcher("/ViewProfile");
		rd.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
