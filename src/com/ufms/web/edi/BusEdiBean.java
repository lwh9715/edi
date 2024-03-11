package com.ufms.web.edi;

import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import org.apache.commons.io.IOUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CIMC
 */

@WebServlet("/api")
@ManagedBean(name = "pages.module.edi.busEdiBean")
public class BusEdiBean extends BaseServlet {

    /**
     * 查詢单条订单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "query")
    public BaseView BusEdiJob(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "base.edi.jobinfo";
            Map args = new HashMap(10);
            args.put("nos", json.equals("") ? "" : " x.jobno ILIKE '%" + json + "%'");

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("jobs", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
