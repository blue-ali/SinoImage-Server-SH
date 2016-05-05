$(function(){
		
	var 
		index = 0 ;
		Sheigh = 560 ;
		timer = null ;
		imgTotal = $(".album-imglist li").length ; 
		len = Math.ceil(imgTotal / 7);
		 
	function NextPage()
	{
		if(index>(len-1))
		{
			index = 0 ;
		}
		$(".album-imgs").stop(true, false).animate({top: -index*Sheigh+"px"},621)		
	}
	
	function PrevPage()
	{
		if(index<0)
		{
			index = len-1 ;
		}
		$(".album-imgs").stop(true, false).animate({top: -index*Sheigh+"px"},621)		
	}		
			
	//下一页
	$(".slider-btn-next").click(function(){
		 index++ ;
		 NextPage();
	});
	//上一页
	$(".slider-btn-prev").click(function(){
		 index-- ;
		 PrevPage();
	});
			
})
