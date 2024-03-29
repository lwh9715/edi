/**
  扩展一个ufmsUtile工具模块
**/
layui.define(function(exports){
	const version = '0.0.1';
	const modelName = 'ufmsUtil'; // 组件名称
	var obj = {
		toEnglish:(n)=>{//数字转英文 参考：//https://blog.csdn.net/csongxuan/article/details/81706968
			//参数
		    var NumtoEnglish = {},
		        xiao = "",
		        zheng = "",
		        regxinteger = /^([0-9]{1,}([.][0-9]*)?)$/;
		    //数字英文写法
		    NumtoEnglish.tally = {
		        arr1: ["zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"],
		        arr2: ["ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"],
		        arr3: ["twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"],
		        arr4: ["hundred", "thousand", "million", "billion", "trillion", "quadrillion"]
		    }
		    //转换整数部分
		    NumtoEnglish.Convert_integer = function (n) {
		        try {
		            var fenge = this.toThousands(n).split(',');
		            result = "";
		            for (var i = 0; i < fenge.length; i++) {
		                if (fenge[i].length == 3) {
		                    result += this.tally.arr1[fenge[i].substring(0, 1)] + " "; //百位
		                    result += this.tally.arr4[0];
		                    if (this.doubledight(fenge[i].substring(1)) != "") {
		                        result += " and " + this.doubledight(fenge[i].substring(1));
		                    }
		                } else if (fenge[i].length == 2) {
		                    result += this.doubledight(fenge[i]) //十位
		                } else if (fenge[i].length == 1) {
		                    result += this.tally.arr1[fenge[i]]; //个位
		                }
		                //添加千分位单位（数字超过1000，每三位数字分配一个单位）
		                if (i < fenge.length - 1) {
		                    result += " " + this.tally.arr4[fenge.length - 1 - i] + " ";
		                }
		            }
		            return result;
		        } catch (ex) {
		            console.error(ex);
		        }
		    }
		    //转换小数部分
		    NumtoEnglish.Convert_decimal = function (n) {
		        var d = n.split('');
		        var result = '';
		        if (d.length > 0) {
		            d.forEach(a => {
		                result += this.Convert_integer(a) + " ";
		            });
		        }
		        return result;
		    }
		    //组合两位数
		    NumtoEnglish.doubledight = function (n) {
		        var result = "";
		        if (parseInt(n) != 0) {
		            var dd = n.split('');
		            if (dd[0] < 1) {
		                result = this.tally.arr1[dd[1]];
		            } else if (dd[0] == 1) {
		                result = this.tally.arr2[dd[1]];
		            } else {
		                result = this.tally.arr3[dd[0] - 2] + "-" + this.tally.arr1[dd[1]];
		            }
		        }
		        return result;
		    }
		 
		    //转换千分位显示，例：1000000 = 1,000,000
		    NumtoEnglish.toThousands = function (num) {
		        var num = (num || 0).toString(),
		            result = '';
		        while (num.length > 3) {
		            result = ',' + num.slice(-3) + result;
		            num = num.slice(0, num.length - 3);
		        }
		        if (num) {
		            result = num + result;
		        }
		        return result;
		    }
		 
	        if (!regxinteger.test(parseInt(n))) {
	            return "Error：Must in digital format";
	        }
	 
	        //分割整数和小数（如果有小数的话）
	        var NumList = n.toString().split('.'),
	            zheng = NumtoEnglish.Convert_integer(NumList[0]); //整数部分
	        //如果分割长度是2，说明是小数
	        if (NumList.length == 2) {
	            if (NumList[1].length <= 2) {
	                xiao = NumtoEnglish.Convert_decimal(NumList[1]);
	            } else {
	                //如果小数超过2位，不转换，返回原数据
	                return n;
	            }
	        }
	        //返回转换结果
	        return zheng + (xiao == "" ? "" : " point " + xiao);
		},
		toFixed:(s,d)=>{//保留d位小数（四舍五入）
			if(!s)s='0'
			s += '';
			if(!d)d=0; 
		    if(s.indexOf(".")==-1)s+="."; 
		    s+=new Array(d+1).join("0"); 
		    if(new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+(d+1)+"})?)\\d*$").test(s)){
		        var s="0"+RegExp.$2,pm=RegExp.$1,a=RegExp.$3.length,b=true;
		        if(a==d+2){
		            a=s.match(/\d/g); 
		            if(parseInt(a[a.length-1])>4){
		                for(var i=a.length-2;i>=0;i--){
		                    a[i]=parseInt(a[i])+1;
		                    if(a[i]==10){
		                        a[i]=0;
		                        b=i!=1;
		                    }else break;
		                }
		            }
		            s=a.join("").replace(new RegExp("(\\d+)(\\d{"+d+"})\\d$"),"$1.$2");

		        }if(b)s=s.substr(1); 
		        return (pm+s).replace(/\.$/,"");
		    }
		    return s;
		}
	}
	//输出接口
	exports(modelName, obj);
})
