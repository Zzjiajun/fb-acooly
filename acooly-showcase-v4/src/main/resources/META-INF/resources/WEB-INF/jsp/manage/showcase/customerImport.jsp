<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	$.acooly.framework.createUploadify({
        /** 上传导入的URL */
        url:'/manage/showcase/customer/importJson.html?_csrf=${requestScope["org.springframework.security.web.csrf.CsrfToken"].token}&importIgnoreTitle=true&splitKey=v',
        /** 导入操作消息容器 */
        messager:'manage_customer_import_uploader_message',
        /** 上传导入文件表单ID */
        uploader:'manage_customer_import_uploader_file',
        jsessionid:'<%=request.getSession().getId()%>'
	});
});
</script>
<div align="center">
<table class="tableForm" width="100%">
  <tr>
    <td colspan="2">
    根据文件扩展名自动适配导入文件类型，目前支持的格式包括：Excel(*.xls)和CSV(*.csv)。 请：<a href="/showcase/import_template_customer.xlsx" target="_blank">模板下载</a>。
    </td>
  </tr>
  <tr>
    <td colspan="2" height="25"><div id="manage_customer_import_uploader_message" style="color: red;text-align: center;"></div></td>
  </tr>
  <tr>
    <th width="30%">选择文件：</th>
    <td>
    <input type="file" name="manage_customer_import_uploader_file" id="manage_customer_import_uploader_file" />
    </td>
  </tr>
</table>
</div>
