
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>流程模板明细</title>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript" src="${ctx}/js/hotent/subform.js"></script>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">流程模板详细信息</span>
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
	<table class="formTable" border="1" cellpadding="2" cellspacing="0">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">流程模板</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">模版名称:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.templatename}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">别名:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.bm}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">模板类别:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;padding:2px;" class="selectinput"><c:if test="${codeTemplate.templatetype=='1'}">流程图</c:if><c:if test="${codeTemplate.templatetype=='2'}">操作图</c:if><c:if test="${codeTemplate.templatetype=='3'}">事务图</c:if><c:if test="${codeTemplate.templatetype=='4'}">word文档</c:if></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">流程类别:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;padding:2px;" class="selectinput"><c:if test="${codeTemplate.subjecttype=='1'}">行政执法</c:if><c:if test="${codeTemplate.subjecttype=='2'}">政务公开</c:if></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">流程名称:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.subjectname}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">备注:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.bz}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">是否标记为模板:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;padding:2px;" class="selectinput"><c:if test="${codeTemplate.issign=='1'}">是</c:if><c:if test="${codeTemplate.issign=='2'}">fou</c:if></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">创建时间:</td>
   <td class="formInput" style="width:80%;">${codeTemplate.createtime}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">模板生成的文件路径:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.mbscdwjlj}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">模板文件生成的文件名称:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.mbwjscdwjmc}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">模板XML:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${codeTemplate.templateXML}</span></td>
  </tr>
 </tbody>
</table>
</body>
</html>
