var timeData = ["#1","#2","#3",,"#4","#5","#6","#7","#8","#9","#10","#11","#12","#14","#15","#16","#17","#18","#19","#20","#21","#3"];
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

option = {
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
    }, {
        left: 40,
        right: 40,
    }],
    xAxis: [{
        type: 'category',
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
        boundaryGap: false,
        axisLine: {
            onZero: true,
            lineStyle: {
                color: '#FFFFFF',
                width: 4,
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
        name: '',
        type: 'line',
        symbol: 'circle',
        symbolSize: 9,
        showSymbol: true,
        itemStyle: {
            color: 'blue'
        },
        lineStyle: {
            normal: {
                width: 4,
                color: '#FFA101'
            }
        },
        markArea: {
            silent: true,
            label: {
                normal: {
                    position: ['10%', '50%']
                }
            },
            data: [
                [{
                    yAxis: 0.3,
                    itemStyle: {
                        normal: {
                            color: 'rgba(70,66,125,0.4)'
                        }
                    },
                }, {
                    yAxis: 0.2
                }],
                [{
                    yAxis: 0.2,
                    itemStyle: {
                        normal: {
                            color: 'rgba(14,113,155,0.4)'
                        }
                    },
                }, {
                    yAxis: -0.2,
                }],
                [{
                    yAxis: -0.2,
                    itemStyle: {
                        normal: {
                            color: 'rgba(70,66,125,0.4)'
                        }
                    }
                }, {
                    yAxis: -0.3,
                }]
            ]
        },
        data: datas

    },{
        name: '',
        type: 'line',
        symbol: 'circle',
        symbolSize: 9,
        showSymbol: false,
        lineStyle: {
            normal: {
                width: 4,
                color: '#00E1BA'
            }
        },
        markArea: {
            silent: true,
            label: {
                normal: {
                    position: ['10%', '50%']
                }
            },
            data: [
                [{
                    yAxis: 0.3,
                    itemStyle: {
                        normal: {
                            color: 'rgba(70,66,125,0.4)'
                        }
                    },
                }, {
                    yAxis: 0.2
                }],
                [{
                    yAxis: 0.2,
                    itemStyle: {
                        normal: {
                            color: 'rgba(14,113,155,0.4)'
                        }
                    },
                }, {
                    yAxis: -0.2,
                }],
                [{
                    yAxis: -0.2,
                    itemStyle: {
                        normal: {
                            color: 'rgba(70,66,125,0.4)'
                        }
                    }
                }, {
                    yAxis: -0.3,
                }]
            ]
        },
        data: datas2

    }]
};

// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main-line2'));
// 使用刚指定的配置项和数据显示图表。
myChart.setOption(option);