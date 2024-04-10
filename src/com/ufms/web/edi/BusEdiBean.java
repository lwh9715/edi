package com.ufms.web.edi;

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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
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
     * 搜索单号
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "searchJob")
    public BaseView BusEdiJob(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "base.edi.jobinfo";
            Map args = new HashMap(10);

            //逗号分隔转list
            String param = request.getParameter("nos");
            List<String> arlist = Arrays.asList(param.split(","));

            String ediStr = "";

            String str = "";
            String[] val = new String[0];
            String vessel = "";
            String voyage = "";

            if (request.getParameter("type").equals("vessel")) {
                if (param.contains(",")) {
                    String[] st1 = param.split(",");
                    vessel = st1[0];
                    voyage = st1[1];
                } else {
                    vessel = param;
                    voyage = param;
                }
            } else if (param.contains(",")) {
                val = param.split(",");
            } else {
                str = param;
            }

            if (val != null && StrUtils.isNull(str) && StrUtils.isNull(vessel)) {
                ediStr = " AND x.jobno IN (" + val + ")";
            }

            if (!StrUtils.isNull(str)) {
                ediStr = "AND (x.jobno ilike '%" + str + "%' " +
                        " OR EXISTS(SELECT 1 FROM jobshipping jp WHERE x.jobid = jp.jobid AND jp.sono ilike '%" + str + "%') " +
                        " OR EXISTS(SELECT 1 FROM jobshipping jp WHERE x.jobid = jp.jobid AND jp.blnom ilike '%" + str + "%'))";
            }

            if (!StrUtils.isNull(vessel)) {
                ediStr = "AND (EXISTS(SELECT 1 FROM jobshipping jp WHERE x.jobid = jp.jobid AND jp.vessel ilike '% " + vessel + "%')" +
                        " AND EXISTS(SELECT 1 FROM jobshipping jp WHERE x.jobid = jp.jobid AND jp.voyage ilike '%" + voyage + "%'))";
            }

            args.put("filter", ediStr.equals("") ? "" : ediStr);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("jobs", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 导出edi文本
     */
    @Action(method = "findAllJob")
    public void findAllJobReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            //逗号分隔转list
            String param = request.getParameter("nos");
            List<String> list = Arrays.asList(param.split(","));

            //拼接edi
            String ediStr = "";

            String bolSql = "base.edi.bolinfo";
            String conSql = "base.edi.coninfo";
            Map args = new HashMap(10);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            //查询edi数据-bol
            for (int i = 0; i < list.size(); i++) {

                args.put("filter", "AND x.jobno = '" + list.get(i) + "'");
                List<Map> bollist = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(bolSql, args);

                if (bollist == null) {
                    continue;
                }

                if (!ediStr.contains("VOY")) {
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + bollist.get(0).get("blnom") + ".txt\"");
                    //第一行拼接
                    ediStr += "\"VOY" + "\",\"" + "60326L" + "\",\"" + "Y0011" + "\",\"" + isNullToString(bollist.get(0).get("vessel")) + "\",\""
                            + isNullToString(bollist.get(0).get("voyage")) + "\",\"" + isNullToString(bollist.get(0).get("pod")) + "\",\""
                            + isNullToString(bollist.get(0).get("eta")) + "\",\"" + "Rotation 待补充" + "\",\"" + "MFI" + "\",\"" + "1" + "\",\"" + "1\"" + "\n";
                }

                ediStr += "\"BOL" + "\",\""
                        + isNullToString(bollist.get(0).get("jobno")) + "\",\"" //BILL OF LADING NO
                        + "\",\"" //PARTNERING LINE CODE
                        + "\",\"" //PARTNERING AGENT CODE
                        + isNullToString(bollist.get(0).get("pol")) + "\",\"" //PORT CODE OF ORIGIN
                        + isNullToString(bollist.get(0).get("pol")) + "\",\"" //PORT CODE OF LOADING
                        + isNullToString(bollist.get(0).get("pod")) + "\",\"" //PORT CODE OF DISCHARGE
                        + isNullToString(bollist.get(0).get("pod")) + "\",\"" //PORT CODE OF DESTINATION
                        + isNullToString(bollist.get(0).get("loaddate")) + "\",\n\"" //DATE OF LOADING
                        + "\",\"" //MANIFEST REGISTRATION
                        + isNullToString(bollist.get(0).get("impexp")) + "\",\"" //TRADE CODE - 贸易方式
                        + "\",\"" //TRANS-SHIPMENT MODE
                        + "\",\"" //BILL OF LADING OWNER NAME
                        + "\",\"" //BILL OF LADING OWNER ADDRESS
                        + isNullToString(bollist.get(0).get("consgoods")) + "\",\"" //CARGO CODE - 货物代码
                        + "\",\"" //CONSOLIDATED CARGO INDICATOR - 合并货物
                        + "\",\"" //STORAGE REQUEST CODE - 存储
                        + isNullToString(bollist.get(0).get("ldtype")) + "\",\"" //CONTAINER SERVICE TYPE - 集装箱类型
                        + "CN" + "\",\"" //COUNTRY OF ORIGIN 原产国
                        + "\",\"" //ORIGINAL CONSIGNEE NAME 收货人名
                        + "\",\"" //ORIGINAL CONSIGNEE ADDRESS 原始收货人
                        + "\",\"" //ORIGINAL VESSEL NAME 船名
                        + "\",\"" //ORIGINAL VOYAGE  NUMBER 航次
                        + "\",\"" //ORIGINAL BOL NUMBER 原始BOL数字
                        + "\",\"" //ORIGINAL SHIPPER NAME 原发货人名
                        + "\",\"" //ORIGINAL SHIPPER ADDRESS 原发货人地址
                        + isNullToString(strIntercept((String) bollist.get(0).get("cnorname"), 30)) + "\",\"" //SHIPPER NAME 发货人名
                        + "CHINA" + "\",\"" //SHIPPER ADDRESS
                        + "\",\"" //SHIPPER COUNTRY CODE
                        + (isNullToString(bollist.get(0).get("impexp")) == "T" ? "T9999" : "") + "\",\"" //CONSIGNEE CODE 收货人代码
                        + isNullToString(strIntercept((String) bollist.get(0).get("cneename"), 48)) + "\",\"" //CONSIGNEE NAME 收货人名称
                        + "P.O. BOX 65546" + "\",\"" //CONSIGNEE ADDRESS 收货人地址
                        + "\",\"" //NOTIFY1 CODE 通知人代码
                        + isNullToString(bollist.get(0).get("notifyname")) + "\",\"" //NOTIFY1 NAME 通知人名称
                        + "P.O. BOX 65546" + "\",\"" //NOTIFY2 CODE
                        + "\",\"" //NOTIFY2 NAME
                        + "\",\"" //NOTIFY2 ADDRESS
                        + "\",\"" //NOTIFY3 CODE
                        + "\",\"" //NOTIFY3 NAME
                        + "\",\"" //NOTIFY3 ADDRESS
                        + "\",\""
                        + isNullToString(strIntercept((String) bollist.get(0).get("markno"), 200)) + "\",\"" //MARKS & NUMBERS 唛头
                        + 770000 + "\",\"" //COMMODITY CODE 商品代码
                        + isNullToString(strIntercept((String) bollist.get(0).get("goodsname"), 100)) + "\",\"" //COMMODITY DESCRIPTION 商品名称
                        + bollist.get(0).get("pieces") + "\",\"" //PACKAGES
                        + isNullToString((String) bollist.get(0).get("packtype")) + "\",\"" //PACKAGE TYPE
                        + isNullToString((String) bollist.get(0).get("packcode")) + "\",\"" //PACKAGE TYPE CODE 包装CODE
                        + isNullToString(strIntercept((String) bollist.get(0).get("cntno"), 10)) + "\",\"" //CONTAINER NUMBER 箱号
                        + lastStr((String) bollist.get(0).get("cntno")) + "\",\"" //CHECK DIGIT
                        + 0 + "\",\""  //NO OF CONTAINERS 集装箱数量
                        + 0 + "\",\""  //NO. OF TEUS T量
                        + 0 + "\",\""  //TOTAL TARE WEIGHT IN MT 皮重
                        + bollist.get(0).get("grswgt") + "\",\""  //CARGO WEIGHT IN KG 货重
                        + bollist.get(0).get("grswgt") + "\",\""  //GROSS WEIGHT IN KG 毛重
                        + bollist.get(0).get("cbm") + "\",\""  //CARGO VOLUME IN CUBIC METRE 体积
                        + 0 + "\",\""  //TOTAL QUANTITY 总数量
                        + bollist.get(0).get("cbm") + "\",\"" //FREIGHT TONNE 货运吨
                        + 0 + "\",\"" //NO OF PALLETS 托盘数量
                        + "\",\"" //SLAC INDICATOR
                        + "\",\"" //CONTRACT CARRIAGE CONDITION 条款
                        + "\"\n"; //REMARKS 备注

                args.put("filter", "AND x.jobid = " + bollist.get(0).get("jobid") + "");
                List<Map> conlist = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(conSql, args);
                //查询edi数据-con
                for (int j = 0; j < conlist.size(); j++) {
                    ediStr += "\"CTR" + "\",\"" + isNullToString(strIntercept((String) conlist.get(j).get("cntno"), 10)) + "\",\""
                            + isNullToString(lastStr((String) conlist.get(j).get("cntno"))) + "\",\"" //校正位
                            + conlist.get(j).get("cntgw") + "\",\""
                            + isNullToString(conlist.get(j).get("sealno")) + "\"\n";

                    ediStr += "\"CON" + "\",\"" + (1 + j) + "\",\""
                            + isNullToString(strIntercept((String) conlist.get(j).get("marks"), 200)) + "\",\"" //标记与号码
                            + isNullToString(conlist.get(j).get("cargodesc")) + "\",\""
                            + isNullToString(conlist.get(j).get("indicat")) + "\",\""
                            + 770000 + "\",\""
                            + conlist.get(j).get("pieces") + "\",\""
                            + isNullToString(conlist.get(j).get("packtype")) + "\",\"" //包装
                            + isNullToString(conlist.get(j).get("packcode")) + "\",\"" //包装代码
                            + 0 + "\",\"" //托盘数量
                            + conlist.get(j).get("grswgt") + "\",\""
                            + conlist.get(j).get("cbm") + "\",\""
                            + "\",\"" //危险器
                            + "\",\"" //IMO
                            + "\",\"" //危险品UN
                            + 0 + "\",\"" //闪点
                            + "\",\"" //温度单位
                            + "D" + "\",\"" //要求储存危险品
                            + "\",\""
                            + 0 + "\",\""
                            + 0 + "\",\""
                            + "\"\n";
                }
            }
            ediStr += "\"END" + "\",\""
                    + list.size() + "\",\"" //校正位
                    + 0 + "\",\""
                    + "Version no: 1,Product sr. no: EDI/1.0" + "\"\n";

            PrintWriter writer = response.getWriter();
            writer.println(ediStr);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //非空判断
    private String isNullToString(Object val) {
        if (val == null || val == "") {
            return "";
        } else {
            return (String) val;
        }
    }

    //截取字符串
    private String strIntercept(String val, Integer len) {
        String str = "";
        if (val == null || val == "") {
            return str;
        }
        if (val.length() > len) {
            String letStr = val.substring(0, len);
            str = letStr.replaceAll("\r\n", " ");
        } else {
            str = val.replaceAll("\r\n", " ");
        }
        return str;
    }

    //获取最后一位字符串
    private String lastStr(String val) {
        String str = "";
        if (val == null || val == "") {
            return str;
        }
        if (val.length() > 1) {
            str = val.substring(val.length() - 1);
        }
        return str;
    }

}
