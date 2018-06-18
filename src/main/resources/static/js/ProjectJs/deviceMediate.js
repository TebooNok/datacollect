$(document).ready(function(){
	var testdata2 = [ ];
	$('#testtable3').yhhDataTable({
		'paginate':{
			'changeDisplayLen':true,
			'type':'updown',
			'visibleGo': true
		},
		'tbodyRow':{
			'zebra':true,
            'hover':true,  /*行hover效果*/
			'write':function(d){
				return '<tr><td style="width: 20%">'+dataChange(d.timestamp)+'</td><td style="width: 15%">'+(d.status == 1? '正常':'异常')+'</td><td style="width: 15%">'+d.b+'</td><td style="width: 15%">'+d.c+'</td><td style="width: 15%">'+d.deviceId+'</td><td><div> \n' +
                    '                 <input title="详情" type="button"  class="btn btn-primary backSearch police_caozuo" data-attr=\''+JSON.stringify(d)+'\' onclick="details(this)"/>' +
                    '                 <input title="修改" type="button"  class="btn btn-primary edit police_caozuo"   data-attr=\''+JSON.stringify(d)+'\' onclick="edit(this)"/>' +
                    '<input title="启用" type="button"class="btn btn-danger police_del police_caozuo start_use" onclick="start_use(\''+d.deviceId+'\')"/>' +
                    '                 <input title="停用" type="button"   class="btn btn-danger police_del police_caozuo stop_icon" onclick="stop(\''+d.deviceId+'\')"/>' +
                    '                 <input title="删除" type="button"   class="btn btn-danger police_del police_caozuo del_icon" onclick="del(\''+d.a+'\')"/>' +
                    '             </div> </td></tr>';
			}
		},
        'serverSide': true, /*是否从服务器获取数据*/
        /*ajax参数*/
        'ajaxParam': {
            'url':'http://19.0.1.20:8081/monitor/queryDemoDevice.do', /*url地址*/
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
            console.log("111")
            return d;
        },  /*传递到服务器的数据预处理方法*/
        'backDataHandle':function(d){
            if(d==null){
                layer.alert('数据返回错误',{icon:0,btnAlign: 'c',skin: 'del-class'})
            }
            console.log('检索结果是是是出参：' + JSON.stringify(d));
            var r = {'errFlag':false,'errMsg':'','dataLen':0,'data':[],'origData':null};
            if(d.code == 200) {
                r.errMsg='success',r.dataLen=d.total,r.data=d.demoDeviceList;
                r.origData=d.demoDeviceList;
                r.origData = d.demoDeviceList
            }else if(d.code == 401) {
                console.log("11")
                layer.alert('您没有权限',{icon:0,btnAlign: 'c',skin: 'del-class'})
            }

            return r;
        },  /*预处理从服务器的接收数据或者js传入的数据*/
        'beforeShow':function(){},  /*显示之前的额外处理事件*/
        'afterShow':function(errFlag,errMsg,dataLen,listData){
        }  /*显示之后的额外处理事件*/
    });
    function  dataChange(timestamp) {
        if(timestamp == ''){
            return 无
        }else{
            var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            Y = date.getFullYear() + '-';
            M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            D = date.getDate() + ' ';
            h = date.getHours() + ':';
            m = date.getMinutes() + ':';
            s = date.getSeconds();
            return Y+M+D+h+m+s;
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