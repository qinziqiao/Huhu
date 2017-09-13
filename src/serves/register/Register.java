package serves.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql.MySQLInformation;
import sql.UserTable;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserTable ut;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			ut=new UserTable(MySQLInformation.uri, MySQLInformation.account, MySQLInformation.password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String account,password,type,name,photo;
		account=request.getParameter("account");
		password=request.getParameter("password");
		type=request.getParameter("type");
		name=request.getParameter("name");
		photo=request.getParameter("photo");
		PrintWriter out=response.getWriter();
		JsonObject jo=new JsonObject();
		try {
			switch (ut.insert(account, password, type, name, photo)) {
			case 0:
				jo.addProperty("isOK", true);
				jo.addProperty("type", 0);
				jo.addProperty("information", "regist success");
				out.println(jo.toString());
				break;
			case 1:
				jo.addProperty("isOK", true);
				jo.addProperty("type", 1);
				jo.addProperty("information", "account is exist");
				out.println(jo.toString());
				break;
			case 2:
				jo.addProperty("isOK", true);
				jo.addProperty("type", 2);
				jo.addProperty("information", "name is exist");
				out.println(jo.toString());
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			jo.addProperty("isOK", false);
			jo.addProperty("information", "service error");
			response.setStatus(500);
			out.println(jo.toString());
			e.printStackTrace();
		}
	}

}
