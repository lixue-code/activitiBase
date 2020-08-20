package com.oa.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oa.pojo.Employee;
import com.oa.service.EmployeeService;
import com.oa.utils.Constants;


@Controller

public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value = "/login")
	public String login(String username, String password, String inputcode,HttpSession session, Model model) {

		String  code = (String)session.getAttribute("code");
		
		//验证码正确则比较用户信息
		if(code.equals(inputcode)) {
			// 根据员工姓名去查询员工信息
			Employee employee = this.employeeService.findEmployeeByName(username);
			if (username.equals(employee.getName())) {
				// 再从查到的用户中取出password 和 用户传入进来的对比
				if (password.equals(employee.getPassword())) {
					// 此处说明已经完全正确，需要将查询到的信息保存到session域中
					session.setAttribute(Constants.GLOBLE_USER_SESSION, employee);

					// 跳转到index
					return "index";

				} else {
					// 账号或者密码不正确
					model.addAttribute("errorMsg", "账号或者密码不正确");
					return "login";

				}
			} else {
				// 账号或者密码不正确
				model.addAttribute("errorMsg", "账号或者密码不正确");
				return "login";
			}
		}else {
			model.addAttribute("errorMsg", "验证码不正确");
			System.out.println("验证码不正确");
			return "login";
		}
		
		
		
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session)
	{


     // 清除session
		session.invalidate();
	 // 重定向到login.jsp
		return "redirect:/login.jsp";
		
	}
}
