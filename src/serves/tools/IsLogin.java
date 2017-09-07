package serves.tools;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class IsLogin {

	public static String isLogin(HttpServletRequest request){
		HttpSession session=request.getSession(false);
		if(session!=null){
			return (String) session.getAttribute("id");
		}else{
			return null;
		}
	}
}
