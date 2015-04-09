<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="cc.icld.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<script language="javascript"
	src="<%=request.getContextPath()%>/jquery.js"></script>
<script type="text/javascript">
	function Initialization() {
		window.open("Randomization.jsp");
	}
	function doRecordNum() {
		var table = $("#table").val();
		var Descartestable = $("#Descartestable").val();
		alert(table);
		$
				.ajax({
					type : "post",//请求方式
					url : "http://localhost:8080/PPDP/ShowRandomization?method=SetRecordNum&table="
							+ table + "&Descartestable=" + Descartestable,//发送请求地址
					//请求成功后的回调函数有两个参数
					datatype : "text",
					success : function(data) {
						alert("设置成功!" + data);
					}
				});
	}
	function doShowInitialDataSet() {
		var table = $("#table").val();
		window.location.href = "http://localhost:8080/ICLD_PrivacyProtec/ShowPartition?method=ShowInitialDataSet&table="
				+ table;
	}
	function SelectAll111() {
		 var checkboxs=document.getElementsByName("attr");
		 for (var i=0;i<checkboxs.length;i++) {
		  var e=checkboxs[i];
		  e.checked=!e.checked;
		 }
		}
	function doTopPartition(){
		var str=document.getElementsByName("attr");
		var objarray=str.length;
		var chestr="";
		for (i=0;i<objarray;i++)
		{
		  if(str[i].checked == true)
		  {
		   chestr+=str[i].value+",";
		  }
		}
		alert(chestr);
		if(chestr==""){
			alert("请选择属性");
		}
		else
		window.open("http://localhost:8080/ICLD_PrivacyProtec/ShowPartition?method=doTopPartition&&chestr="+chestr);
	}
	function doPGRPartition(){
		window.open("http://localhost:8080/ICLD_PrivacyProtec/ShowPartition?method=doPGRPartition");
	}
</script>
<%
	List<ColumnCount> attriList = (List<ColumnCount>) request
			.getAttribute("attriList");
	List<Map<String, Object>> MyDataSet = (List<Map<String, Object>>) request
			.getAttribute("MyDataSet");
%>
<body>
	<form action="ShowPartition?method=ShowInitialDataSet" name="form1"
		id="form1" method="post">
		<table>
			<tr>
				<td>输入要操作的表<input type="text" id="table" name="table"></td>
				<td><input type="submit"></td>
			</tr>
		</table>
	</form>
	<%
		if (attriList != null && attriList.size() > 0) {
	%>
	<table>
		<tr>
			<td>属性名</td>
			<td>不同值个数</td>
		</tr>
		<%
			for (ColumnCount obj : attriList) {
		%>
		<tr>
			<td><input value="<%=obj.getColName()%>" name="attr" type="checkbox" ></td>
			<td><%=obj.getColName()%></td>
			<td><%=obj.getColCount()%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<td><button onclick="javascript:SelectAll111();">全选/反选</button></td>
			<td><button onclick="javascript:doTopPartition();">第一层划分</button></td>
			<td><button onclick="javascript:doPGRPartition();">PGR划分</button></td>
		</tr>
	</table>
	<%
		}
	%>
	<table border="1" cellspacing="0">
		<tr>
			<%
				if (attriList != null && attriList.size() > 0) {
					for (ColumnCount obj : attriList) {
						if (obj.getColName().equals("age")) {
			%>
			<td width="10%" style="background: gray;">age</td>
			<%
				}
						if (obj.getColName().equals("sex")) {
			%>
			<td width="5%" style="background: gray;">sex</td>
			<%
				}
						if (obj.getColName().equals("education")) {
			%>
			<td width="10%" style="background: gray;">education</td>
			<%
				}
						if (obj.getColName().equals("marital_status")) {
			%>
			<td width="10%" style="background: gray;">marital_status</td>
			<%
				}
						if (obj.getColName().equals("workclass")) {
			%>
			<td width="10%" style="background: gray;">workclass</td>
			<%
				}
						if (obj.getColName().equals("relationship")) {
			%>
			<td width="10%" style="background: gray;">relationship</td>
			<%
				}
						if (obj.getColName().equals("race")) {
			%>
			<td width="10%" style="background: gray;">race</td>
			<%
				}
					}
			%>
			<td width="10%" style="background: gray;">occupation</td>
			<%
				}
			%>
		</tr>
		<%
			if (MyDataSet != null && MyDataSet.size() > 0) {
				int count = 0;
				int groupID = 1;
				for (Map<String, Object> temp : MyDataSet) {
					count++;
					String SAvalue = "";
					//System.out.println(temp);
		%>
		<tr>
			<%
				for (ColumnCount obj : attriList) {
			%>
			<td><%=(temp.get(obj.getColName()))%></td>

			<%
				}
			%>
			<td><%=temp.get("occupation")%></td>
		</tr>
		<%
			}
				%>
				<tr>共有<%=count %>个元组</tr>
				<%
			}
		%>

	</table>
</body>
</html>