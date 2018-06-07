var timeData = ["1h","4h","8h","12h","16h","20h","24h"];
function randomData() {
    return ((Math.random()*(-1) + Math.random()) * 0.3).toFixed(1)
}

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


option2 = {
    title: {
        text: '',
        x: 'left',
    },
    tooltip: {
        trigger: 'axis',

        axisPointer: {
            animation: false
        }
    },
    legend: {
        data: ['流量'],
        x: 'center'
    },
    axisPointer: {
        link: {
            xAxisIndex: 'all'
        }
    },
    grid: [{
        left: 40,
        right: 40,
        show: true,
        borderWidth:'0'
    }, {
        left: 40,
        right: 40,
        show: true,
        borderWidth:'0'
    }],
    xAxis: [{
        type: 'category',
        boundaryGap: false,
        axisLine: {
            onZero: true,
            lineStyle: {
                color: '#FFFFFF',
                width: 4,
            }
        },
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
        data: timeData
    }, {
        gridIndex: 1,
        zlevel: -1,
    }],

    yAxis: [{

        type: 'value',
        max: 0.3,
        name: '上浮/下沉量',
        min: -0.3,
        interval: 0.1,
        axisLine: {
            onZero: true,
            lineStyle: {
                color: '#FFFFFF',
                width: 4,
            }
        },
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },

    }, {
        gridIndex: 1,
        zlevel: -1,
    }],
    series: [{
        name: '上浮',
        type: 'line',
        symbol: 'circle',
        symbolSize: 10,
        showSymbol: true,
        itemStyle: {
            normal: {
                color: 'rgb(126,200,85)'
            } 
        },
        lineStyle: {
            normal: {
                width: 4,
                color: 'rgba(126,200,85,0.65)'
            }
        },
        data: datas

    },{
        name: '上沉',
        type: 'line',
        symbol: 'circle',
        symbolSize: 10,
        showSymbol: true,
        itemStyle: {
            normal: {
                color: 'rgb(249,231,27)'
            } 
        },
        lineStyle: {
            normal: {
                width: 4,
                color: 'rgba(249,231,27,0.65)'
            }
        },
        data: datas2

    }]
};

// 基于准备好的dom，初始化echarts实例
var myChart2 = echarts.init(document.getElementById('main-line2'));
// 使用刚指定的配置项和数据显示图表。
myChart2.setOption(option2);