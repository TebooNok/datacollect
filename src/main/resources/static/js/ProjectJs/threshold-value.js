
function save() {
    var alarmLevel1 = $("#first").val()
    var alarmLevel2 = $("#second").val()
    var alarmLevel3 = $("#thrid").val()
    if((alarmLevel1 == '') || (alarmLevel2 == '') || (alarmLevel3 =='')){
        layer.alert('请确定阈值全部输入', { icon: 0, btnAlign: 'c', skin: 'del-class' });
    }else {
        $ajax(
        'monitor/setAlarmThre.do',
        {alarmLevel1,alarmLevel2,alarmLevel3},

        function (res) {
            console.log(res)
            console.log(res.code)
            layer.alert('保存成功', { icon: 0, btnAlign: 'c', skin: 'del-class' });
        },
        function (res) {
            console.log(res)
            layer.alert('保存失败', { icon: 0, btnAlign: 'c', skin: 'del-class' });
        },
        'post',
        'json'
    )}

    //   $.ajax({
    //         type: 'POST',
    //         url: 'https://jsonplaceholder.typicode.com/posts/1',
    //         data:{alarmLevel1:alarmLevel1,alarmLevel2:alarmLevel2,alarmLevel3:alarmLevel3},
    //     }).success(function () {
    //       layer.alert('保存成功', {icon:0,btnAlign: 'c',skin: 'del-class' });
    //   }).error(function () {
    //       layer.alert('保存失败', {icon:0,btnAlign: 'c',skin: 'del-class' });
    //   });
}

function cancel() {
    $("#first").val("")
    $("#second").val("")
    $("#thrid").val("")
}
