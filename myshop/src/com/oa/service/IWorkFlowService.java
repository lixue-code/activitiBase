package com.oa.service;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.oa.pojo.Leavebill;

public interface IWorkFlowService {

	public void deployNewProcess(InputStream inputStream,String processName);
	
	/**
	 * 部署 bpmn 和png zip 文件
	 * @param inputStream
	 * @param processName
	 * @param isZip
	 */
	public void deployNewProcess(InputStream inputStream,String processName,boolean isZip);
	
	
	/**
	 * 根据用户名查询待办事项
	 * @param userName
	 * @return
	 */
	public List<Task> getTaskListByUserName(String userName);
	
	
	/**
	 * 启动流程
	 * @param userName
	 */
	public void startProcess(Long leaveId,String userName);
	
	
	/**
	 * 根据任务id获取 请假实体
	 * @param taskId
	 * @return
	 */
	 public Leavebill getLeavebillByTaskId(String taskId) ;
	 
	 
	 
	 /**
	  * 根据任务id获取批注信息
	  * @param taskId
	  * @return
	  */
	 public List<Comment> getCommentByTaskId(String taskId);
	 
	 
	 /**
	  * 提交批注信息并完成当前任务
	  * @param leaveId
	  * @param taskId
	  * @param comment
	  * @param userName
	  * @return
	  */
	 public void submitTask(long leaveId,String taskId,String comment,String userName);

	 
	 /**
	  * 获取待办任务数量
	  * @param userName
	  * @return
	  */
	public int getTaskCount(String userName);
		
	
}
