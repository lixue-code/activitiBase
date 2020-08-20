package com.oa.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.oa.pojo.Employee;
import com.oa.service.EmployeeService;
import com.oa.utils.Constants;

public class MyTaskListener implements TaskListener{

	@Override
	public void notify(DelegateTask delegateTask) {
		
		//获取IOC容器
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		//获取EmployeeServiceImpl
		EmployeeService employeeService = applicationContext.getBean("employeeServiceImpl",EmployeeService.class);
		
		//获取request
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();
		
		//获取session
		HttpSession session = request.getSession();
		//获取当前待办人
		Employee employee = (Employee)session.getAttribute(Constants.GLOBLE_USER_SESSION);

		//获取上一级
		Employee manager = employeeService.findEmployeeManagerByManagerId(employee.getManagerId());
		
		//设置代办人
		delegateTask.setAssignee(manager.getName());
	}

}
