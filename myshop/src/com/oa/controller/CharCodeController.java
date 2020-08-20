package com.oa.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;

@Controller
@SessionAttributes("code")
public class CharCodeController {

	
	@RequestMapping(value="/getCharCode")
	
	public void getCharCode(HttpServletResponse response,Model model) {
		
		System.out.println("进入了验证码控制器");
		
		ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 60, 4, 4);
		
		String code = captcha.getCode();
		
		try {
			captcha.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("生成的验证码:"+code);
		
		//将code存放到session域中
		model.addAttribute("code",code);
		
		
	}
}
