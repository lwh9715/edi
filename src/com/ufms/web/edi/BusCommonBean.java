package com.ufms.web.edi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import com.ufms.base.web.base.UserSession;
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
@ManagedBean(name = "pages.module.commerce.busCommonBean")
public class BusCommonBean extends BaseServlet {


    public static DaoIbatisTemplate daoIbatisTemplatesta = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

    /**
     * 获取下拉框数据接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "common")
    public BaseView queryCommon(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject object = getJsonObject(request);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            Long id = 0L;
            String code = "";
            String type = "";
            String keyword = "";
            StringBuffer sql = null;

            if (object.containsKey("query")) {
                keyword = object.get("query").toString();
            }

            if (object.containsKey("code")) {
                if (!StrUtils.isNull(object.get("code").toString())) {
                    code = object.get("code").toString();
                }
            }

            if (object.containsKey("jobtype")) {
                if (!StrUtils.isNull(object.get("jobtype").toString())) {
                    type = object.get("jobtype").toString();
                }
            }

            if (object.containsKey("id")) {
                if (!StrUtils.isNull(object.get("id").toString())) {
                    id = Long.parseLong(object.get("id").toString());
                }
            }

            if (object.get("methodFlag").toString().equals("sale")) {

                sql = querySale(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("sale", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("goodstrack")) {

                sql = queryGoodsTrack(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("goodstrack", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("report")) {

                sql = queryJasperReport(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("report", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("custype")) {

                sql = queryCustomCustype(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("custype", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("cusStatus")) {

                sql = queryCustomStatus(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("cusStatus", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("cusclass")) {

                sql = queryCustomCusclass(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("cusclass", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("cusConsignor")) {

                sql = queryCusConsignor(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("cusConsignor", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("port")) {

                sql = queryPort(type, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("port", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("address")) {

                sql = queryAddress(code, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("address", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("product")) {

                sql = queryProduct(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("product", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("consignor")) {

                sql = queryConsignor(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("consignor", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("approver")) {

                sql = queryApprover(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("approver", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("bookingagent")) {

                sql = queryServiceAgent(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("bookingagent", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("deliveryagent")) {

                sql = queryServiceAgent(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("deliveryagent", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("clearanceagent")) {

                sql = queryServiceAgent(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("clearanceagent", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("warehouse")) {

                sql = queryWarehouse(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("warehouse", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("contact")) {

                sql = queryContact(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("contact", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("common")) {

                StringBuffer company = queryCompany(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(company.toString());
                view.add("company", StrUtils.getMapVal(result, "json"));

                StringBuffer service = queryService(keyword);
                Map<String, String> result1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(service.toString());
                view.add("service", StrUtils.getMapVal(result1, "json"));

                StringBuffer cntype = queryCntype(keyword);
                Map<String, String> result2 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(cntype.toString());
                view.add("cntype", StrUtils.getMapVal(result2, "json"));

                StringBuffer delivery = queryDelivery(keyword);
                Map<String, String> result3 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(delivery.toString());
                view.add("delivery", StrUtils.getMapVal(result3, "json"));

                StringBuffer serverType = queryServerType(keyword);
                Map<String, String> result4 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(serverType.toString());
                view.add("serverType", StrUtils.getMapVal(result4, "json"));

//                StringBuffer deliveryStatus = queryDeliveryStatus(keyword);
//                Map<String, String> result5 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deliveryStatus.toString());
//                view.add("deliveryStatus", StrUtils.getMapVal(result5, "json"));

                StringBuffer goodtypelist = queryGoodtype(keyword);
                Map<String, String> result6 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(goodtypelist.toString());
                view.add("goodtype", StrUtils.getMapVal(result6, "json"));

                StringBuffer packagelist = queryPackage(keyword);
                Map<String, String> result7 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(packagelist.toString());
                view.add("package", StrUtils.getMapVal(result7, "json"));

                StringBuffer serviceterm = queryServiceterm(keyword);
                Map<String, String> result8 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(serviceterm.toString());
                view.add("serviceterm", StrUtils.getMapVal(result8, "json"));
            }
        } catch (Exception e) {
            view.setData(null);
        }
        return view;
    }

    /**
     * 获取业务员sql
     */
    private StringBuffer querySale(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.corpid,sc.deptid,(select abbr from sys_corporation s where s.id = sc.corpid limit 1) as corper," +
                "(select name from sys_department sd where sd.id = sc.deptid limit 1) as depter FROM sys_user sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND (sc.id = " + id + ")");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND sc.issales = true AND sc.isdelete = false ORDER BY sc.code ASC LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取委托人sql
     */
    private StringBuffer queryConsignor(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.namee,sc.salesid,sc.abbr FROM sys_corporation sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND sc.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%') OR (sc.code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND sc.isdelete = false ORDER BY sc.code LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取分公司sql
     */
    private StringBuffer queryCompany(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id, sc.code, sc.namec, sc.abbr FROM sys_corporation sc WHERE sc.iscustomer = FALSE ORDER BY sc.code) T;");
        return sql;
    }

    /**
     * 获取服务类型sql
     */
    private StringBuffer queryService(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE t.fkcode = 250 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取派送方式sql
     */
    private StringBuffer queryDelivery(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 1250 and t.code <> '' LIMIT 10) T;");
        return sql;
    }


    /**
     * 获取报关状态sql
     */
    private StringBuffer queryCustomStatus(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 100 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取报关公司sql
     */
    private StringBuffer queryCusConsignor(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.namee,sc.salesid,sc.abbr FROM sys_corporation sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND sc.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (sc.namec ILIKE '%" + keyword + "%' OR  sc.namee ILIKE '%" + keyword + "%')");
        }
        sql.append("\n AND sc.iscustom = true AND sc.isdelete = false ORDER BY sc.code DESC LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取报关方式sql
     */
    private StringBuffer queryCustomCusclass(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 40 and t.namec <> '报关方式' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取报关类型sql
     */
    private StringBuffer queryCustomCustype(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 45 and t.namec <> '报关类型' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取轨迹模板sql
     */
    private StringBuffer queryGoodsTrack(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.* FROM bus_goodstrack_temp t WHERE tmptype = 'T' ORDER BY id) T;");
        return sql;
    }

    /**
     * 货物类型
     */
    private StringBuffer queryGoodtype(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT code,namec FROM dat_filedata WHERE isleaf = true and fkcode = 1260 and code <> '') T;");
        return sql;
    }

    /**
     * 服务条款
     */
    private StringBuffer queryServiceterm(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE isleaf = true and fkcode = 1270 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 地址簿
     */
    private StringBuffer queryAddress(String code, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id,code,attn,companyname,postcode,countrycode,province,city,addressone,tel FROM dat_address WHERE 1 = 1");
        if (!StrUtils.isNull(code)) {
            sql.append("\n AND code = '" + code + "'");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (companyname ILIKE '%" + keyword + "%' OR  code ILIKE '%" + keyword + "%')");
        }
        sql.append("\n AND isdelete = false ORDER BY code DESC LIMIT 50) T;");
        return sql;
    }

    /**
     * 产品名称
     */
    private StringBuffer queryProduct(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id, code, namec, namee FROM dat_product WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (code ILIKE '%" + keyword + "%' OR  namec ILIKE '%" + keyword + "%')");
        }
        sql.append("\n AND isdelete = false ORDER BY code DESC LIMIT 20) T;");
        return sql;
    }


    /**
     * 获取派送状态sql
     */
    private StringBuffer queryDeliveryStatus(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT code,namec FROM dat_filedata WHERE fkcode = 1310 AND code <> '' ORDER BY code LIMIT 20) T;");
        return sql;
    }

    /**
     * 获取服务类型sql
     */
    private StringBuffer queryServerType(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 1320 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取港口sql
     */
    private StringBuffer queryPort(String type, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.id,t.code,t.namec,t.namee FROM dat_port t WHERE 1 = 1");
        if (!StrUtils.isNull(type) && (type.equals("H") || type.equals("X") || type.equals("T"))) {
            sql.append("\n AND t.isair = false");
        }
        if (!StrUtils.isNull(type) && type.equals("K")) {
            sql.append("\n AND t.isair = true");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((namec ILIKE '%" + keyword + "%') OR (namee ILIKE '%" + keyword + "%') OR (code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND isdelete = FALSE ORDER BY t.code LIMIT 10) T;");
        return sql;
    }

    /**
     * 柜型
     */
    private StringBuffer queryCntype(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id, code  FROM dat_cntype WHERE isdelete = FALSE  ORDER BY code) T;");
        return sql;
    }

    /**
     * 包装
     */
    private StringBuffer queryPackage(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.namee,t.namee FROM dat_package t WHERE isdelete = FALSE ORDER BY namee) T;");
        return sql;
    }

    /**
     * 审核人
     */
    private StringBuffer queryApprover(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.id,t.code,t.namec,t.corper FROM sys_user t WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND t.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (( t.namec ILIKE '%" + keyword + "%') OR (t.namee ILIKE '%" + keyword + "%') OR (t.code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n and isdelete = FALSE and isadmin = 'N' ORDER BY t.code limit 10) T;");
        return sql;
    }

    /**
     * 订舱代理，派送代理，清关代理
     */
    private StringBuffer queryServiceAgent(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.namee,sc.salesid,sc.abbr FROM sys_corporation sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND sc.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n and (( sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%') OR (sc.code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n and isdelete = FALSE ORDER BY sc.code limit 10) T;");
        return sql;
    }

    /**
     * 仓库档案
     */
    private StringBuffer queryWarehouse(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT dw.id, dw.code, dw.namec, dw.namee, dw.addressc FROM dat_warehouse dw WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND dw.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n and (( dw.namec LIKE '%" + keyword + "%') OR (dw.namee iLIKE '%" + keyword + "%'))");
        }
        sql.append("\n and isdelete = FALSE ORDER BY id limit 10) T;");
        return sql;
    }

    /**
     * 报表/面单列表
     */
    private StringBuffer queryJasperReport(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (select code,namec,modcode from sys_report WHERE modcode = 'commerce'");
        sql.append("\n and isdelete = FALSE ORDER BY code limit 10) T;");
        return sql;
    }

    /**
     * 仓库收发通信息
     */
    private StringBuffer queryContact(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.customerabbr,sc.name,sc.contactxt,sc.inputer,sc.updater FROM sys_corpcontacts sc where 1 = 1");
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n and (( sc.customerabbr ILIKE '%" + keyword + "%') OR (sc.name ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n and sc.isdelete = FALSE limit 10) T;");
        return sql;
    }


    /**
     * 解析前端传获取值
     *
     * @param request
     * @return
     * @throws IOException
     */
    private JSONObject getJsonObject(HttpServletRequest request) throws IOException {
        String json;
        InputStream is = request.getInputStream();
        json = IOUtils.toString(is, "UTF-8");
        json = StrUtils.getSqlFormat(json);
        return JSONObject.parseObject(json);
    }

    /**
     * 跳转工作单
     *
     * @param request
     * @return
     */
    @Action(method = "skipJob")
    public BaseView skipJob(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject object = getJsonObject(request);

            String querySql = "base.commerce.querynos";
            Map args = new HashMap();
            args.put("nos", "\n AND nos = '" + object.get("nos").toString().toUpperCase() + "'");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("nos", StrUtils.getMapVal(list.get(0), "nos"));
            view.setData(String.valueOf(view));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Action(method = "getBusnodesclist")
    public BaseView BusCommerceGetBusnodesclist(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql = "pages.module.commerce.busCommonBean.grid.page";
            String querySql1 = "pages.module.commerce.busCommonBean.grid.count";

            Map args = new HashMap();
            String qry = "\n1=1 AND r.corpid =" + jsonObject.get("corpid").toString();
            args.put("qry", qry);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> list1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql1, args);
            view.add("list", JSON.toJSONString(list));
            view.add("total", StrUtils.getMapVal(list1.get(0), "counts"));
            view.setData(String.valueOf(view));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    //获取工作单编号
    @Action(method = "getnos")
    public BaseView getnos(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String code = jsonObject.get("code").toString();
            String corpid = jsonObject.get("corpid").toString();
            String querySql = "select f_auto_busno('code=" + code + ";date='||to_char(NOW(),'yyyy-MM-dd')||';corpid='||" + corpid + "||';') as nos;";
            Map<String, String> result1 = daoIbatisTemplatesta.queryWithUserDefineSql4OnwRow(querySql);
            view.add("nos", StrUtils.getMapVal(result1, "nos"));
            view.setData(String.valueOf(view));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取登录用户名
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "checkLoginUser")
    public BaseView checkLoginUser(HttpServletRequest request) {
        BaseView view = new BaseView();
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        view.setData(userSession.getCorpid().toString());
        return view;
    }

}
