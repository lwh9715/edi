layui.define(function (exports) {
    var $ = layui.$;
    const modelName = 'i18np'; // 组件名称
    var obj ={
    	loadProperties : function (type,callback) {
	        $.i18n.properties({
	        	name: 'strings', // 资源文件名称  
	            path:  getContextPath()+'/res/i18n/', // 资源文件所在目录路径  
	            mode: 'map', // 模式：变量或 Map  
	            language: type, // 对应的语言  
	            cache: false,  
	            encoding: 'UTF-8',
	            callback: function () { callback()}
	        });
    	},
    	translate : function(selector,type){
        	this.loadProperties(type,function(){
        		selector.forEach((elem)=>{
        			$(elem).each(function(i){
        				if(/.*[\u4e00-\u9fa5]+.*$/.test($(this).text())) {//里面包含中文才替换
        					$(this).text($.i18n.prop($(this).text()));
        				}
    				});
        		})
        	})
        },
        translateOnTanle:function(type){
        	this.translate(['table th div[class*=layui-table-cell] span'],type);
        }
        ,
        translateOnForm:function(type){
        	this.translate(['h1,h2,h3,h4,h5','.layui-form-label'],type);
        },
        translateOnNav:function(type){
        	this.translate(['.main-news a .translate'],type);
        }
    }
    
    exports(modelName, obj);
});