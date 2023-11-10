<div class="card-body">
    <dl class="row">
        <dt class="col-sm-3">ID:</dt>
        <dd class="col-sm-9">${accounts.id}</dd>
        <dt class="col-sm-3">账户信息:</dt>
        <dd class="col-sm-9">${accounts.accountName}</dd>
        <dt class="col-sm-3">邮箱:</dt>
        <dd class="col-sm-9">${accounts.mail}</dd>
        <dt class="col-sm-3">持有者:</dt>
        <dd class="col-sm-9">${accounts.holder}</dd>
        <dt class="col-sm-3">余额:</dt>
        <dd class="col-sm-9">${accounts.balance}</dd>
        <dt class="col-sm-3">账户编号:</dt>
        <dd class="col-sm-9">${accounts.mobileNo}</dd>
        <dt class="col-sm-3">状态:</dt>
        <dd class="col-sm-9">${accounts.status}</dd>
        <dt class="col-sm-3">创建时间:</dt>
        <dd class="col-sm-9">${accounts.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
        <dt class="col-sm-3">修改时间:</dt>
        <dd class="col-sm-9">${accounts.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
        <dt class="col-sm-3">备注:</dt>
        <dd class="col-sm-9">${accounts.comments}</dd>
    </dl>
</div>