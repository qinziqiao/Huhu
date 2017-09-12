package serves.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sql.MySQLInformation;
import sql.UserTable;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	int a=0;
	private static final long serialVersionUID = 1L;
	private UserTable ut; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		//�ѵ�½����
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		if(session!=null){
			out.println(session.getAttribute("id"));
		}else{
			out.println("please login again");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String account,password;
		account=request.getParameter("account");
		password=request.getParameter("password");
		JsonObject jo=new JsonObject();
		PrintWriter out=response.getWriter();
		ResultSet rs;
		try {
			rs = ut.selectByAccountPassword(account, password);
			if(rs.next()){
				//��½�ɹ�
				HttpSession session=request.getSession(true);
				session.setAttribute("id",rs.getString("id"));
				jo.addProperty("uid", rs.getString("id"));
				jo.addProperty("type", 1);
				jo.addProperty("isOK", true);
				jo.addProperty("information", "login success");
				out.println(jo.toString());
			}else{
				//��½ʧ��
				jo.addProperty("type", 0);
				jo.addProperty("isOK", true);
				jo.addProperty("information", "account or password is not correct");
				out.println(jo.toString());
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
