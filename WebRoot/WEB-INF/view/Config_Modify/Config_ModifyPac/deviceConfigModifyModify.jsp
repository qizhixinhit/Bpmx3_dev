<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/js/hotent/scriptMgr.js"></script>
<script type="text/javascript">
	function afterOnload(){
		var afterLoadJs=[
		     			'${ctx}/js/hotent/formdata.js',
		     			'${ctx}/js/hotent/subform.js'
		 ];
		ScriptMgr.load({
			scripts : afterLoadJs
		});
	}
</script>
<table class="formTable" border="1" cellpadding="2" cellspacing="0">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">设备配置变更表</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">序号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="ID" lablename="序号" class="inputText" validate="{maxlength:20}" isflag="tableflag" type="text" value="${deviceConfigModify.ID}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">设备编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="Dev_ID" lablename="设备编号" class="inputText" validate="{maxlength:50,required:true}" isflag="tableflag" type="text" value="${deviceConfigModify.Dev_ID}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">操作人:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="Operator" lablename="操作人" class="inputText" validate="{maxlength:50,required:true}" isflag="tableflag" type="text" value="${deviceConfigModify.Operator}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">操作时间:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="Operate_Time" lablename="操作时间" class="inputText" validate="{maxlength:200,required:true}" isflag="tableflag" type="text" value="${deviceConfigModify.Operate_Time}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">审批人:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="Approval_Person" lablename="审批人" class="inputText" validate="{maxlength:50,required:true}" isflag="tableflag" type="text" value="${deviceConfigModify.Approval_Person}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">审批时间:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="Approval_Date" lablename="审批时间" class="inputText" validate="{maxlength:50,required:true}" isflag="tableflag" type="text" value="${deviceConfigModify.Approval_Date}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">审批状态:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="Approval_State" lablename="审批状态" class="inputText" validate="{maxlength:50,required:true}" isflag="tableflag" type="text" value="${deviceConfigModify.Approval_State}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">变更前内容:</td>
   <td class="formInput" style="width:80%;"><textarea name="Before_Change" validate="{empty:false}">${deviceConfigModify.Before_Change}</textarea></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">变更后内容:</td>
   <td class="formInput" style="width:80%;"><textarea name="After_Change" validate="{empty:false}">${deviceConfigModify.After_Change}</textarea></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">备注:</td>
   <td class="formInput" style="width:80%;"><textarea name="Remark" validate="{empty:false}">${deviceConfigModify.Remark}</textarea></td>
  </tr>
 </tbody>
</table>
