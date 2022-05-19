package com.example.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

	final RfaController rfaController;

	@Value("${web.message}")
	private String message;

	public HelloController(RfaController rfaController) {
		this.rfaController = rfaController;
	}

//	@RequestMapping("/")
//	public String index() {
//		return "Greetings from Spring Boot + Tanzu + WeB fleet Security check change test2!";
//	}

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("message", message);
		return "index";
	}

	@PostMapping("/rfa")
	public String uploadRFA(@RequestParam(name="rfaContent", required=false, defaultValue="Some RFA content")String rfaContent) {
		rfaController.uploadRfa(rfaContent);
		return "index";
	}
}