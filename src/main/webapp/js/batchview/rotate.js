
/*自适应大小*/
//左侧小图
function ResizeImage69(objImg) {
	ResizeImage(69,69,objImg);
}
//左侧选中小图
function ResizeImage80(objImg) {
	ResizeImage(80,80,objImg);
}
function AutoResizeImage(objImg) {
	var maxWidth =  $("#srcPic").width();
	var maxHeight =  $("#srcPic").height();
	ResizeImage(maxWidth, maxHeight, objImg);
}

function ResizeImage(maxWidth, maxHeight, objImg) {
    //var img = new Image();
    //img.src = objImg.src;
    var hRatio;
    var wRatio;
    var Ratio = 1;
    var w = objImg.width;
    var h = objImg.height;
    wRatio = maxWidth / w;
    hRatio = maxHeight / h;
    if (maxWidth == 0 && maxHeight == 0) {
        Ratio = 1;
    } else if (maxWidth == 0) { //
        if (hRatio < 1) Ratio = hRatio;
    } else if (maxHeight == 0) {
        if (wRatio < 1) Ratio = wRatio;
    } else if (wRatio < 1 || hRatio < 1) {
        Ratio = (wRatio <= hRatio ? wRatio: hRatio);
    }
    if (Ratio < 1) {
        w = w * Ratio;
        h = h * Ratio;
    }
    objImg.height = h;
    objImg.width = w;
	//让图片居中显示
	var marginLeft=(maxWidth-w)/2;  
	$('#'+objImg.id).css("left",marginLeft); 
	var marginTop=(maxHeight-h)/2;  
	$('#'+objImg.id).css("top",marginTop);
}

function AutoResizeImageAfterRotate(id){
	var maxWidth =  $("#srcPic").width();
	var maxHeight =  $("#srcPic").height();
	
    var objImg = document.getElementById(id);
	var hRatio;
    var wRatio;
    var Ratio = 1;
    var w = objImg.width;
    var h = objImg.height;
    wRatio = maxWidth / w;
    hRatio = maxHeight / h;
    if (maxWidth == 0 && maxHeight == 0) {
        Ratio = 1;
    } else if (maxWidth == 0) { //
        if (hRatio < 1) Ratio = hRatio;
    } else if (maxHeight == 0) {
        if (wRatio < 1) Ratio = wRatio;
    } else if (wRatio < 1 || hRatio < 1) {
        Ratio = (wRatio <= hRatio ? wRatio: hRatio);
    }
    if (Ratio < 1) {
        w = w * Ratio;
        h = h * Ratio;
    }
    objImg.style.height = h+'px';
    objImg.style.width = w+'px';
	//让图片居中显示
	var marginLeft=(maxWidth-w)/2;  
	$('#oImg').css("left",marginLeft); 
	var marginTop=(maxHeight-h)/2;  
	$('#oImg').css("top",marginTop); 	
}

/*绑定事件*/
function addEvent(obj, sType, fn) {
	if (obj.addEventListener) {
		obj.addEventListener(sType, fn, false);
	} else {
		obj.attachEvent('on' + sType, fn);
	}
};
function removeEvent(obj, sType, fn) {
	if (obj.removeEventListener) {
		obj.removeEventListener(sType, fn, false);
	} else {
		obj.detachEvent('on' + sType, fn);
	}
};
function prEvent(ev) {
	var oEvent = ev || window.event;
	if (oEvent.preventDefault) {
		oEvent.preventDefault();
	}
	return oEvent;
}
/*添加滑轮事件*/
function addWheelEvent(obj, callback) {
	if (window.navigator.userAgent.toLowerCase().indexOf('firefox') != -1) {
		addEvent(obj, 'DOMMouseScroll', wheel);
	} else {
		addEvent(obj, 'mousewheel', wheel);
	}
	function wheel(ev) {
		var oEvent = prEvent(ev),
		delta = oEvent.detail ? oEvent.detail > 0 : oEvent.wheelDelta < 0;
		callback && callback.call(oEvent, delta);
		return false;
	}
};
/*页面载入后*/
var imgIndex = 0; //选中的图片索引值，默认为0
window.onload = function() {
	for ( var i = 0; i < (imgTotal-1); i++ ) {
		var pic = document.getElementById("img"+i);
		if(i==imgIndex){
			ResizeImage80(pic);
		} else {
			ResizeImage69(pic);
		}
	}
	showPic(imgIndex);
	AutoResizeImage(oImg);
	autoBind();
};


function autoBind(){
	var oImg = document.getElementById('oImg');
	/*拖拽功能*/
	(function() {
		addEvent(oImg, 'mousedown', function(ev) {
			var oEvent = prEvent(ev),
			oParent = oImg.parentNode,
			disX = oEvent.clientX - oImg.offsetLeft,
			disY = oEvent.clientY - oImg.offsetTop,
			startMove = function(ev) {
				if (oParent.setCapture) {
					oParent.setCapture();
				}
				var oEvent = ev || window.event,
				l = oEvent.clientX - disX,
				t = oEvent.clientY - disY;
				oImg.style.left = l +'px';
				oImg.style.top = t +'px';
				oParent.onselectstart = function() {
					return false;
				}
			}, endMove = function(ev) {
				if (oParent.releaseCapture) {
					oParent.releaseCapture();
				}
				oParent.onselectstart = null;
				removeEvent(oParent, 'mousemove', startMove);
				removeEvent(oParent, 'mouseup', endMove);
			};
			addEvent(oParent, 'mousemove', startMove);
			addEvent(oParent, 'mouseup', endMove);
			return false;
		});
	})();
	/*以鼠标位置为中心的滑轮放大功能*/
	(function() {
		addWheelEvent(oImg, function(delta) {
			var ratioL = (this.clientX - oImg.offsetLeft) / oImg.offsetWidth,
			ratioT = (this.clientY - oImg.offsetTop) / oImg.offsetHeight,
			ratioDelta = !delta ? 1 + 0.1 : 1 - 0.1,
			w = parseInt(oImg.offsetWidth * ratioDelta),
			h = parseInt(oImg.offsetHeight * ratioDelta),
			l = Math.round(this.clientX - (w * ratioL)),
			t = Math.round(this.clientY - (h * ratioT));
			with(oImg.style) {
				width = w +'px';
				height = h +'px';
				left = l +'px';
				top = t +'px';
			}
		});
	})();
};


function rotate(id, angle, whence) {
    var p = document.getElementById(id);
    if (!whence) {
        p.angle = ((p.angle == undefined ? 0 : p.angle) + angle) % 360;
    } else {
        p.angle = angle;
    }
    if (p.angle >= 0) {
        var rotation = Math.PI * p.angle / 180;
    } else {
        var rotation = Math.PI * (360 + p.angle) / 180;
    }
    var costheta = Math.cos(rotation);
    var sintheta = Math.sin(rotation);
    if (document.all && !window.opera) {
        var canvas = document.createElement('img');
        canvas.src = p.src;
        canvas.height = p.height;
        canvas.width = p.width;
        canvas.style.filter = "progid:DXImageTransform.Microsoft.Matrix(M11=" + costheta + ",M12=" + ( - sintheta) + ",M21=" + sintheta + ",M22=" + costheta + ",SizingMethod='auto expand')";
    } else {
        var canvas = document.createElement('canvas');
        if (!p.oImage) {
            canvas.oImage = new Image();
            canvas.oImage.src = p.src;
        } else {
            canvas.oImage = p.oImage;
        }
        //alert(canvas.width)
        canvas.style.width = canvas.width = Math.abs(costheta * canvas.oImage.width) + Math.abs(sintheta * canvas.oImage.height);
        canvas.style.height = canvas.height = Math.abs(costheta * canvas.oImage.height) + Math.abs(sintheta * canvas.oImage.width);
        var context = canvas.getContext('2d');
        context.save();
        if (rotation <= Math.PI / 2) {
            context.translate(sintheta * canvas.oImage.height, 0);
        } else if (rotation <= Math.PI) {
            context.translate(canvas.width, -costheta * canvas.oImage.height);
        } else if (rotation <= 1.5 * Math.PI) {
            context.translate( - costheta * canvas.oImage.width, canvas.height);
        } else {
            context.translate(0, -sintheta * canvas.oImage.width);
        }
        context.rotate(rotation);
        context.drawImage(canvas.oImage, 0, 0, canvas.oImage.width, canvas.oImage.height);
        context.restore();
    }
    canvas.id = p.id;
    canvas.angle = p.angle;
    p.parentNode.replaceChild(canvas, p);
}

//向右旋转
function rotateRight(id, angle) {
    rotate(id, angle == undefined ? 90 : angle);
	
    AutoResizeImageAfterRotate(id);	
    autoBind();	
}

//向左旋转
function rotateLeft(id, angle) {
    rotate(id, angle == undefined ? -90 : -angle);
	
    AutoResizeImageAfterRotate(id);
    autoBind();
}

function showPic(i) {
	if(i!=imgIndex){
		$("#img"+i).parent().parent().addClass("focus");
		$("#img"+i).parent().parent().addClass("imgitem-focus");
		$("#img"+imgIndex).parent().parent().removeClass("focus");
		$("#img"+imgIndex).parent().parent().removeClass("imgitem-focus");
		//AutoResizeImageAfterRotate(69,69,"img"+imgIndex);
		//AutoResizeImageAfterRotate(80,80,"img"+i);
		
		//调整左侧位置
		if((imgIndex<=3 && i <=3) || (imgIndex>=(imgTotal-4) && i >=(imgTotal-4))){
		} else{
			$(".album-imgs").stop(true, false).animate({top: -(i-3)*80+"px"},621);
		}
		
		imgIndex = i;	
	}
    var p = document.getElementById("vertical");
    var pic = document.getElementById("img"+i);
    var oImg = document.getElementById("oImg");
	
	//var fileName = pic.src.match(/[^\/]*$/)[0];
	//$('#fileName').html(fileName);	
	var filecount = $('#imgcount').val();
	$('#filecount').html('图片总数：'+filecount);
	//alert(fileSeq);
	var fileSeq = $('#seq'+i).val();
	$('#fileName').html('图片序号：'+fileSeq);	
    p.removeChild(oImg);
    var img = document.createElement("img");
    //设置 img 属性，如 id
    img.setAttribute("id", "oImg");
	//设置 img 图片地址
	img.src = pic.src;
	p.appendChild(img);
    AutoResizeImage(img);
    autoBind();
}

function turnPre(){
	if(imgIndex==0) {
		alert("已经是第一张图");
	} else{		
    var i = imgIndex-1;
	showPic(i);
	}
}

function turnNext(){
    if(imgIndex==(imgTotal-2)) {
		alert("已经是最后一张图");
	} else{
    	var i = imgIndex+1;
		showPic(i);
	}
}