package com.oa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.mapper.LeavebillMapper;
import com.oa.pojo.Leavebill;
import com.oa.service.ILeaveBillService;

@Service
public class LeaveBillServiceImpl implements ILeaveBillService {
	
	@Autowired
	private LeavebillMapper leavebillMapper;

	@Override
	public void saveLeaveBill(Leavebill leavebill) {
	
		//添加请假信息
		leavebillMapper.insert(leavebill);
	}

}
