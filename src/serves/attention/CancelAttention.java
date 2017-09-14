package serves.attention;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.IsLogin;
import serves.tools.GlobalVar.GlobalParameter;
import sql.AttentionTable;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class CancelAttention
 */
@WebServlet("/CancelAttention")
public class CancelAttention extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelAttention() {
        super();
        // TODO Auto-generated constructor stub
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
		String request_id_string =IsLogin.isLogin(request);
		if(request_id_string==null){
			response.setStatus(500);
			Response(response, false);
			return;
		}
			
		int request_id=-1;
		int request_att_id=-1;
		boolean isOK=true;
		//查看请求
		try {
			request_id = Integer.parseInt( request_id_string );
		    request_att_id = Integer.parseInt( request.getParameter("att_id"));
		} catch (NumberFormatException  e) {
			System.out.println("传输请求数据格式异常");
			isOK = false;
			Response(response, isOK);
			return;
		}	
		
		if(request_id<0|| request_att_id<0){
			Response(response, false);
			return;
		}
		
		//操作数据库
		try {
			AttentionTable at=new AttentionTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			if(at.delete(request_id+"", request_att_id+"")!=0){
				isOK=true;
			}
			else{
				isOK=false;
			}
			Response(response, isOK);
		
		} catch (ClassNotFoundException e) {
			System.out.println("找不到数据库类");
			isOK=false;
			Response(response, isOK);
		} catch (SQLException e) {
			System.out.println("无法连接到数据库或操作失败");
			System.out.println(e);
			isOK=false;
			Response(response, isOK);
		}	
		
	}
	
	//response响应数据
	private boolean Response(HttpServletResponse response, boolean isOK){
		PrintWriter out;
		JsonObject jObject =new JsonObject();
		try {
			out = response.getWriter();
			String msg = null;
			//回复
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
				   
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
					
			} catch (IOException e) {
					System.err.println(e);
			}
			return true;
		}
		

}