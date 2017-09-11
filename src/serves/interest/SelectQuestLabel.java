package serves.interest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.GlobalVar.GlobalParameter;
import sql.QuestLabelMapTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@WebServlet("/SelectQuestLabel")
public class SelectQuestLabel extends HttpServlet{

	 
	/**
	 * 处理查询请求，需要request，post型，
	 * 含有 用户uid 参数
	 * TODO: 用户是否是已经登录的状态，使用cookie
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response){	
		int request_qid=-1;
		boolean isOK = true;
		PrintWriter out=null;
		response.setContentType("text/html;charset=utf-8");
	    response.setCharacterEncoding("utf-8");
		//查看请求
		try {
			request_qid = Integer.parseInt( request.getParameter("qid"));			
			out= response.getWriter();
		}catch (IOException e) {
			System.out.println("io异常");
			//TODO:如何处理该异常
			try {
				response.sendError(404, "您要查找的资源不存在");
			} catch (IOException e1) {
				System.out.println("IO异常");
			}
		}catch (NumberFormatException  e) {
			System.out.println("所填数据异常");
			isOK = false;
			if(out!=null){
				response.setStatus(500);
				Response(out, false,null);
			}
			return;
		} 
		
		if(request_qid<0){
			response.setStatus(500);
			Response(out, false,null);
			return;
		}
		
		try {
			QuestLabelMapTable questLabelMapTable=new QuestLabelMapTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs =questLabelMapTable.getQLMT(request_qid);
			//对结果集进行JSON解析
			if(rs.next()==false){
				//没有找到数据
				response.setStatus(500);
				Response(out, false,null);
			}
			else{
				rs.beforeFirst();
				Response(out, true,rs);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(500);
			Response(out, false,null);
			return;
		}
		
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response){
			doGet(request, response);
	}
	
	/**
	 * response响应数据
	 * @param out
	 * @param isOK
	 * @return 是否满足了请求
	 * 如果返回false，可能后台服务异常，也可能是数据库里面找不到所需数据
	 */
	private boolean Response(PrintWriter out, boolean isOK,ResultSet rs){
		JsonObject jObject = new JsonObject();
		//如果不OK
		if(!isOK){
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		//如果OK
		JsonArray jarray  = new JsonArray();
		try {
			while(rs.next()){
				int lid_temp = rs.getInt("lid");
				JsonObject jo = new JsonObject();
				jo.addProperty("label",lid_temp);
				jarray.add(jo);
			}		
		} catch (SQLException e) {
			isOK=false;
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		
		jObject.addProperty("isOK", isOK);
		jObject.add("labels",jarray);
		out.print(jObject.toString());
		out.flush();
		out.close();
		return true;
	}

}
