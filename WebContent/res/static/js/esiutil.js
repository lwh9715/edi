/**
  扩展一个esi页面处理模块
**/      
layui.define(['form','table','ufmsUtil'],function(exports){
	const version = '0.0.1';
	const modelName = 'esiUtil'; // 组件名称
	const $ = layui.$,form=layui.form,table=layui.table, layer = layui.layer,ufmsUtile = layui.ufmsUtil;
	var obj = {
		chooseContainer:()=>{
			let tabledata = table.cache.dataReload;
			let grswgtsum = 0;
			let cbmsum = 0;
			let totledescs = '';
			let cntnumber = 0;
			let goodsdescs = '';
			let isLdtype = false;
			let piecesum = 0;
			let packageearr = [];
			for(var i=0;i<tabledata.length;i++){
				if(tabledata[i].LAY_CHECKED == true){
					grswgtsum += Number(tabledata[i].grswgt);
					cbmsum	+=	Number(tabledata[i].cbm);
					piecesum += Number(tabledata[i].piece);
					cntnumber += 1;
					if(tabledata[i].ldtype === 'F'){
						isLdtype = true
					}
					packageearr.push(tabledata[i].packagee)
				}
			}
			packageearr = Array.from(new Set(packageearr))//去重
			if(isLdtype){//整柜
				totledescs = 'SAY '+ufmsUtile.toEnglish(cntnumber)+'(' +getCntdesc(tabledata)+ ') CONTAINER'+(cntnumber===1?'':'S')+' ONLY';
				goodsdescs = 'SHIPPER’S LOAD,COUNT & SEAL\n(' +getCntdesc(tabledata)+ ')CONTAINER'+(cntnumber===1?'':'S')+' S.T.C.';
			}else{//散货
				totledescs = 'SAY TOTAL '+ufmsUtile.toEnglish(piecesum)+'(' +piecesum+ ') '+(packageearr.length === 1?packageearr[0]:'PACKAGE(S)')+' ONLY';
				goodsdescs = 'SHIPPER’S LOAD,COUNT & SEAL\n';
			}
			form.val('esi',{
				grswgt:ufmsUtile.toFixed(grswgtsum,3)+'KGS'
				,cbm:ufmsUtile.toFixed(cbmsum,3)+'CBM'
				,totledesc:totledescs.replace(/’/g,"'")
				,marksno:getMarksno(tabledata)
				,goodsdesc:goodsdescs.replace(/’/g,"'")+getGoodsdesc(tabledata)
			});
		}
	}
	//直接返回数量+箱量的字符串
	var getCntdesc = (tabledata)=>{
		//console.log(cntypeArr);
		var cntArray = []
		for(var i=0;i<tabledata.length;i++){
			if(tabledata[i].LAY_CHECKED == true){
				if(tabledata[i].cntypeid){
					let cntdes = cntypeArr.find((s)=> {
						    return tabledata[i].cntypeid === s.val;
						})
					cntArray.push(cntdes.label);
				}
			}
		}
		var cntnumbers = cntArray.reduce( (allNames, name)=>{ 
		  if (name in allNames) {
		    allNames[name]++;
		  }
		  else {
		    allNames[name] = 1;
		  }
		  return allNames;
		}, {});
		return JSON.stringify(cntnumbers).replace(/[{}"]/g, '').replace(/:/g, '*');
	}
	
	//标记与号码
	var getMarksno = (tabledata)=>{
		var marknos = '';
		var cntdesc = '';
		tabledata.forEach(function(elem){
			if(elem.LAY_CHECKED == true){
				if(elem.markno&&elem.markno!=''){
					marknos += elem.markno+',';
				}
				let cntdes = cntypeArr.find((s)=> {
				    return elem.cntypeid === s.val;
				})
				elem.sealno = elem.sealno?elem.sealno:''
				elem.packagee = elem.packagee?elem.packagee:''
				cntdesc += elem.cntno + '/' + cntdes.label+'/'+elem.sealno+'/'+Number(elem.piece)+elem.packagee+'/\n'+Number(elem.grswgt)+'KGS'+'/'+Number(elem.cbm)+'CBM\n'
			}
		});
		if(marknos.replace(/,/g,'') === ''){
			marknos = 'N/M';
		}
		marknos += '\nCONTAINERS/SEAL NO.:\n';
		return marknos + cntdesc
	}
	//
	var getGoodsdesc =(tabledata)=>{
		const shipping = form.val('esi');
		let piecesum = 0;
		let packagees = '';
		let goodsnamees = '';
		tabledata.forEach(function(elem){
			if(elem.LAY_CHECKED == true){
				piecesum += Number(elem.piece)
				if(elem.packagee&&elem.packagee!=''){
					packagees += packagees+','
				}
				
				if(elem.goodsnamee&&elem.goodsnamee!=''){
					goodsnamees += elem.goodsnamee+'\n'
				}
			}
		});	
		if(packagees.replace(/,/g,'') === ''){
			packagees = 'PACKAGE(S)';
		}
		return '\n'+shipping.carryitem+'\n\n'+piecesum+' '+packagees+'\n\n'+goodsnamees+'\n';
	}
	
	//输出接口
	exports(modelName, obj);
});

