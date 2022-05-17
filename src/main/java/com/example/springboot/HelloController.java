package com.example.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@Value("${web.message}")
	private String message;


//	@RequestMapping("/")
//	public String index() {
//		return "Greetings from Spring Boot + Tanzu + WeB fleet Security check change test2!";
//	}

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("message", message);
		return "index";
	}
}