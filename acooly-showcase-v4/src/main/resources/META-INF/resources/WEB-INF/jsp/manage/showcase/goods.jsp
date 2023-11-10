<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<div class="easyui-layout" data-options="fit:true,border:false">

    <!-- 菜单树 -->
    <div data-options="region:'west',border:true,split:true" style="width: 200px;" align="left">
        <div class="easyui-panel" style="padding: 0 5px;">
            <a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="manage_goodsCategory_tree_toggle()">[+/-]</a> <a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="manage_goodsCategory_tree_add()">添加根</a>
        </div>
        <div id="manage_goodsCategory_tree" class="ztree"></div>
    </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">

    <table id="manage_goods_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/showcase/goods/listJson.html" toolbar="#manage_goods_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">编号</th>
			<th field="code">商品编码</th>
			<th field="model">商品型号</th>
			<th field="name">商品名称</th>
			<th field="unit" formatter="mappingFormatter">商品单位</th>
			<th field="brand">商品品牌</th>
			<th field="descn" formatter="contentFormatter">商品描述</th>
			<th field="detailUrl">商品详情</th>
			<th field="price">商品价格</th>
			<th field="stock" >商品库存</th>
			<th field="supplier">提供商</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
			<th field="comments" formatter="contentFormatter">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_goods_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_goods_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/showcase/goods/edit.html',id:'{0}',entity:'goods',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/showcase/goods/deleteJson.html','{0}','manage_goods_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_goods_toolbar">
        商品名称: <input type="text" class="text" size="15" name="search_LIKE_name"/>
        <input id="manage_goods_categoryPath" name="search_RLIKE_categoryName" type="hidden">
        <input id="manage_goods_categoryId" name="search_EQ_categoryId" type="hidden">
        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_goods_toolbar','manage_goods_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
        <a href="#" class="easyui-linkbutton" plain="true" onclick="manage_goods_create()"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
    </div>
  </div>

</div>
<script type="text/javascript">

    /**
     * 商品查询
     */
    function manage_goods_search(goodsCategory) {
        $('#manage_goods_categoryId').val(goodsCategory.id);
        $('#manage_goods_categoryName').text(goodsCategory.name);
        $.acooly.framework.search('manage_goods_toolbar', 'manage_goods_datagrid');
    }

    function getZTree(){
        return $.fn.zTree.getZTreeObj("manage_goodsCategory_tree");
    }


    function manage_goodsCategory_preTreeData(rows){
        for(var i=0;i<rows.length;i++){
            rows[i].icon=null;
            rows[i].iconSkin="fa fa-folder";
            if(rows[i].children != null && rows[i].children.length > 0){
                manage_goodsCategory_preTreeData(rows[i].children);
                rows[i].isParent=true;
            }else{
                rows[i].isParent=false;
            }
        }
    }


    /**
     * 加载和构建分类目录
     * @param defaultNode
     * @param clear
     */
    function manage_goodsCategory_loadTree(defaultNode, clear) {
        $.ajax({
            url : '/manage/showcase/goodsCategory/loadTree.html',
            success : function(data, status) {
                if (typeof (data) == 'string') {
                    data = eval('(' + data + ')');
                }
                if (!data.success) {
                    return;
                }
                var rows = data.rows;
                manage_goodsCategory_preTreeData(rows);
                // for(var i=0;i<rows.length;i++){
                //     rows[i].isParent=true;
                //     rows[i].icon=null;
                //     rows[i].iconSkin="fa fa-folder";
                // }
                // console.info(rows);
                $.fn.zTree.init($("#manage_goodsCategory_tree"), {
                    view : {
                        showLine : true,
                        showIcon : true,
                        showTitle : true,
                        addHoverDom : manage_goodsCategory_tree_addHoverDom,
                        removeHoverDom : manage_goodsCategory_tree_removeHoverDom,
                    },
                    edit : {
                        enable : true,
                        showRemoveBtn : false,
                        showRenameBtn : false
                    },
                    data: {
                        keep: {
                            parent: true
                        }
                    },
                    callback : {
                        onClick : function(event, treeId, treeNode, clickFlag) {
                            manage_goods_search(treeNode)
                        },
                        onDrop : function(event, treeId, treeNodes, targetNode, moveType) {
                            manage_goodsCategory_tree_moveup(treeNodes[0], targetNode, moveType);
                        }
                    }
                }, rows);
                var zTree = getZTree();
                zTree.expandAll(true);
                if (defaultNode)
                    zTree.selectNode(defaultNode, true);
                if (clear){
                    //manage_goodsCategory_form_add();
                }
            }
        });
    }

    /**
     * 鼠标移动到节点动态添加添加和删除链接。
     */
    function manage_goodsCategory_tree_addHoverDom(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");
        // 添加子
        if ($("#manage_goodsCategory_add_Btn_" + treeNode.id).length == 0) {
            var html = "<a id='manage_goodsCategory_add_Btn_" + treeNode.id + "' href='javascript:;' onclick='manage_goodsCategory_tree_add(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;'>添加</a>";
            aObj.append(html)
        }
        // 编辑
        if ($("#manage_goodsCategory_edit_Btn_" + treeNode.id).length == 0) {
            var html = "<a id='manage_goodsCategory_edit_Btn_" + treeNode.id + "' href='javascript:;' onclick='manage_goodsCategory_tree_edit(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;'>修改</a>";
            aObj.append(html)
        }
        // 删除
        if (!treeNode.children || treeNode.children.length == 0) {
            if ($("#manage_goodsCategory_delete_Btn_" + treeNode.id).length > 0)
                return;
            var html = "<a id='manage_goodsCategory_delete_Btn_" + treeNode.id + "' href='javascript:;' onclick='manage_goodsCategory_tree_delete(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;'>删除</a>";
            aObj.append(html)
        }
    }

    /**
     * 鼠标移出节点处理
     */
    function manage_goodsCategory_tree_removeHoverDom(treeId, treeNode) {
        if ($("#manage_goodsCategory_add_Btn_" + treeNode.id).length > 0)
            $("#manage_goodsCategory_add_Btn_" + treeNode.id).unbind().remove();
        if ($("#manage_goodsCategory_edit_Btn_" + treeNode.id).length > 0)
            $("#manage_goodsCategory_edit_Btn_" + treeNode.id).unbind().remove();
        if ($("#manage_goodsCategory_delete_Btn_" + treeNode.id).length > 0)
            $("#manage_goodsCategory_delete_Btn_" + treeNode.id).unbind().remove();
    }

    /**
     * 添加子节点
     * @param parentId
     */
    function manage_goodsCategory_tree_add(parentId) {
        var url = '/manage/showcase/goodsCategory/create.html';
        if (parentId) {
            url += '?parentId=' + parentId;
        }
        $.acooly.framework.create({
            url : url,
            entity : 'goodsCategory',
            width : 500,
            height : 200,
            onSuccess : function(result) {
                var zTree = $.fn.zTree.getZTreeObj("manage_goodsCategory_tree");
                var row = result.entity;
                row.icon=null;
                row.iconSkin="fa fa-folder";
                var newNode;
                if (parentId) {
                    var parentNode = zTree.getNodeByParam("id", parentId, null);
                    newNode = zTree.addNodes(parentNode, row, false);
                } else {
                    newNode = zTree.addNodes(null, row, false);
                }
                zTree.selectNode(newNode);
            }
        });
    }

    /**
     * 修改节点
     * @param id
     */
    function manage_goodsCategory_tree_edit(id) {
        $.acooly.framework.edit({
            url : '/manage/showcase/goodsCategory/edit.html',
            id : id,
            entity : 'goodsCategory',
            width : 500,
            height : 200,
            reload : false,
            onSuccess : function(result) {
                console.info(result);
                var zTree = $.fn.zTree.getZTreeObj("manage_goodsCategory_tree");
                var node = zTree.getNodeByParam("id", id, null);
                node.name = result.entity.name;
                zTree.updateNode(node, true);
            }
        })
    }


    /**
     * 删除子节点
     * @param id
     */
    function manage_goodsCategory_tree_delete(id) {
        $.messager.confirm("确认", "你确认删除该类型？	", function(r) {
            if (!r)
                return;
            $.ajax({
                url : '/manage/showcase/goodsCategory/deleteJson.html?id=' + id,
                success : function(data, status) {
                    if (data.success) {
                        var zTree = $.fn.zTree.getZTreeObj("manage_goodsCategory_tree");
                        var node = zTree.getNodeByParam("id", id, null);
                        zTree.removeNode(node);
                    }
                    if (data.message)
                        $.messager.show({
                            title : '提示',
                            msg : data.message
                        });
                }
            });
        })
    }

    //移动
    function manage_goodsCategory_tree_moveup(sourceNode, targetNode, moveType) {
        if (!moveType || moveType == null)
            return;
        $.ajax({
            url : '/manage/showcase/goodsCategory/move.html',
            data : {
                sourceId : sourceNode.id,
                targetId : targetNode.id,
                moveType : moveType
            },
            success : function(data, status) {
                if (data.success) {
                    console.info("save move success!");
                }
            }
        });
    }

    //展开/收缩
    var manage_goodsCategory_tree_expand_state = true;
    function manage_goodsCategory_tree_toggle() {
        getZTree().expandAll(!manage_goodsCategory_tree_expand_state);
        manage_goodsCategory_tree_expand_state = !manage_goodsCategory_tree_expand_state;
    }

    function manage_goods_create() {
        var typeId = $('#manage_goods_categoryId').val();
        if (!typeId) {
            alert("请先选择分类");
            return;
        }
        $.acooly.framework.create({
            url : '/manage/showcase/goods/create.html?categoryId=' + typeId,
            entity : 'goods',
            width : 600,
            height : 500,
            maximizable : false
        });
    }

    $(function() {
        manage_goodsCategory_loadTree();
        $.acooly.framework.registerKeydown('manage_goods_toolbar','manage_goods_datagrid');
    });

</script>
