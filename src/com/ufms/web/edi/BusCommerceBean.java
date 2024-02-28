package com.ufms.web.edi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.utils.ExceptionTipsUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import com.ufms.base.web.base.UserSession;
import org.apache.commons.io.IOUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CIMC
 */

@WebServlet("/commerce")
@ManagedBean(name = "pages.module.commerce.busCommerceBean")
public class BusCommerceBean extends BaseServlet {

    /**
     * 查詢单条订单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "query")
    public BaseView BusCommerceJobId(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql = "base.commerce.fina_jobs";
            String querySql1 = "base.commerce.bus_commerce";
            String querySql2 = "base.commerce.bus_packlist";
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> list1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql1, args);
            List list2 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql2, args);
            view.add("jobs", StrUtils.getMapVal(list.get(0), "jobs"));
            view.add("commerce", StrUtils.getMapVal(list1.get(0), "commerce"));
            view.add("buspacklist", list2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    /**
     * 预报创建订单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "save")
    public BaseView BusCommerceSave(HttpServletRequest request) {
        BaseView view = new BaseView();
        String resultStr = (new StringBuffer().append("{\"success\":").append(false) + "}".toString());
        try {
            UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            JSONObject finajobs = new JSONObject();
            finajobs.put("corpid", userSession.getCorpid());
            object.put("finajobs", finajobs);
            String querySql = "";
            querySql = "SELECT f_so_commerce_join('" + object.toString() + "') AS json;";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

            String reRet = "";
            if (!StrUtils.getMapVal(map, "json").isEmpty()) {

                reRet = StrUtils.getMapVal(map, "json");
                finajobs.put("id", reRet);

                String querySql1 = "SELECT f_commerce_buspacklist_join('" + object.toString() + "') AS json;";
                daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);

                String querySql2 = "SELECT id,nos from fina_jobs where id = " + reRet + " and isdelete = false limit 1";
                Map map1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql2);
                resultStr = (new StringBuffer().append("{\"success\":").append(true).append(",\"id\":\"").append(map1.get("id"))
                        .append("\",\"nos\":\"").append(map1.get("nos") + "\"}").toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setData(resultStr);
        return view;
    }

    /**
     * 小程序--预报创建订单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "appsave")
    public BaseView BusCommerceAppSave(HttpServletRequest request) {
        BaseView view = new BaseView();
        String resultStr = (new StringBuffer().append("{\"success\":").append(false) + "}".toString());

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            JSONObject finajobs = new JSONObject();
            String querySql = "";
            querySql = "SELECT f_so_commerce_join('" + object.toString() + "') AS json;";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

            String reRet = "";
            if (!StrUtils.getMapVal(map, "json").isEmpty()) {
                reRet = StrUtils.getMapVal(map, "json");
                finajobs.put("id", reRet);

                String querySql1 = "SELECT f_commerce_buspacklist_join('" + object.toString() + "') AS json;";
                daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);

                String querySql2 = "SELECT id,nos from fina_jobs where id = " + reRet + " and isdelete = false limit 1";
                Map map1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql2);
                resultStr = (new StringBuffer().append("{\"success\":").append(true).append(",\"id\":\"").append(map1.get("id"))
                        .append("\",\"nos\":\"").append(map1.get("nos") + "\"}").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setData(resultStr);
        return view;
    }

    /**
     * 小程序--工作單列表
     *
     * @param request
     * @param response
     * @return
     */
    @Action(method = "applist")
    public String appList(HttpServletRequest request, HttpServletResponse response) {
        try {
            String corpid = request.getParameter("corpid");
            String querySql = "base.commerce.applist";
            Map args = new HashMap();
            args.put("customerid", "" + corpid + "");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            return new StringBuffer().append("{\"data\":").append(StrUtils.getMapVal(list.get(0), "applist") + "}").toString();

        } catch (Exception e) {
            return ExceptionTipsUtil.getTipsJson(e);
        }
    }

    /**
     * 获取统计报表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "business")
    public BaseView BusCommerceBusinessList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            String sql = "";
            String arapsql = "";
            String isvirarapsql = "";
            String greater = "";
            String wmsin = "";
            String nossql = "";
            String corpsql = "";
            String financial = "";
            String airsql = "";
            String shipsql = "";
            String landsql = "";
            String custsql = "";
            String commercesql = "";
            String newfield = " '' AS newfield";


            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            corpsql += " AND a.corpid = " + userSession.getCorpid();

            if (object.containsKey("nostype") || object.get("nostype") != null) {

                if (object.get("nostype").toString().equals("TSZ")) {
                    nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND ((fj.nos ilike 'TSZ%' AND fj.deptid <> 1565607982274) OR (fj.deptid = 516230252274 AND fj.deptop <> 1565607982274) OR (fj.deptop = 516230252274 AND fj.deptid <> 1565607982274)))";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TBZ%' AND (fj.nos ilike 'TAE%' OR fj.deptop = 516228802274))";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    nossql += " AND (fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%' AND ((fj.nos ilike 'TBZ%' AND fj.deptid <> 516230252274) OR (fj.deptid = 1565607982274 AND fj.deptop <> 516230252274) OR (fj.deptop = 1565607982274 AND fj.deptid <> 516230252274)))";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    nossql += " AND fj.nos ilike 'TDA%'";
                } else if (object.get("nostype").toString().equals("TOC")) {
                    nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND (fj.nos ilike 'TBZ%' OR fj.deptid = 1565607982274 OR fj.deptop = 1565607982274 OR fj.nos ilike 'TSZ%' OR fj.deptid = 516230252274 OR fj.deptop = 516230252274))";
                } else {
                    //途曦按照部分区分工作单
                    if (userSession.getCorpid().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + userSession.getUserid() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND (fj.nos ilike 'TBZ%' OR fj.deptop = 1565607982274 OR fj.nos ilike 'TSZ%' OR fj.deptop = 516230252274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TBZ%' AND (fj.nos ilike 'TAE%' OR fj.deptop = 516228802274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%' AND (fj.nos ilike 'TBZ%' OR fj.deptop = 1565607982274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            nossql += " AND (fj.nos ilike 'TDA%')";
                        }
                    }
                }
            }

            //默认显示1个月内的数据--打开高级查找后查恢复所有数据
            if (object.containsKey("islimit") && object.get("islimit") != null && !StrUtils.isNull(object.get("islimit").toString())) {
                if (object.get("islimit").toString() == "true") {
                    sql += " AND fj.jobdate > CURRENT_DATE - INTERVAL '1 months'";
                }
            }

            //高级查找ETD
            if (object.containsKey("etd") && object.get("etd") != null && !StrUtils.isNull(object.get("etd").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("etd").toString());
                airsql += " AND bc.singtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                custsql += " AND bc.singtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                shipsql += " AND bc.etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
                landsql += " AND bc.loadtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                commercesql += " AND bc.etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //是否ETD为空
            if (object.containsKey("isetd") && object.get("isetd") != null && !StrUtils.isNull(object.get("isetd").toString())) {
                if (object.get("isetd").toString() == "true") {
                    airsql += " AND bc.singtime is null";
                    custsql += " AND bc.singtime is null";
                    shipsql += " AND bc.etd is null";
                    landsql += " AND bc.etd is null";
                    commercesql += " AND bc.etd is null";
                }
            }
            //是否ETA为空
            if (object.containsKey("iseta") && object.get("iseta") != null && !StrUtils.isNull(object.get("iseta").toString())) {
                if (object.get("iseta").toString() == "true") {
                    airsql += " AND bc.eta is null";
                    custsql += " AND bc.eta is null";
                    shipsql += " AND bc.eta is null";
                    landsql += " AND false";
                    commercesql += " AND bc.eta is null";
                }
            }
            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                //批量查询支持空格 和 逗号
                String replaceAll = object.get("nos").toString().replaceAll(",", "','").replaceAll(" ", "','");
                airsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                shipsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                landsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                custsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                commercesql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') " +
                        "OR bc.ponum ilike '%" + object.get("nos").toString() + "%' OR bc.ponum in ('" + replaceAll + "') OR bc.sonum in ('" + replaceAll + "'))";
            }
            //ETA
            if (object.containsKey("eta") && object.get("eta") != null && !StrUtils.isNull(object.get("eta").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("eta").toString());
                airsql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                custsql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                shipsql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                commercesql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                landsql += " AND false";
            }
            //工作单日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                sql += " AND fj.jobdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //业务日期
            if (object.containsKey("submitime") && object.get("submitime") != null && !StrUtils.isNull(object.get("submitime").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("submitime").toString());
                sql += " AND fj.submitime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //费用日期
            if (object.containsKey("arapdate") && object.get("arapdate") != null && !StrUtils.isNull(object.get("arapdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("arapdate").toString());
                sql += " AND EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = false AND jobid = fj.id AND arapdate BETWEEN '" + array.get(0) + "' AND '" + array.get(1) + "')";
            }
            //期间sql
            if (object.containsKey("financialdate") && object.get("financialdate") != null && !StrUtils.isNull(object.get("financialdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("financialdate").toString());
                //费用过滤
                isvirarapsql += " AND (SELECT x.year || '-' || x.period FROM fs_vch x WHERE x.isdelete = FALSE\n" +
                        " AND x.id = ANY (SELECT y.vchid FROM fs_vchdtl y WHERE y.isdelete = FALSE AND y.id = a.vchdtlid LIMIT 1)\n" +
                        " LIMIT 1) BETWEEN '" + array.get(0) + "' and '" + array.get(1) + "'";
                //工作单过滤
                sql += " AND EXISTS(SELECT 1 FROM fs_vch x WHERE x.isdelete = FALSE\n" +
                        " AND (x.year || '-' || x.period) BETWEEN '" + array.get(0) + "' and '" + array.get(1) + "'\n" +
                        " AND x.id in (SELECT y.vchid FROM fs_vchdtl y WHERE y.isdelete = FALSE AND y.id in (SELECT xx.vchdtlid FROM fina_arap xx WHERE xx.jobid = fj.id)) LIMIT 1)";
            }
            //M单查h单
            if (object.containsKey("mbl") && object.get("mbl") != null && !StrUtils.isNull(object.get("mbl").toString())) {
                sql += " AND parentid = (SELECT id FROM fina_jobs WHERE nos = '" + object.get("mbl").toString() + "' AND isdelete = FALSE LIMIT 1)";
            }
            //po Num
            if (object.containsKey("ponum") && object.get("ponum") != null && !StrUtils.isNull(object.get("ponum").toString())) {
                airsql += " AND false";
                custsql += " AND false";
                shipsql += " AND false";
                landsql += " AND false";
                commercesql += " AND bc.ponum ilike '%" + object.get("ponum").toString() + "%'";
            }
            //mbl Num
            if (object.containsKey("mblnum") && object.get("mblnum") != null && !StrUtils.isNull(object.get("mblnum").toString())) {
                airsql += " AND bc.mawbno ilike '%" + object.get("mblnum").toString() + "%'";
                shipsql += " AND bc.mblno ilike '%" + object.get("mblnum").toString() + "%'";
                landsql += " AND false";
                custsql += " AND false";
                commercesql += " AND bc.mblnum ilike '%" + object.get("mblnum").toString() + "%'";
            }
            //so
            if (object.containsKey("sonum") && object.get("sonum") != null && !StrUtils.isNull(object.get("sonum").toString())) {
                airsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                custsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                shipsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                landsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                commercesql += " AND bc.sonum ilike '%" + object.get("sonum").toString() + "%'";
            }
            //参考号
            if (object.containsKey("refno") && object.get("refno") != null && !StrUtils.isNull(object.get("refno").toString())) {
                sql += " AND (fj.refno2 ilike '%" + object.get("refno").toString() + "%' OR fj.refno ilike '%" + object.get("refno").toString() + "%')";
            }
            //客商名称
            if (object.containsKey("customerabbr") && object.get("customerabbr") != null && !StrUtils.isNull(object.get("customerabbr").toString())) {
                sql += " AND fj.customerabbr ilike '%" + object.get("customerabbr").toString() + "%'";
            }
            if (object.containsKey("polnamec") && object.get("polnamec") != null && object.get("polnamec") != null && !StrUtils.isNull(object.get("polnamec").toString())) {
                airsql += " AND bc.polcode ilike '%" + object.get("polnamec").toString() + "%'";
                shipsql += " AND bc.polcode ilike '%" + object.get("polnamec").toString() + "%'";
                landsql += " AND FALSE";
                custsql += " AND FALSE";
                commercesql += " AND bc.polcode ilike '%" + object.get("polnamec").toString() + "%'";
            }
            if (object.containsKey("podnamec") && object.get("podnamec") != null && !StrUtils.isNull(object.get("podnamec").toString())) {
                airsql += " AND bc.podcode ilike '%" + object.get("polnamec").toString() + "%'";
                shipsql += " AND bc.podcode ilike '%" + object.get("polnamec").toString() + "%'";
                landsql += " AND FALSE";
                custsql += " AND FALSE";
                commercesql += " AND bc.podcode ilike '%" + object.get("polnamec").toString() + "%'";
            }
            if (object.containsKey("sales") && object.get("sales") != null && !StrUtils.isNull(object.get("sales").toString())) {
                sql += " AND fj.sales ilike '%" + object.get("sales").toString() + "%'";
            }
            //录入人
            if (object.containsKey("inputer") && object.get("inputer") != null && !StrUtils.isNull(object.get("inputer").toString())) {
                sql += " AND fj.inputer ilike '%" + object.get("inputer").toString() + "%'";
            }
            //更新人
            if (object.containsKey("updater") && object.get("updater") != null && !StrUtils.isNull(object.get("updater").toString())) {
                sql += " AND fj.updater ilike '%" + object.get("updater").toString() + "%'";
            }
            //操作
            if (object.containsKey("opname") && object.get("opname") != null && !StrUtils.isNull(object.get("opname").toString())) {
                sql += " AND (EXISTS (SELECT x.userid FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_air y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_truck y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_commerce y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1)))";
            }
            //客服
            if (object.containsKey("csname") && object.get("csname") != null && !StrUtils.isNull(object.get("csname").toString())) {
                sql += " AND (EXISTS (SELECT x.userid FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_air y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_truck y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_commerce y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1)))";
            }
            //是否入仓
            if (object.containsKey("isinwmsin") && object.get("isinwmsin") != null && !StrUtils.isNull(object.get("isinwmsin").toString())) {
                if (object.get("isinwmsin").toString() == "true") {
                    wmsin += " AND bc.wmsindate is not null";
                } else {
                    wmsin += " AND bc.wmsindate is null";
                }
            }
            //是否主单
            if (object.containsKey("ismbl") && object.get("ismbl") != null && !StrUtils.isNull(object.get("ismbl").toString())) {
                if (object.get("ismbl").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)";
                } else {
                    sql += " AND fj.jobtype = 'D' AND fj.parentid > 0";
                }
            }
            //是否香港代收付
            if (object.containsKey("isghk") && object.get("isghk") != null && !StrUtils.isNull(object.get("isghk").toString())) {
                if (object.get("isghk").toString() == "true") {
                    sql += " AND EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 157970752274 AND isdelete = FALSE)";
                }
            }
            //非集拼
            if (object.containsKey("isorder") && object.get("isorder") != null && !StrUtils.isNull(object.get("isorder").toString())) {
                if (object.get("isorder").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND NOT EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)";
                } else {
                    sql += " AND fj.jobtype = 'D' AND fj.parentid = 0";
                }
            }
            //完成状态
            if (object.containsKey("iscomplete") && object.get("iscomplete") != null && !StrUtils.isNull(object.get("iscomplete").toString())) {
                if (object.get("iscomplete").toString() == "true") {
                    sql += " AND fj.iscomplete = true";
                } else {
                    sql += " AND fj.iscomplete = false";
                }
            }
            //审核状态
            if (object.containsKey("ischeck") && object.get("ischeck") != null && !StrUtils.isNull(object.get("ischeck").toString())) {
                if (object.get("ischeck").toString() == "true") {
                    sql += " AND fj.ischeck = true";
                } else {
                    sql += " AND fj.ischeck = false";
                }
            }
            //贸易方式
            if (object.containsKey("istrade") && object.get("istrade") != null && !StrUtils.isNull(object.get("istrade").toString())) {
                if (object.get("istrade").toString() == "true") {
                    sql += " AND fj.tradeway <> ''";
                } else {
                    sql += " AND fj.tradeway = ''";
                }
            }
            //费用完成状态--应收
            if (object.containsKey("isar") && object.get("isar") != null && !StrUtils.isNull(object.get("isar").toString())) {
                if (object.get("isar").toString() == "true") {
                    sql += " AND fj.iscomplete_ar = true";
                } else {
                    sql += " AND fj.iscomplete_ar = false";
                }
            }
            //费用完成状态--应付
            if (object.containsKey("isap") && object.get("isap") != null && !StrUtils.isNull(object.get("isap").toString())) {
                if (object.get("isap").toString() == "true") {
                    sql += " AND fj.iscomplete_ap = true";
                } else {
                    sql += " AND fj.iscomplete_ap = false";
                }
            }
            //费用完成状态--财务
            if (object.containsKey("isfinancial") && object.get("isfinancial") != null && !StrUtils.isNull(object.get("isfinancial").toString())) {
                if (object.get("isfinancial").toString() == "true") {
                    sql += " AND fj.isconfirm2_pp = true";
                } else {
                    sql += " AND fj.isconfirm2_pp = false";
                }
            }
            //亏损状态
            if (object.containsKey("islose") && object.get("islose") != null && !StrUtils.isNull(object.get("islose").toString())) {
                if (object.get("islose").toString() == "true") {
                    greater += " AND profitusd < 0";
                } else {
                    greater += " AND profitrmb < 0";
                }
            }
            //增减费用
            if (object.containsKey("isamend") && object.get("isamend") != null && !StrUtils.isNull(object.get("isamend").toString())) {

                if (object.get("isamend").toString().equals("true")) {

                    //费用日期
                    if (object.containsKey("arapdate") && object.get("arapdate") != null && !StrUtils.isNull(object.get("arapdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("arapdate").toString());
                        isvirarapsql += " AND a.isamend = TRUE AND a.arapdate BETWEEN '" + array.get(0) + "' AND '" + array.get(1) + "'";
                    }

                    sql += " AND (SELECT EXISTS(SELECT 1 FROM fina_arap WHERE jobid = fj.id AND isdelete = FALSE AND isamend = TRUE))";
                }
            }
            //虚拟费用
            if (object.containsKey("isvirtual") && object.get("isvirtual") != null && !StrUtils.isNull(object.get("isvirtual").toString())) {
                if (object.get("isvirtual").toString().equals("true")) {
                    isvirarapsql += " AND a.rptype = 'O'";
                } else {
                    isvirarapsql += " AND a.rptype <> 'O'";
                }
            }

            //合并费用不一致
            if (object.containsKey("ismergercost") && object.get("ismergercost") != null && !StrUtils.isNull(object.get("ismergercost").toString())) {
                if (object.get("ismergercost").toString().equals("true")) {
                    sql += " AND fj.jobtype <> 'D' AND EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)" +
                            " AND ((SELECT COALESCE(sum(amount), 0) FROM fina_arap " +
                            " WHERE jobid = fj.id and rptype = 'O' and araptype = 'AR' AND isdelete = FALSE)" +
                            " <> (SELECT COALESCE(sum(amount), 0) FROM fina_arap " +
                            " WHERE jobid IN (SELECT id FROM fina_jobs WHERE isdelete = FALSE AND parentid = fj.id) " +
                            " AND rptype = 'O' AND araptype = 'AP' AND isdelete = FALSE))";
                }
            }

            //业务日期不一致
            if (object.containsKey("issubmitime") && object.get("issubmitime") != null && !StrUtils.isNull(object.get("issubmitime").toString())) {
                if (object.get("issubmitime").toString() == "true") {
                    sql += " AND (EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.parentid = fj.id AND xx.isdelete = FALSE AND xx.jobtype = 'D' AND xx.submitime <> fj.submitime) " +
                            "OR fj.submitime IS NULL) AND fj.jobdate > '2024-01-01'";
                }
            }

            //承担负利润
            if (object.containsKey("undertake") && object.get("undertake") != null && !StrUtils.isNull(object.get("undertake").toString())) {
                if (object.get("undertake").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND (fj.deptid = 516230252274 OR fj.deptop = 516230252274) " +
                            "AND EXISTS(SELECT 1 FROM fina_jobs xx WHERE (xx.deptid = 1565607982274 OR xx.deptop = 1565607982274) AND parentid = fj.id AND xx.isdelete = FALSE)";

                    greater += " AND profitrmb < 0";

                    newfield = " NULLIF((select SUM(y.vol) from fina_jobs x, bus_commerce y where x.id = y.jobid and x.isdelete = false " +
                            "and x.parentid = result.id AND (x.deptid = 1565607982274 OR x.deptop = 1565607982274)) / " +
                            "(select SUM(y.vol) from fina_jobs x, bus_commerce y where x.id = y.jobid and x.isdelete = false" +
                            " and x.parentid = result.id) * result.profitrmb, 0)::NUMERIC(18, 2) AS newfield";

                }
            }

            //欠款状态
            if (object.containsKey("isowe") && object.get("isowe") != null && !StrUtils.isNull(object.get("isowe").toString())) {
                if (object.get("isowe").toString() == "true") {
                    sql += " AND ((SELECT COALESCE (SUM (f_amtto((CASE WHEN isamend = TRUE " +
                            "AND (CASE WHEN COALESCE(f_sys_config('fina_arap_porfit_isamend_jobdate'),'N') = 'Y' " +
                            "THEN TRUE ELSE FALSE END) = FALSE THEN a.arapdate ELSE fj.jobdate END), a.currency, 'CNY', a.amount - a.amtstl2)), 0) " +
                            "FROM fina_arap a WHERE a.isdelete = FALSE AND a.araptype = 'AR' AND a.rptype <> 'O' " +
                            "AND a.jobid = fj.id AND a.corpid =" + userSession.getCorpid() + ") > 0)";
                } else {
                    sql += " AND ((SELECT COALESCE (SUM (f_amtto((CASE WHEN isamend = TRUE " +
                            "AND (CASE WHEN COALESCE(f_sys_config('fina_arap_porfit_isamend_jobdate'),'N') = 'Y' " +
                            "THEN TRUE ELSE FALSE END) = FALSE THEN a.arapdate ELSE fj.jobdate END), a.currency, 'CNY', a.amount - a.amtstl2)), 0) " +
                            "FROM fina_arap a WHERE a.isdelete = FALSE AND a.araptype = 'AP' AND a.rptype <> 'O' " +
                            "AND a.jobid = fj.id AND a.corpid =" + userSession.getCorpid()+ ") > 0)";
                }
            }

            //往来不一致
            if (object.containsKey("isconsistent") && object.get("isconsistent") != null && !StrUtils.isNull(object.get("isconsistent").toString())) {
                if (object.get("isconsistent").toString().equals("true")) {
                    sql += " AND (SELECT (SELECT COUNT(1) AS c\n" +
                            "        FROM (SELECT t.corpid,\n" +
                            "                     t.customerid,\n" +
                            "                     t.currency,\n" +
                            "                     COALESCE(t.amount, 0) + COALESCE(t2.amount, 0) AS amount\n" +
                            "              FROM (SELECT a.corpid,\n" +
                            "                           customerid,\n" +
                            "                           currency,\n" +
                            "                           sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
                            "                    FROM fina_arap a\n" +
                            "                    WHERE a.jobid = fj.id\n" +
                            "                      AND a.isdelete = FALSE\n" +
                            "                      AND a.rptype != 'O'\n" +
                            "                      AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
                            "                    GROUP BY a.corpid, customerid, currency) t\n" +
                            "                       LEFT JOIN (SELECT a.corpid,\n" +
                            "                                         customerid,\n" +
                            "                                         currency,\n" +
                            "                                         sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
                            "                                  FROM fina_arap a\n" +
                            "                                  WHERE a.jobid = fj.id\n" +
                            "                                    AND a.isdelete = FALSE\n" +
                            "                                    AND a.rptype != 'O'\n" +
                            "                                    AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
                            "                                  GROUP BY a.corpid, customerid, currency) t2 ON (t2.customerid = t.corpid AND t2.corpid = t.customerid AND t2.currency = t.currency)\n" +
                            "             ) TT\n" +
                            "        WHERE TT.amount != 0) > 0)";
                }
            }

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + userSession.getUserid()
                    + "\n OR (fj.inputer ='" + userSession.getUsercode() + "' OR fj.inputer ='" + userSession.getUsername() + "')" //录入人有权限
                    + "\n OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + userSession.getUserid() + ")) " +
                    "AND fj.corpid <> " + userSession.getCorpid() + " AND " + userSession.getCorpid()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_user y WHERE y.custlibid = x.id AND y.userid = " + userSession.getUserid()
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //关联的业务员的单，都能看到
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_role y WHERE y.custlibid = x.id "
                    + "\n AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + userSession.getUserid() + " AND z.roleid = y.roleid)"
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //组关联业务员的单，都能看到
                    //过滤工作单指派
                    + "\n OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_commerce y WHERE x.linkid = y.id AND x.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND x.linktype = 'J' AND x.userid =" + userSession.getUserid() + "))";

            filter += " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT fj.corpid UNION SELECT fj.corpidop " +
                    "UNION SELECT fj.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete = FALSE) " +
                    "AND x.ischoose = TRUE AND userid =" + userSession.getUserid() + ") OR COALESCE(fj.saleid,0) <= 0)";

            //统计报表只显示途曦的工作单
            sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274 AND isdelete = FALSE)))";

            String querySql = "base.commerce.business";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("airsql", airsql);
            args.put("shipsql", shipsql);
            args.put("landsql", landsql);
            args.put("custsql", custsql);
            args.put("commercesql", commercesql);
            args.put("filter", filter);
            args.put("nostype", nossql);
            args.put("arapsql", arapsql);
            args.put("isvirarapsql", isvirarapsql);
            args.put("greater", greater);
            args.put("newfield", newfield);
            args.put("isinwmsin", wmsin);
            args.put("corpsql", corpsql);
            args.put("financial", financial);
            args.put("userid", userSession.getUserid());
            args.put("corpid", userSession.getCorpid());

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("businesslist", StrUtils.getMapVal(list.get(0), "businesslist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 执行sql
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "savesql")
    public BaseView BusCommerceSaveSql(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            daoIbatisTemplate.updateWithUserDefineSql(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
