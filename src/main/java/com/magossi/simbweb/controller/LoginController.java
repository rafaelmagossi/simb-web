package com.magossi.simbweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping
	public String login() {
		return "/login";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String loginAceito() {
		return "/produto/PesquisaProdutos";
	}
	

}
