package com.oa.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.oa.pojo.Employee;
import com.oa.pojo.Leavebill;
import com.oa.service.ILeaveBillService;
import com.oa.service.IWorkFlowService;
import com.oa.utils.Constants;



@Controller
public class WorkFlowController {
	
	@Autowired
	private IWorkFlowService iWorkFlowService;
	
	@Autowired
	private ILeaveBillService iLeaveBillService;

	@RequestMapping(value="/deployProcess")
	public String deployProcess(MultipartFile multipartFile,String processName) {
		
		
		try {
			iWorkFlowService.deployNewProcess(multipartFile.getInputStream(), processName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "add_process";
	}
	
	
	@RequestMapping(value="/deployProcessAll")
	public String deployProcessAll(MultipartFile multipartFile,String processName,HttpServletRequest request) {
		
		boolean isZip = false;
		String projectPath = request.getContextPath();
		String orignFileName = multipartFile.getOriginalFilename();
		String suffix = orignFileName.substring(orignFileName.lastIndexOf("."));
		System.out.println("文件后缀名:"+suffix);
		System.out.println("项目路径：:"+projectPath);
		System.out.println("原始文件名:"+orignFileName);
		
		//将流程文件上传到服务器
		try {
			multipartFile.transferTo(new File(projectPath+"\\resources\\diagram\\"+orignFileName));
		} catch (IllegalStateException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if("zip".equals(suffix)) {
			isZip = true;
		}
		
		try {
			iWorkFlowService.deployNewProcess(multipartFile.getInputStream(), processName,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "add_process";
	}
	
	
	
	/**
	 * 添加请假，并启动流程
	 * @param leavebill
	 * @param session
	 * @return
	 */
	
	@RequestMapping(value="/saveStartLeave")
	public String saveStartLeave(Leavebill leavebill,HttpSession session){
		
		leavebill.setLeavedate(new Date());
		leavebill.setState(1); // 状态
		
	   Employee employee = 	(Employee) session.getAttribute(Constants.GLOBLE_USER_SESSION);
	   
	   leavebill.setUserId(employee.getId());
	   
	   
	   this.iLeaveBillService.saveLeaveBill(leavebill);
	   
	   
	   //mybatis 设置插入后立即返回主键
	   //System.out.println("leaveBill.getId():"+leavebill.getId());
	   
	   
	  
	   // 启动流程 leavebillId 用于组合成 bussiness_key  name 用于设置流程变量
	  this.iWorkFlowService.startProcess(leavebill.getId(),employee.getName());
	   
	   return "redirect:/taskList";
	}
	
	
	
	/**
	 * 查看个人待办事项
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/taskList")
	public ModelAndView getTaskListByUserName(HttpSession session) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		Employee  employee = (Employee)session.getAttribute(Constants.GLOBLE_USER_SESSION);
		String userName = employee.getName();
		List<Task> taskList = iWorkFlowService.getTaskListByUserName(userName);
		
		modelAndView.addObject("taskList", taskList);
		modelAndView.setViewName("workflow_task");
		return modelAndView;
		
	}
	
	
	/**
	 * 回显请假信息并推进流程 
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value="/viewTaskForm")
	public ModelAndView viewTaskForm(String taskId) {
		
		
		System.out.println("进入了控制器");
		ModelAndView modelAndView = new ModelAndView();
		
		Leavebill leavebillByTaskId = iWorkFlowService.getLeavebillByTaskId(taskId);
		List<Comment> commentList = iWorkFlowService.getCommentByTaskId(taskId);
		
		
		modelAndView.addObject("bill", leavebillByTaskId);
		modelAndView.addObject("commentList", commentList);
		
		
		modelAndView.addObject("taskId", taskId);
		modelAndView.setViewName("approve_leave");
		return modelAndView;
		
	} 
	
	
	@RequestMapping(value="/submitTaskAndComment")
	public String submitTaskAndComment(String leaveId,String taskId,String comment,HttpSession session) {
		
		Employee employee = (Employee)session.getAttribute(Constants.GLOBLE_USER_SESSION);
		String userName = employee.getName();
		iWorkFlowService.submitTask(Long.parseLong(leaveId), taskId, comment, userName);
		return "redirect:/taskList";
		
	}
	
	@RequestMapping(value = "/getTaskcount")
	public int getTaskcount(HttpSession session) {
		Employee employee = (Employee)session.getAttribute(Constants.GLOBLE_USER_SESSION);
		int count = iWorkFlowService.getTaskCount(employee.getName());
		return count;
		
	}
	
	
	
	
	
}
