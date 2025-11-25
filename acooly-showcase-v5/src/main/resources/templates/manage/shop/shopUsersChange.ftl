<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%" align="left">
        <form id="manage_shopUsersChange_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <input type="hidden" name="id" value="${shopUsers.id}" />

            <div class="form-group row">
                <label class="col-sm-3 col-form-label">登录账号</label>
                <div class="col-sm-9 col-form-content">${shopUsers.email}</div>
            </div>

            <div class="form-group row">
                <label for="manage_user_newPassword" class="col-sm-3 col-form-label">设置新密码</label>
                <div class="col-sm-9">
                    <input type="password" name="newPassword" id="manage_user_newPassword" placeholder="设置新密码..." class="form-control easyui-validatebox"
                           validType="commonRegExp['${SHOP_PASSWORD_REGEX}','${SHOP_PASSWORD_ERROR}']" data-options="required:true"/>
                </div>
            </div>

            <div class="form-group row">
                <label for="manage_user_confirmNewPassword" class="col-sm-3 col-form-label">确认新密码</label>
                <div class="col-sm-9">
                    <input name="confirmNewPassword" id="manage_user_confirmNewPassword" type="password" placeholder="请再次输入新密码..."
                           validType="equals['#manage_user_newPassword']" class="form-control easyui-validatebox"
                           data-options="required:true,missingMessage:'请再次填写新登录密码'"/>
                </div>
            </div>

            <div class="form-group row">
                <label for="manage_user_adminPassword" class="col-sm-3 col-form-label">当前用户密码</label>
                <div class="col-sm-9">
                    <input name="adminPassword" id="manage_user_adminPassword" type="password"
                           validType="commonRegExp['${SHOP_PASSWORD_REGEX}','${SHOP_PASSWORD_ERROR}']" class="form-control easyui-validatebox"
                           data-options="required:true"/>
                </div>
            </div>
        </form>
    </div>
</div>
