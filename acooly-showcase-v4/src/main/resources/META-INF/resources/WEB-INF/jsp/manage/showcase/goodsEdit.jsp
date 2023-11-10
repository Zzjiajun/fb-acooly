<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_goods_editform" action="${pageContext.request.contextPath}/manage/showcase//goods/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="goods" scope="request">
		  <input name="id" type="hidden" />
		  <input type="hidden" name="categoryId" value="${categoryId}"/>
		  <input type="hidden" name="categoryName" value="${categoryName}"/>
		  <table class="tableForm" width="100%">
			<tr>
				<th>分类名称：</th>
				<td>${categoryName}</td>
			</tr>
			<tr>
				<th>商品名称：</th>
				<td><input type="text" name="name" size="48" placeholder="请输入商品名称..." class="easyui-validatebox text" data-options="validType:['length[1,32]'],required:true"/></td>
			</tr>					
			<tr>
				<th>商品单位：</th>
				<td><select name="unit" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allUnits}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>商品品牌：</th>
				<td><input type="text" name="brand" size="48" placeholder="请输入商品品牌..." class="easyui-validatebox text" data-options="validType:['length[1,16]']"/></td>
			</tr>					
			<tr>
				<th>商品描述：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入商品描述..." style="width:300px;" name="descn" class="easyui-validatebox" data-options="validType:['length[1,255]']"></textarea></td>
			</tr>					
			<tr>
				<th>商品详情：</th>
				<td><input type="text" name="detailUrl" size="48" placeholder="请输入商品详情..." class="easyui-validatebox text" data-options="validType:['length[1,128]']"/></td>
			</tr>					
			<tr>
				<th>商品价格：</th>
				<td><input type="text" name="price" size="48" placeholder="请输入商品价格..." style="height: 27px;line-height: 27px;" class="easyui-validatebox text" data-options="validType:['length[1,19]']"/></td>
			</tr>					
			<tr>
				<th>商品库存：</th>
				<td><input type="text" name="stock" size="48" placeholder="请输入商品库存..." style="height: 27px;line-height: 27px;" class="easyui-numberbox text" data-options="validType:['length[1,10]']"/></td>
			</tr>					
			<tr>
				<th>提供商：</th>
				<td><input type="text" name="supplier" size="48" placeholder="请输入提供商..." class="easyui-validatebox text" data-options="validType:['length[1,32]']"/></td>
			</tr>
        </table>
      </jodd:form>
    </form>
</div>
