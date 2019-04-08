package com.imagetracker.controller;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.imagetracker.bean.File;
import com.imagetracker.dto.ConnectionDao;
import com.imagetracker.service.CommonConstants;
import com.imagetracker.service.SystemService;

@RestController
public class SystemController {

	@Autowired
	SystemService service;
	private List<String> asList;
	ConnectionDao dao = new ConnectionDao();

	@RequestMapping(value = "/addFileToSharedFolder", method = RequestMethod.POST)
	public ModelAndView addFileToSharedFolder(@RequestParam("file") MultipartFile file, Model model) {
		String[] validExtension = { "tif", "jpg", "gif", "png" };
		String[] fileName = file.getOriginalFilename().split("\\.");
		ModelAndView mav = new ModelAndView();
		asList = Arrays.asList(validExtension);
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(fileName[0]);
		boolean b = m.find();
		if (asList.contains(fileName[1]) && (!m.find())) {
			service.addFileToSharedFolder(file);
			service.rekognitionImage(file.getOriginalFilename());
			mav.addObject("parameters", CommonConstants.BUCKET_NAME);
			mav.setViewName("display");
			return mav;
		} else {
			mav.addObject("parameters", "File is not Image or File name contains Special Charaters.");
			mav.setViewName("errorPage");
			return mav;
		}

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String passsword,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		ConnectionDao dao = new ConnectionDao();
		int userId = dao.getUserList(dao.RetriveConnection(), username, passsword);
		if (userId!=0) {
			session.setAttribute("userId", userId);
			session.setAttribute("username", username);
			mav.setViewName("home");
		} else {
			mav.addObject("parameters", "Invalid Username/Password");
			mav.setViewName("login");
		}

		return mav;

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(@RequestParam("username") String username,
			@RequestParam("password") String passsword) {
		ModelAndView mav = new ModelAndView();
		
		if (dao.verifyUser(dao.RetriveConnection(), username)) {
			mav.addObject("parameters", "User already exist");
			mav.setViewName("login");

		} else {
			dao.createUser(dao.RetriveConnection(), username, passsword);
			mav.addObject("parameters", "User created Successfully");
			mav.setViewName("login");
		}

		return mav;

	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView logut() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");

		return mav;

	}

	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public ModelAndView loginForm() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;

	}


	@RequestMapping(value = "/regisrationForm", method = RequestMethod.GET)
	public ModelAndView regisrationForm() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("reigster");

		return mav;

	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public ModelAndView home(HttpSession session) {
		//service.getObjDetails();
		List<File> usersImageInfo = dao.getUsersImageInfo(dao.RetriveConnection(),
				String.valueOf(session.getAttribute("username")),
				String.valueOf(session.getAttribute("userId")));
		ModelAndView mav = new ModelAndView();
		//mav.addObject("url",usersImageInfo.)
		mav.setViewName("home");

		return mav;

	}

	/*
	 * @RequestMapping(value = "/displayLables", method = RequestMethod.PUT) publi
	 * 
	 * 
	 * c ModelAndView displayLables(@RequestParam Map<String, String> objModel) {
	 * ModelAndView mav = new ModelAndView(); mav.addObject("parameters",
	 * CommonConstants.BUCKET_NAME); mav.setViewName("display"); return mav; }
	 */
}
