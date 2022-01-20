package com.gym.controller;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadFile {
	@Autowired
	ServletContext servletContext;
	
	@RequestMapping("uploadimage")
	public String UploadImage() {
		
		return "admin/uploadfile";
	}
	
	@RequestMapping(value = "uploadimage", method = RequestMethod.POST)
	public @ResponseBody ModelAndView UploadImage(@RequestParam("image") MultipartFile file) {
		ModelAndView mav= new ModelAndView("admin/uploadfile");
		String name = "a";
		
		String extensionFile = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		//check file khác rỗng và là 1 trong 2 file 
		System.out.print("check "+file.isEmpty());
		System.out.print("check1 "+extensionFile);
		if (!file.isEmpty() && (extensionFile.trim() == "jpg" || extensionFile.trim() == "png") ) {
			
			try {
				byte[] bytes = file.getBytes();
				File dir = new File(servletContext.getRealPath("E:\\upload"));
				
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				file.transferTo(serverFile);
				
				mav.addObject("trangThai", "success");
				return mav;
				
				 } catch (Exception e) {
				  
				  mav.addObject("trangThai", "You failed to upload " + name + " => " +
				  e.getMessage()); return mav; }
				 
		} else {
			
			mav.addObject("trangThai","You failed to upload " + name+ " because the file was empty.");
			return mav;
		}
	}
}
