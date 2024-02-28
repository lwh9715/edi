function getContextPath(){ 
	var pathName = document.location.pathname; 
	var index = pathName.substr(1).indexOf("/"); 
	var result = pathName.substr(0,index+1); 
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