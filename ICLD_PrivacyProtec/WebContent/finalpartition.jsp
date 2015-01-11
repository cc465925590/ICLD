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
	function doFinalPartition() {
		window
				.open("http://localhost:8080/ICLD_PrivacyProtec/ShowPartition?method=doFinalPartition");
	}
</script>
<%
	String[] FinalNSAs = (String[]) request.getAttribute("FinalNSAs");
	List<LinkedList<Map<String, Object>>> FinalBlockList = (List<LinkedList<Map<String, Object>>>) request
			.getAttribute("FinalBlockList");
	List<Set<String>> SASetList = (List<Set<String>>) request
			.getAttribute("SASetList");
	float GCPValue = (Float) request.getAttribute("GCPValue");
	float GLPValue = (Float) request.getAttribute("GLPValue");
	Long runningTime = (Long) request.getAttribute("runningTime");
%>
<body>
	<div>
		GCP=<%=GCPValue%><br> GLP=<%=GLPValue%><br>
	</div>
	<table border="1" cellspacing="0">
		<tr>
			<%
				if (FinalNSAs != null && FinalNSAs.length > 0) {
					for (String obj : FinalNSAs) {
						if (obj.equals("age")) {
			%>
			<td width="10%" style="background: gray;">age</td>
			<%
				}
						if (obj.equals("sex")) {
			%>
			<td width="5%" style="background: gray;">sex</td>
			<%
				}
						if (obj.equals("education")) {
			%>
			<td width="10%" style="background: gray;">education</td>
			<%
				}
						if (obj.equals("marital_status")) {
			%>
			<td width="10%" style="background: gray;">marital_status</td>
			<%
				}
						if (obj.equals("workclass")) {
			%>
			<td width="10%" style="background: gray;">workclass</td>
			<%
				}
						if (obj.equals("relationship")) {
			%>
			<td width="10%" style="background: gray;">relationship</td>
			<%
				}
						if (obj.equals("race")) {
			%>
			<td width="10%" style="background: gray;">race</td>
			<%
				}
					}
			%>
			<td width="10%" style="background: gray;">occupation</td>
			<td width="10%" style="background: gray;">ID</td>
			<td width="10%" style="background: gray;">SASet</td>
			<td width="10%" style="background: gray;">序号</td>
			<%
				}
			%>
		</tr>
		<%
			int columnnum = 1;
			if (FinalBlockList != null && FinalBlockList.size() > 0) {
				int groupID = 0;
				for (LinkedList<Map<String, Object>> block : FinalBlockList) {
					int blocknum = 0;
					for (Map<String, Object> temp : block) {
		%>
		<tr>
			<%
				for (String obj : FinalNSAs) {
			%>
			<td><%=(temp.get(obj))%></td>

			<%
				}
			%>
			<td><%=temp.get("occupation")%></td>
			<td><%=temp.get("ID")%></td>
			<td><%=temp.get("SASet")%></td>
			<td><%=columnnum%></td>
		</tr>
		<%
			columnnum++;
						blocknum++;
					}
		%>
		<tr>
			<td>----</td>
			<td>----</td>
			<td>----</td>
			<td>----</td>
			<td>----</td>
			<td>----</td>
			<td>----</td>
			<td>扰动的SA集合:<%=SASetList.get(groupID)%></td>
			<td>----</td>
			<td>----</td>
			<td>块的记录数:<%=blocknum%></td>
		</tr>
		<%
			groupID++;
				}
			}
		%>

	</table>
</body>
</html>