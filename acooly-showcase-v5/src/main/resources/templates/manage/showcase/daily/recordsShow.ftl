<div class="card-body">
    <dl class="row">
        <dt class="col-sm-3">ID:</dt>
        <dd class="col-sm-9">${records.id}</dd>
        <dt class="col-sm-3">用户:</dt>
        <dd class="col-sm-9">${records.userName}</dd>
        <dt class="col-sm-3">账户花销信息:</dt>
        <dd class="col-sm-9"  id="accountInformation" >${records.accountInformation}</dd>
        <dt class="col-sm-3">账户充值信息:</dt>
        <dd class="col-sm-9" id="recharge">${records.recharge}</dd>
        <dt class="col-sm-3">花销:</dt>
        <dd class="col-sm-9">${records.spending}</dd>
        <dt class="col-sm-3">业绩:</dt>
        <dd class="col-sm-9">${records.grades}</dd>
        <dt class="col-sm-3">创建时间:</dt>
        <dd class="col-sm-9">${records.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
        <dt class="col-sm-3">修改时间:</dt>
        <dd class="col-sm-9">${records.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
    </dl>
</div>
<script>
    // var input1 = document.getElementById("accountInformation");
    // var input3 = document.getElementById("recharge");
    // var rechargeElement = document.getElementById("recharge");
    //
    // // 检查输入框是否可编辑
    // if (rechargeElement.hasAttribute("readonly")) {
    //     // 如果输入框为只读，则将其设置为可编辑
    //     rechargeElement.removeAttribute("readonly");
    // } else {
    //     // 如果输入框已经可编辑，则无需进行任何操作
    // }
    // // 将输入框的值设置为新的充值信息
    // rechargeElement.value = input3.value.toString().replace(/,/g, '<br>');

</script>