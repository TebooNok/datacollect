var timeData = ["#1","#2","#3","#4","#5","#6","#7","#8","#9","#10","#11","#12","#13","#14","#15","#16","#17","#18","#19","#20","#21"];
function randomData() {
    return ((Math.random()*(-1) + Math.random()) * 50).toFixed(1)
}

function renderImage() {
    var img =  document.createElement('img');
    img.src = "images/up-line.png";
    return img
}

function renderImageDown() {
    var img =  document.createElement('img');
    img.src = "images/down-line.png";
    return img
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

MAKELINE.homeFunc('main-line',datas,datas2,'#333333');