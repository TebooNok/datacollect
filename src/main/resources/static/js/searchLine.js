
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


function renderLine(datas,datas2,timeData,flag) {
  
    MAKELINE.homeFunc('main-line',datas,datas2,'#333333',timeData,flag);
}

