<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <base href="<%=basePath%>">
    <title>演示bs_typeahead插件</title>
    <script type="text/javascript">
        var name2id=[];

        $(function () {
             $("#customerName").typeahead({
                 // source:["字节跳动","meiming","节节高","百度"]
                 //query查询的字符串
                 source:function (query,process) {
                   $.ajax({
                       url:'workbench/transaction/typeahead.do',
                       data:{
                           customerName:query
                       },
                       type:'post',
                       dataType:'json',
                       success:function(data){  //data->customerList json对象
                           //alert(data.length);
                           var customerNameArr=[];
                           $.each(data,function(index,obj){
                              // alert(obj.name);
                               customerNameArr.push(obj.name);
                               name2id[obj.name]=obj.id;
                           })
                           //alert(customerNameArr);
                           process(customerNameArr); //process()将字符串数组交给source属性
                       }
                   })

                 },

                 afterSelect:function (item) {
                    alert(name2id[item])
                 }
             })
        });
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
