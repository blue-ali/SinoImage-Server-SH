<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>影像调阅页面</TITLE> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/batchview/jquery-1.7.2.js"></script>
<LINK href="<%=request.getContextPath()%>/css/batchview/detailbase.css" rel="stylesheet" type="text/css">
<LINK href="<%=request.getContextPath()%>/css/batchview/fmdetail.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	$.ajaxSetup({
		async : false
	});
	$(function(){
		var txtBatchNo = '<%=request.getParameter("barno")%>';
		$("#txtBatchNo").attr("value",txtBatchNo);
		$("#fileBatchNo").html('报销单号：'+txtBatchNo);
		/* $(document).ready(function() {

			 $(".radioItem").change(

			function() {

			var $selectedvalue = $("input[name='RadioGroup1']:checked").val();

			alert($selectedvalue);
"src/main/webapp/jsp/unity/imagetreeview.jsp"
			if ($selectedvalue == 1) {

			window.location = "http://www.g.cn";

			}

			else {

			window.location = "http://www.baidu.com";

			}

			});});  */
		//提交
		 
		
		$("#remarksubmit").click(function(){
			//alert($('#remarkForm').serialize());
			var batchid = $("tbody[id=batchBody] tr td[name=batchid]").html();
			//var batchone = $("tbody[id=batchBody] tr td[name=batchaddone]").html();
			if(batchid!=""&&batchid!=null&&typeof(batchid) != "undefined"){
				batchid = batchid+ "|0";
				$("#dealBatchNo").val(batchid);
			}else{
				batchid = $("#txtBatchNo").val() + "|99";
				$("#dealBatchNo").val(batchid);
			}
			
			var fileid = $("tbody[id=imgBody] tr td[name=fileid]");
			var fileone = $("tbody[id=imgBody] tr td[name=checkone]");
			if(fileid.length ==0){
				$("#dealImgList").val("");
				//alert($("#dealImgList").val()+"555555555");
			} else{
				var writeFile;
				var writeFileList;
				for(var i=0;i<fileid.length;i++){
					var fileval1 = fileid.eq(i).html();
					var fileoneval1 = fileone.eq(i).html();
					var remark;
					if(fileoneval1=="影像与报销单不符"){
						remark = '00';
					}else if(fileoneval1=="影像不清晰"){
						remark = '01';
					}else if(fileoneval1=="影像显示不全"){
						remark = '02';
					}else{
						remark = "03";
					}
					writeFile = fileval1 + "|" +remark;
					if(i != fileid.length){
						writeFile = writeFile + ",";
					}
				}
				$("#dealImgList").val(writeFile);
					//alert($("#dealImgList").val());
			}
			alert($('#remarkForm').serialize());
			var checkbatch = $("#dealBatchNo").val();
			var checkimg = $("#dealImgList").val();
			if(checkbatch==""&&checkimg==""){
				alert("请选择提交的数据");
			}else{
				var hint =  confirm('确定提交备注信息吗？');	
				if(hint==true){
				 $.ajax({
	                type: "POST",
	                dataType: "html",
	                url:"<%=request.getContextPath()%>/BatchVerify",
	                data: $('#remarkForm').serialize(),
	                success: function (result) {
	                    var strresult=result;
	                    //alert(strresult);
	                    //window.opener=null;
	                    window.close();
	                }/* ,
	                error: function(data) {
	                    alert("error:"+data.responseText);
	                 } */
					}) 
				}	
				
			}
			
		});
		
		 $("#deletesubmit").click(function(){
			$(".delete").remove();
			$("#dealBatchNo").val("");
			$("#dealImgList").val("");
		}); 
		
					
		//单个删除
		$("td input[type=button]").live("click",function(){
			$(this).parent().parent().remove();
			/*  var id = $(this).parent().parent().attr("id");
			alert(id);
			if(id!=''||(typeof(id) != "undefined")){
				if(id=="batch"){
					$("#dealBatchNo").val('');
				} else{
					var dealImgList = $("#dealImgList").val();
					alert(dealImgList+"??????//");
					var imgListTmp;
					var trlist = dealImgList.split('\\|');
					alert(trlist.length);
					for(var i=0;i<trlist.length;i++){
						var imgInfo = trlist[i].split('-');
						if(imgInfo[0]!=id){
							imgListTmp += trlist[i];
						}else{
							
						}
					}
					$("#dealImgList").val(imgListTmp);
					alert($("#dealImgList").val());
				} 
			} */
			
		});
		//添加批次备注
		$("#addbatch").click(function(){
			var batchid = $("#txtBatchNo").val();
			var batchaddone = $('input:radio[name="RadioGroup1"]:checked').val().split('|');
			//var batchTD = $("<td><input name='batchid' type='hidden' value='"+batchid+"' />"+batchid+"</td>");
			var batchTD = $("<td name='batchid'>"+batchid+"</td>");
			var addoneTD = $("<td name='batchaddone'>"+batchaddone[1]+"</td>");
			var option = $("<td><input type='button' value='删除' /></td>");	
			var tr = $("<tr id='batch' class='delete'></tr>");
			tr.append(batchTD);
			tr.append(addoneTD);
			tr.append(option);
			$("#batchBody").html(tr);
			/* $("#dealBatchNo").val(batchid+'-'+batchaddone[0]);
			alert($("#dealBatchNo").val()); */
		});
		//添加一张影像
		 $("#addbtn").click(function(){
			var seqeance = $("#fileName").html();
			var fileid = seqeance.split('：');
			var fileseq = fileid[1];
			
			$("#tr"+fileseq).remove();
			
			var console = $('input:radio[name="RadioGroup2"]:checked').val().split('|');
			var nameTD = $("<td name='fileid'>"+fileseq+"</td>");
			var consoleTD = $("<td name='checkone'>"+console[1]+"</td>");
			var option = $("<td><input type='button' value='删除' /></td>");			
			//var aaa ="<tr><td><input type='checkbox'></td><td>"+name+"<td/><td>"+console+"</td><td><input type='button' value='delete' /></td></tr>";
			var tr = $("<tr id='tr"+fileseq+"' class='delete'></tr>");
			tr.append(nameTD);
			tr.append(consoleTD);
			tr.append(option); 
			 if(fileseq==''){
				alert("未点中图片");
			}else if(console==''||(typeof(console) == "undefined")){
				alert("请点击单张影像审核的选项");
			}else{
				$("#imgBody").append(tr);
			}
			/* var imgListTmp = $("#dealImgList").val();
			if(imgListTmp==''||(typeof(imgListTmp) == "undefined")){
				imgListTmp = fileseq+"-"+console[0];
			}  else if(){
			
		 	} else{
				imgListTmp += "," + fileseq+"-"+console[0];
			}
			alert(imgListTmp);
			 $('#dealImgList').val(imgListTmp); */
		}); 
		//获取批次信息的值
		/* function findBatch(){
			var batch = new Object();
			batch.id = txtBatchNo;
			batch.addone = $("#batchid");
			var values = new Array();
			var files = $(".imgitem").val();
			for(var i=0;i<files.length;i++){
				if(files.name =='fileid'){
					values.push(files[i].value);
					return values;
				}
			}
		} */
		
		
		//父页面传过来的批次号
		//var txtBatchNo = window.opener.document.getElementById("txtBatchNo").value;
		//文件树展示
		loadFirstImg();//右侧区域图片
		
		function loadFirstImg(){
			 //$.getJSON('http://127.0.0.1:9080/TigEra.Document.Server/serverForBrowserView?batchNo=' + txtBatchNo 
			<%-- //$.get('<%=request.getContextPath()%>/cm/imageContentAction.action?batchNo=' + txtBatchNo  --%>
			$.getJSON('<%=request.getContextPath()%>/GetBatchForWeb?batchNo=' + txtBatchNo
				 + '&rdm=' + Math.random(),
		 		 function(data) {
				 	console.log(data);
				 	var oneDiv = "";
					var imagesinfo = new Array("");
				 	var imagesinfo2 = new Array("");
				 	var imagesinfo3 = new Array("");
				 	var filecount = data.rows.length;
					for(var i=0;i<data.rows.length;i++){
						var imageName = data.rows[i].fileName;
						var imageSequece = data.rows[i].fileId;
						var imagePath = data.rows[i].fileUrl;
						imagesinfo.push(imagePath);
						imagesinfo2.push(imageName); 
						imagesinfo3.push(imageSequece); 
													
					    var houzui=imageName.split(".");
					    var last=houzui[houzui.length-1];
					    var tp ="jpg,Jpg,JPG,gif,Gif,GIF,bmp,Bmp,BMP,jpeg,Jpeg,JPEG,png,Png,PNG";
					    var rs=tp.indexOf(last);
					    if(rs>=0){
							var img = imagesinfo[i+1];
							//var reImage = img.substring(0,img.length -1) + "0";
							var reImage = img;
							var imgName = imagesinfo2[i+1];
							var imgSequece = imagesinfo3[i+1];
							//alert(imgSequece+"/////////////");
							oneDiv = oneDiv + createImg(i,reImage,imgName,reImage,imgSequece,filecount);
							//oneDiv = oneDiv + createImg(i,reImage,reImage);
							//alert(document.getElementById("img0").value);
					    }else{
							 var img = imagesinfo[i+1];
							 var reImage = img.substring(0,img.length -1) + "0";
							 var txt ="txt,TXT";
							 var doc ="doc,DOC,docx,DOCX";
							 var pdf ="pdf,PDF";
							 var txtrs=txt.indexOf(last);
							 var docrs=doc.indexOf(last);
							 var pdfrs=pdf.indexOf(last);
							 
							 if(txtrs>=0){
								 var tmpImg = "<%=request.getContextPath()%>/png/txt.png";
							 }else if(docrs>=0){
								 var tmpImg = "<%=request.getContextPath()%>/png/word.png";
							 }else if(pdfrs>=0){
								 var tmpImg = "<%=request.getContextPath()%>/png/pdf.png";
							 } else {
								 var tmpImg = "<%=request.getContextPath()%>/png/otherdoc.png";
							 }
							
							 var imgName = imagesinfo2[i+1];
							 var imgSequece = imagesinfo3[i+1];
							 var downUrl =  reImage;	 
							 oneDiv = oneDiv + createImg(i,reImage,reImage,downUrl,imgSequece);
						}
					    //alert(oneDiv);	
					}
					oneDiv = oneDiv + "<li title='' class='imgenditem' data-idx='enditem_0'></li>";
					
					//alert("++++++++++"+imgTotal);
					//document.getElementById("Div1_main").appendChild(oneDiv);
					$('#Div1_main').html(oneDiv);
				});
		}
		
		 
	});
	
	function createImg(i,imgPath, imgName, downUrl,imgSequece,filecount){
		// i 编号   imgPath 显示图片地址 imgName 显示图片名称 downUrl下载文件地址
		var newLi = " <li class='imgitem";
		if(i==0) {newLi +=" focus imgitem-focus";}
		newLi += "' data-idx='"+i+"'><div class='img-box' onclick='showPic("+i+")'><img id='img"+i+"' src='"+imgPath+"' alt='"+imgName+"' title='"+imgName+"'>";
		newLi += "<input name='seq"+i+"' id='seq"+i+"' type=hidden value='"+imgSequece+"' />";
		newLi += "<input name='imgcount' id='imgcount' type=hidden value='"+filecount+"' />";
		newLi += "</div></li>";        
		return newLi;
	}
</script>

<META name="GENERATOR" content="MSHTML 11.00.9600.17842"></HEAD> 
<BODY>
<DIV id="wrapper">
  <DIV id="header">
    <DIV class="bg" id="headerbg">
      <DIV class="bgline">
        <DIV class="bglineleft"></DIV>
        <DIV class="bglinecenter"></DIV>
        <DIV class="bglineright"></DIV>
      </DIV>
    </DIV>
  </DIV>
  <div id="lider" style="height: 628px;">
<div style="width: 100px;">
<div class="album-pnl" style="width: 96px; height:590px; visibility: visible;">
<div id="bottomDockPnl">
<div class="album-container" style="width: 90px; height: 590px; padding-top: 15px; padding-bottom: 16px; margin-top: 0px; margin-bottom: 0px; border-top-width: 0px; border-bottom-width: 0px;">
<div class="album-imgs" style="width: 90px;">
<ul class="album-imglist clearfix" style="left: 0px; height: 6000px;" id="Div1_main">
  </ul>
</div>
<span class="slider-btn slider-btn-prev" hidefocus="true" ondragstart="return false;" href="javascript:void(0);"><i></i></span>
<span class="slider-btn slider-btn-next" hidefocus="true" ondragstart="return false;" href="javascript:void(0);"><i></i></span>
</div>
</div>
</div>
</div>
</div>
<div id="main" style="height: 683px;">
<div id="container" style="height: 628px;">
<div class="img-container" id="srcPic" style="height: 628px;">
<div class="bigImg" id="vertical">
<img id="oImg" />
</div>
</div>
<span class="img-prev" style="height: 628px;" hidefocus="true" ondragstart="return false;" target="_blank" href="javascript:void(0);" onclick="turnPre();"><span class="img-switch-btn"></span></span>
<span class="img-next" style="height: 628px;" hidefocus="true" ondragstart="return false;" target="_blank" href="javascript:void(0);" onclick="turnNext();"><span class="img-switch-btn"></span></span>
</div>
</div>
<div id="sider" style="height: 628px;">
<div style="width: 295px;">
<div class="card-box pic-info" id="xianguiprovidercard" style="display: none;"></div>
<div class="card-box pic-info" id="picInfoPnl"><h5 class="modtitle"><span class="pmt-before"></span><em>图片信息</em></h5><div class="card-content"><div class="dutu-info" style="display: block;">
      <h5 id="fileBatchNo"></h5><h5 id="filecount"></h5><h5 id="fileName"></h5>
      </div><div class="src-site-info src-site-info-full">
<p class="site"><span class="pubtime"></span></p>
<div class="revolve_left"></div>
    <a href="javascript:void(0)" class="revol_left_txt" onclick="rotateLeft('oImg',90);">向左旋转</a>
	<a href="javascript:void(0)" class="revol_right_txt" onclick="rotateRight('oImg',90);">向右旋转</a> 
    <div class="revolve_right"></div><p class="more"><span class="m-word"></span><span>&nbsp;</span></p></div></div></div>
<div id="siderCardsAfterWikiInfo"><div class="card-box rsresult-card" style="height: auto; display: block;"><h5 class="card-title"><span class="text">批次审核</span></h5><div class="cnt-wrapper" style="height: auto; position: static;">
  <div class="card-content">
      <label>
        <input type="radio" name="RadioGroup1" value="0|缺失影像，需追加" checked="checked">
        缺失影像，需追加</label>
      <br>
      <center><input type="button" value="添加批次处理" id="addbatch"/></center>
	    <!-- <label>内容输入：</label> 
	    <textarea name="DIVCSS5" cols="30" rows="4"> 
	        www.DIVCSS5.com-网页制作教程 
	    </textarea> -->
      <br>     
    </div></div></div></div>
    <div id="siderCardsAfterWikiInfo"><div class="card-box rsresult-card" style="height: auto; display: block;"><h5 class="card-title"><span class="text">单张影像审核</span></h5><div class="cnt-wrapper" style="height: auto; position: static;">
  <div class="card-content">
      <label>
	        <input type="radio" class="" name="RadioGroup2" value="00|影像与报销单不符" checked="checked">
	        影像与报销单不符</label>
	      <label>
	        <input type="radio" class="" name="RadioGroup2" value="01|影像不清晰">
	        影像不清晰</label>  <br>
	        <label>
	        <input type="radio" class="" name="RadioGroup2" value="02|影像显示不全">
	        影像显示不全</label>  
	        <label>
	        <input type="radio" class="" name="RadioGroup2" value="03|其他原因">
	        其他原因</label>
	         <input type="button" value="添加处理图片" id="addbtn"/>
   	</div></div>
    <h5 class="card-title"><span class="text">备注提交列表</span></h5>
    <div class="cnt-wrapper" style="height: auto; position: static;">
  <div class="card-content">
<form action="" id="remarkForm" >
<%-- action="<%=request.getContextPath()%>/BackBatchOrFileService" method="post"> --%>
   	

    <table cellpadding="0" cellspacing="0" align="center" border="1" width="100%">
  		<thead><tr><th>报销单号</th><th>备注信息</th><th>操作</th></tr></thead>
		<tbody id="batchBody"></tbody>
	</table><p><br/></p>
	<table cellpadding="0" cellspacing="0" align="center" border="1" width="100%">
  		<thead><tr><th>图片序号</th><th>处理选择</th><th>操作</th></tr></thead>
		<tbody id="imgBody"></tbody>
	</table>
	<input id="txtBatchNo" type="hidden" name="txtBatchNo" />
	<input id="imagepath" type="hidden"  value=""/>
	<input type="hidden" id="dealBatchNo" name="dealBatchNo" value="" />
	<input type="hidden" id="dealImgList" name="dealImgList" value="" />
	
	<br/>
	<center>
	<input type="button" id="remarksubmit" value="提交" />&nbsp;&nbsp;
	<input type="button" id="deletesubmit" value="重置" />
	</center>
	</form>
	</div></div>
    </div></div>
</div>
</div></div>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/batchview/rotate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/batchview/scrollscript.js"></script>
</BODY></HTML>
