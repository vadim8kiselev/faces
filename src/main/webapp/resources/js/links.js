var array = [];
var speed = 15;
var timer = 10;


function slider(target, showfirst) {
    var slider = document.getElementById(target);
    var divs = slider.getElementsByTagName('div');
    for (var index = 0; index < divs.length; index++) {
        var div = divs[index];
        var divid = div.id;
        if (divid.indexOf("header") != -1) {
            div.onclick = new Function("processClick(this)");
        } else if (divid.indexOf("body") != -1) {
            var section = divid.replace('-body', '');
            array.push(section);
            div.maxh = div.offsetHeight;
            if (showfirst == 1 && index == 1) {
                div.style.display = 'block';
            } else {
                div.style.display = 'none';
            }
        }
    }
}


function processClick(div) {
    for (var index = 0; index < array.length; index++) {
        var section = array[index];
        var container = section + '-body';
        var containerDiv = document.getElementById(container);
        clearInterval(containerDiv.timer);
        if (document.getElementById(section + '-header') == div &&
            containerDiv.style.display == 'none') {
            containerDiv.style.height = '0px';
            containerDiv.style.display = 'block';
            initSlide(container, 1);
        } else if (containerDiv.style.display == 'block') {
            initSlide(container, -1);
        }
    }
}


function initSlide(id, dir) {
    var container = document.getElementById(id);
    container.direction = dir;
    container.timer = setInterval("slide('" + id + "')", timer);
}


function slide(id) {
    var container = document.getElementById(id);
    var direction = container.direction;
    var maxHeight = container.maxh;
    var currentHeight = container.offsetHeight;
    var dist;
    if (direction == 1) {
        dist = (Math.round((maxHeight - currentHeight) / speed));
    } else {
        dist = (Math.round(currentHeight / speed));
    }
    if (dist <= 1) {
        dist = 1;
    }
    container.style.height = currentHeight + (dist * direction) + 'px';
    container.style.opacity = currentHeight / maxHeight;
    container.style.filter = 'alpha(opacity=' + (currentHeight * 100 / maxHeight) + ')';
    if (currentHeight < 2 && direction != 1) {
        container.style.display = 'none';
        clearInterval(container.timer);
    } else if (currentHeight > (maxHeight - 2) && direction == 1) {
        clearInterval(container.timer);
    }
}