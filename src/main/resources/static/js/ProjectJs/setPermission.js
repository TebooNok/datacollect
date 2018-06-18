$(document).ready(function () {
    function renderTable(url,data){
        $('#testtable3').yhhDataTable({
            'paginate': {
                'changeDisplayLen': true,
                'type': 'updown',
                'visibleGo': true
            },
            'tbodyRow': {
                'zebra': true,
                'write': function (d) {
                    return '<tr><td style="width: 25%">' + d.id + '</td><td style="width: 25%">' + d.name + '</td><td style="width: 25%">' + d.role + '</td><td> <div> \n' +
                        '                 <input type="button"  value="编辑" class="btn btn-primary" data-attr=\'' + JSON.stringify(d) + '\' onclick="edit(this)"/>' +
                        '                 <input type="reset"  value="删除" class="btn btn-danger" onclick="del(\'' + d.id + '\')"/>' +
                        '             </div> </td></tr>';
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
                'data': data
            },
            'sendDataHandle': function (d) {
                d.page = d.currentPage;
                delete d.currentPage;
                d.dataNum = d.displayDataLen;
                delete d.displayDataLen;
                console.log('检索结果入参：' + JSON.stringify(d));
                return d;
            }, /*传递到服务器的数据预处理方法*/
            'backDataHandle': function (d) {
                if (d == null) {
                    layer.alert('数据返回错误', { icon: 0, btnAlign: 'c', skin: 'del-class' })
                }
                console.log('检索结果是是是出参：' + JSON.stringify(d));
                var r = { 'errFlag': false, 'errMsg': '', 'dataLen': 0, 'data': [], 'origData': null };
                if (d.code == 200) {
                    r.errMsg = 'success', r.dataLen = d.total, r.data = d.userList;
                    r.origData = d.userList;
                    r.origData = d.userList
                } else if (d.code == 401) {
    
                    layer.alert('您没有权限', { icon: 0, btnAlign: 'c', skin: 'del-class' })
                }
                return r;
            }, /*预处理从服务器的接收数据或者js传入的数据*/
            'beforeShow': function () {
            }, /*显示之前的额外处理事件*/
            'afterShow': function (errFlag, errMsg, dataLen, listData) {
            }  /*显示之后的额外处理事件*/
        });
    }

    renderTable("http://19.0.1.20:8081/monitor/queryUser.do")
    
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

    function search() {
        id = $("#importId").val()
        select =
            console.log(id)
    }

    $("#search_btn").click(function () {
        var params = getUrlParam($("#search").serialize());
        var data = [];
        for(var i in params) {
            if(params[i].length > 0) {
                data[i] = params[i]
            }
            
        }

        console.log(data)
        
       
        
       renderTable("http://19.0.1.20:8081/monitor/queryUser.do",data)
    })
})


