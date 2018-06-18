window.onload = function() {

    var timeData = ["#1","#2","#3","#4","#5","#6","#7","#8","#9","#10","#11","#12","#13","#14","#15","#16","#17","#18","#19","#20","#21"];
    var datas = [
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
    ]
    
    var datas2 = [
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
        randomData(),
    ]

layui.config({
    base: './'
}).extend({
    formSelects: 'formSelects-v3'
});
layui.use('laydate', function () {
    var laydate = layui.laydate;
    laydate.render({
        elem: '#time1',
        type: 'datetime',
       
         done: function (value, date, endDate) {
           // $("input[name=alarmStartTime]").val(value)
        }
    });
    laydate.render({
        elem: '#time2',
        type: 'datetime',
      
         done: function (value, date, endDate) {
           // $("input[name=alarmEndTime]").val(value)
        }
    });
});

function renderData(data) {
    $ajax('monitor/queryData.do', data, function (res) {

        if (res.code == 200) {
            var tr = "";
            for (var i in res.alarmInfoList) {
                console.log(i)
                tr += "<tr><td>" + res.alarmInfoList[i].alarmDateTime + "</td>";
                tr += "<td>" + res.alarmInfoList[i].alarmDevicePosition + "号桥墩</td>";
                tr += "<td>" + (res.alarmInfoList[i].alarmDeviceDirection == 1 ? '上行' : '下行') + "</td>";
                tr += "<td>" + (res.alarmInfoList[i].height / 1000) + "</td>";
                if (res.alarmInfoList[i].alarmStatus == 1) {
                    tr += "<td>未处理</td>"
                } else if (res.alarmInfoList[i].alarmStatus == 2) {
                    tr += "<td>处理中</td>"
                } else if (res.alarmInfoList[i].alarmStatus == 3) {
                    tr += "<td>已处理</td>"
                } else {
                    tr += "<td>未警告</td>"
                }
                tr += "</tr>"
            }

            document.getElementById("tbody").innerHTML = tr;
        }
    },
        function (res) {

        },
        'get');
}

function renderLineByData(data) {
    $ajax('monitor/queryData.do', data, function (res) {

        if (res.code == 200) {
            var preg = /(\d+-)|(\d+\s)/g;
            var item = res.alarmInfoList;
            var xdata = item.map(function(item){
                return item.alarmDateTime.replace(preg,"").replace(/\:00$/,"");
            })
         
            var updata = item.map(function(item) {
                if(item.alarmDeviceDirection == 1) {
                    return item.height / 1000
                } 
            });

            var downdata = item.map(function(item) {
                if(item.alarmDeviceDirection == 2) {
                    return item.height / 1000
                }
            });


            renderLine(updata,downdata,xdata,true)
        }

    },function(res) {},'get')
}

renderLine(datas,datas2,timeData,true);

// 数组去重
function getUniqueArr(data) {
    data.sort();
    var res = [];
    var json = {};
    for(var i=0;i<data.length;i++) {
        if(!json[data[i]]) {
            res.push(data[i])
            json[data[i]] = 1;
        }
    }
    return sortByNum(res);
}

// 数组拍寻
function sortByNum(arr) {
 for(var i=0;i<arr.length;i++) {
     for(var j=0; j<arr.length-i-1;j++) {
         if(arr[j] >arr[j+1]) {
             var hand = arr[j];
             arr[j] = arr[j+1];
             arr[j+1] = hand;
         }
     }
 }
 return arr
}

function getUrlParam(params) {
    var arr = params.split("&");
    var args = {};
    for (var i = 0; i < arr.length; i++) {
        item = arr[i].split("=")
        var name = decodeURIComponent(item[0]), value = decodeURIComponent(item[1]);
        if (name) {
            args[name] = value
        }
    }
    return args;
}



renderLineByData({ mode: 2 })

$("#search").on('click',function() {
    var data = getUrlParam($("#search_alarm").serialize());
    
    for(var i in data) {
        if(data[i].length <= 0) {
           delete data[i]
        } else {
            var preg = /\+/g;
            data[i] = data[i].replace(preg," ")
        }
    }
    data.mode = 2;
  
    if(!data.deviceId) {
        layer.msg('设备Id不能为空');
        return false;
    }
    if(!data.startDateTime) {
        layer.msg('开始时间不能为空');
        return false;
    }
    if(!data.endDateTime) {
        layer.msg('结束时间不能为空');
        return false;
    }

   

    data.templateType = "Custom_100";
   console.log(data)
    
    renderData(data)
})
$("#reset").on('click',function() {
   window.location.reload()
});

$("#excel").on('click',function() {
 
    $ajax("monitor/queryDataExcel.do",{},function(res){
        console.log(res)
    },function(res){},'get');
});



}
