<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_shopSubCategories_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">是否启用：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_isActive"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">是否显示：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_isShow"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createdAt" name="search_GTE_createdAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createdAt" name="search_LTE_createdAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <label class="col-form-label">更新时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updatedAt" name="search_GTE_updatedAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updatedAt" name="search_LTE_updatedAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_shopSubCategories_searchform','manage_shopSubCategories_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_shopSubCategories_datagrid" class="easyui-datagrid" url="/manage/shop/shopSubCategories/listJson.html" toolbar="#manage_shopSubCategories_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >子级分类ID</th>
                <th field="parentId" sortable="true" sum="true">父级分类ID</th>
                <th field="name" formatter="contentFormatter">子级分类名称</th>
                <th field="slug" formatter="contentFormatter">子级分类标识</th>
                <th field="description" formatter="contentFormatter">子级分类描述</th>
<#--                <th field="sortOrder" sortable="true" sum="true">排序顺序</th>-->
                <th field="isActive" sortable="true" sum="true"  formatter="activeCategoryFormatter">是否启用</th>
                <th field="isShow" sortable="true" sum="true"  formatter="showCategoryFormatter">是否显示</th>
<#--                <th field="icon" formatter="contentFormatter">分类图标URL</th>-->
<#--                <th field="image" formatter="contentFormatter">分类图片URL</th>-->
<#--                <th field="seoTitle" formatter="contentFormatter">SEO标题</th>-->
<#--                <th field="seoKeywords" formatter="contentFormatter">SEO关键词</th>-->
<#--                <th field="seoDescription" formatter="contentFormatter">SEO描述</th>-->
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_shopSubCategories_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopSubCategories_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/shop/shopSubCategories/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopSubCategories/edit.html',id:'{0}',entity:'shopSubCategories',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopSubCategories/deleteJson.html','{0}','manage_shopSubCategories_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopSubCategories_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopSubCategories/create.html',entity:'shopSubCategories',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopSubCategories/deleteJson.html','manage_shopSubCategories_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopSubCategories_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopSubCategories_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopSubCategories/exportXls.html','manage_shopSubCategories_searchform','子级分类表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopSubCategories/exportCsv.html','manage_shopSubCategories_searchform','子级分类表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopSubCategories/importView.html',uploader:'manage_shopSubCategories_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopSubCategories_searchform', 'manage_shopSubCategories_datagrid');
        });


        function activeCategoryFormatter(value) {
            if (value == 1 || value == '1') {
                return '<span class="badge badge-featured"><i class="fa fa-star fa-fw"></i>启用</span>';
            } else {
                return '<span class="badge badge-normal">禁止</span>';
            }
        }

        function showCategoryFormatter(value) {
            if (value == 1 || value == '1') {
                return '<span class="badge badge-featured"><i class="fa fa-star fa-fw"></i>显示</span>';
            } else {
                return '<span class="badge badge-normal">隐藏</span>';
            }
        }
    </script>
</div>
