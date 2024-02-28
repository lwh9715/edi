<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    </head>
    <body>
    	<% 
    		String server = request.getServerName();
    		out.print(server);
    		if(server.equalsIgnoreCase("www.sobooking.cn")){
    			response.sendRedirect("http://www.sobooking.cn/so/sobooking/index.html");
    		}else if(server.equalsIgnoreCase("so.ufms.cn") || server.equalsIgnoreCase("sobooking.ufms.cn")){
    			response.sendRedirect("http://so.ufms.cn/so/sobooking/index.html");
    		}else{
    			response.sendRedirect("http://47.90.101.204/hx");
    		}
    	%>
    </body>
</html>