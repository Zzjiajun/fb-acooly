<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_goodsCategory_editform" action="${pageContext.request.contextPath}/manage/showcase/goodsCategory/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="goodsCategory" scope="request">
        <input name="id" type="hidden" />
		<input name="parentId" type="hidden" value="${parentId}"/>
        <table class="tableForm" width="100%">
			<tr>
				<th>类型名称：</th>
				<td><input type="text" name="name" size="48" class="easyui-validatebox text" data-options="required:true" validType="length[1,32]"/></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="comments" class="easyui-validatebox"  validType="length[1,255]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
