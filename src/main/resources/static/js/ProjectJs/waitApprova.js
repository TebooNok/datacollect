
    function getStatus(params) {
        switch (params) {
            case 1:
                return '未处理';
            case 2:
                return '处理中';
            case 3:
                return '已处理';
            default:
                return '未告警'
        }
    }

    function renderTable(url) {
    $('#testtable3').yhhDataTable({
        'paginate': {
            'changeDisplayLen': true,
            'type': 'updown',
            'visibleGo': true
        },
        'tbodyRow': {
            'zebra': true,
            'write': function (d) {
                var tr = '<tr><td><input type="checkbox" name="" class="checkbox" lay-skin="primary" /> <i class="layui-icon layui-icon-ok "></i></td><td>' + (d.alarmType == 1 ? '上浮' : '下沉') + '</td><td>' + d.alarmDevicePosition + '号桥墩' + '</td><td>' + d.alarmDateTime + '</td><td>' + getStatus(d.alarmStatus) + '</td><td>' + d.alarmProcessUser + '</td><td>' + d.alarmProcessTime + '</td><td>' + d.alarmProcessMessage + '</td><td>' + d.alarmConfirmUser + '</td><td>' + d.alarmConfirmTime + '</td><td><p class="noWord">' + d.alarmConfirmMessage + '</p></td><td> <div> \n' +
                    '                 <input title="查看详情" type="button"  class="btn btn-primary backSearch police_caozuo" data-attr=\'' + JSON.stringify(d) + '\' onclick="views(this)"/>';
                if (d.alarmStatus == 1) {


                    tr += '                 <input title="管理员确认" type="button"  class="btn btn-primary admins police_caozuo" onclick="disposed(' + d.alarmDeviceId + ',\'' + d.alarmProcessUser + '\',' + d.alarmType + ',\'' + d.alarmDateTime + '\'' + ',' + d.alarmDevicePosition + ')"/>';
                }
                if (d.alarmStatus == 2) {
                    tr += '				<input title="高级管理员确认" type="button"  class="btn btn-primary Higeradmins police_caozuo" onclick="affirmDispose('+ d.alarmDeviceId + ',\'' + d.alarmProcessUser + '\',' + d.alarmType + ',\'' + d.alarmDateTime + '\'' + ',' + d.alarmDevicePosition + ',\'' + d.alarmProcessTime + '\'' + ','+ d.alarmLevel +',\''+ d.alarmProcessMessage + '\')"/>';
                }
                tr += '				<input title="删除" type="button"  class="btn btn-primary police_del police_caozuo" onclick="del(\'' + d.a + '\')"/>' +
                    '             </div> </td></tr>';

                return tr;
            }
        },
        'serverSide': true, /*是否从服务器获取数据*/
        /*ajax参数*/
        'ajaxParam': {
            'url': url, /*url地址*/
            'type': 'GET', /*ajax传输方式*/
            'dataType': 'json', /*ajax传送数据格式*/
            'jsonp': 'callback', /*dataType是jsonp的时候，传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名*/
            'jsonpCallback': 'jsonpCallBack', /*dataType是jsonp的时候，自定义的jsonp回调函数名称*/
        },
        'sendDataHandle': function (d) {
            d.page = d.currentPage;
            delete d.currentPage;
            d.dataNum = d.displayDataLen;
            delete d.displayDataLen;
            return d;
        }, /*传递到服务器的数据预处理方法*/
        'backDataHandle': function (d) {
            if (d == null) {
                layer.alert('数据返回错误', { icon: 0, btnAlign: 'c', skin: 'del-class' })
                return false;
            }

            var r = { 'errFlag': false, 'errMsg': '', 'dataLen': 0, 'data': [], 'origData': null };
            if (d.code == 200) {
                r.errMsg = 'success', r.dataLen = d.total, r.data = d.alarmInfoList;
                r.origData = d.alarmInfoList;
                r.origData = d.alarmInfoList

            } else if (d.code == 401) {
                console.log("11")
                layer.alert('您没有权限', { icon: 0, btnAlign: 'c', skin: 'del-class' })
            }
            return r;
        }, /*预处理从服务器的接收数据或者js传入的数据*/
        'beforeShow': function () {
           
        }, /*显示之前的额外处理事件*/
        
        'afterShow': function (errFlag, errMsg, dataLen, listData) {
            $(".yhh-data-table-frame").first();

            if($(".yhh-data-table-frame").length > 1) {
                $(".yhh-data-table-frame").first().html($(".yhh-data-table-frame").last().html());
             //   $(".yhh-data-table-frame").last().remove();
            }
            
        }  /*显示之后的额外处理事件*/
        
    });

}

    renderTable('http://19.0.1.20:8081/monitor/queryAlarm.do?mode=4')

    /*更新表格*/
    var refreshTable = function (data, page) {

        if ($.isEmptyObject(data)) data = {};
        var toData = {
            'ajaxParam': { 'data': data }
        }
        if (!$.isEmptyObject(page)) {
            toData.paginate = {};
            toData.paginate.currentPage = page;
        }
        var $table = $page.find('.result-list');
        $table.yhhDataTable('refresh', toData);

    }
