<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${customerBasic.id}</td>
	</tr>					
	<tr>
		<th width="25%">用户名:</th>
		<td>${customerBasic.username}</td>
	</tr>					
	<tr>
		<th>婚姻状况:</th>
		<td>${customerBasic.maritalStatus.message}</td>
	</tr>					
	<tr>
		<th>子女情况:</th>
		<td>${customerBasic.childrenCount.message}</td>
	</tr>					
	<tr>
		<th>教育背景:</th>
		<td>${customerBasic.educationLevel.message}</td>
	</tr>					
	<tr>
		<th>月收入:</th>
		<td>${customerBasic.incomeMonth.message}</td>
	</tr>					
	<tr>
		<th>社会保险:</th>
		<td>${allSocialInsurances[customerBasic.socialInsurance]}</td>
	</tr>					
	<tr>
		<th>公积金:</th>
		<td>${allHouseFunds[customerBasic.houseFund]}</td>
	</tr>					
	<tr>
		<th>住房情况:</th>
		<td>${customerBasic.houseStatue.message}</td>
	</tr>					
	<tr>
		<th>是否购车:</th>
		<td>${allCarStatuss[customerBasic.carStatus]}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${customerBasic.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>更新时间:</th>
		<td><fmt:formatDate value="${customerBasic.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${customerBasic.comments}</td>
	</tr>					
</table>
</div>
