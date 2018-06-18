$(document).ready(function(){
     var testdata2 = []
	$('#testtable3').yhhDataTable({
            'tbodyRow':{
                'zebra':true,
                'write':function(d){
                    return '<tr><td style="width: 15%">'+d.deviceId+'</td><td style="width: 15%">'+(formatter(d.deviceStatus))+'</td><td style="width: 15%">'+(d.devicePosition)+'号'+ (d.deviceDirection== 1?'上行':'下行') +'</td><td style="width: 15%">'+(d.deviceType == 1? '基准传感器':'数据传感器')+'</td><td style="width: 15%">'+'无'+'</td><td><div> \n' +
                        '<input title="详情" type="button"  class="btn btn-primary backSearch police_caozuo" data-attr=\''+JSON.stringify(d)+'\' onclick="details(this)"/>' +
                        '<input title="修改" type="button"  class="btn btn-primary edit police_caozuo" data-attr=\''+JSON.stringify(d)+'\' onclick="edit(this)"/>' +
                        '<input title="初始化" type="button"  class="btn btn-primary edit police_caozuo initialize_icon" onclick="initialize(\''+d.deviceId+'\')"/>' +
                        '<input title="重启" type="button"   class="btn btn-danger police_del police_caozuo restart_icon" onclick="restart(\''+d.deviceId+'\')"/>' +
                        '<input title="删除" type="button"   class="btn btn-danger police_del police_caozuo del_icon" onclick="del(\''+d.a+'\')"/>' +
                        '             </div> </td></tr>';
                }
            },
    	'paginate':{
    		'visibleGo': true, /*是否开启直接翻至某页功能*/
    		'type':'full', /*默认按钮样式递增（numbers只有数字按钮，updown增加上下页按钮，full增加首尾页按钮）*/
    		'displayLen':10,  /*每页显示条数*/
    		'currentPage':1 /*当前页码（初始页码）*/
   	},
    	'serverSide': true, /*是否从服务器获取数据*/
    	/*ajax参数*/
    	'ajaxParam': {
			'url':'http://19.0.1.20:8081/monitor/queryMonitorDevice.do', /*url地址*/
			'type':'GET', /*ajax传输方式*/
			'dataType':'json', /*ajax传送数据格式*/
			'jsonp':'callback', /*dataType是jsonp的时候，传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名*/
            'jsonpCallback':'jsonpCallBack', /*dataType是jsonp的时候，自定义的jsonp回调函数名称*/
			// 'data':data /*传到服务器的数据*/
		},
		'sendDataHandle':function(d){
			d.page = d.currentPage;
			delete d.currentPage;
			d.dataNum = d.displayDataLen;
			delete d.displayDataLen;
            console.log('检索结果入参：' + JSON.stringify(d));
       
			return d;
		},  /*传递到服务器的数据预处理方法*/
		'backDataHandle':function(d){
           if(d==null){
               layer.alert('数据返回错误',{icon:0,btnAlign: 'c',skin: 'del-class'})
		   }
			console.log('检索结果是是是出参：' + JSON.stringify(d));
			var r = {'errFlag':false,'errMsg':'','dataLen':0,'data':[],'origData':null};
			if(d.code == 200) {
                			r.errMsg='success',r.dataLen=d.total,r.data=d.monitorDeviceSettingList;
                			r.origData=d.monitorDeviceSettingList;
				r.origData = d.monitorDeviceSettingList
			}else if(d.code == 401) {
				console.log("11")
                layer.alert('您没有权限',{icon:0,btnAlign: 'c',skin: 'del-class'})
			}

			return r;
		},  /*预处理从服务器的接收数据或者js传入的数据*/
    	'beforeShow':function(res){
            console.log(res)
        },  /*显示之前的额外处理事件*/
    	'afterShow':function(errFlag,errMsg,dataLen,listData){
    	}  /*显示之后的额外处理事件*/
	});
    function  formatter(val) {
        if(val== 0){
            return '未初始化'
        }else if(val== 1){
            return '正常'
        }else {
            return '异常'
        }
    }
	/*更新表格*/ 
	var refreshTable = function(data,page){
			if ($.isEmptyObject(data)) data = {};
			var toData = {
				'ajaxParam':{'data':data}
			}
			if (!$.isEmptyObject(page)){
				toData.paginate = {};
				toData.paginate.currentPage = page;
			}
			var $table = $page.find('.result-list');
			$table.yhhDataTable('refresh',toData);
		}
});