let showcase_demo_roweditor = {

    mapping: null,

    editIndex: undefined,

    initMapping: function () {
        $.ajax({
            url: 'http://127.0.0.1:8083/manage/demo/roweditor/roweditor/mapping.html',
            async: false,
            success: function (result) {
                $.showcase.roweditor.mapping = result;
                console.info("initMapping:", $.showcase.roweditor.mapping);
            }
        });
    },


    /**console
     * 是否正在编辑
     * 验证和提交编辑
     * @returns {boolean}
     */
    endEditing: function () {
        if ($.showcase.roweditor.editIndex == undefined) {
            return true
        }
        if ($('#manage_roweditor_datagrid').datagrid('validateRow', $.showcase.roweditor.editIndex)) {
            $('#manage_roweditor_datagrid').datagrid('endEdit', $.showcase.roweditor.editIndex);
            // $.showcase.roweditor.editIndex = undefined;
            return true;
        } else {
            return false;
        }
    },

    editing: function () {
        return $.showcase.roweditor.editIndex != undefined;
    },

    insertFirst: function () {
        if (this.endEditing()) {
            $('#manage_roweditor_datagrid').datagrid('insertRow', {index: 0, row: {animal: 'Snake', status: 'enable'}});
            $.showcase.roweditor.editIndex = 0;
            // $.showcase.roweditor.editIndex = $('#manage_roweditor_datagrid').datagrid('getRows').length - 1;
            $('#manage_roweditor_datagrid').datagrid('selectRow', $.showcase.roweditor.editIndex)
                .datagrid('beginEdit', $.showcase.roweditor.editIndex);
        }
    },
    accept: function () {
        if (!$.showcase.roweditor.endEditing()) {
            return;
        }
        $('#manage_roweditor_datagrid').datagrid('acceptChanges');
        let rows = $('#manage_roweditor_datagrid').datagrid('getRows');
        let row = rows[$.showcase.roweditor.editIndex];
        $.showcase.roweditor.editIndex = undefined;
        $.ajax({
            url: '/manage/demo/roweditor/roweditor/' + (row.id ? 'updateJson' : 'saveJson') + '.html',
            method: 'POST',
            data: row,
            success: function (result) {
                if (result.success) {
                    $.acooly.messager("保存", "行操作保存成功", "success");
                } else {
                    $.acooly.messager("保存", result.message, "warning");
                }
            }
        });
    },

    reject: function () {
        $('#manage_roweditor_datagrid').datagrid('rejectChanges');
        $.showcase.roweditor.editIndex = undefined;
    },

    remove: function () {
        if ($.showcase.roweditor.editIndex == undefined) {
            return
        }
        var rows = $.acooly.framework._getCheckedRows(datagrid);
        $('#manage_roweditor_datagrid').datagrid('cancelEdit', $.showcase.roweditor.editIndex)
            .datagrid('deleteRow', $.showcase.roweditor.editIndex);
        $.showcase.roweditor.editIndex = undefined;
    },

    onClickCell: function (index, field) {
        if (index == $.showcase.roweditor.editIndex) {
            return;
        }
        if ($.showcase.roweditor.endEditing()) {
            $('#manage_roweditor_datagrid').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            var ed = $('#manage_roweditor_datagrid').datagrid('getEditor', {index: index, field: field});
            if (ed) {
                ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            }
            $.showcase.roweditor.editIndex = index;
        } else {
            setTimeout(function () {
                $('#manage_roweditor_datagrid').datagrid('selectRow', $.showcase.roweditor.editIndex);
            }, 0);
        }
    },

    onEndEdit: function (index, row) {
        // var ed = $(this).datagrid('getEditor', {
        //     index: index,
        //     field: 'registryChannel'
        // });
        // row.registryChannel = $(ed.target).combobox('getValue');
    },


    cellVal: function (name, val) {
        if (!$.showcase.roweditor.editing()) {
            return;
        }
        let index = $.showcase.roweditor.editIndex;
        let ed = $('#manage_roweditor_datagrid').datagrid('getEditor', {index: index, field: name});
        let classes = $(ed.target).attr('class');
        if (classes.indexOf('select2bs4') != -1) {
            // select2
            $(ed.target).val(val).trigger("change");
        } else if (classes.indexOf('combobox') != -1) {
            $(ed.target).combobox('setValue', val);
        } else if (classes.indexOf('datebox-f') != -1) {
            $(ed.target).datebox('setValue', val);
        } else {
            $(ed.target).val(val);
        }
    },

    getChanges: function () {
        var rows = $('#manage_roweditor_datagrid').datagrid('getChanges');
        console.info("changes:", rows);
    },

    parseIdCard: function (obj) {
        let idCard = $(obj).val();
        if (!obj || !idCard) {
            return;
        }
        if (!$(obj).validatebox('isValid')) {
            return;
        }
        $.ajax({
            url: '/manage/showcase/member/showcaseMember/parseIdCard.html',
            data: {idcard: idCard},
            success: function (result) {
                console.info("idcardParse:", result);
                if (result.success) {
                    let birthday = result.entity.birthday;
                    birthday = birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6, 8);
                    let age = new Date().getFullYear() - parseInt(birthday.substring(0, 4));
                    let gender = result.entity.gender;
                    let animal = result.entity.animalSign;
                    $.showcase.roweditor.cellVal('birthday', birthday);
                    $.showcase.roweditor.cellVal('age', age);
                    $.showcase.roweditor.cellVal('gender', gender);
                    $.showcase.roweditor.cellVal('animal', animal);

                    // 测试：动态更新下拉列表的可选项（registryChannel）
                    let index = $.showcase.roweditor.editIndex;
                    let ed = $('#manage_roweditor_datagrid').datagrid('getEditor', {index: index, field: 'registryChannel'});
                    // 清空原有选项
                    $(ed.target).empty();
                    // 动态添加新的选项，可以通过数组或对象，循环添加
                    var data = $.showcase.roweditor.mapping['allRegistryChannels'];
                    var i = 0;
                    for (let key in data) {
                        if(i++ < 4){
                            $(ed.target).append(new Option(data[key], key, true, true)).trigger('change');
                        }
                    }
                    // $(ed.target).append(new Option("中国", "1111", true, true)).trigger('change');
                    // $(ed.target).append(new Option("日本", "2222", true, true)).trigger('change');
                    // 如有需要，动态设置选择值
                    $(ed.target).val('WEB').trigger('change');
                }
            },
            error: function (r, e) {
                $.messager.show({title: '提示', msg: e});
            }
        });

    }

};

/***
 * JQuery静态类前缀处理
 */
(function ($) {
    if (!$.showcase) {
        $.showcase = {};
    }
    if (!$.showcase.roweditor) {
        $.extend($.showcase, {roweditor: showcase_demo_roweditor});
    }
})(jQuery);


/**
 * 全局注册datagrid的自定义行编辑表单
 */
$.extend($.fn.datagrid.defaults.editors, {
    acoolyText: {
        init: function (container, options) {
            let content = "<div><input type=\"text\" class=\"easyui-validatebox form-control form-control-sm\"";
            if (options.placeholder) {
                content += " placeholder=\"" + options.placeholder + "\"";
            }
            if (options.validType) {
                content += " data-options=\"validType:" + options.validType + "\"";
            }
            if (options.inputmask) {
                content += " data-inputmask=\"" + options.inputmask + "\" data-mask";
            }
            if (options.required) {
                content += " required=\"true\"";
            }
            content += "/></div>"
            let html = $(content).appendTo(container);
            let input = $(html).children()[0];
            $(input).inputmask();
            if (options.events) {
                for (let key in options.events) {
                    $(input).on(key, function () {
                        options.events[key].call(this, $(input));
                    });
                    console.info("registry event: ", key)
                }
            }
            $.parser.parse(html);
            return input;
        },
        destroy: function (target) {
            $(target).remove();
        },
        getValue: function (target) {
            return $(target).val();
        },
        setValue: function (target, value) {
            $(target).val(value);
        },
        resize: function (target, width) {
            $(target)._outerWidth(width);
        }
    },
    select2: {
        init: function (container, options) {
            console.info("select2 init:", options);
            if (options.data) {
                let html = "<select class=\"form-control input-sm select2bs4\">";
                for (let key in options.data) {
                    html += "<option value='" + key + "'>" + options.data[key] + "</option>";
                }
                html += "</select>";
                let select = $(html).appendTo(container);
                $(select).select2({theme: 'bootstrap4'});
                return select;
            } else {
                $.ajax({
                    url: options.url,
                    method: 'POST',
                    async: false,
                    success: function (data) {
                        let html = "<select class=\"form-control input-sm select2bs4\">";
                        $.each(data, function (index, obj) {
                            html += "<option value='" + obj[options.valueField] + "'>" + obj[options.textField] + "</option>";
                        });
                        html += "</select>";
                        let select = $(html).appendTo(container);
                        $(select).select2({theme: 'bootstrap4'});
                        return select;
                    }
                });
            }
        },
        destroy: function (target) {
            $(target).remove();
        },
        getValue: function (target) {
            // console.info("select2 getValue:", target);
            return $(target).val();
        },
        setValue: function (target, value) {
            // console.info("select2 setValue:", target, value);
            $(target).val(value).trigger('change');
        },
        resize: function (target, width) {
            $(target)._outerWidth(width);
        }
    }
});