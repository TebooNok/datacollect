
var MAKELINE = {
    homeFunc: function(id,data,data2,axisColor) {
        // id 绑定节点的ID名称  data,data2 数据  axisColor 网格和坐标轴的颜色
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById(id));

        var option = this.getOption(data,data2,axisColor ? axisColor : '#FFF');
        
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    },
    getOption: function(data,data2,axisColor) {
       var option = {
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
                type: 'plain',
                show: true,
                data:[{name: '上行'},{name: '下行'}]
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
                boundaryGap: true,
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    onZero: true,
                    lineStyle: {
                        color: axisColor,
                        width: 4
                    }
                },
                splitLine: {
                    lineStyle: {
                        type: 'dashed',
                        color: axisColor
                    }
                },
                axisTick: {
                    show: true,
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
                        color: axisColor,
                        width: 4,
                    }
                },
                splitLine: {
                    lineStyle: {
                        type: 'dashed',
                        color: axisColor
                    }
                },
        
            }, {
                gridIndex: 1,
                zlevel: -1
            }],
            series: [{
                name: '上线',
                type: 'line',
                smooth: false,
                symbol: 'image://images/arrow_03.png',
                symbolSize: 15,
                showSymbol: true,
                itemStyle: {
                    normal: {
                        width: 4,
                        color: '#FFA101'
                    }
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
                                    color: {
                                        image: renderImage()
                                    },
                                    borderWidth: 0,
                                }
                            },
                            label: {
                                backgroundColor: 'yellow'
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
                                    color: {
                                        image:renderImageDown()
                                    },
                                    borderWidth: 0,
                                }
                            }
                        }, {
                            yAxis: -0.3,
                        }]
                    ]
                },
                data: datas
        
            },{
                name: '下线',
                type: 'line',
                smooth: false,
                symbol: 'image://images/blue-circle.png',
                symbolSize: 15,
                showSymbol: true,
                itemStyle: {
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

        return option
    }
}