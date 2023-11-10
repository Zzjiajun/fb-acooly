/**
 * 会员子对象TAB管理
 * @type {{main_datagrid: string, selectTab: acooly_showcase_member_tab.selectTab, loadMemberContactsWithList: acooly_showcase_member_tab.loadMemberContactsWithList, loadMemberFileWithPager: acooly_showcase_member_tab.loadMemberFileWithPager, mainDatagridOnClick: acooly_showcase_member_tab.mainDatagridOnClick}}
 */
let acooly_showcase_member_tab = {

    main_datagrid: "manage_showcaseMember_datagrid",

    mainDatagridOnClick: function () {
        let index = $.acooly.framework.getSelectedTabIndex("manage_showcaseMember_tabs");
        $.acooly.showcase.member.tab.selectTab(index, true);
    },

    /**
     * EasyUI-Tab的onSelect事件
     */
    onSelect: function (title, index) {
        $.acooly.showcase.member.tab.selectTab(index, false);
    },

    /**
     * 选择tab时动态加载对应数据
     * @param title
     * @param index
     */
    selectTab: function (index, reset) {
        var row = $.acooly.framework.getSelectedRow("manage_showcaseMember_datagrid");
        if (!row) return;
        if (index == 0) {
            // 按分页方式加载
            $.acooly.showcase.member.file.loadPage();
        } else {
            // 列表方式全数据加载
            $.acooly.showcase.member.contact.loadList(reset);
        }
    }
}


/**
 * 会员联系人管理
 * @type {{}}
 */
let acooly_showcase_member_contact = {

    /**
     * 联系人列表加载
     * 注意：这里演示列表查询非分页查询
     */
    loadList: function (reset) {
        $.acooly.framework.fireSelectRow("manage_showcaseMember_datagrid", function (row) {
            // 选中的会员记录的会员编码
            let memberNo = row.memberNo;
            var queryParams = {'search_EQ_memberNo': memberNo};
            if (reset) {
                $('#manage_showcaseMemberContact_searchform')[0].reset();
            } else {
                // 序列化查询条件
                $.extend(true, queryParams, serializeObject($('#manage_showcaseMemberContact_searchform')));
            }
            $.acooly.framework.loadGrid({
                gridId: "manage_showcaseMemberContact_datagrid",
                url: '/manage/showcase/member/showcaseMemberContact/queryJson.html',
                ajaxData: queryParams
            });

            // $.acooly.framework.create({url:'/manage/showcase/member/showcaseMemberContact/create.html',entity:'showcaseMemberContact',width:500,height:500})

        }, '请先选择操作会员数据行');
    },

    add: function () {
        $.acooly.framework.fireSelectRow("manage_showcaseMember_datagrid", function (row) {
            let memberNo = row.memberNo;
            $.acooly.framework.create({
                url: '/manage/showcase/member/showcaseMemberContact/create.html',
                entity: 'showcaseMemberContact', width: 500, height: 500,
                ajaxData: {"memberNo": row.memberNo}
            });
        }, '请先选择操作会员数据行');
    },

    remove: function () {

    }

}


let acooly_showcase_member_profile = {
    photoFormatter: function (value, row, index, data) {
        if (!value || value == '') return null;
        let fileMeta = $.acooly.file.parse(value);
        let url = value;
        if (data && data.data && data.data.serverRoot) {
            url = data.data.serverRoot + value;
        }
        let html = "<a href='javascript:;' onclick=\"$.acooly.file.play('" + url + "')\"><img src='" + url + "' style='width: 48px;height: 48px;padding:4px;'></a>"
        return html;
    }
}
/**
 * 会员附件管理
 * @type {{load: acooly_showcase_memberFile.load, upload: acooly_showcase_memberFile.upload}}
 */
let acooly_showcase_memberFile = {

    /**
     * 文件批量上传视图
     */
    upload: function () {
        $.acooly.framework.fireSelectRow($.acooly.showcase.member.tab.main_datagrid, function (row) {
            let uploader = "manage_showcaseMemberFile_uploader_file";
            let uploadDialog = $('<div/>').dialog({
                title: '<i class="fa fa-cloud-upload fa-lg fa-fw fa-col"></i> 上传附件',
                href: '/manage/showcase/member/showcaseMemberFile/uploaderView.html?memberNo=' + row.memberNo,
                modal: true, width: 400, height: 400,
                buttons: [{
                    text: '<i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i> 上传',
                    handler: function () {
                        $('#' + uploader).uploadifive('upload');
                    }
                }, {
                    text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col" ></i> 关闭',
                    handler: function () {
                        uploadDialog.dialog('close');
                    }
                }],
                onClose: function () {
                    $.acooly.showcase.member.file.loadPage();
                    $(this).dialog('destroy');
                }
            });
        }, '请先选择项目数据行');
    },

    /**
     * 文件列表分页查询加载
     */
    loadPage: function () {
        $.acooly.framework.fireSelectRow("manage_showcaseMember_datagrid", function (row) {
            let memberNo = row.memberNo;
            $("#manage_showcaseMemberFile_datagrid").datagrid("options").url = "/manage/showcase/member/showcaseMemberFile/listJson.html";
            $('#manage_showcaseMemberFile_datagrid').datagrid("load", {
                "search_EQ_memberNo": memberNo
            });
        }, '请先选择操作会员数据行');
    }

}


/**
 * 会员管理
 * @type {{memberNoGenerate: acooly_showcase_member.memberNoGenerate, usernameVerify: (function(*=): undefined), toggleCustomTypes: acooly_showcase_member.toggleCustomTypes, tab: {main_datagrid: string, selectTab: function(*): void, loadMemberContactsWithList: function(), loadMemberFileWithPager: function(): void, mainDatagridOnClick: function(): void}, idCardVerify: (function(*=): undefined), initEditor: acooly_showcase_member.initEditor}}
 */
let acooly_showcase_member = {

    tab: acooly_showcase_member_tab,
    file: acooly_showcase_memberFile,
    contact: acooly_showcase_member_contact,
    profile: acooly_showcase_member_profile,

    /**
     * 显示/隐藏自定义类型组
     */
    toggleCustomTypes: function (customerType) {
        if (customerType == 'normal') {
            $('#manage_showcaseMember_editform_customType_container').hide();
        } else {
            $('#manage_showcaseMember_editform_customType_container').show();
        }
    },

    /**
     * 会员编码生成器
     */
    memberNoGenerate: function (obj) {
        $.ajax({
            url: '/manage/showcase/member/showcaseMember/did.html',
            beforeSend: function () {
                $(obj).html("<i class=\"fa fa-spinner fa-spin fa-fw\"></i>");
            },
            complete: function () {
                $(obj).html("生成");
            },
            success: function (result) {
                if (result.success) {
                    $.acooly.formVal("manage_showcaseMember_editform", "memberNo", result.data.id);
                } else {
                    $.messager.show({title: '提示', msg: result.message});
                }
            },
            error: function (r, e) {
                $.messager.show({title: '提示', msg: e});
            }
        });
    },

    /**
     * 初始扩展信息的多媒体编辑器
     * 依赖ofile组件
     */
    initEditor: function () {
        // 从meta中获取token
        let token = $("meta[name='X-CSRF-TOKEN']").attr("content");
        $.acooly.editor.kindEditor({
            minHeight: '310',
            textareaId: 'manage_showcaseMember_editform_context',
            uploadUrl: '/manage/showcase/member/showcaseMember/kindEditor.htm?_csrf=' + token
        });
    },

    /**
     * 解析身份证信息
     * @param obj
     */
    idCardVerify: function (obj) {

        let idCard = $(obj).val();
        if (!obj || !idCard) {
            return;
        }
        // todo:封装到framework中（validate，isValid）
        // 如果文本框的合法性验证没有通过，则不进行解析
        if (!$(obj).validatebox('isValid')) {
            return;
        }
        let formId = "manage_showcaseMember_editform";
        $.ajax({
            url: '/manage/showcase/member/showcaseMember/parseIdCard.html',
            data: {idcard: idCard},
            beforeSend: function () {
                let html = "<div class=\"input-group-append\"><span class=\"input-group-text\"><i class=\"fa fa-spinner fa-spin fa-fw\"></i></span></div>"
                $(obj).after(html);
            },
            complete: function () {
                $(obj).next().remove();
            },
            success: function (result) {
                if (result.success) {
                    let birthday = result.entity.birthday;
                    birthday = birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6, 8);
                    let age = new Date().getFullYear() - parseInt(birthday.substring(0, 4));
                    let gender = result.entity.gender;
                    let animal = result.entity.animalSign;
                    $.acooly.formVal(formId, "birthday", birthday);
                    $.acooly.formVal(formId, "age", age);
                    $.acooly.formVal(formId, "gender", gender);
                    $.acooly.formVal(formId, "animal", animal);
                }
            },
            error: function (r, e) {
                $.messager.show({title: '提示', msg: e});
            }
        });
    },

    /**
     * 用户名检查
     */
    usernameVerify: function (obj) {
        let username = $(obj).val();
        if (!obj || !username) {
            return;
        }
        // 如果文本框的合法性验证没有通过，则不进行解析
        if (!$(obj).validatebox('isValid')) {
            return;
        }
        let formId = "manage_showcaseMember_editform";
        $.ajax({
            url: '/manage/showcase/member/showcaseMember/checkUniqueUsername.html',
            data: {username: username},
            beforeSend: function () {
                let html = "<div class=\"input-group-append\"><span class=\"input-group-text\"><i class=\"fa fa-spinner fa-spin fa-fw\"></i></span></div>"
                $(obj).after(html);
            },
            complete: function () {
                $(obj).next().remove();
            },
            success: function (result) {
                if (!result.success) {
                    $(obj).tooltip({
                        position: 'right',
                        content: result.message,
                        onShow: function () {
                            $(this).tooltip('tip').css({
                                backgroundColor: '#666',
                                borderColor: '#666'
                            });
                        }
                    });
                }
            },
            error: function (r, e) {
                $.messager.show({title: '提示', msg: e});
            }
        });
    },

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

    $.extend($.acooly.showcase, {member: acooly_showcase_member});
    // $.extend($.acooly.showcase, {memberFile: acooly_showcase_memberFile});

})(jQuery);