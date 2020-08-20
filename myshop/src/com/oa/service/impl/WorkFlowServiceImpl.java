package com.oa.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.mapper.LeavebillMapper;
import com.oa.pojo.Leavebill;
import com.oa.service.IWorkFlowService;
import com.oa.utils.Constants;

@Service
public class WorkFlowServiceImpl implements IWorkFlowService {
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private LeavebillMapper leavebillMapper;

	@Override
	public void deployNewProcess(InputStream inputStream, String processName) {
		
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		//部署流程
		this.repositoryService
		.createDeployment()
		.name(processName)
		.addZipInputStream(zipInputStream)
		.deploy();

	}

	@Override
	public void deployNewProcess(InputStream inputStream, String processName, boolean isZip) {
		if(isZip) {
			ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			//部署流程
			this.repositoryService
			.createDeployment()
			.name(processName)
			.addZipInputStream(zipInputStream)
			.deploy();
		}
		
		//普通文件的方式
		this.repositoryService
		.createDeployment()
		.name(processName)
		.addInputStream("", inputStream)
		.deploy();
	}

	
	

	@Override
	public void startProcess(Long leaveId,String userName) {
		
		
		String process_key = Constants.Leave_KEY;
		
		//设置流程变量
		Map<String, Object>map = new HashMap<String,Object>();
		map.put("userName", userName);
		
		// 设置Bussiness_key的规则
		String BUSSINESS_KEY = process_key+"."+leaveId;
		
		map.put("objId", BUSSINESS_KEY);
		
		
		this.runtimeService
		.startProcessInstanceByKey(process_key,BUSSINESS_KEY,map);
		
	}
	
	
	
		
	
	
	/**
	 * 查询用户待办事项
	 */
	@Override
	public List<Task> getTaskListByUserName(String userName) {
		List<Task> list = this.taskService
		.createTaskQuery()
		.taskAssignee(userName)
		.orderByTaskCreateTime()
		.desc()
		.list();
		// TODO Auto-generated method stub
		return list;
	}

	/**
	 * 根据任务id获取请假实例
	 */
	@Override
	public Leavebill getLeavebillByTaskId(String taskId) {
		//获取任务
		Task task = taskService.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
		.processInstanceId(task.getProcessInstanceId())
		.singleResult();
		
		
		String businessKey = processInstance.getBusinessKey();
		
		String leaveId = "";
		if(businessKey!=null && ! "".equals(businessKey)) {
			leaveId = businessKey.split("\\.")[1];
		}
		
		Leavebill leavebill = leavebillMapper.selectByPrimaryKey(Long.parseLong(leaveId));
		
		
		
		// TODO Auto-generated method stub
		return leavebill;
	}

	/**
	 * 根据任务id获取批注信息
	 * task获取流程实例id
	 * taskService获取流程批注
	 */
	@Override
	public List<Comment> getCommentByTaskId(String taskId) {
		
		Task task = this.taskService.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		
		List<Comment> processInstanceComments = this.taskService.getProcessInstanceComments(task.getProcessInstanceId());
		
		return processInstanceComments;
	}

	
	/**
	 * 提交并完成当前的任务
	 */
	@Override
	public void submitTask(long leaveId, String taskId, String comment, String userName) {
		
		//获取任务task
		Task task = this.taskService
		.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		
		String processInstanceId = task.getProcessInstanceId();
		//添加批注的审核人
		//批注的审核人为当前用户
		Authentication.setAuthenticatedUserId(userName);
		
		//添加批注
		this.taskService.addComment(taskId,processInstanceId , comment);
		
		//完成当前节点
		taskService.complete(taskId);
		
		ProcessInstance processInstance = this.runtimeService
		.createProcessInstanceQuery()
		.processInstanceId(processInstanceId)
		.singleResult();
		
		//判断流程是否结束
		if(processInstance == null) {
			//流程结束则将请假表的状态改成2
			Leavebill leavebill = leavebillMapper.selectByPrimaryKey(leaveId);
			leavebill.setState(2);
			leavebillMapper.updateByPrimaryKey(leavebill);
			
		}
		
		
	}

	
	/**
	 * 获取待办任务数量
	 */
	@Override
	public int getTaskCount(String userName) {
		List<Task> list = this.taskService
				.createTaskQuery()
				.taskAssignee(userName)
				.list();
				// TODO Auto-generated method stub
				return list.size();
	}
	
	
	
	
	
	
	
	 
	
	
	
	
	

}
