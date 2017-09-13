package serves.user;

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

import serves.tools.IsLogin;
import sql.MySQLInformation;
import sql.UserTable;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class UserInformation
 */
@WebServlet("/UserInformation")
public class UserInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserTable ut;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInformation() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String id=request.getParameter("id");
		PrintWriter out=response.getWriter();
		JsonObject jo=new JsonObject();
		if(id==null){
			id=IsLogin.isLogin(request);
		}
		try {
			ResultSet rs=ut.selectById(id);
			if(rs.next()){
				jo.addProperty("name", rs.getString("name"));
				jo.addProperty("type", rs.getString("type"));
				jo.addProperty("agree_sum", rs.getString("agree_sum"));
				jo.addProperty("photo", rs.getString("photo"));
			}else{
				jo.addProperty("information", "this people is not found");
			}
			jo.addProperty("idOK", true);
			out.println(jo.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			jo.addProperty("idOK", false);
			response.setStatus(500);
			out.println(jo.toString());
			e.printStackTrace();
		}
	}

}
