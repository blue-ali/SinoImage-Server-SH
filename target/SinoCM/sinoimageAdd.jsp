<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
 <HEAD>
  <TITLE> New Document </TITLE>
  <META NAME="Generator" CONTENT="EditPlus">
  <META NAME="Author" CONTENT="">
  <META NAME="Keywords" CONTENT="">
  <META NAME="Description" CONTENT="">

 

 </HEAD>

 <BODY>
  
   <script type="text/javascript">  


	function CheckBrowser()
	{
		var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
		var isOpera = userAgent.indexOf("Opera") > -1;
		if (isOpera) 
		{
			return "Opera" 
		}; //判断是否Opera浏览器
		if (userAgent.indexOf("Firefox") > -1) 
		{
			return "FF"; 
		} //判断是否Firefox浏览器
		if (userAgent.indexOf("Chrome") > -1)
		{
			return "Chrome"; 
		} //判断是否Chrome浏览器
		if (userAgent.indexOf("Safari") > -1)
		{
			return "Safari"; 
		} //判断是否Safari浏览器
		if (userAgent.indexOf("MSIE") > -1 && !isOpera) 
		{
			return "IE"; 
		};    //判断是否IE浏览器
	}

	//以下是调用上面的函数
	function DynCreateControl()
	{


		
		if (CheckBrowser() == "FF" || CheckBrowser() == "Chrome")
		{
			var dwf = document.createElement("object");
			dwf.setAttribute("id","UCBench");
			dwf.setAttribute("TYPE","application/x-itst-activex");
			dwf.setAttribute("progid","Debug/LogsSetup.cab#version=2,1,2,4");   //这个修改，实现版本发布、升级
			dwf.setAttribute("width","100%");
			dwf.setAttribute("height","100%"); 
			dwf.setAttribute("clsid","{17E03D90-5299-474b-A5F0-9CCC0BB094F3}"); 
			dwf.setAttribute("ALIGN","baseline"); 
			dwf.setAttribute("BORDER","0");
			dwf.setAttribute("param_url",window.location.href);
			document.body.appendChild(dwf); 
			return;

		}
		
		if (CheckBrowser() == "Opera")
		{
			alert("目前不支持此浏览器，请选择IE/Firefox/Chrome");
			return;
		}
		if (CheckBrowser() == "Safari") 
		{
			alert("目前不支持此浏览器，请选择IE/Firefox/Chrome");
			return;
		}

//		if (CheckBrowser() == "IE") 有些时候ie自己也识别不出自己是ie。。。shit
		{

			var dwf = document.createElement('object');
			dwf.setAttribute("id","UCBench");
			dwf.setAttribute("classid","CLSID:17E03D90-5299-474b-A5F0-9CCC0BB094F3");
			dwf.setAttribute("codeBase","Debug/LogsSetup.cab#version=1,0,9");   //这个修改，实现版本发布、升级
			dwf.setAttribute("width","100%");
			dwf.setAttribute("height","100%");
			document.body.appendChild(dwf);

			
		}

	}

	DynCreateControl();

	url = window.location.href;
//	url = url + "&ocxversion=1,0,7"

	var UCBenchPlugin = window.document.getElementById("UCBench");
	UCBenchPlugin.url = url
		
	window.onbeforeunload = function() {
    
    UCBenchPlugin.removeFile();
};
</script>

 
 
 </BODY>
</HTML>
