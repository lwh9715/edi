package com.ufms.web.view;

import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.db.DaoUtil;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import com.ufms.base.web.base.UserSession;

@WebServlet("/index")
@ManagedBean(name = "pages.module.indexBean")
public class IndexBean extends BaseServlet {
	
	
	@Action(method="logout")
	public BaseView logout(HttpServletRequest request) {
		BaseView baseView = new BaseView();
		baseView.setSuccess(false);
		try {
			request.getSession().removeAttribute("SESSIONKEY");
			request.getSession().setAttribute("userSession",null);
			baseView.setSuccess(true);
			baseView.setMessage("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseView;
	}
	
	
	@Action(method="checkLogin")
	public BaseView checkLogin(HttpServletRequest request) {
		BaseView baseView = new BaseView();
		baseView.setSuccess(false);
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			String token = request.getParameter("token");
			String sid = request.getParameter("sid");
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			if(!StrUtils.isNull(sid)&&!"undefined".equals(sid)&&userSession!=null&&userSession.getSid().equals((sid))){
				String username = "";
				String userinfo = "";
				if(userSession.getSystemUser()){
					String usersql = "SELECT COALESCE(namec,'') AS user ,'-'" +
							"||COALESCE((SELECT name FROM sys_department WHERE id = x.deptid),'')" +
							"||'-'||COALESCE((SELECT abbr FROM sys_corporation WHERE id = x.corpid),'') AS userinfo FROM sys_user x WHERE id= "+userSession.getUserid();
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(usersql);
					username = m.get("user").toString();
					userinfo = m.get("userinfo").toString();
				}else{
					username = userSession.getUsername();
				}
				baseView.setSuccess(true);
				baseView.setMessage("check ok");
				baseView.add("username", username);
				baseView.add("userinfo", userinfo);
			}else{
			}
			//System.out.println(token + "--" + sid + "--" + request.getRequestedSessionId());
		} catch (Exception e) {
			e.printStackTrace();
			baseView.setMessage("ERROR");
		}
		return baseView;
	}
	
	@Action(method="pricehomepage")
	public String pricehomepage(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		StringBuilder sb = new StringBuilder();
		sb.append("select");
		sb.append("\n datefm||'/'||dateto AS datefmto,t.tt,t.id , t.pol, t.pod,t.via,t.shipping,t.cost20+ph.cost20 AS cost20p,t.cost40gp+ph.cost40gp AS cost40gpp,t.cost40hq+ph.cost40hq AS cost40hqp");
		if(userSession==null){
			sb.append("\n,FALSE AS islogin");
		}else{
			sb.append("\n,TRUE AS islogin");
			sb.append("\n,(SELECT x.qq");
			sb.append("\n			FROM sys_user x WHERE x.isdelete = FALSE ");
			sb.append("\n			AND EXISTS(SELECT 1 FROM cs_user a,sys_corporation b WHERE a.corpid = b.id");
			sb.append("\n								AND (x.id = b.salesid OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = b.id AND x.id = userid))");
			sb.append("\n								AND a.id = "+userSession.getUserid()+") LIMIT 1) AS userqq");
		}
		sb.append("\nFROM _price_fcl t ,price_homepage ph");
		sb.append("\nWHERE isvalid = 'R'");
		sb.append("\n	AND isrelease = true");
		sb.append("\n	AND isdelete = false");
		sb.append("\n	AND t.id = ph.priceid");
		sb.append("\nORDER BY daterelease desc ,pol,pod ");
		sb.append("\nLIMIT 8");
		return DaoUtil.queryForJsonArray(sb.toString());
	}
	
	
	@Action(method="getWebBanner")
	public String getWebBanner(HttpServletRequest request) {
		String key = request.getParameter("key");
		String querySql = "select (SELECT val FROM web_config WHERE key = 'webfinabilladdress')||'/attachment/'"
			+"\n||filename AS filename  from jsonb_to_recordset((SELECT val FROM web_config WHERE key = '"+key+"' LIMIT 1)::jsonb) AS x(filename TEXT)";
		return DaoUtil.queryForJsonArray(querySql);
	}
	
	@Action(method="getwebconfig")
	public BaseView getWebConfig(HttpServletRequest request) {
		BaseView baseView = new BaseView();
		baseView.setSuccess(false);
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> lists = daoIbatisTemplate.queryWithUserDefineSql("SELECT * FROM web_config");
			for(int i=0;i<lists.size();i++){
				Map m = lists.get(i);
				String key = m.get("key").toString();
				String val = m.get("val").toString();
				baseView.add(key, val);
			}
			baseView.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			baseView.setMessage("ERROR");
		}
		return baseView;
	}
	
	@Action(method="getwebmenu")
	public BaseView getWebMenu(HttpServletRequest request) {
		BaseView baseView = new BaseView();
		baseView.setSuccess(false);
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			String sql = "SELECT * FROM web_menu WHERE ishide = FALSE ORDER BY ordno";
			String jsondata = DaoUtil.queryForJsonArray(sql);
			baseView.add("datajson", jsondata);
			baseView.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			baseView.setMessage("ERROR");
		}
		return baseView;
	}
	
	@Action(method="getCssForButton")
	public String getCssForButton(HttpServletRequest request) {
		String pid = request.getParameter("pid");
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
		String querySql = "SELECT f_common_getCssForButton('pid="+pid+"') AS css";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		return m!=null?m.get("css").toString():"";
	}
}

