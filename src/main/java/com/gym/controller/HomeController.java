package com.gym.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gym.entity.NhanVien;
import com.gym.entity.TaiKhoan;
import com.gym.service.NhanVienService;
import com.gym.service.TaiKhoanService;

@Controller
public class HomeController {
	@Autowired
	private NhanVienService nhanVienService;
	@Autowired
	private TaiKhoanService taiKhoanService;
	
	
	@RequestMapping("home")
	public String Index() {
		return "introduce/index";
	}
	@RequestMapping("index")
	public String Trangchu() {
		return "introduce/index";
	}
	@RequestMapping("contact")
	public String Contact() {
		return "introduce/contact";
	}
	@RequestMapping("about")
	public String About() {
		return "introduce/about";
	}
	@RequestMapping("blog")
	public String Blog() {
		return "introduce/blog";
	}
	@RequestMapping(value="login",method=RequestMethod.GET)
	public String ShowLogin(HttpSession session) {
		List<NhanVien> nhanViens= nhanVienService.listAll();
		for(NhanVien nhanVien:nhanViens) {
			try {
				if(session.getAttribute("username").equals(nhanVien.getTaiKhoan().getUserName()))
				return "redirect:manager/home";
			}catch(Exception e) {
				return "introduce/login";
			}
		}
		return "introduce/login";
		
	}
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	public String Login( ModelMap modelMap,@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session) {
		List<TaiKhoan> taiKhoans = taiKhoanService.listAll();
		for(TaiKhoan item:taiKhoans){
	        //System.out.println(item.getUserName()+"---"+item.getPassWord()+"\n");
			if(username.equalsIgnoreCase(item.getUserName()) && password.equalsIgnoreCase(item.getPassWord()) && item.getTrangThai()==1 ) {
				session.setAttribute("username", username);
				session.setAttribute("password", password);
				
				session.setAttribute("manv", ""+item.getNhanVien().getMaNV());
				session.setAttribute("tennv", ""+item.getNhanVien().getTenNV());
				session.setAttribute("diachi", ""+item.getNhanVien().getDiaChi());				
				session.setAttribute("email", ""+item.getNhanVien().getEmail());
				session.setAttribute("sdt", ""+item.getNhanVien().getSdt());
				session.setAttribute("gioitinh", ""+item.getNhanVien().getGioiTinh());
				session.setAttribute("chucvu", ""+item.getPhanQuyen().getChucVu());
				session.setAttribute("maQuyen", ""+item.getPhanQuyen().getMaQuyen());
				return "redirect:manager/home";
			}
	    }
		
		modelMap.addAttribute("error", "Sai Username hoặc Password!");
		return "introduce/login";
		
	}
}
