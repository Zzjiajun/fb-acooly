/**
 * 通过身份证号码自动解析
 * @param obj
 */
function manage_customer_parse_idcard(obj) {
    // 如果文本框的合法性验证没有通过，则不进行解析
    if (!$(obj).validatebox('validate')) {
        return;
    }
    var idcard = $(obj).val();
    $.ajax({
        url: '/manage/showcase/customer/parseIdCard.html',
        data: {idcard: idcard},
        beforeSend: function () {
            $(obj).after('<img src="/assert/image/icon/loading_sm.gif" style="margin-left: -20px;margin-top: 5px;width: 16px;"/>');
        },
        complete: function () {
            $(obj).next().remove();
        },
        success: function (result) {
            if (result.success) {
                var birthday = result.entity.birthday;
                var age = new Date().getFullYear() - parseInt(birthday.substring(0, 4));
                birthday = birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6, 8);
                $('#manage_customer_editform_age').numberbox("setValue",age);
                $('#manage_customer_editform_birthday').val(birthday);
                $('#manage_customer_editform_gender').combobox('select', result.entity.gender == 'M' ? 1 : 2);
            }
        },
        error: function (r, e) {
            $.messager.show({title: '提示', msg: e});
        }
    });
}