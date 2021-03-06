<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 运货表</title>
	<%@include file="/codegen/include/customForm.jsp" %>
	<script type="text/javascript">
		$(function() {
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#orderShippingForm').form();
			$("a.save").click(function() {
				frm.ajaxForm(options);
				if(frm.valid()){
					OfficePlugin.submit();
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
						
					}
					var tel = document.all("PHONE").value;
							if(/^0?1[3|4|5|8][0-9]\d{8}$/g.test(tel)||(/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/g.test(tel)))
							{
					$('#orderShippingForm').submit();}
					else {
          		 alert("手机号错误");
					}
				}
			});
		});
		function showResponse(responseText) {
		alert("Aaa");
		var email =$("input[name='EMAIL']").val();
		FIRSTNAME = $("input[name='FIRSTNAME']").val();
		LASTNAME = $("input[name='LASTNAME']").val();
		ADDRESS = $("input[name='ADDRESS']").val();
		CITY = $("input[name='CITY']").val();
		STATE = $("input[name='STATE']").val();
		ZIP = $("input[name='ZIP']").val();
		PHONE = $("input[name='PHONE']").val();
		SHIPPING = $("input[name='SHIPPING']").val();
		
			var obj = new com.hotent.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
					window.location.href = "${ctx}/e_business/e_business/orderBilling/add.ht?EMAIL="+email+"&FIRSTNAME="+FIRSTNAME+"&LASTNAME="+LASTNAME+"&ADDRESS="+ADDRESS+"&CITY="+CITY+"&STATE="+STATE+"&ZIP="+ZIP+"&PHONE="+PHONE+"&SHIPPING="+SHIPPING;		
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
	</script>
</head>
<body>
<%String email = request.getParameter("EMAIL");
  
   %>

<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
		    <c:choose>
			    <c:when test="${not empty orderShippingItem.id}">
			        <span class="tbar-label"><span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label"><span>
			    </c:otherwise>	
		    </c:choose>
		</div>
		
	</div>
	<form id="orderShippingForm" method="post" action="save.ht?EMAIL=<%=email%>">
		<div type="custform">
			<div class="panel-detail">
				<table class="formTable" border="1" cellpadding="2" cellspacing="0">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">运货表</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">EMAIL地址:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="EMAIL" lablename="EMAIL地址" class="inputText" validate="{maxlength:30,required:true,email:true}" isflag="tableflag" type="text" value="${orderShipping.EMAIL}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">名字:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="FIRSTNAME" lablename="名字" class="inputText" validate="{maxlength:14,required:true}" isflag="tableflag" type="text" value="${orderShipping.FIRSTNAME}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">姓氏:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="LASTNAME" lablename="姓氏" class="inputText" validate="{maxlength:15,required:true}" isflag="tableflag" type="text" value="${orderShipping.LASTNAME}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">地址:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="ADDRESS" lablename="地址" class="inputText" validate="{maxlength:50,required:true}" isflag="tableflag" type="text" value="${orderShipping.ADDRESS}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">城市:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="CITY" lablename="城市" class="inputText" validate="{maxlength:30,required:true,汉字:true}" isflag="tableflag" type="text" value="${orderShipping.CITY}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">州代码:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="STATE" lablename="州代码" class="inputText" validate="{maxlength:2,required:true,英文字母:true}" isflag="tableflag" type="text" value="${orderShipping.STATE}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">邮编:</td>
   <td class="formInput" style="width:80%;"><input name="ZIP" showtype="{'coinValue':'','isShowComdify':0,'decimalValue':0}" validate="{number:true,maxIntLen:6,maxDecimalLen:0,required:true}" type="text" value="${orderShipping.ZIP}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">电话:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="PHONE" lablename="电话" class="inputText" validate="{maxlength:15,required:true,手机号码:true}" isflag="tableflag" type="text" value="${orderShipping.PHONE}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" align="right" nowrap="nowarp">运货类型:</td>
   <td class="formInput" style="width:80%;"><label><input name="SHIPPING" value="NEXT DAY" lablename="运货类型" validate="{required:true}" type="radio" <c:if test='${orderShipping.SHIPPING=="NEXT DAY"}'>checked='checked'</c:if> />NEXT DAY</label><label><input name="SHIPPING" value="TWO DAY" lablename="运货类型" validate="{required:true}" type="radio" <c:if test='${orderShipping.SHIPPING=="TWO DAY"}'>checked='checked'</c:if> />TWO DAY</label><label><input name="SHIPPING" value="FIVE DAY" lablename="运货类型" validate="{required:true}" type="radio" <c:if test='${orderShipping.SHIPPING=="FIVE DAY"}'>checked='checked'</c:if> />FIVE DAY</label></td>
  </tr>
  <tr>
  <td><input id="EMAIL" value="<%=email%>" type="hidden">
  </td>
  </tr>
 </tbody>
</table>
			</div>
		</div>
		<input type="hidden" name="id" value="${orderShipping.id}"/>
	</form>
	<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="javascript:;"><span></span>继续</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link back" href="list.ht"><span></span>返回</a></div>
			</div>
		</div>
</body>
</html>
