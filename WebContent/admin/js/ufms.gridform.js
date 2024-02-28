//https://www.cnblogs.com/wyy1234/p/9454414.html
function getAction(){
	return getContextPath()+action;
}


var editWindow;
$(function () {
	editWindow = $('#editWindow').clone(true);
	$('#editWindow').remove();
});

function add(){
	//页面层
    index = layer.open({
        type: 1,
        title:'Add',
        skin: 'layui-layer-rim', //加上边框
        area: [window_width, window_height], //宽高
        content: editWindow.html()  //调到新增页面
    });
    var form = layui.form;
    addAfter();
	form.render();
}

function addAfter(){
	
}


function search(){
	var qryJson = $("#gridQry").serializeJson();
	layui.table.reload('grid', {
		        page: {
		          curr: 1 //重新从第 1 页开始
		        }
		        ,where: {
		            qry: JSON.stringify(qryJson)
		        }
		      }, 'data');
    return false;
}

function gridInit(){
	layui.use('table', function(){
		  //$('#editWindow').hide();
		  var table = layui.table;
		  var tableDate = $('#grid'); // 找到目标表格

		  tableIns = table.render({
		     elem: '#grid'
		    ,id:'grid'
		    ,url:getAction()+'?method=queryList'
			,method:"POST"
		    ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,cols: gridDefine
		    ,toolbar: true
		    ,totalRow: true
		    ,page: true
		    ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
		      return {
		        "code": '0', //解析接口状态
		        "msg": '', //解析提示文本
		        "count": res.count, //解析数据长度
		        "data": res.data //解析数据列表
		      };
		    }
		  	,toolbar: '#toolbarGrid'

		    ,done: function(res, curr, count){
		      //$('#grid').next().find('.layui-table-body').find("table" ).find("tbody").children("tr").on('dblclick',function(){
		         // var id = JSON.stringify($('#grid').next().find('.layui-table-body').find("table").find("tbody").find(".layui-table-hover").data('index'));
		          //var obj = res.data[id];
		          //console.log(obj);
		          //gridOnDblclick(obj);
		      //})
		    }
		  });
		  
		  //监听行单击事件（单击事件为：row）
		  table.on('rowDouble()', function(obj){
		    var data = obj.data;
		    gridOnDblclick(data);
		   // layer.alert(JSON.stringify(data), {
		   //   title: '当前行数据：'
		   // });
		    //标注选中样式
		    obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
		  });
	});
}



function gridOnDblclick(obj){
	//页面层
    index = layer.open({
        type: 1,
        title:'Edit',
        skin: 'layui-layer-rim', //加上边框
        area: [window_width, window_height], //宽高
        content: editWindow.html()  //调到新增页面
    });
	
	$.ajax({
		  type: 'GET',
		  url: getAction()+"?method=findObject&id="+obj.id,
		  success: function(res){
		  	var jsonData = JSON.parse(res);
		  	console.log(jsonData);
		  	//$("#editForm").setForm(jsonData);
			var form = layui.form;
			form.val("formfilter", jsonData);
			form.render();
			gridOnDblclickAfter(obj);
		  }
	});
}

function gridOnDblclickAfter(obj){
	
}

function getGridQryJson(){
	var d = {};
    var t = $('#gridQry').serializeArray();
    $.each(t, function() {
      d[this.name] = this.value;
    });
    console.log(d);
    return JSON.stringify(d);
}