<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>


<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_customerBasic_searchform','manage_customerBasic_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;height: 40%;" align="left">
    <form id="manage_customerBasic_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					用户名: <input type="text" class="text" size="15" name="search_LIKE_username"/>
				婚姻状况: <select style="width:80px;height:27px;" name="search_EQ_maritalStatus" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allMaritalStatuss}"><option value="${e.key}" ${param.search_EQ_maritalStatus == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				子女情况: <select style="width:80px;height:27px;" name="search_EQ_childrenCount" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allChildrenCounts}"><option value="${e.key}" ${param.search_EQ_childrenCount == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				教育背景: <select style="width:80px;height:27px;" name="search_EQ_educationLevel" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allEducationLevels}"><option value="${e.key}" ${param.search_EQ_educationLevel == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				月收入: <select style="width:80px;height:27px;" name="search_EQ_incomeMonth" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allIncomeMonths}"><option value="${e.key}" ${param.search_EQ_incomeMonth == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				社会保险: <select style="width:80px;height:27px;" name="search_EQ_socialInsurance" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allSocialInsurances}"><option value="${e.key}" ${param.search_EQ_socialInsurance == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				公积金: <select style="width:80px;height:27px;" name="search_EQ_houseFund" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allHouseFunds}"><option value="${e.key}" ${param.search_EQ_houseFund == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				住房情况: <select style="width:80px;height:27px;" name="search_EQ_houseStatue" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allHouseStatues}"><option value="${e.key}" ${param.search_EQ_houseStatue == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				是否购车: <select style="width:80px;height:27px;" name="search_EQ_carStatus" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allCarStatuss}"><option value="${e.key}" ${param.search_EQ_carStatus == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
					更新时间: <input size="15" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_customerBasic_searchform','manage_customerBasic_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a> 
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_customerBasic_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/showcase/customerBasic/listJson.html" toolbar="#manage_customerBasic_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="username">用户名</th>
			<th field="maritalStatus" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allMaritalStatuss',value);} ">婚姻状况</th>
			<th field="childrenCount" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allChildrenCounts',value);} ">子女情况</th>
			<th field="educationLevel" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allEducationLevels',value);} ">教育背景</th>
			<th field="incomeMonth" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allIncomeMonths',value);} ">月收入</th>
			<th field="socialInsurance" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allSocialInsurances',value);} ">社会保险</th>
			<th field="houseFund" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allHouseFunds',value);} ">公积金</th>
			<th field="houseStatue" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allHouseStatues',value);} ">住房情况</th>
			<th field="carStatus" data-options="formatter:function(value){ return formatRefrence('manage_customerBasic_datagrid','allCarStatuss',value);} ">是否购车</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">更新时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_customerBasic_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_customerBasic_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/showcase/customerBasic/edit.html',id:'{0}',entity:'customerBasic',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/showcase/customerBasic/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/showcase/customerBasic/deleteJson.html','{0}','manage_customerBasic_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_customerBasic_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/customerBasic/create.html',entity:'customerBasic',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a> 
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/customerBasic/deleteJson.html','manage_customerBasic_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_customerBasic_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_customerBasic_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/showcase/customerBasic/exportXls.html','manage_customerBasic_searchform','客户基本信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>  
        <div onclick="$.acooly.framework.exports('/manage/showcase/customerBasic/exportCsv.html','manage_customerBasic_searchform','客户基本信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div> 
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/customerBasic/importView.html',uploader:'manage_customerBasic_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a> 
    </div>
  </div>

</div>
