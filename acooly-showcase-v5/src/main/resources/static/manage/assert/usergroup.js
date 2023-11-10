/**
 * UserGroup动态表单Demo
 * @type {{_onSubmit: (function(): boolean), load: acooly_showcase_usergroup.load, rowDeleteAll: acooly_showcase_usergroup.rowDeleteAll, rowDelete: acooly_showcase_usergroup.rowDelete, rowAdd: acooly_showcase_usergroup.rowAdd, createBatch: acooly_showcase_usergroup.createBatch}}
 */
var acooly_showcase_usergroup = {

    /**
     * 批量创建
     */
    createBatch: function () {
        var that = this;
        $.acooly.framework.create({
            url: '/manage/demo/usergroup/userGroup/batchCreate.html',
            entity: 'userGroup',
            width: 800, height: 600,
            reload: true,
            title: '批量添加',
            addButton: "批量保存",
            onSubmit: that._onSubmit
        });
    },

    /**
     * 编辑时，加载已经保存的数据
     */
    load: function () {
        var that = this;
        $.ajax({
            url: '/manage/demo/usergroup/userGroup/queryJson.html',
            data: {sort: 'id', order: 'asc'},
            success: function (result) {
                let usergroups = result.rows;
                $.each(usergroups, function (i, e) {
                    that.rowAdd(e);
                });
            }
        });
    },

    /**
     * 添加行
     * @param entity 编辑时传入的已保存实体
     */
    rowAdd: function (entity) {
        // 获取新增行的序号
        let dataIndex = 0;
        $('.usergroup-row').each(function (index, e) {
            let tmp = parseInt($(e).attr('data-index'));
            if (tmp > dataIndex) dataIndex = tmp;
        });
        let index = dataIndex + 1;
        let data = {'i': index};

        // 渲染
        $.acooly.template.renderTo("manage_userGroup_row_container", "manage_userGroup_row_template", data, {append: true});

        var rowContainerId = 'manage_userGroup_row' + index;
        // select2渲染（下拉选项）
        $("#" + rowContainerId).find('.select2bs4').select2({theme: 'bootstrap4'});
        // input-mask and easyui 初始化渲染
        $("#" + rowContainerId).find('[data-mask]').inputmask();
        $.parser.parse("#" + rowContainerId);

        // 如果是编辑，设置初始值
        if (entity) {
            $.acooly.formVal(rowContainerId, "id_" + index, entity.id);
            $.acooly.formVal(rowContainerId, "name_" + index, entity.name);
            $.acooly.formVal(rowContainerId, "users_" + index, entity.users);
        }
        $("#" + rowContainerId).form('validate');
    },

    /**
     * 删除行
     * @param index
     */
    rowDelete: function (index) {
        $.messager.confirm("批量处理", "您确定要当前记录？清除后需点击保存才能生效。", function (r) {
            if (r) {
                $('#manage_userGroup_row' + index).remove();
            }
        });
    },

    /**
     * 删除所有
     */
    rowDeleteAll: function () {
        $.messager.confirm("批量处理", "您确定要清除所有记录？清除后需点击保存才能生效。", function (r) {
            if (r) {
                $('.usergroup-row').remove();
            }
        });
    },

    /**
     * 提交批量表单前的自定义处理
     * @returns {boolean}
     * @private
     */
    _onSubmit: function () {
        return true;
    }


};

/***
 * JQuery静态类前缀处理
 */
(function ($) {
    if (!$.acooly) {
        $.acooly = {};
    }
    if (!$.acooly.showcase) {
        $.extend($.acooly, {showcase: {}});
    }

    $.extend($.acooly.showcase, {usergroup: acooly_showcase_usergroup});

})(jQuery);