
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
                max: 50,
                name: '上浮/下沉量 mm',
                min: -50,
                interval: 5,
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
                        width: 2,
                        color: '#67b930'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 2,
                        color: '#67b930'
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
                        yAxis:50,
                        itemStyle: {
                            normal: {
                                color: 'rgba(255,13,0,0.25)'
                            }
                        },
                    }, {
                        yAxis: 30,
                    }],
                        [{
                            yAxis:30,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(255,97,0,0.23)'
                                }
                            },
                        }, {
                            yAxis: 15,
                        }],
                        [{
                            yAxis: 15,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(254,249,166,0.28)'
                                }
                            },
                        }, {
                            yAxis: 5,
                        }],[{
                            yAxis: 5,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(0,255,240,0.2)'
                                }
                            },
                        }, {
                            yAxis: -5,
                        }],[{
                            yAxis: -5,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(254,249,166,0.28)'
                                }
                            },
                        }, {
                            yAxis: -15,
                        }],
                        [{
                            yAxis: -15,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(255,97,0,0.23)'
                                }
                            }
                        }, {
                            yAxis: -30,
                        }],
                        [{
                            yAxis: -30,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(255,13,0,0.25)'
                                }
                            }
                        }, {
                            yAxis: -50,
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
                        width: 3,
                        color: '#2c6ea0'
                    }
                },
				lineStyle: {
                    normal: {
                        width: 3,
                        color: '#2c6ea0'
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
                            yAxis:50,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(247,213,225,0.4)'
                                }
                            },
                        }, {
                            yAxis: 25
                        }],
                        [{
                            yAxis: 25,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(235,220,249,0.1)'
                                }
                            },
                        }, {
                            yAxis: 5,
                        }],[{
                            yAxis: 5,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(205,255,252,0.1)'
                                }
                            },
                        }, {
                            yAxis: -5,
                        }],[{
                            yAxis: -5,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(235,220,249,0.1)'
                                }
                            },
                        }, {
                            yAxis: -25,
                        }],
                        [{
                            yAxis: -25,
                            itemStyle: {
                                normal: {
                                    color: 'rgba(247,213,225,0.4)'
                                }
                            }
                        }, {
                            yAxis: -50,
                        }]
                    ]
                },
                data: datas2
        
            }]
        };

        return option
    }
}