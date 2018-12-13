package com.javalec.board.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javalec.board.command.BCommand;
import com.javalec.board.command.BContentCommand;
import com.javalec.board.command.BDeleteCommand;
import com.javalec.board.command.BListCommand;
import com.javalec.board.command.BModifyCommand;
import com.javalec.board.command.BReplyCommand;
import com.javalec.board.command.BReplyViewCommand;
import com.javalec.board.command.BWriteCommand;

@Controller
public class BController {
	BCommand command=null;

	@RequestMapping("/list")
	public String list(Model model){
		System.out.println("list()");
		command=new BListCommand();
		command.execute(model);
		return "list";	//goto view page
	}

	@RequestMapping("/write_view")			//write form
	public String write_view(Model model){
		System.out.println("write_view()");
		return "write_view";
	}

	@RequestMapping("/write")	//form post 처리를 위해 HttpSevletRequest필요
	public String write(HttpServletRequest request,Model model){
		System.out.println("write()");
		model.addAttribute("request",request);
		command=new BWriteCommand();
		command.execute(model);
		return "redirect:list";
	}

	@RequestMapping("/content_view")
	public String content_view(HttpServletRequest request,Model model){
		System.out.println("content_view()");
		model.addAttribute("request",request);
		command=new BContentCommand();
		command.execute(model);
		return "content_view";
	}

	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public String modify(HttpServletRequest request,Model model){
		System.out.println("modify()");
		model.addAttribute("request",request);
		command=new BModifyCommand();
		command.execute(model);
		return "redirect:list";
	}
	
	@RequestMapping("/reply_view")
	public String reply_view(HttpServletRequest request,Model model){
		System.out.println("reply_view()");
		model.addAttribute("request",request);
		command=new BReplyViewCommand();
		command.execute(model);
		return "reply_view";
	}
	
	@RequestMapping("/reply")
	public String reply(HttpServletRequest request,Model model){
		System.out.println("reply()");
		model.addAttribute("request",request);
		command=new BReplyCommand(); 
		return "redirect:list";
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request,Model model){
		System.out.println("delete()");
		model.addAttribute("request",request);
		command=new BDeleteCommand();
		command.execute(model);
		return "redirect:list";
	}
}
