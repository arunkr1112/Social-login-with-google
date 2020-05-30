package com.arun.social.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.arun.social.model.UserInfo;
import com.arun.social.service.GoogleService;


@Controller
public class GoogleController {
	
	
	@Autowired
	private GoogleService googleService;
	
	// Temp
	private String accessToken;
	
	

	@GetMapping(value = "/")
	public String home() {
		
		return "view/home";
	}
	
	
	@GetMapping(value = "/googlelogin")
	public RedirectView googlelogin() {
		
		RedirectView redirectView = new RedirectView();
		
		String url = googleService.googlelogin();
		
		System.err.println("Url here: " +url);
		redirectView.setUrl(url);
		return redirectView;
		
	}
	
	@GetMapping(value = "/google")
	public String google(@RequestParam("code") String code) {
		
		String accessToken = googleService.getGoogleAccessToken(code);
		
		return "redirect:/googleprofiledata/" + accessToken;
		
	}
	
	@GetMapping(value = "/googleprofiledata/{accessToken:.+}")
	public String googleprofiledata(@PathVariable String accessToken , Model model) {

		return "view/loggedin";
	}
	
	@GetMapping(value = "/userprofile")
	public String userProfile(Model model) {
		
		Person user = googleService.getGoogleUserProfile(accessToken);
		
		UserInfo userInfo = new UserInfo(user.getGivenName(), user.getFamilyName(), user.getImageUrl());
		model.addAttribute("user", userInfo);
		
		return "view/userprofile";
	}

}


