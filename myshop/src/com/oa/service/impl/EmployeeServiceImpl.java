package com.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.mapper.EmployeeMapper;
import com.oa.pojo.Employee;
import com.oa.pojo.EmployeeExample;
import com.oa.pojo.EmployeeExample.Criteria;
import com.oa.service.EmployeeService;



@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Override
	public Employee findEmployeeByName(String name) {
		
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Employee> list = this.employeeMapper.selectByExample(example);
		
		if(list!=null &&list.size()>0)
		{
		  return list.get(0);
		}
		
		return null;
	}

	@Override
	public Employee findEmployeeManagerByManagerId(long manageId) {
		
		return this.employeeMapper.selectByPrimaryKey(manageId);
	}

}
