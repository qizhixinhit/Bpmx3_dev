
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>KVM表明细</title>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">KVM表详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link back" href="list.ht"><span></span>返回</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<table cellpadding="2" cellspacing="0" border="1" class="formTable">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">KVM表</td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">管理IP:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${kvm.manage_IP}</span></td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">设备编号:</td>
   <td class="formInput" style="width:80%;">${kvm.dev_ID}</td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">KVM类型:</td>
   <td class="formInput" style="width:80%;">${kvm.kvm_TYPE}</td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">配置信息:</td>
   <td class="formInput" style="width:80%;"><textarea name="config_info" validate="{empty:false}" readonly="readonly">${kvm.config_info}</textarea></td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">备注:</td>
   <td class="formInput" style="width:80%;"><textarea name="remark" validate="{empty:false}" readonly="readonly">${kvm.remark}</textarea></td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">流程定义ID:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${kvm.actdefid}</span></td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">流程节点ID:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${kvm.nodeid}</span></td>
  </tr>
 </tbody>
</table>
</body>
</html>
