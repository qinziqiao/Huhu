package serves.interest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import serves.tools.GlobalVar.GlobalParameter;
import sql.LabelTable;
import sql.QuestLabelMapTable;

/**
 * Servlet implementation class SelectAllLabel
 */
@WebServlet("/SelectAllLabel")
public class SelectAllLabel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectAllLabel() {
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
		boolean isOK = true;
		PrintWriter out=null;
		response.setContentType("text/html;charset=utf-8");
	    response.setCharacterEncoding("utf-8");
		//查看请求
		try {			
			out= response.getWriter();
		}catch (IOException e) {
			System.out.println("io异常");
			try {
				response.sendError(404, "您要查找的资源不存在");
			} catch (IOException e1) {
				System.out.println("IO异常");
			}
		}		
		
		try {
			LabelTable lt=new LabelTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs=lt.getAllLabel();
			//对结果集进行JSON解析
			if(rs.next()==false){
				//没有找到数据
				response.setStatus(501);
				Response(out, false,null);
			}
			else{
				rs.beforeFirst();
				Response(out, true,rs);
			}
			lt.CloseConnection();
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(500);
			Response(out, false,null);
			return;
		}
		
		// TODO Auto-generated method stub
	}
	
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
				int lid_temp = rs.getInt("id");
				String label_temp = rs.getString("label");
				JsonObject jo = new JsonObject();
				jo.addProperty("lid",lid_temp);
				jo.addProperty("label", label_temp);
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
