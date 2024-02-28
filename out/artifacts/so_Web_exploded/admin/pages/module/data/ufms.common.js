function getContextPath(){ 
	var pathName = document.location.pathname; 
	var index = pathName.substr(1).indexOf("/"); 
	var result = pathName.substr(0,index+1); 
	return result; 
} 




//公共ajax请求
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