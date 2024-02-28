function getContextPath(){ 
	var pathName = document.location.pathname; 
	var index = pathName.substr(1).indexOf("/"); 
	var result = pathName.substr(0,index+1); 
	//result = '';
	return result; 
} 

function getRequest() {
    var url = location.search; // 获取url中含"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

function getSid(){
	return "&token=" + $.cookie('token') + "&sid=" + $.cookie('sid');
}

//全局配置
$.ajaxSetup({
	beforeSend: function(jqXHR, settings) {
		settings.url += settings.url.match(/\?/) ? "&" : "?";
		settings.url += "token=" + $.cookie('token') + "&sid=" + $.cookie('sid')+ "&language=" + $.cookie('language');
	}
});
function actionPost(service,action,jsonData,callback){
	$.ajax({
		  type: 'POST',
		  url: getContextPath()+"/"+service+"?method="+action,
		  contentType:'application/json',
		  data: jsonData,
	        success:function(result){
	            callback(result);
	        },
	        error:function(result){
	            callback(result);
	        }
	});
}

// 公共ajax请求
//url、type、async、cache、headers、data、dataType、contentType、callback
//https://www.liangzl.com/get-article-detail-2595.html
function action(service,action,jsonData,callback){
	$.ajax({
	  type: 'POST',
	  url: getContextPath()+"/"+service+"?method="+action,
	  contentType:'application/json',
	  data: jsonData,
        success:function(result){
            callback(result);
        },
        error:function(result){
            callback(result);
        }
    });
}

function actionGet(service,action,args,callback){
	$.ajax({
	  type: 'GET',
	  url: getContextPath()+"/"+service+"?method="+action+"&"+args,
	  contentType:'application/json',
	  success:function(result){
	  	callback(result);
	  },
	  error:function(result){
        callback(result);
      }
    });
}

//测试样例
/**

action('subscribe','get','',function(data){
    console.log(data)
});

var options = {
    url:Server.ApiHost+"/utils/cms/getCatalogListTree",
    type:'get',
    headers:'',
    data:{"tenantId":userInfo.tenantId,"isTree":"0","publishFlag":"0"}
};
sendRequest(options,function(data){
    console.log(data)
});
**/

layui.config({
    base: getContextPath()+'/res/static/js/' 
  }).use('firm'); 

//全局定义一次, 加载formSelects
layui.config({
     base:  getContextPath()+'/res/' //此处路径请自行处理, 可以使用绝对路径
}).extend({
    formSelects: 'layui/layui-formSelects/formSelects-v4'
   ,tablePlug: 'layui/lay/modules/ex/tablePlug'
   ,optimizeSelectOption:'layui/lay/modules/ex/optimizeSelectOption'
   ,xmSelect: 'layui/lay/modules/xm-select/xm-select'
   ,esiUtil:'static/js/esiutil'
   ,ufmsUtil:'static/js/ufmsutil'
   ,i18np:'static/js/i18np'
});

/*
 * fromselect下拉框封装
 * islongselect：是否是远程搜索 
 */
function randerfromselect(service,action,xmselect,formSelects,islongselect){
	actionGet(service,action.split('?')[0],action.indexOf('?')==-1?"":action.split('?')[1],function(res){
		formSelects.btns(xmselect, []);
		formSelects.render(xmselect, {
	            template: function(name, value, selected, disabled){
				var name = value.name.split("/");//用/分割，如果有则显示在右边
	            return name[0] + (name[1] == undefined? '':'<span style="position: absolute; right: 0; color: #A0A0A0; font-size: 12px;">' + name[1] + '</span>');
	        }
		})
		.data(xmselect, 'local', {
		    arr: JSON.parse(res)
		})
		;
		$(".xm-select-tips").each(function(i){
			if($(this).html().indexOf('[') < 0){
				$(this).html($.i18n.prop($(this).html()));
			}
		});
        $('.xm-select--suffix input').each(function(i){
        	if($(this).attr('placeholder').indexOf('[') < 0){
            	$(this).attr('placeholder',$.i18n.prop($(this).attr('placeholder')));
        	}
        });
	});
	if(islongselect){
		formSelects.config(xmselect, {
			searchUrl: getContextPath()+"/"+service+"?method="+action.replace('?','&'),//搜索地址, 默认使用xm-select-search的值, 此参数优先级高
			searchName: 'keyword'      //自定义搜索内容的key值
		}, false);
	}
	formSelects.filter( function(id, inputVal, val, isDisabled){
        if(
            val.name.toUpperCase().indexOf(inputVal.toUpperCase()) != -1 //文本是否包含 不区分大小写
        ){
            return false;
        }
        return true;
    });
}

/*
 * xmselect下拉框
 * */
var randerXmselect = {
		rander : function(xmSelect,configData){
			function getSelectmodule(){
				if(configData.radio = true){//单选样式
					configData.model = {
							icon: 'hidden',
							label: {
								type: 'text'
							}
						},
					configData.theme = {
						color: '#5FB878'
					}
				}
				let selectmodule = xmSelect.render({
					el: configData.el,
					language:getLanguage(),
					data: [],
					initValue :configData.initValue,
					on:configData.on,
					initValue:configData.initValue
				})
				actionGet('/combobox',configData.action.split('?')[0],configData.action.indexOf('?')==-1?"":configData.action.split('?')[1],function(res){
					configData.data = JSON.parse(res);
					selectmodule.update(configData)
				});
				return selectmodule;
			}
			return getSelectmodule();
		},
		getConfig : function(){//此方法设置默认参数，可在实例化处修改
			return {
				autoRow: true,
				paging: true,
				radio: true,
				clickClose: true,
				filterable:true,//开启搜索
				theme: {
					color: '#0081ff'
				},
				template({ item, sels, name, value }){
					var name = item.name.split("/");//用/分割，如果有则显示在右边
					return name[0] + (name[1] == undefined? '':'<span style="position: absolute; right: 2px; color: #8799a3;overflow: hidden;text-overflow: ellipsis;width:50%;font-size: 12px;">' + name[1] + '</span>');
				}
			}
		}
}

//获得网站设置中的Logo
function getWeblogo(callback){
	$.ajax({
		type:'POST',
		dataType: 'html',
		url: getContextPath()+"/login?method=getLogopath",
		success:function(date){
			if(date.toString().indexOf("ERROR") == 0){
				callback("");
			}
			callback(date);
		}
	});
}
//获得网站设置中的登录Logo
function getWebLoginlogo(callback){
	$.ajax({
		type:'POST',
		dataType: 'html',
		url: getContextPath()+"/login?method=getWebLoginlogo",
		success:function(date){
			if(date.toString().indexOf("ERROR") == 0){
				callback("");
			}
			callback(date);
		}
	});
}
//获得网站设置中的注册Logo
function getWebRegisterlogo(callback){
	$.ajax({
		type:'POST',
		dataType: 'html',
		url: getContextPath()+"/login?method=getWebRegisterlogo",
		success:function(date){
			if(date.toString().indexOf("ERROR") == 0){
				callback("");
			}
			callback(date);
		}
	});
}

(function ($) {//获得URL参数
	  $.getUrlParam = function (name) {
	   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	   var r = window.location.search.substr(1).match(reg);
	   if (r != null) return unescape(r[2]); return null;
	  }
	 })(jQuery);

$(function($) {
	actionPost('/index','getwebconfig',"",function(res){
		 var data = JSON.parse(res);
		 if(data.success == true){
			 $('meta[name="description"]').attr('content',data.webDescription);
			 $('meta[name="author"]').attr('content',data.webAuthor);
			 $('meta[name="keywords"]').attr('content',data.webKeywords);
		 }
	});
})
$(function($) {
	$.i18n.properties({
		name: 'strings', // 资源文件名称  
        path:  getContextPath()+'/res/i18n/', // 资源文件所在目录路径  
        mode: 'map', // 模式：变量或 Map  
        language: getLanguage(), // 对应的语言  
        cache: false,  
        encoding: 'UTF-8',
        callback : function() {//加载成功后设置显示内容
            $(".translate").each(function(i){
            	if(/.*[\u4e00-\u9fa5]+.*$/.test($(this).text())) {
            		$(this).text($.i18n.prop($(this).text()));
            	}
			});
        }
    });
});

function getLanguage(){
	var languagev = $.cookie('language');
	if(languagev == null || languagev == ''){
		languagev = 'ch';
	}
	return languagev;
}

$(function($) {
	
});

function getCombobox(form){//初始化动态添加下拉框
	$('select[bind=comboboxs]').each(function(index,element){
		var name = element.name;
		actionGet('/combobox',name,'',function(res){
		    var data = JSON.parse(res);
		    for(var i=0; i<data.length; i++){
		    	var option="<option value=\""+data[i].val+"\""+">"+data[i].label+"</option>"; //动态添加数据
		    	$("select[name="+name+"]").append(option);
		    }
		    form.render('select');
	    });
	});
}

/*
 * 初始化动态添加下拉框,并且赋值
 * formdata:表单数据，根据表单数据设置初始值
 * */
function getComboboxSet(form,formdata){
	$('select[bind=comboboxs]').each(function(index,element){
		var name = element.name;
		var qry = $(this).attr("bind-qry");;
		eval("var boxValue = formdata."+name);
//		console.log(boxValue,qry,name);
		actionGet('/combobox',qry,'',function(res){
		    var data = JSON.parse(res);
		    let optionHTML = "<option value=''>请选择</option>";
		    for(var i=0; i<data.length; i++){
		    	optionHTML += "<option value=\""+data[i].val+"\""+(data[i].val==boxValue?" selected ":" ")+">"+data[i].label+"</option>"; //动态添加数据
		    }
		    $("select[name="+name+"]").append(optionHTML);
		    form.render('select');
	    });
	});
}

function isWeiXin(){
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}

function closelayer(){
	layer.closeAll(); 
}

//2731 SO：绑定微信后处理自动登录
//如果是微信端，并且有openid，ajax方式 用openid 调用登录方法，获取session
function weinLogin(){
	var openid = $.cookie('opneid');
	if(openid != null&&isWeiXin()==true){
		var para = new Object;
		para.openid = openid;
		actionPost('/login','openidlogin',JSON.stringify(para),function(res){
    		var resjson = JSON.parse(res);
    		//console.log(resjson);
        	if(resjson.success == true){
        		$.cookie('sid', resjson.sid,{ expires: 30, path: '/so' });
		    	$.cookie('token', resjson.token,{ expires: 30, path: '/so' });
		    	$.cookie('namec', resjson.namec,{ expires: 30, path: '/so' });
		    	$.cookie('issysuser', resjson.issysuser,{ expires: 30, path: '/so' });
		    	if($.cookie('issysuser') == 'true'){
		  		  location.href='./manager_staff.html';
			  	}else{
			  	  location.href='./manager.html';
			  	}
			}
    	})
	}
}

//判断是否是移动端
function isMove(){
	if(/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)){
		return true;
	}else{
		return false;
	}
}

//验证登录
function verifyLogin(){
	actionGet('index','checkLogin','',function(res){
		 var data = JSON.parse(res);
		 if(data.success){
		 }else{
			 location.href=getContextPath()+'/login.html';
		 }
	})
}

//记录日志
function logsubmit(paradata){
	paradata.browserType = getBrowser();
	actionPost('/login','savesyslog',JSON.stringify(paradata),function(res){
		var resjson = JSON.parse(res);
    	
	})
}
//获得浏览器类型
function getBrowser() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    var isIE = userAgent.indexOf("compatible") > -1
            && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    var isEdge = userAgent.indexOf("Edge") > -1; //判断是否IE的Edge浏览器
    var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
    var isSafari = userAgent.indexOf("Safari") > -1
            && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器
    var isChrome = userAgent.indexOf("Chrome") > -1
            && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器

    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion == 7) {
            return "IE7";
        } else if (fIEVersion == 8) {
            return "IE8";
        } else if (fIEVersion == 9) {
            return "IE9";
        } else if (fIEVersion == 10) {
            return "IE10";
        } else if (fIEVersion == 11) {
            return "IE11";
        } else {
            return "0";
        }//IE版本过低
        return "IE";
    }
    if (isOpera) {
        return "Opera";
    }
    if (isEdge) {
        return "Edge";
    }
    if (isFF) {
        return "FF";
    }
    if (isSafari) {
        return "Safari";
    }
    if (isChrome) {
        return "Chrome";
    }
}

//控制按钮权限
function setButtonClass(data){
	var new_element = document.createElement("script");
    new_element.setAttribute("type", "text/javascript");
    new_element.setAttribute(
      "src",
      "/so/index?method=getCssForButton&pid="+data.pid
    );
    document.body.appendChild(new_element);
}

function getUuid(){
	var d=[];
	var a="0123456789abcdef";
	for(var b=0;b<36;b++){
		d[b]=a.substr(Math.floor(Math.random()*16),1);
	}
	d[14]="4";
	d[19]=a.substr((d[19]&3)|8,1);
	d[8]=d[13]=d[18]=d[23]="-";
	var c=d.join("");
	return c;
}
