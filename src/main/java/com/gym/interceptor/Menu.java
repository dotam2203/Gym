package com.gym.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



public class Menu implements HandlerInterceptor {
	
	public boolean preHandle(
	  HttpServletRequest request,
	  HttpServletResponse response, 
	  Object handler) throws Exception {
		List<String> menus=new ArrayList<String>();
		//trả về list chức năng Trang chủ trong file navbar.jsp
		menus.add("home");
		menus.add("contact");
		menus.add("about");
		
		menus.add("blog");
		menus.add("login");
		System.out.println("Add oke");
		 request.setAttribute("menus", menus);
		 return true;
	}
	public void postHandle(
	  HttpServletRequest request, 
	  HttpServletResponse response,
	  Object handler, 
	  ModelAndView modelAndView) throws Exception {
	    // your code
	}
	public void afterCompletion(
	  HttpServletRequest request, 
	  HttpServletResponse response,
	  Object handler, Exception ex) {
	    // your code
	}
}
