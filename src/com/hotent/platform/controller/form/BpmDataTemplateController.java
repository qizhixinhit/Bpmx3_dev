package com.hotent.platform.controller.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fr.data.Dictionary.MV;
import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.bpm.model.FlowNode;
import com.hotent.core.bpm.model.NodeCache;
import com.hotent.core.excel.util.ExcelUtil;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.DateFormatUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.BpmNodeButton;
import com.hotent.platform.model.bpm.BpmNodeSet;
import com.hotent.platform.model.form.BpmDataTemplate;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.form.BpmFormField;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.form.BpmFormTemplate;
import com.hotent.platform.model.form.CommonVar;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.model.system.SysBusEvent;
import com.hotent.platform.model.system.SysObjLog;
import com.hotent.platform.service.bpm.BpmDefinitionService;
import com.hotent.platform.service.bpm.BpmNodeButtonService;
import com.hotent.platform.service.bpm.BpmNodeSetService;
import com.hotent.platform.service.form.BpmDataTemplateService;
import com.hotent.platform.service.form.BpmFormDefService;
import com.hotent.platform.service.form.BpmFormHandlerService;
import com.hotent.platform.service.form.BpmFormTableService;
import com.hotent.platform.service.form.BpmFormTemplateService;
import com.hotent.platform.service.system.SysBusEventService;

/**
 * <pre>
 * 对象功能:业务数据模板 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zxh
 * 创建时间:2013-09-05 14:14:50
 * </pre>
 */
@Controller
@RequestMapping("/platform/form/bpmDataTemplate/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class BpmDataTemplateController extends BaseController {
	@Resource
	private BpmNodeSetService bpmNodeSetService;
	@Resource  
	private BpmDataTemplateService bpmDataTemplateService;
	@Resource
	private BpmNodeButtonService bpmNodeButtonService;
	@Resource
	private BpmFormTableService bpmFormTableService;
	@Resource
	private BpmFormTemplateService  bpmFormTemplateService ;
	@Resource
	private BpmFormDefService bpmFormDefService;
	@Resource
	private BpmFormHandlerService bpmFormHandlerService;
	@Resource
	private BpmDefinitionService bpmDefinitionService;
	@Resource
	private SysBusEventService sysBusEventService;

	/**
	 * 添加或更新业务数据模板。
	 * 
	 * @param request
	 * @param response
	 * @param bpmDataTemplate
	 *            添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description = "添加或更新业务数据模板",execOrder=ActionExecOrder.AFTER,
			detail="<#if isAdd>添加<#else>更新</#if>业务数据模板:【${SysAuditLinkService.getBpmDataTemplateLink(Long.valueOf(tempId))}】")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Boolean buttonFlagsher =  RequestUtil.getBoolean(request, "buttonFlagsher",true);
		long defIdsher = RequestUtil.getLong(request,  "defIdsher",0);
		String nodeIdsher = RequestUtil.getString(request, "nodeIdsher");
		String resultMsg = null;
		BpmDataTemplate bpmDataTemplate = getFormObject(request);//这个html模板只传了templateAlias，TemplateHtml=null
		try {
			boolean flag=bpmDataTemplate.getId() == null || bpmDataTemplate.getId() == 0; 
			bpmDataTemplateService.save(bpmDataTemplate);//在这个过程中进行了第一次模板解释，然后复制到templateHtml上
			resultMsg = flag?"添加业务数据模板成功": "更新业务数据模板成功";
			SysAuditThreadLocalHolder.putParamerter("isAdd", flag);
			SysAuditThreadLocalHolder.putParamerter("tempId", bpmDataTemplate.getId().toString());
			writeResultMessage(response.getWriter(), resultMsg,
					ResultMessage.Success);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(),
					resultMsg + "," + e.getMessage(), ResultMessage.Fail);
		}
		Long id=bpmDataTemplate.getId();
		String foKey=bpmDataTemplate.getFormKey();
		Long tabid=bpmDataTemplate.getTableId();
		System.out.println("+++ save formkey :"+foKey);
		System.out.println("+++ save tableid :"+tabid);
		System.out.println("+++ save id :"+id);
		HttpSession session=request.getSession();
	    session.setAttribute("id", id); 
	    session.setAttribute("foKey", foKey); 
	    session.setAttribute("tabid", tabid); 
	}


	/**
	 * 添加或更新业务数据模板。
	 * 
	 * @param request
	 * @param response
	 * @param bpmDataTemplate
	 *            王焕然流程定义中绑定数据模板添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("savesher")
	@Action(description = "添加或更新业务数据模板",execOrder=ActionExecOrder.AFTER,
			detail="<#if isAdd>添加<#else>更新</#if>业务数据模板:【${SysAuditLinkService.getBpmDataTemplateLink(Long.valueOf(tempId))}】")
	public void savesher(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Boolean buttonFlagsher =  RequestUtil.getBoolean(request, "buttonFlagsher",true);
		long defIdsher = RequestUtil.getLong(request,  "defIdsher",0);
		String nodeIdsher = RequestUtil.getString(request, "nodeIdsher");
		BpmDefinition bpmDefinitionsher=bpmDefinitionService.getById(defIdsher);
		String actdefIdsher = bpmDefinitionsher.getActDefId();
		String resultMsg = null;
		BpmDataTemplate bpmDataTemplate = getFormObject(request);//这个html模板只传了templateAlias，TemplateHtml=null
		try {
			boolean flag=bpmDataTemplate.getId() == null || bpmDataTemplate.getId() == 0; 
			bpmDataTemplateService.savesher(bpmDataTemplate,defIdsher,nodeIdsher,buttonFlagsher,actdefIdsher);//在这个过程中进行了第一次模板解释，然后复制到templateHtml上
			resultMsg = flag?"添加业务数据模板成功": "更新业务数据模板成功";
			SysAuditThreadLocalHolder.putParamerter("isAdd", flag);
			SysAuditThreadLocalHolder.putParamerter("tempId", bpmDataTemplate.getId().toString());
			writeResultMessage(response.getWriter(), resultMsg,
					ResultMessage.Success);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(),
					resultMsg + "," + e.getMessage(), ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得 BpmDataTemplate 实体
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected BpmDataTemplate getFormObject(HttpServletRequest request)
			throws Exception {

		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher((new String[] { "yyyy-MM-dd" })));

		String json = RequestUtil.getString(request, "json",false);
		if (StringUtil.isEmpty(json))
			return null;
		JSONObject obj = JSONObject.fromObject(json);

		String displayField=obj.getString("displayField");
		String conditionField=obj.getString("conditionField");
		String sortField=obj.getString("sortField");
		String filterField=obj.getString("filterField");
		String manageField=obj.getString("manageField");
		String exportField=obj.getString("exportField");
		
		obj.remove("displayField");
		obj.remove("conditionField");
		obj.remove("sortField");
		obj.remove("filterField");
		obj.remove("manageField");
		
		BpmDataTemplate bpmDataTemplate = (BpmDataTemplate) JSONObject.toBean(obj, BpmDataTemplate.class);
		
		bpmDataTemplate.setDisplayField(displayField);
		bpmDataTemplate.setConditionField(conditionField);
		bpmDataTemplate.setSortField(sortField);
		bpmDataTemplate.setFilterField(filterField);
		bpmDataTemplate.setManageField(manageField);
		bpmDataTemplate.setExportField(exportField);
		
		
		return bpmDataTemplate;
	}
	/**
	 * 取得业务数据模板分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("showme")
	@Action(description = "查看业务数据模板分页列表")
	public ModelAndView showme(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = RequestUtil.getLong(request,"id");
		BpmDataTemplate template = bpmDataTemplateService.getById(id);
		String name = template.getName();
		String formkey = template.getFormkey();
		Long defId = template.getDefId();
		String type=template.getTemplateAlias();
		String displayfield = template.getDisplayField();
		String managefield = template.getManageField();
		ModelAndView mv = this.getAutoView();
		mv.addObject("name", name)
		.addObject("id", id)
		.addObject("formkey", formkey)
		.addObject("defId", defId)
		.addObject("type", type)
		.addObject("displayfield", displayfield)
		.addObject("managefield", managefield.replace("'","*"));
		return mv;
	}
	
	/**
	 * 取得业务数据模板分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("showevent")
	@Action(description = "查看业务数据模板分页列表")
	public ModelAndView showevent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = RequestUtil.getLong(request,"id");
		int index = RequestUtil.getInt(request,"index");
		BpmDataTemplate template = bpmDataTemplateService.getById(id);
		String managefield = template.getManageField();
		JSONArray managefieldjson = JSONArray.fromObject(managefield);
		ModelAndView mv = this.getAutoView();
		mv.addObject("manageone", managefieldjson.get(index).toString().replace("'","*"));
		return mv;
	}
	
	/**
	 * 取得业务数据模板分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看业务数据模板分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<BpmDataTemplate> list = bpmDataTemplateService
				.getAll(new QueryFilter(request, "bpmDataTemplateItem"));
		ModelAndView mv = this.getAutoView().addObject("bpmDataTemplateList",
				list);

		return mv;
	}

	/**
	 * 业务数据模板帅气的绑定参数小窗口之绑定多列
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return王焕然
	 * @throws Exception
	 */
	@RequestMapping("sherdatabang")
	@Action(description = "查看业务数据模板分页列表")
	public ModelAndView sherdatabang(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView().addObject("",null);
		Long btnId = Long.parseLong(RequestUtil.getString(request,"btnId"));
		System.out.println("btnId:::::::::::"+btnId);
		Long defIdsher = RequestUtil.getLong(request,"defIdsher",0);
		Long thistime = RequestUtil.getLong(request,"thistime");
		String actdefIdsher = RequestUtil.getString(request,"actdefIdsher");
		System.out.println("thistime:::"+thistime);
		Long bpmFormTableIdsher = RequestUtil.getLong(request,"bpmFormTableIdsher",0);
		BpmNodeSet bpmnodesetsher = new BpmNodeSet();
		ArrayList<String> nodeIdsher = new ArrayList<String>();
		//List<BpmNodeSet> nodesetlist = bpmNodeSetService.getByDefId(defIdsher);
		//for(int i = 0;i<nodesetlist.size();i++){
			//nodeIdsher.add(nodesetlist.get(i).getNodeName()+":"+nodesetlist.get(i).getNodeId());
		//}boolean accessOrder = false;
		boolean accessOrder = false;
		Map<String ,FlowNode> flowNode=new  LinkedHashMap<String ,FlowNode>(20,0.80f,accessOrder);
		Map<String,FlowNode> map=NodeCache.readFromXml(actdefIdsher);
		Set<String> keys = map .keySet();//索引键集
        if(keys != null){
			 Iterator iterator = keys.iterator();  
	            while(iterator.hasNext()){ 
	            	Object kess = iterator.next();
	            	FlowNode value = (FlowNode)map.get(kess); 
	            	String key=kess.toString(); 
	            	if(key.contains("UserTask")||key.contains("ScriptTask")){
	            		flowNode.put(key,value);
	            		nodeIdsher.add(flowNode.put(key,value).getNodeName()+":"+flowNode.put(key,value).getNodeId());
	            	}
	            	
	            }
	    }
		BpmFormTable bpmformtable =  bpmFormTableService.getByTableId(bpmFormTableIdsher,1);
		List<BpmFormField> bpmformfield =  bpmformtable.getFieldList();
		ArrayList<String> fieldnamelistsher = new ArrayList<String>();
		for(int i = 0;i<bpmformfield.size();i++){
			fieldnamelistsher.add(bpmformfield.get(i).getFieldName());
		}
		mv.addObject("nodeIdsher", nodeIdsher)
		  .addObject("thistime", thistime)
		  .addObject("btnId", btnId)
		  .addObject("fieldnamelistsher", fieldnamelistsher)
		  .addObject("actdefIdsher", actdefIdsher);
		return mv;
	}
	/**
	 * 业务数据模板帅气的绑定参数小窗口之绑定后续自由跳节点
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return王焕然
	 * @throws Exception
	 */
	@RequestMapping("sherdatabangnew")
	@Action(description = "查看业务数据模板分页列表")
	public ModelAndView sherdatabangnew(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView().addObject("",null);
		Long btnId = Long.parseLong(RequestUtil.getString(request,"btnId"));
		System.out.println("btnId:::::::::::"+btnId);
		Long defIdsher = RequestUtil.getLong(request,"defIdsher",0);
		Long thistime = RequestUtil.getLong(request,"thistime");
		String actdefIdsher = RequestUtil.getString(request,"actdefIdsher");
		String scansher = RequestUtil.getString(request,"scansher");
		System.out.println("thistime:::"+thistime);
		Long bpmFormTableIdsher = RequestUtil.getLong(request,"bpmFormTableIdsher",0);
		BpmNodeSet bpmnodesetsher = new BpmNodeSet();
		ArrayList<String> nodeIdsher = new ArrayList<String>();
		//List<BpmNodeSet> nodesetlist = bpmNodeSetService.getByDefId(defIdsher);
		//for(int i = 0;i<nodesetlist.size();i++){
			//nodeIdsher.add(nodesetlist.get(i).getNodeName()+":"+nodesetlist.get(i).getNodeId());
		//}boolean accessOrder = false;
		boolean accessOrder = false;
		Map<String ,FlowNode> flowNode=new  LinkedHashMap<String ,FlowNode>(20,0.80f,accessOrder);
		Map<String,FlowNode> map=NodeCache.readFromXml(actdefIdsher);
		Set<String> keys = map .keySet();//索引键集
        if(keys != null){
			 Iterator iterator = keys.iterator();  
	            while(iterator.hasNext()){ 
	            	Object kess = iterator.next();
	            	FlowNode value = (FlowNode)map.get(kess); 
	            	String key=kess.toString(); 
	            	if(key.contains("UserTask")||key.contains("ScriptTask")){
	            		flowNode.put(key,value);
	            		nodeIdsher.add(flowNode.put(key,value).getNodeName()+":"+flowNode.put(key,value).getNodeId());
	            	}
	            	
	            }
	    }
		BpmFormTable bpmformtable =  bpmFormTableService.getByTableId(bpmFormTableIdsher,1);
		List<BpmFormField> bpmformfield =  bpmformtable.getFieldList();
		ArrayList<String> fieldnamelistsher = new ArrayList<String>();
		for(int i = 0;i<bpmformfield.size();i++){
			fieldnamelistsher.add(bpmformfield.get(i).getFieldName());
		}
		mv.addObject("nodeIdsher", nodeIdsher)
		  .addObject("thistime", thistime)
		  .addObject("scansher", scansher)
		  .addObject("btnId", btnId)
		  .addObject("fieldnamelistsher", fieldnamelistsher)
		  .addObject("actdefIdsher", actdefIdsher);
		return mv;
	}
	
	/**
	 * 删除业务数据模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除业务数据模板",execOrder=ActionExecOrder.BEFORE,
			detail="删除业务数据模板：" +
			"<#list StringUtils.split(id,\",\") as item>" +
			"<#assign entity=bpmDataTemplateService.getById(Long.valueOf(item))/>" +
			"【${entity.name}】" +
			"</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			bpmDataTemplateService.delByIds(lAryId);
			message = new ResultMessage(ResultMessage.Success, "删除业务数据模板成功!");
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail, "删除失败"
					+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 添加业务数据模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "编辑业务数据模板")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		
		Long tableId = RequestUtil.getLong(request, "tableId");
		String formKey = RequestUtil.getString(request, "formKey");
		String name = RequestUtil.getString(request, "name");
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(tableId,0);
		List<BpmFormTemplate> templates = bpmFormTemplateService.getDataTemplate();
		
		
		BpmDataTemplate bpmDataTemplate =null;//bpmDataTemplateService.getById(id);
		
		System.out.println("+++ edit :"+name);
		if(BeanUtils.isEmpty(bpmDataTemplate)){
			bpmDataTemplate = new BpmDataTemplate();
			bpmDataTemplate.setFormKey(formKey);
			bpmDataTemplate.setTableId(tableId);
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,""));
			bpmDataTemplate.setName(name);
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,""));
		}else{
			Long defId = bpmDataTemplate.getDefId();
			if(BeanUtils.isNotEmpty(defId)){
				BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
				if(BeanUtils.isNotEmpty(bpmDefinition))
					bpmDataTemplate.setSubject(bpmDefinition.getSubject());
			}
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,bpmDataTemplate.getDisplayField()));
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,bpmDataTemplate.getExportField()));
		}
		bpmDataTemplate.setSource((bpmFormTable.getIsExternal()==BpmFormTable.NOT_EXTERNAL)?BpmDataTemplate.SOURCE_CUSTOM_TABLE:BpmDataTemplate.SOURCE_OTHER_TABLE);
		
		
		return this.getAutoView().addObject("bpmFormTableJSON", JSONObject.fromObject(bpmFormTable))
				.addObject("DataRightsJson",JSONObject.fromObject(bpmDataTemplate))
				.addObject("bpmDataTemplate",bpmDataTemplate)
				.addObject("bpmFormTableIdsher", bpmFormTable.getTableId())
				.addObject("manageField", "[]")
				.addObject("templates",templates)
				.addObject("name",name);
				//.addObject("id",id);
	}
	
	//编辑业务数据模板
	
	
	@RequestMapping("editys")
	@Action(description = "编辑业务数据模板")
	public ModelAndView editys(HttpServletRequest request) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		String formKey = RequestUtil.getString(request, "formKey");
		String name = RequestUtil.getString(request, "name");
		Long id =RequestUtil.getLong(request, "id");
		System.out.println("+++ edit ++ Id  :   "+id);
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(tableId,0);
		List<BpmFormTemplate> templates = bpmFormTemplateService.getDataTemplate();
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		System.out.println("+++ edit Id:"+bpmDataTemplate.getId());
		//String name = RequestUtil.getString(request, "name");
		//System.out.println("controller ++ edit name : "+name);
		
		if(BeanUtils.isEmpty(bpmDataTemplate)){
			bpmDataTemplate = new BpmDataTemplate();
			bpmDataTemplate.setFormKey(formKey);
			bpmDataTemplate.setTableId(tableId);
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,""));
			bpmDataTemplate.setName(name);
			bpmDataTemplate.setId(id);
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,""));
		}else{
			Long defId = bpmDataTemplate.getDefId();
			if(BeanUtils.isNotEmpty(defId)){
				BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
				if(BeanUtils.isNotEmpty(bpmDefinition))
					bpmDataTemplate.setSubject(bpmDefinition.getSubject());
			}
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,bpmDataTemplate.getDisplayField()));
			
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,bpmDataTemplate.getExportField()));
		}
		bpmDataTemplate.setSource((bpmFormTable.getIsExternal()==BpmFormTable.NOT_EXTERNAL)?BpmDataTemplate.SOURCE_CUSTOM_TABLE:BpmDataTemplate.SOURCE_OTHER_TABLE);
		
		BpmFormTable bpmformtable =  bpmFormTableService.getByTableId(bpmFormTable.getTableId(),1);
		List<BpmFormField> bpmformfield =  bpmformtable.getFieldList();
		ArrayList<String> fieldnamelistsher = new ArrayList<String>();
		for(int i = 0;i<bpmformfield.size();i++){
			fieldnamelistsher.add(bpmformfield.get(i).getFieldName());
		}
		return this.getAutoView().addObject("bpmFormTableJSON", JSONObject.fromObject(bpmFormTable))
				.addObject("DataRightsJson",JSONObject.fromObject(bpmDataTemplate))
				.addObject("bpmFormTableIdsher", bpmFormTable.getTableId())
				.addObject("manageField", bpmDataTemplate.getManageField())
				.addObject("bpmDataTemplate",bpmDataTemplate)
				.addObject("fieldnamelistsher",  fieldnamelistsher)
				.addObject("templateAlias", bpmDataTemplate.getTemplateAlias())
				.addObject("templates",templates)
				.addObject("name",name)
				.addObject("id",id);
	}
	
	//重新操作模板
	
	@RequestMapping("editcxcz")
	@Action(description = "编辑业务数据模板")
	public ModelAndView editcxcz(HttpServletRequest request) throws Exception {

		HttpSession session=request.getSession();
		Long tableId =(Long) session.getAttribute("tabid");//RequestUtil.getLong(request, "tableId");
		String formKey =(String) session.getAttribute("foKey");// RequestUtil.getString(request, "formKey");
		String name = RequestUtil.getString(request, "name");
		Long id =(Long) session.getAttribute("id"); //RequestUtil.getLong(request, "id");
		System.out.println("+++ edit ++ Id  :   "+id);		
		System.out.println("+++ save formkey :"+formKey);
		System.out.println("+++ save tableid :"+tableId);
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(tableId,0);
		List<BpmFormTemplate> templates = bpmFormTemplateService.getDataTemplate();
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		session.removeAttribute("id");
		session.removeAttribute("tabid");
		session.removeAttribute("foKey");
		//String name = RequestUtil.getString(request, "name");
		//System.out.println("controller ++ edit name : "+name);
		
		if(BeanUtils.isEmpty(bpmDataTemplate)){
			bpmDataTemplate = new BpmDataTemplate();
			bpmDataTemplate.setFormKey(formKey);
			bpmDataTemplate.setTableId(tableId);
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,""));
			bpmDataTemplate.setName(name);
			bpmDataTemplate.setId(id);
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,""));
		}else{
			Long defId = bpmDataTemplate.getDefId();
			if(BeanUtils.isNotEmpty(defId)){
				BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
				if(BeanUtils.isNotEmpty(bpmDefinition))
					bpmDataTemplate.setSubject(bpmDefinition.getSubject());
			}
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,bpmDataTemplate.getDisplayField()));
			
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,bpmDataTemplate.getExportField()));
		}
		
		bpmDataTemplate.setSource((bpmFormTable.getIsExternal()==BpmFormTable.NOT_EXTERNAL)?BpmDataTemplate.SOURCE_CUSTOM_TABLE:BpmDataTemplate.SOURCE_OTHER_TABLE);
		
		
		return this.getAutoView().addObject("bpmFormTableJSON", JSONObject.fromObject(bpmFormTable))
				.addObject("DataRightsJson",JSONObject.fromObject(bpmDataTemplate))
				.addObject("bpmFormTableIdsher", bpmFormTable.getTableId())
				.addObject("bpmDataTemplate",bpmDataTemplate)
				.addObject("templates",templates)
				.addObject("name",name)
				.addObject("id",id);
	}
	/**
	 * 编辑业务数据模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception\
	 */
	@RequestMapping("editsher")
	@Action(description = "编辑业务数据模板")
	public ModelAndView editsher(HttpServletRequest request) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		String formKey = RequestUtil.getString(request, "formKey");
		Long templateIdsher = Long.parseLong(RequestUtil.getString(request, "templateIdsher"));
		Boolean buttonFlagsher =  RequestUtil.getBoolean(request, "buttonFlagsher",true);
		long defIdsher =RequestUtil.getLong(request,  "defIdsher",0);
		String nodeIdsher =RequestUtil.getString(request, "nodeIdsher");
		BpmDefinition bpmDefinitionsher=bpmDefinitionService.getById(defIdsher);
		String actdefIdsher = bpmDefinitionsher.getActDefId();
		System.out.println("editsher:::::::defIdsher:"+defIdsher+",nodeIdsher:"+nodeIdsher+",buttonFlagsher:"+buttonFlagsher);
		
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(tableId,0);
		List<BpmFormTemplate> templates = bpmFormTemplateService.getDataTemplate();
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(templateIdsher);
		
		if(BeanUtils.isEmpty(bpmDataTemplate)){
			bpmDataTemplate = new BpmDataTemplate();
			bpmDataTemplate.setFormKey(formKey);
			bpmDataTemplate.setTableId(tableId);
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,""));
			
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,""));
		}else{
			Long defId = bpmDataTemplate.getDefId();
			if(BeanUtils.isNotEmpty(defId)){
				BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
				if(BeanUtils.isNotEmpty(bpmDefinition))
					bpmDataTemplate.setSubject(bpmDefinition.getSubject());
			}
			bpmDataTemplate.setDisplayField(this.getDisplayField(bpmFormTable,bpmDataTemplate.getDisplayField()));
			
			bpmDataTemplate.setExportField(this.getExportField(bpmFormTable,bpmDataTemplate.getExportField()));
		}
		bpmDataTemplate.setSource((bpmFormTable.getIsExternal()==BpmFormTable.NOT_EXTERNAL)?BpmDataTemplate.SOURCE_CUSTOM_TABLE:BpmDataTemplate.SOURCE_OTHER_TABLE);
		return this.getAutoView().addObject("bpmFormTableJSON", JSONObject.fromObject(bpmFormTable))
				.addObject("DataRightsJson",JSONObject.fromObject(bpmDataTemplate))
				.addObject("bpmDataTemplate",bpmDataTemplate)
				.addObject("bpmFormTableIdsher", bpmFormTable.getTableId())
				.addObject("templates",templates)
				.addObject("bpmDefinitionsher",bpmDefinitionsher)
				.addObject("manageField", bpmDataTemplate.getManageField())
				.addObject("defIdsher", defIdsher)
				.addObject("actdefIdsher", actdefIdsher)
				.addObject("nodeIdsher", nodeIdsher)
				.addObject("buttonFlagsher",buttonFlagsher);
	}
	/**
	 * 编辑业务数据模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception\
	 */
	@RequestMapping("yucansave")
	@Action(description = "绑定剩余参数")
	public void yucansave(HttpServletRequest request) throws Exception {
		String list = RequestUtil.getString(request, "list");
		System.out.println("listttttttttt"+list);
		String templateIdsher = RequestUtil.getString(request, "templateIdsher");
		Long templateId = Long.parseLong(templateIdsher);
		String snode = RequestUtil.getString(request, "snode");
		Long btnId = RequestUtil.getLong(request, "btnId");
		BpmDataTemplate bpmdatatemplate = bpmDataTemplateService.getById(templateId);
		bpmdatatemplate.setManageField(list);
		bpmDataTemplateService.update(bpmdatatemplate);
		bpmNodeButtonService.getById(btnId).setBtdes(snode);//所绑后续节点
		bpmNodeButtonService.update(bpmNodeButtonService.getById(btnId));
	}
//显示字段	
	private String getDisplayField(BpmFormTable bpmFormTable,
			String displayField) {
		Map<String, String>  map = getDisplayFieldRight(displayField); 
		Map<String, String>  descMap = getDisplayFieldDesc(displayField); 
		if(BeanUtils.isNotEmpty(bpmFormTable)){
			List<BpmFormField> fieldList = bpmFormTable.getFieldList();
			JSONArray jsonAry = new JSONArray();
			for (BpmFormField bpmFormField : fieldList) {
				JSONObject json = new JSONObject();
				json.accumulate("name", bpmFormField.getFieldName());
				String desc =  bpmFormField.getFieldDesc();
				if(BeanUtils.isNotEmpty(map) && map.containsKey(bpmFormField.getFieldName())){
					desc = descMap.get(bpmFormField.getFieldName());
				}
				json.accumulate("desc", desc);
				json.accumulate("type", bpmFormField.getFieldType());
				json.accumulate("style", bpmFormField.getDatefmt());
				String right = "";
				if(BeanUtils.isNotEmpty(map))
					right =  map.get(bpmFormField.getFieldName());
				if(StringUtils.isEmpty(right))
					right = getDefaultDisplayFieldRight();
				
				json.accumulate("right",right);
				jsonAry.add(json);
			}
			displayField =jsonAry.toString();
		}
		System.out.println("高能来了：：：："+displayField);
		return displayField;
	}

	private Map<String, String> getDisplayFieldDesc(String displayField) {
		if(StringUtil.isEmpty(displayField))
			return null;
		Map<String, String>  map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);
		
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			String desc = (String) json.get("desc");
			map.put(name, desc);
		}
		return map;
	}

	private Map<String, String> getDisplayFieldRight(String displayField) {
		if(StringUtil.isEmpty(displayField))
			return null;
		Map<String, String>  map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);
		
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			JSONArray right = (JSONArray) json.get("right");
			map.put(name, right.toString());
		}
		return map;
	}

	private String getDefaultDisplayFieldRight() {
		JSONArray jsonAry = new JSONArray();
		for (int i = 0; i < 2; i++) {
			JSONObject json = new JSONObject();
			json.accumulate("s", i);
			json.accumulate("type", "none");
			json.accumulate("id", "");
			json.accumulate("name", "");
			json.accumulate("script", "");
			jsonAry.add(json);
		}
		return jsonAry.toString();
	}
//====end 显示字段	
//==== 导出字段	
	private String getExportField(BpmFormTable bpmFormTable,
			String field) {
		Map<String, String>  map = getExportFieldRight(field); 
		Map<String, String>  descMap = getExportFieldDesc(field); 
		if(BeanUtils.isEmpty(bpmFormTable))
			return field;
		
		JSONArray jsonAry = new JSONArray();

		List<BpmFormField> fieldList = bpmFormTable.getFieldList();
		jsonAry.add(getTableField(bpmFormTable,fieldList,map,descMap));
		List<BpmFormTable> subTableList = bpmFormTable.getSubTableList();
		for (BpmFormTable subTable : subTableList) {
			jsonAry.add(getTableField(subTable,subTable.getFieldList(),map,descMap));
		}

		return jsonAry.toString();
	}

	private JSONObject getTableField(BpmFormTable bpmFormTable,List<BpmFormField> fieldList, Map<String, String> map, Map<String, String> descMap) {
		JSONObject tableJson = new JSONObject();
		tableJson.element("tableName", bpmFormTable.getTableName());
		tableJson.element("tableDesc", bpmFormTable.getTableDesc());
		tableJson.element("isMain", bpmFormTable.getIsMain());
		JSONArray jsonAry = new JSONArray();
		for (BpmFormField bpmFormField : fieldList) {
			JSONObject json = new JSONObject();
			String key = bpmFormTable.getTableName()+bpmFormField.getFieldName();
			json.element("tableName", bpmFormTable.getTableName());
			json.element("isMain", bpmFormTable.getIsMain());
			json.element("name", bpmFormField.getFieldName());
			String desc =  bpmFormField.getFieldDesc();
			if(BeanUtils.isNotEmpty(map)&& map.containsKey(bpmFormField.getFieldName())){
				desc = descMap.get(key);
			}
			json.element("desc", desc);
			json.element("type", bpmFormField.getFieldType());
			json.element("style", bpmFormField.getDatefmt());
			String right = "";
			if(BeanUtils.isNotEmpty(map))
				right =  map.get(key);
			if(StringUtils.isEmpty(right))
				right = getDefaultExportFieldRight();	
			json.element("right",right);
			jsonAry.add(json);
		}
		tableJson.element("fields", jsonAry.toArray());
		return tableJson;
			
	}

	private Map<String, String> getExportFieldDesc(String field) {
		if(StringUtil.isEmpty(field))
			return null;
		Map<String, String>  map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(field);		
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			JSONArray  fields = (JSONArray) json.get("fields");
			for (Object obj1 : fields) {
				JSONObject json1 = JSONObject.fromObject(obj1);
				String name = (String) json1.get("name");
				String tableName = (String) json1.get("tableName");
				String desc = (String) json1.get("desc");
				map.put(tableName+name, desc);
			}
		}
		return map;
	}

	private Map<String, String> getExportFieldRight(String field) {
		if(StringUtil.isEmpty(field))
			return null;
		Map<String, String>  map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(field);
		
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			JSONArray  fields = (JSONArray) json.get("fields");
			for (Object obj1 : fields) {
				JSONObject json1 = JSONObject.fromObject(obj1);
				String name = (String) json1.get("name");
				String tableName = (String) json1.get("tableName");
				JSONArray right = (JSONArray) json1.get("right");
				map.put(tableName+name, right.toString());
			}
		}
		return map;
	}

	private String getDefaultExportFieldRight() {
		JSONArray jsonAry = new JSONArray();
			JSONObject json = new JSONObject();
			json.accumulate("s", 2);
			json.accumulate("type", "none");
			json.accumulate("id", "");
			json.accumulate("name", "");
			json.accumulate("script", "");
			jsonAry.add(json);
		return jsonAry.toString();
	}	
//==== end 导出字段	
	/**
	 * 取得业务数据模板明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看业务数据模板明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		return getAutoView().addObject("bpmDataTemplate", bpmDataTemplate);
	}
	
	
	/**
	 * 数据列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*
		Long id = RequestUtil.getLong(request,"id");
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		return getAutoView().addObject("bpmDataTemplate", bpmDataTemplate);
	}*/
	@RequestMapping("dataList_{alias}")
	public ModelAndView dataList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="alias") Long alias) throws Exception {
		//取得当前页URL,如有参数则带参数

		System.out.println("editTemplate  alias :"+alias);

		ModelAndView mv=new ModelAndView();

		String url="dataList_" + alias +".ht";
		String toReplaceUrl="getDisplay_" + alias +".ht";
		String __baseURL = request.getRequestURI().replace(url, toReplaceUrl);
		Map<String,Object> params = RequestUtil.getQueryMap(request);
		Map<String,Object> queryParams =RequestUtil.getQueryParams(request);
		//取得传入参数ID
		params.put("__baseURL", __baseURL);
		params.put(BpmDataTemplate.PARAMS_KEY_CTX, request.getContextPath());
		params.put("__tic", "bpmDataTemplate");
		String html = bpmDataTemplateService.getDisplayys(alias, params,queryParams);
		mv.addObject("html", html);
		mv.setViewName("/platform/form/bpmDataTemplatePreview.jsp");
		return mv;
	}
	
	/**
	 * 数据列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("dataListbyKey_{alias}")
	public ModelAndView dataListbyKey(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="alias") Long alias) throws Exception {
		//取得当前页URL,如有参数则带参数
		ModelAndView mv=new ModelAndView();
		String url="dataListbyKey_" + alias +".ht";
		String toReplaceUrl="getDisplaybyKey_" + alias +".ht";
		String __baseURL = request.getRequestURI().replace(url, toReplaceUrl);
		System.out.println("url:::"+url+",toReplaceUrl:::::"+toReplaceUrl+",__baseURL::::"+__baseURL);
		Map<String,Object> params = RequestUtil.getQueryMap(request);
		Map<String,Object> queryParams =RequestUtil.getQueryParams(request);
		//取得传入参数ID
		params.put("__baseURL", __baseURL);
		params.put(BpmDataTemplate.PARAMS_KEY_CTX, request.getContextPath());
		params.put("__tic", "bpmDataTemplate");
		String html = bpmDataTemplateService.getDisplayys(alias, params,queryParams);
		mv.addObject("html", html);
		mv.setViewName("/platform/form/bpmDataTemplatePreview.jsp");
		return mv;
	}
	/**
	 * 预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("preview")
	public ModelAndView preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//取得当前页URL,如有参数则带参数
		String __baseURL = request.getRequestURI().replace("/preview.ht", "/getDisplay.ht");		
		Map<String,Object> params = RequestUtil.getQueryMap(request);
		Map<String,Object> queryParams =RequestUtil.getQueryParams(request);
		//取得传入参数ID
		Long id = RequestUtil.getLong(request,"__displayId__");
		
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		
		
	       //获取条件字段为动态输入的查询条件
	    String json = bpmDataTemplate.getConditionField();
	    JSONArray jsonArr = JSONArray.fromObject(json);
	    List<String> na= new ArrayList<String>();
	    for(int i = 0 ; i<jsonArr.size();i++){
	        if(jsonArr.getJSONObject(i).getString("vf").equals("5")){
	        	na.add(jsonArr.getJSONObject(i).getString("na"));
	        }
	    }   
	    //获取动态查询条件字段
	    String[] str = new String[na.size()] ;
	    for(int i=0;i<na.size();i++){
	        str[i] = RequestUtil.getString(request,na.get(i));
	        System.out.println("参数值 = " + str[i]);
	    }
		params.put("__baseURL", __baseURL);
		params.put(BpmDataTemplate.PARAMS_KEY_CTX, request.getContextPath());
		params.put("__tic", "bpmDataTemplate");
        params.put("str", str);
		String html = bpmDataTemplateService.getDisplay(id.toString(),params,queryParams);
		//String html = bpmDataTemplateService.getDisplay(id, params, queryParams);
		return getAutoView().addObject("html", html);
	}
	
	
	/**
	 * 展示数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getDisplay_{alias}")
	public Map<String,Object> getDisplay(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="alias") String alias) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("success",true);
		try {
			Map<String,Object> params= RequestUtil.getQueryMap(request);
			Map<String,Object> queryParams =RequestUtil.getQueryParams(request);
			
			String __baseURL=request.getRequestURI();
			params.put("__baseURL", __baseURL);
			params.put("__ctx", request.getContextPath());
			params.put("__displayId__",alias);
			params.put(BpmDataTemplate.PARAMS_KEY_FILTERKEY, RequestUtil.getString(request, BpmDataTemplate.PARAMS_KEY_FILTERKEY));
			params.put(BpmDataTemplate.PARAMS_KEY_ISQUERYDATA, true);
			params.put("__tic", "bpmDataTemplate");
			

			String html = bpmDataTemplateService.getDisplay(alias, params,queryParams);
			map.put("html", html);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success",false);
			map.put("msg",e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 编辑模板
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editTemplate")
	public ModelAndView editTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = RequestUtil.getLong(request,"id");
		System.out.println("editTemplate  id :"+id);
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		return getAutoView().addObject("bpmDataTemplate", bpmDataTemplate);
	}
	
	
	/**
	 * 保存模板
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveTemplate")
	public void saveTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String resultMsg="";
		System.out.println("进来了也已！");
		Long id = RequestUtil.getLong(request, "id");
		String templateHtml= RequestUtil.getString(request, "templateHtml");
		
		templateHtml = templateHtml.replace("''", "'");
		BpmDataTemplate bpmDataTemplate = bpmDataTemplateService.getById(id);
		bpmDataTemplate.setTemplateHtml(templateHtml);
		bpmDataTemplateService.update(bpmDataTemplate);
		resultMsg="更新自定义表管理显示模板成功";
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 过滤条件窗口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("filterDialog")
	public ModelAndView filterDialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		BpmFormTable bpmFormTable = bpmDataTemplateService.getFieldListByTableId(tableId);
		String source =(bpmFormTable.getIsExternal()==BpmFormTable.NOT_EXTERNAL)?BpmDataTemplate.SOURCE_CUSTOM_TABLE:BpmDataTemplate.SOURCE_OTHER_TABLE;

		List<CommonVar>  commonVars = CommonVar.geCommonVars();
		return this.getAutoView()
				.addObject("commonVars",commonVars)
				.addObject("bpmFormTable", bpmFormTable)
				.addObject("tableId",tableId)
				.addObject("source",source);
	}
	
	/**
	 * 脚本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("script")
	public ModelAndView script(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		BpmFormTable bpmFormTable = bpmDataTemplateService.getFieldListByTableId(tableId);
		List<CommonVar>  commonVars = CommonVar.geCommonVars();
		String source =(bpmFormTable.getIsExternal()==BpmFormTable.NOT_EXTERNAL)?BpmDataTemplate.SOURCE_CUSTOM_TABLE:BpmDataTemplate.SOURCE_OTHER_TABLE;
			
		return this.getAutoView()
				.addObject("commonVars",commonVars)
				.addObject("bpmFormTable", bpmFormTable)
				.addObject("tableId",tableId)
				.addObject("source",source);
	}
	
	

	/**
	 * 编辑数据。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editData_{alias}")
	@Action(description = "编辑业务数据模板数据",detail="编辑业务数据模板数据")
	public ModelAndView editData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="alias") String alias ) throws Exception {
		
		String pk = RequestUtil.getString(request,"__pk__");
		
		
		boolean hasPk= StringUtil.isNotEmpty(pk);
		String returnUrl = RequestUtil.getPrePage(request);
		String ctxPath = request.getContextPath();
		BpmFormDef bpmFormDef = null;
		Long tableId =0L;
		String tableName="";
		String pkField="";
		SysBusEvent sysBusEvent=null;
		if (StringUtil.isNotEmpty(alias)) {
			alias=bpmDataTemplateService.getById(Long.parseLong(alias)).getFormkey();			
			bpmFormDef = bpmFormDefService.getDefaultVersionByFormKey(alias);		
			tableId = bpmFormDef.getTableId();
			BpmFormTable bpmFormTable= bpmFormTableService.getById(tableId);
			tableName=bpmFormTable.getTableName();
			String html = bpmFormHandlerService.obtainHtml(bpmFormDef, ContextUtil.getCurrentUserId(), pk, "", "#dataTem", "",ctxPath, "",false);
			pkField=bpmFormTable.getPkField();
			bpmFormDef.setHtml(html);
			
			sysBusEvent=sysBusEventService.getByFormKey(alias);
		} 
		ModelAndView mv=new ModelAndView();
		mv.setViewName("/platform/form/bpmDataTemplateEditData.jsp");
		return mv.addObject("bpmFormDef", bpmFormDef)
				.addObject("id", pk)
				.addObject("pkField", pkField)
				.addObject("tableId", tableId)
				.addObject("alias", alias)
				.addObject("tableName", tableName)
				.addObject("returnUrl", returnUrl)
				.addObject("sysBusEvent", sysBusEvent)
				.addObject("hasPk",hasPk);		
	}
	
	/**
	 * 明细数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("detailData_{alias}")
	@Action(description = "查看业务数据模板明细数据",detail="查看业务数据模板明细数据")
	public ModelAndView detailData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="alias") String alias) throws Exception {
		
		String pk = RequestUtil.getString(request,"__pk__");
		alias=bpmDataTemplateService.getById(Long.parseLong(alias)).getFormkey();
		String form= bpmDataTemplateService.getForm(alias,pk);
		ModelAndView mv=new ModelAndView();
		mv.setViewName("/platform/form/bpmDataTemplateDetailData.jsp");
		return mv.addObject("form", form);		
	}
	
	
	/**
	 * 删除数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
//	@Action(description = "删除业务数据模板数据",
//			detail="删除业务数据模板：" +
//					"<#assign entity=bpmDataTemplateService.getById(Long.valueOf(__displayId__))/>" +
//					"【${entity.name}】的数据"
//					)
	@RequestMapping("deleteData_{alias}")
	public void deleteData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="alias")String alias ) throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		alias=bpmDataTemplateService.getById(Long.parseLong(alias)).getFormkey();
		String pk = RequestUtil.getString(request,"__pk__");
		try {
			bpmDataTemplateService.deleteData(alias, pk);

			message = new ResultMessage(ResultMessage.Success, "删除成功!");
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);	
	}
	
	/**
	 * 导出数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportData_{alias}")
	@Action(description = "导出业务数据模板数据",detail="导出业务数据模板数据")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response,@PathVariable(value="alias")String alias ) throws Exception {
		Map<String,Object> params= RequestUtil.getQueryMap(request);
		
		String [] exportIds = RequestUtil.getStringAryByStr(request, "__exportIds__");
		int exportType = RequestUtil.getInt(request, "__exportType__");

		HSSFWorkbook wb = bpmDataTemplateService.export(alias,exportIds,exportType,params);
		String fileName = "DataTemplate_"+  DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
		ExcelUtil.downloadExcel(wb, fileName, response);
	}
	
	/**
	 * 导入业务数据模板数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("importData_{alias}")
	@Action(description = "导入业务数据模板数据",detail="导入业务数据模板数据")
	public ModelAndView importData(HttpServletRequest request,
			HttpServletResponse response,@PathVariable(value="alias")String alias) throws Exception {
		ModelAndView mv=new ModelAndView();
		mv.setViewName("/platform/form/bpmDataTemplateImportData.jsp");
		return mv.addObject("alias", alias);
		
	}
	
	

	/**
	 * 导入数据保存
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importSave")
	@Action(description = "导入数据保存",detail="导入业务数据模板数据")
	public void importSave(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String alias = RequestUtil.getString(request, "alias");
		String importType = RequestUtil.getString(request,"importType");
		MultipartFile file = request.getFile("file");
		ResultMessage message = null;
		try {
			bpmDataTemplateService.importFile(file.getInputStream(),alias,importType);
			message = new ResultMessage(ResultMessage.Success,
					"导入成功!");
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail,
					"导入出错了，请检查导入格式是否正确或者导入的数据是否有问题！");
		}
		writeResultMessage(response.getWriter(), message);
	}
	/**
	 * 选择自定义业务数据模板
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllDataTemplates")
	@ResponseBody
	public List<BpmDataTemplate> getAllDataTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<BpmDataTemplate> list = bpmDataTemplateService.getAll();
		for(BpmDataTemplate bt:list){
			bt.setTemplateHtml("");
		}
		return list;
	}
	
	
	
	@RequestMapping("getCountByName")
	@ResponseBody
	public Integer getCountByName(HttpServletRequest request, HttpServletResponse response){
		String name=RequestUtil.getString(request, "name");
		return bpmDataTemplateService.getCountByName(name);
	}
}