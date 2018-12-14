package com.javalec.board.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.javalec.board.dao.BDao;

public class BDeleteCommand implements BCommand{

	@Override
	public void execute(Model model) {
		Map<String, Object> map=model.asMap();
		HttpServletRequest request=(HttpServletRequest)map.get("request");
		String bId=request.getParameter("bId");
		BDao dao=new BDao();
		dao.delete(bId);
	}

}
