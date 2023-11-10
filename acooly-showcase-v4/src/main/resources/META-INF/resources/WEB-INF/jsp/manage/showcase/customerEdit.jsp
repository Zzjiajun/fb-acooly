<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<div>
    <form id="manage_customer_editform"
          action="${pageContext.request.contextPath}/manage/showcase/customer/${action=='create'?'saveJson':'updateJson'}.html"
          method="post">
        <jodd:form bean="customer" scope="request">
            <input name="id" type="hidden"/>
            <table class="tableForm" width="100%">
                <tr>
                    <th width="15%">用户名：</th>
                    <td><input type="text" name="username" placeholder="请输入用户名..." size="32" class="easyui-validatebox text"
                               data-options="required:true,validType:'account[6,12]'"/></td>
                    <th width="15%">姓名：</th>
                    <td><input type="text" name="realName" placeholder="请输入中文姓名..." size="32" class="easyui-validatebox text"
                               data-options="required:true,validType:['CHS','length[2,16]']"/></td>
                </tr>
                <tr>
                    <th>身份证：</th>
                    <td>
                        <input type="hidden" name="idcardType" value="cert"/>
                        <input type="text" name="idcardNo" id="manage_customer_editform_idcardNo"
                               onblur="manage_customer_parse_idcard($(this))" size="32" placeholder="支持自动解析身份证号码..."
                               class="easyui-validatebox text" required="true" validType="cert"/>
                    </td>
                    <th>性别：</th>
                    <td><select name="gender" id="manage_customer_editform_gender" editable="false" panelHeight="auto"
                                class="easyui-combobox" data-options="required:true">
                        <c:forEach items="${allGenders}" var="e">
                            <option value="${e.key}">${e.value}</option>
                        </c:forEach>
                    </select></td>
                </tr>
                <tr>
                    <th>年龄：</th>
                    <td><input type="text" name="age" id="manage_customer_editform_age" size="32" style="height: 27px;line-height: 27px;"
                               class="easyui-numberbox text" validType="length[1,4]"/></td>
                    <th>生日：</th>
                    <td><input type="text" name="birthday" id="manage_customer_editform_birthday" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${customer.birthday}" pattern="yyyy-MM-dd"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/></td>
                </tr>


                <tr>
                    <th>手机号码：</th>
                    <td><input type="text" name="mobileNo" size="32" data-options="" class="easyui-validatebox text" validType="mobile"/>
                    </td>
                    <th>邮件：</th>
                    <td><input type="text" name="mail" size="32" class="easyui-validatebox text"
                               data-options="validType:['email','length[0,64]']"/></td>
                </tr>
                <tr>
                    <th>薪水：</th>
                    <td><input type="text" name="salaryMoney" size="32" style="height: 27px;line-height: 27px;" class="easyui-numberbox text"
                               precision="2" data-options="validType:'money'"
                               value="<fmt:formatNumber pattern="###.##" value="${customer.salary/100}"/>"/></td>
                    <th>手续费：</th>
                    <td><input type="text" name="fee" size="32" style="height: 27px;line-height: 27px;" class="easyui-numberbox text"
                               precision="2" data-options="validType:'money'"/></td>
                </tr>
                <tr>
                    <th>客户类型：</th>
                    <td><select name="customerType" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox">
                        <c:forEach items="${allCustomerTypes}" var="e">
                            <option value="${e.key}">${e.value}</option>
                        </c:forEach>
                    </select></td>
                    <th>状态：</th>
                    <td><select name="status" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox"
                                data-options="required:true">
                        <c:forEach items="${allStatuss}" var="e">
                            <option value="${e.key}">${e.value}</option>
                        </c:forEach>
                    </select></td>
                </tr>
                <tr>
                    <th>简介：</th>
                    <td colspan="3"><input type="text" name="subject" size="83" class="easyui-validatebox text"
                                           validType="length[1,64]"/></td>
                </tr>
                <tr>
                    <th>备注：</th>
                    <td colspan="3"><input type="text" name="comments" size="83" class="easyui-validatebox text"
                                           validType="length[1,128]"/></td>
                </tr>
            </table>
        </jodd:form>
    </form>

    <script>



    </script>


</div>
