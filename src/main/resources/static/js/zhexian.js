
function newDateString(days) {
    return moment().add(days, 'd').format();
}

var img = new Image();
img.src = 'images/colorthree_06.png';
img.onload = function() {
    var ctx = document.getElementById('canvas2').getContext('2d');
    var fillPattern = ctx.createPattern(img, 'repeat');
    console.log(fillPattern)
    var datas = [{
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }];

    var datas2 = [{
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }, {
        y: randomScalingFactor()
    }];

    var color = Chart.helpers.color;
    var config = {
        type: 'line',
        data: {
            labels: ['#1', '#2', '#3', '#4', '#5', '#6', '#7', '#8', '#9', '#10', '#11', '#12', '#13', '#14', '#15', '#16', '#17', '#18', '#19', '#20', '#21'],
            datasets: [{
                label: '上行',
                backgroundColor: color(window.chartColors.yellow).alpha(0.5).rgbString(),
                borderColor: window.chartColors.yellow,
                pointBackgroundColor: '#175993',
                fill: true,
                borderWidth: 5,
                pointBorderWidth: 3,
                pointBorderColor:color(window.chartColors.yellow).alpha(1).rgbString(),
                data: datas,
            }, {
                label: '下行',
                backgroundColor: color(window.chartColors.green).alpha(0.5).rgbString(),
                borderColor: window.chartColors.green,
                color: '#FFFFFF',
                borderWidth: 5,
                pointBackgroundColor: '#175993',
                pointBorderWidth: 3,
                pointBorderColor:color(window.chartColors.green).alpha(1).rgbString(),
                fill: true,
                data: datas2,
            }]
        },
        options: {
            defaultFontColor: '#FFFFFF',
            responsive: true,
            title: {
                display: true,
                text: ''
            },
            legend: {
                display: true,
                position: 'right',
                fullWidth: true,
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        color: 'rgba(255,255,255,1)'
                    },
                    scaleLabel: {
                        display: true,
                        color: 'red'
                    },
                    ticks: {
                        major: {
                            fontStyle: 'bold',
                            fontColor: '#FFFFFF'
                        }
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        color: 'rgba(255,255,255,1)'
                    },
                    scaleLabel: {
                        display: true,
                    },
                    ticks: {
                        min: -100,
                        max: 100,
                    }
                }]
            }
        }
    };

    window.onload = function () {
        window.myLine = new Chart(ctx, config);
    };
}