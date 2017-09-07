package serves.attention;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.GlobalVar.GlobalParameter;
import sql.AttentionTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SelectFollower
 */
@WebServlet("/SelectFollower")
public class SelectFollower extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectFollower() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *
	 * 请求中需要有att_id参数
	 * 返回客户端该id所有粉丝
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		int request_att_id=-1;
		boolean isOK = true;
		PrintWriter out=null;
		response.setContentType("text/html;charset=utf-8");
	    response.setCharacterEncoding("utf-8");
		//查看请求
		try {
			request_att_id = Integer.parseInt( request.getParameter("att_id"));
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
			if(out!=null)
				Response(out, false,null);
			return;
		} 
		
		if(request_att_id<0){
			Response(out, false,null);
			System.out.println("查询为负数");
			return;
		}
		
		try {
			AttentionTable at=new AttentionTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs =at.selectByAtt_id(request_att_id+"");
			
			//对结果集进行JSON解析
			if(rs.next()==false){
				//没有找到数据
				Response(out, false,null);
			}
			else{
				rs.beforeFirst();
				Response(out, true,rs);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			Response(out, false,null);
			return;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * response响应数据
	 * @param out
	 * @param isOK
	 * @return 是否满足了请求，
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
				int temp = rs.getInt("id");
				JsonObject jo = new JsonObject();
				jo.addProperty("Follower",temp);
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
		jObject.add("Followers",jarray);
		out.print(jObject.toString());
		out.flush();
		out.close();
		return true;
	}

}
