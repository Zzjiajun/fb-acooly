<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_customerBasic_editform" action="${pageContext.request.contextPath}/manage/showcase/customerBasic/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="customerBasic" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">用户名：</th>
				<td>
				<c:if test="${action == 'create'}">
					${param.username}<input type="hidden" name="username" value="${param.username}"/>
				</c:if>
				<c:if test="${action == 'edit'}">
					${customerBasic.username}<input type="hidden" name="username""/>
				</c:if>				
				</td>
			</tr>					
			<tr>
				<th>婚姻状况：</th>
				<td><select name="maritalStatus" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allMaritalStatuss}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>子女情况：</th>
				<td><input ${action=='create'?'value="0"':''} type="text" placeholder="请输入子女数，如果没有则填写0" name="childrenCount" size="32" style="height: 27px;line-height: 27px;" class="easyui-numberbox text"
						   data-options="validType:['number','length[1,2]']"/>
				</td>
			</tr>					
			<tr>
				<th>教育背景：</th>
				<td><select name="educationLevel" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allEducationLevels}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>月收入：</th>
				<td><select name="incomeMonth" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allIncomeMonths}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>社会保险：</th>
				<td><select name="socialInsurance" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allSocialInsurances}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>公积金：</th>
				<td><select name="houseFund" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allHouseFunds}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>住房情况：</th>
				<td><select name="houseStatue" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allHouseStatues}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>是否购车：</th>
				<td><select name="carStatus" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allCarStatuss}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="comments" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
