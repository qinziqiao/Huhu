package serves.interest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import serves.tools.GlobalVar.GlobalParameter;
import sql.QuestLabelMapTable;

@WebServlet("/AddQuestLabel")
public class AddQuestLabelServlet extends HttpServlet{
	
	/**
	 * 处理请求，需要request，post型，
	 * 含有 用户uid、lid 俩参数
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		
		int request_qid=-1;
		int request_lid=-1;
		boolean isOK=true;
		//查看请求
		try {
			request_qid = Integer.parseInt( request.getParameter("qid"));
		    request_lid = Integer.parseInt( request.getParameter("lid"));
		} catch (NumberFormatException  e) {
			System.out.println("传输请求数据格式异常");
			isOK = false;
			Response(response, isOK);
			return;
		}	
		
		if(request_qid<0|| request_lid<0){
			Response(response, false);
			return;
		}
		
		//操作数据库
		try {
			QuestLabelMapTable ql=new QuestLabelMapTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			if(ql.setQLMT(request_qid, request_lid)){
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
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			doGet(request, response);
	}
	
	
	//response响应数据
		private boolean Response(HttpServletResponse response, boolean isOK){
			PrintWriter out;
			try {
				out = response.getWriter();
				String msg = null;
				//回复
				response.setContentType("text/html;charset=utf-8");
			    response.setCharacterEncoding("utf-8");
			    JsonObject jObject = new JsonObject();
			    
			    jObject.addProperty("isOK", isOK);
				out.print(jObject.toString());
				out.close();
				
			} catch (IOException e) {
				System.err.println(e);
			}
			return true;
		}
	                                         

}
