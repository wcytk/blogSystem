function del(){
	var check = document.getElementsByName("check");
    var len=check.length;
    var idAll="";
    for(var i=0;i<len;i++){
    	if(check[i].checked){
    		idAll+=check[i].value+",";
    	}
    }
     window.location.href="del.do?idAll="+idAll;

}