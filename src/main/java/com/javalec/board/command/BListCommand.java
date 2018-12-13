package com.javalec.board.command;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.javalec.board.dao.BDao;
import com.javalec.board.dto.BDto;

public class BListCommand implements BCommand{

	@Override
	public void execute(Model model) {
		BDao dao=new BDao();
		ArrayList<BDto> dtos=dao.list();
		
		model.addAttribute("list",dtos);//list.jsp에 list이름으로 dtos를 넘겨줌
	}
}
