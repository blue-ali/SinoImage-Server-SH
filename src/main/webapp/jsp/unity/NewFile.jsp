<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调阅页面</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/batchview/jquery-1.7.2.js"></script>
<LINK href="<%=request.getContextPath()%>/css/batchview/detailbase.css" rel="stylesheet" type="text/css">
<LINK href="<%=request.getContextPath()%>/css/batchview/fmdetail.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	$.ajaxSetup({
		async : false
	});
	$(function(){
		var txtBatchNo = '<%=request.getParameter("barno")%>';
		alert(txtBatchNo);
		loadFirst();
		function loadFirst(){
			$.getJSON('http://127.0.0.1:9080/TigEra.Document.Server/serverForBrowserView?batchNo=' + txtBatchNo 
					 + '&rdm=' + Math.random(),
				function(data){
					alert(data);
					console.log(data);
					var imagesinfo = new Array("");
				 	var imagesinfo2 = new Array("");
				 	var imagesinfo3 = new Array("");
				 	//var filecount = data.rows.length;
				 	//alert(filecount);
				}
			);
		}
	});


</script>
</head>
<body>
<h2>Hello World!</h2>
</body>
</html>