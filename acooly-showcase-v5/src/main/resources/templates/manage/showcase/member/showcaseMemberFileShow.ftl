<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">ID:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.id}</dd>
		<dt class="col-sm-3">文件ID:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.objectId}</dd>
		<dt class="col-sm-3">用户编码:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.memberNo}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.username}</dd>
		<dt class="col-sm-3">文件标题:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.fileTitle}</dd>
		<dt class="col-sm-3">文件名:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.fileName}</dd>
		<dt class="col-sm-3">文件路径:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.filePath}</dd>
		<dt class="col-sm-3">文件扩展名:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.fileExt}</dd>
		<dt class="col-sm-3">文件类型:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.fileType.message()}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">备注:</dt>
		<dd class="col-sm-9">${showcaseMemberFile.comments}</dd>
	</dl>
</div>
