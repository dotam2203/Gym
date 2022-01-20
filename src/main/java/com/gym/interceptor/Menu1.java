package com.gym.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



public class Menu1 implements HandlerInterceptor {
	@Autowired
	
	public boolean preHandle(
			  HttpServletRequest request,
			  HttpServletResponse response, 
			  Object handler) throws Exception {
				List<String> menu1s=new ArrayList<String>();
				//trả về list chức năng trang admin trong file sidebar.jsp
				menu1s.add("home");
				menu1s.add("statistics");
				menu1s.add("information");
				request.setAttribute("menu1s", menu1s);
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
