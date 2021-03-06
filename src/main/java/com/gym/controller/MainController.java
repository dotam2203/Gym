package com.gym.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gym.entity.GoiTap;
import com.gym.entity.HoaDon;
import com.gym.entity.KhachHang;
import com.gym.entity.LopDV;
import com.gym.entity.NhanVien;
import com.gym.entity.PhanQuyen;
import com.gym.entity.TaiKhoan;
import com.gym.entity.The;
import com.gym.service.KhachHangService;
import com.gym.service.GoiTapService;
import com.gym.service.HoaDonService;
import com.gym.service.LopDVService;
import com.gym.service.NhanVienService;
import com.gym.service.PhanQuyenService;
import com.gym.service.TaiKhoanService;
import com.gym.service.TheService;

@Controller
@RequestMapping("manager")
public class MainController {
	@Autowired
	LopDVService lopDVService;
	@Autowired
	GoiTapService goiTapService;
	@Autowired
	TheService theService;
	@Autowired
	KhachHangService khachHangService;
	@Autowired
	NhanVienService nhanVienService;
	@Autowired
	HoaDonService hoaDonService;
	@Autowired
	TaiKhoanService taiKhoanService;
	@Autowired
	PhanQuyenService phanQuyenService;
	@Autowired
	ServletContext servletContext;
	@Autowired
	JavaMailSender javaMailSender;
	
	/*
	 * ==================================================== C???P QUY???N: Nh??n Vi??n ch??? c?? quy???n ??? m???c ????NG K?? KH??CH H??NG, H??A ????N, KH??CH H??NG, Qu???n L?? th?? full quy???n
	 * ===================================================
	 */
	/*
	 * ==================================================== TRANG CH???
	 * ===================================================
	 */
	//==============Show th??ng tin t??i kho???n nh??n vi??n ????ng nh???p
	@RequestMapping("taikhoan")
	public ModelAndView showTKDN() {
		ModelAndView mw = new ModelAndView("admin/taikhoan");
		return mw;
	}
	// ============= ????ng nh???p v??o trang ch???
	@RequestMapping("home")
	public ModelAndView TrangChu(HttpServletResponse response, HttpSession session) throws IOException {
		ModelAndView mw = new ModelAndView("admin/trangchu");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}
		List <The> thes = theService.selectByTrangThai("Ch??a Thanh To??n");
		LocalDate localDate = LocalDate.now();
	      String date = "" + localDate;
	      String[] dates = date.split("-");
		
		
		String namHienTai = dates[0];
		String thangHienTai = dates[1];
		float[] danhThuT = {0,0,0,0,0,0,0,0,0,0,0,0};//m???ng ch???a 12 th??ng
		float[] doanhThuTCN = {0,0,0};
		int[] soLuongDV = {0,0,0,0};//4 lo???i dv ch??nh
		int[] soLuongKHT = {0,0,0,0,0,0,0,0,0,0,0,0};
		
		
		String[] fieldBDNCN = {"","",""};
		
		int i;
		List <HoaDon> hoaDons;
		for(i = 1; i <= 12; i++) {
			//System.out.println("sodem= "+i);
			if(String.valueOf(i).length() == 1)
				hoaDons = hoaDonService.selectByYear("" + namHienTai + "-0" + i + "-");

			else
				hoaDons = hoaDonService.selectByYear("" + namHienTai + "-" + i + "-");
			
			if(hoaDons.isEmpty()) 
				continue;
				 
			//hoaDons.forEach(hoaDon-> danhThuT[i] += hoaDon.getThehd().getGoiTap().getGia());
			int a= 0 ;
			List<String> sLKHs = new ArrayList<>(); // t???o danh s??ch KH ???o
			List<String> sLKHs1 = new ArrayList<>();// list remove duplicate
			while(true) {
				try {
					//C???ng t???ng ti???n thu ???????c trong th??ng
					danhThuT[i-1]+=hoaDons.get(a).getThehd().getGoiTap().getGia();
					sLKHs.add(hoaDons.get(a).getThehd().getKhachHang().getMaKH());
					
					a++;
				}
				catch (Exception e){
					break;
				}
			}
				// duplicate ?????m s??? l?????ng kh
				Set<String> store = new HashSet<>();
				
		        for (String sLKH : sLKHs) {
		            if (!store.add(sLKH) == false) {
		            	sLKHs1.add(""+sLKH);
		            }
		        }
		        // ?????m s??? l?????ng
		        soLuongKHT[i-1] = sLKHs1.size();
			
		}
		
		//============== TH???NG K?? DOANH THU ============= 
		float maxDT = danhThuT[0];
		int maxIndexDT = 0;
        for ( i = 1; i < danhThuT.length; i++) {
            if (danhThuT[i] > maxDT) {  
            	maxDT = danhThuT[i];
            	maxIndexDT = i; 
            }
        }
        
        if(maxIndexDT == 0) {
        	doanhThuTCN[0] = danhThuT[maxIndexDT];
        	doanhThuTCN[1] = danhThuT[maxIndexDT + 1];
        	doanhThuTCN[2] = danhThuT[maxIndexDT + 2];
        	fieldBDNCN[0] = "'Th??ng  " + (maxIndexDT + 1) + "'";
        	fieldBDNCN[1] = "'Th??ng  " + (maxIndexDT + 2) + "'";
        	fieldBDNCN[2] = "'Th??ng  " + (maxIndexDT + 3) + "'";
        	
        }
        else if(maxIndexDT == 11) {
        	doanhThuTCN[0] = danhThuT[maxIndexDT - 2];
        	doanhThuTCN[1] = danhThuT[maxIndexDT - 1];
        	doanhThuTCN[2] = danhThuT[maxIndexDT];
        	fieldBDNCN[0] = "'Th??ng  "+ (maxIndexDT + 1) + "'";
        	fieldBDNCN[1] = "'Th??ng  "+ (maxIndexDT) + "'";
        	fieldBDNCN[2] = "'Th??ng  "+ (maxIndexDT-1) + "'";
        }else {
        	doanhThuTCN[0] = danhThuT[maxIndexDT - 1];
        	doanhThuTCN[1] = danhThuT[maxIndexDT];
        	doanhThuTCN[2] = danhThuT[maxIndexDT + 1];
        	fieldBDNCN[0] = "'Th??ng  "+(maxIndexDT) + "'";
        	fieldBDNCN[1] = "'Th??ng  "+(maxIndexDT + 1) + "'";
        	fieldBDNCN[2] = "'Th??ng  "+(maxIndexDT + 2) + "'";
        }
        
        
        //=============== TH???NG K?? D???CH V??? TRONG TH??NG ================
        List <HoaDon> hoaDons1;
        //X??t th??ng <10 => 0X
        if(String.valueOf(thangHienTai).length() == 1) {
        	
        	hoaDons1=hoaDonService.selectByYear("" + namHienTai + "-0" + thangHienTai + "-");
        	System.out.println("NGAY HIEN TAI = " + namHienTai + "-0" + thangHienTai + "-");
        } 
        else{
        	hoaDons1=hoaDonService.selectByYear("" + namHienTai + "-" + thangHienTai + "-");
        	System.out.println("NGAY HIEN TAI = " + namHienTai  + "-" + thangHienTai + "-");
        }
        if(!hoaDons1.isEmpty()) {
        	System.out.println("test-ten-lop=" + hoaDons1.get(0).getThehd().getGoiTap().getLopDV().getTenLop());
        }
     
        for(i=0;;i++) {
        	try {
	        	if(hoaDons1.get(i).getThehd().getGoiTap().getLopDV().getTenLop().equals("Aerobic")) 
	        		soLuongDV[0] += 1;
	        		
	        	else if(hoaDons1.get(i).getThehd().getGoiTap().getLopDV().getTenLop().equals("Boxing")) 
	        		soLuongDV[1] += 1;
	        		
	        	else if(hoaDons1.get(i).getThehd().getGoiTap().getLopDV().getTenLop().equals("Fitness")) 
	        		soLuongDV[2] += 1;
	        		
	        	else if(hoaDons1.get(i).getThehd().getGoiTap().getLopDV().getTenLop().equals("Yoga")) 
        		soLuongDV[3] += 1;
        	}
        	catch(Exception e) {
        		break;
        	}
        }
        
        int tongDV = 0;
        for ( i = 0; i < 4 ; i++)
            tongDV += soLuongDV[i];
        
        
		//=========== TH???NG K?? TOP 5 KH??CH H??NG =========
        List<HoaDon> top5KHs = hoaDonService.findBetweenNamSortGiaTien(namHienTai);     
        List<String> khachHangs = new ArrayList<>();
        List<KhachHang> top5KHTiemNang =new ArrayList<>();
        
        for (int ig = 0; ig < top5KHs.size(); ig++) {
        	String maKHTop = top5KHs.get(ig).getThehd().getKhachHang().getMaKH();
        	float tien = top5KHs.get(ig).getThehd().getGoiTap().getGia();
            for (int j = ig + 1 ; j < top5KHs.size(); j++) {
            	 int flag = 0;
            	 for (String khachHang1 : khachHangs) {
            		 if(khachHang1.split("-")[0].equals(top5KHs.get(j).getThehd().getKhachHang().getMaKH())) { 
            			 flag = 1;
            			 break;
            		 }
            	 }
                 if (top5KHs.get(ig).getThehd().getKhachHang().getMaKH().equals(top5KHs.get(j).getThehd().getKhachHang().getMaKH()) && flag !=1  ) {
                	 maKHTop=top5KHs.get(ig).getThehd().getKhachHang().getMaKH();
                	 tien+=top5KHs.get(j).getThehd().getGoiTap().getGia();
                 }
            }
            int flag = 0;
            for (String khachHang1 : khachHangs) {
       		 	if(khachHang1.split("-")[0].equals(maKHTop)) { 
       		 		flag=1;
       		 		break;
       		 	}
       	 	} 
            if(flag == 0) khachHangs.add("" + maKHTop + "-"+ tien);
        }
        
        //S???p x???p kh??ch H??ng gi???m d???n theo th???i gian
        String time;
        for(int ig = 0; ig < khachHangs.size(); ig++){
            for(int j = ig + 1; j < khachHangs.size(); j++){
                if(Float.parseFloat(khachHangs.get(ig).split("-")[1]) < Float.parseFloat(khachHangs.get(j).split("-")[1])){
                    // Hoan vi 2 so a[i] va a[j]
                	time = khachHangs.get(ig);
                    khachHangs.set(ig,khachHangs.get(j));
                    khachHangs.set(j, time);        
                }
            }
        }
        
        //L???y 5 kh??ch h??ng vip nh???t th??ng
        try {
	        for( int id = 0; id < 5; id++) {
	        	List <KhachHang> KH = khachHangService.selectByMaKH(khachHangs.get(id).split("-")[0]);
	        	top5KHTiemNang.add(KH.get(0));
	        }
        }
        catch(Exception e) {}
		//============================ L???y d??? li???u v??o file trangchu.jsp
		mw.addObject("thes_wtt", thes);
		mw.addObject("danhThuN", Arrays.toString(danhThuT));
		mw.addObject("doanhThuTCN", Arrays.toString(doanhThuTCN));
		mw.addObject("fieldBDNCN", Arrays.toString(fieldBDNCN));
		
		mw.addObject("bdDVT", Arrays.toString(soLuongDV));
		mw.addObject("bdKHN", Arrays.toString(soLuongKHT));
		mw.addObject("maxDT", maxDT);
		mw.addObject("tongDV", tongDV);
		mw.addObject("top5KHTiemNang", top5KHTiemNang);
		
		return mw;
	}

	// ============= ????ng xu???t kh???i trang ch???
	@RequestMapping("logout")
	public String DangXuat(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		// X??a session
		session.removeAttribute("username");
		// X??a cookie
		// System.out.println(request.getCookies().toString());

		for (Cookie ck : request.getCookies()) {
			if (ck.getName().equalsIgnoreCase("SESSIONID")) {
				ck.setMaxAge(0);
				response.addCookie(ck);
			}
		}

		return "redirect:../home";
	}

	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== D???CH V???
	 * ===================================================
	 */
	// ======== Danh s??ch D???ch V??? File sidebar.jsp tr??? v??? file lopdv.jsp
	@RequestMapping("lopdv")
	public ModelAndView ListDichVu(HttpSession session, HttpServletResponse response) throws IOException {
		ModelAndView mw = new ModelAndView("admin/lopdv");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}
		
		List<LopDV> lopDVServices = lopDVService.listAll();
		mw.addObject("lopDVServices", lopDVServices);
		
		return mw;
		
		
	}

	// ============= Th??m D???ch V??? khi nh???n onclick = them File lopdv.jsp
	@RequestMapping(value = "lopdv", method = RequestMethod.POST)
	public ModelAndView ThemDichVu(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws IOException {

		ModelAndView mw = new ModelAndView("admin/lopdv");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		Enumeration<String> parameterNames = request.getParameterNames();
		List<String> paramNames = new ArrayList<>();
		int[] flagParams = { 0, 0, 0, 0, 0 }; // format {ten,goi1,goi2,goi3,goi4}

		// x??c ?????nh c??c gi?? tr??? g???i l??n t??? browser
		// li???t k?? h???t th?? tr??? v??? false, ch??a h???t th?? true
		while (parameterNames.hasMoreElements()) {
			try {
				String parameterName = parameterNames.nextElement();
				if (parameterName.equals("tenlop") && !request.getParameter("tenlop").trim().isEmpty()) {
					flagParams[0] = 1;

				}
				if (parameterName.trim().equals("ngay") && !request.getParameter("giangay").trim().isEmpty()) {
					flagParams[1] = 2;

				}

				if (parameterName.trim().equals("tuan") && !request.getParameter("giatuan").trim().isEmpty()) {
					flagParams[2] = 2;

				}

				if (parameterName.trim().equals("thang") && !request.getParameter("giathang").trim().isEmpty()) {
					flagParams[3] = 2;

				}

				if (parameterName.trim().equals("nam") && !request.getParameter("gianam").trim().isEmpty()) {
					flagParams[4] = 2;

				}

			} catch (Exception e) {
				flagParams[0] = 0;

			}

		}
		// check gi?? ti???n l?? s???
		try {

			String[] arrs = { "giangay", "giatuan", "giathang", "gianam" };
			for (String arr : arrs) {
				if (!request.getParameter(arr).trim().isEmpty()) {
					Float.parseFloat(request.getParameter(arr).trim());
				}

			}

		} catch (Exception e) {
			flagParams[0] = 0;
		}

		List<LopDV> checkTenLop = lopDVService.selectByTenLop(request.getParameter("tenlop").trim());
		if (flagParams[0] == 1 && checkTenLop.size() == 0) {

			// l???y 2 k?? t??? c???a m?? l???p
			String prefixMaLop = request.getParameter("tenlop").trim().substring(0, 1) + request.getParameter("tenlop")
					.trim().substring(request.getParameter("tenlop").trim().length() - 1);

			List<LopDV> lopDVServices = lopDVService.listAll();
			// ============== T??? ?????ng l???y m?? l???p =============
			String maLop = "";

			int maxID = 0;

			try {
				maxID = Integer.parseInt(lopDVServices.get(0).getMaLop().substring(2));
				for (LopDV maLops : lopDVServices) {
					if (Integer.parseInt(maLops.getMaLop().substring(2)) > maxID) {
						maxID = Integer.parseInt(maLops.getMaLop().substring(2));
					}
				}
				maLop = prefixMaLop + (maxID + 1);
			} catch (Exception e) {

				maLop = prefixMaLop + 1;
			}
			maLop = maLop.toUpperCase();

			// ================ Th??m l???p dv file lopdv.jsp btn_Th??m
			LopDV lopDV = new LopDV();
			lopDV.setMaLop(maLop);
			lopDV.setTenLop(request.getParameter("tenlop").trim());
			lopDV.setTrangThai(1);
			lopDVService.save(lopDV);

			// them g??i t???p n???u c??
			if (flagParams[1] == 2) {
				GoiTap goiTapNgay = new GoiTap();
				// format MaGoiTap + MaLop + TenGoiTap + ThoiHan + Gia
				goiTapNgay.setMaGoiTap("GTNG" + maLop.substring(2));
				goiTapNgay.setLopDV(lopDV);
				goiTapNgay.setTenGoiTap("ng??y");
				goiTapNgay.setThoiHan(1);
				goiTapNgay.setTrangThai(1);
				goiTapNgay.setGia(Float.parseFloat(request.getParameter("giangay")));
				goiTapService.save(goiTapNgay);
			}
			if (flagParams[2] == 2) {
				GoiTap goiTapTuan = new GoiTap();
				// format MaGoiTap + MaLop + TenGoiTap + ThoiHan + Gia
				goiTapTuan.setMaGoiTap("GTT" + maLop.substring(2));
				goiTapTuan.setLopDV(lopDV);
				goiTapTuan.setTenGoiTap("tu???n");
				goiTapTuan.setThoiHan(7);
				goiTapTuan.setTrangThai(1);
				goiTapTuan.setGia(Float.parseFloat(request.getParameter("giatuan")));
				goiTapService.save(goiTapTuan);
			}
			if (flagParams[3] == 2) {
				GoiTap goiTapThang = new GoiTap();
				// format MaGoiTap + MaLop + TenGoiTap + ThoiHan + Gia
				goiTapThang.setMaGoiTap("GTTH" + maLop.substring(2));
				goiTapThang.setLopDV(lopDV);
				goiTapThang.setTenGoiTap("th??ng");
				goiTapThang.setThoiHan(30);
				goiTapThang.setTrangThai(1);
				goiTapThang.setGia(Float.parseFloat(request.getParameter("giathang")));
				goiTapService.save(goiTapThang);
			}
			if (flagParams[4] == 2) {
				GoiTap goiTapNam = new GoiTap();
				// format MaGoiTap + MaLop + TenGoiTap + ThoiHan + Gia
				goiTapNam.setMaGoiTap("GTN" + maLop.substring(2));
				goiTapNam.setLopDV(lopDV);
				goiTapNam.setTenGoiTap("n??m");
				goiTapNam.setThoiHan(365);
				goiTapNam.setTrangThai(1);
				goiTapNam.setGia(Float.parseFloat(request.getParameter("gianam")));
				goiTapService.save(goiTapNam);
			}
			mw.addObject("thongbao", "1");
		} else
			mw.addObject("thongbao", "0");

		List<LopDV> lopDVServices = lopDVService.listAll();
		mw.addObject("lopDVServices", lopDVServices);
		return mw;
	}

	// ============= Th??m g??i t???p goitap?id file lopdv
	@RequestMapping(value = "goitap", params = { "id" }, method = RequestMethod.GET)
	public ModelAndView ThemGoiTapDV(@RequestParam("id") String maLop, HttpSession session,
			HttpServletResponse response) throws IOException {

		ModelAndView mw = new ModelAndView("admin/goitap");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		List<LopDV> checkMaLop = lopDVService.selectByMaLop(maLop);
		if (checkMaLop.size() > 0) {
			List<GoiTap> goiTapServices = goiTapService.selectByMaLop(maLop);
			List<LopDV> lopDVs = lopDVService.selectByMaLop(maLop);
			mw.addObject("lopDVs", lopDVs);
			mw.addObject("goiTapServices", goiTapServices);

			return mw;

		}
		// n???u ch??a c?? d???ch v??? th?? out v??? lopdv.jsp
		mw = new ModelAndView("redirect:lopdv");
		return mw;
	}

	// ====================== Sau khi th??m g??i t???p or ch???nh s???a d???ch v??? th??
	// updatelopdv file goitap.jsp
	@RequestMapping(value = "updatelopdv", method = RequestMethod.POST)
	public ModelAndView UpdateLopDV(HttpSession session, HttpServletResponse response,
			@RequestParam("malop") String maLop1, @RequestParam("tengoitap") String tenGoiTap,
			@RequestParam("gia") String gia) throws IOException {
		ModelAndView mw = new ModelAndView("admin/goitap");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		String maLop = maLop1.trim();
		mw.addObject("thongbao", 0);
		List<LopDV> checkLopDV = lopDVService.selectByMaLop(maLop.trim());

		kiemTraLoi: try {
			float giaGoiTap = Float.parseFloat(gia);

			// kiem tra d???ch v??? c?? t???n t???i kh??ng
			if (checkLopDV.size() > 0) {
				GoiTap goiTap = new GoiTap();
				int thoiHan = 0;

				// T??? ?????ng l???y m?? g??i t???p
				// format GT + Thoihan + Tengoitap + s??? c???a m?? L???p
				String maGT = "GT";
				if (Integer.parseInt(tenGoiTap.trim().split(" ")[0]) > 1) {
					if (tenGoiTap.trim().split(" ")[1].equals("ng??y")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 7) {
						maGT += tenGoiTap.trim().split(" ")[0] + "NG" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]);
					} else if (tenGoiTap.trim().split(" ")[1].equals("tu???n")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 4) {
						maGT += tenGoiTap.trim().split(" ")[0] + "T" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]) * 7;
					} else if (tenGoiTap.trim().split(" ")[1].equals("th??ng")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 12) {
						maGT += tenGoiTap.trim().split(" ")[0] + "TH" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]) * 30;
					} else if (tenGoiTap.trim().split(" ")[1].equals("n??m")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 11) {
						maGT += tenGoiTap.trim().split(" ")[0] + "N" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]) * 365;
					} else
						break kiemTraLoi;
				} else {
					if (tenGoiTap.trim().split(" ")[1].equals("ng??y")) {
						maGT += "NG" + maLop.substring(2);
						thoiHan = 1;
					} else if (tenGoiTap.trim().split(" ")[1].equals("tu???n")) {
						maGT += "T" + maLop.substring(2);
						thoiHan = 7;
					} else if (tenGoiTap.trim().split(" ")[1].equals("th??ng")) {
						maGT += "TH" + maLop.substring(2);
						thoiHan = 30;
					} else if (tenGoiTap.trim().split(" ")[1].equals("n??m")) {
						maGT += "N" + maLop.substring(2);
						thoiHan = 365;
					} else
						break kiemTraLoi;

				}

				// check tr??ng g??i t???p trong dv
				List<GoiTap> checkGoiTap = goiTapService.selectByMaGT(maGT);
				if (checkGoiTap.size() > 0)
					break kiemTraLoi;

				// n???u tr??ng th?? nh???p l???i
				goiTap.setMaGoiTap(maGT);
				goiTap.setTenGoiTap(tenGoiTap.trim());
				goiTap.setThoiHan(thoiHan);
				goiTap.setTrangThai(1);// m???c ?????nh tr???ng th??i s??? b???ng 1
				goiTap.setGia(giaGoiTap);
				goiTap.setLopDV(checkLopDV.get(0));
				goiTapService.save(goiTap);
				mw.addObject("thongbao", 1);// check JS
			}
		} catch (Exception e) {

		}

		List<GoiTap> goiTapServices = goiTapService.selectByMaLop(maLop.trim());
		List<LopDV> lopDVs = lopDVService.selectByMaLop(maLop.trim());
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("goiTapServices", goiTapServices);
		return mw;
	}
	
	//
	
	// ================= X??a d???ch v??? n???u ch??a c?? g??i t???p xoalopdv?id file lopdv
	@RequestMapping(value = "xoalopdv", method = RequestMethod.POST)
	public ModelAndView XoaDichVu(HttpSession session,HttpServletResponse response,@RequestParam("maLop") String maLop) throws IOException{
		ModelAndView mw = new ModelAndView("apixoalopdv");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}
		
		List<GoiTap> goiTaps = goiTapService.selectByMaLop(maLop);
		if (goiTaps.isEmpty()) {
			lopDVService.delete(maLop);
			mw.addObject("thongbaoxoa", 1);// x??a th??nh c??ng
		}
		else 
			mw.addObject("thongbaoxoa", 0);//x??a th???t b???i
		
		return mw;
	}

	// ================= X??a G??i t???p n???u kh??ch h??ng ch??a ????ng k??
	@RequestMapping(value = "xoagoitap", method = RequestMethod.POST)
	public ModelAndView XoaGoiTap(HttpSession session, HttpServletResponse response, @RequestParam("maGT") String maGT)
			throws IOException {
		ModelAndView mw = new ModelAndView("apixoagoitap");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		mw.addObject("thongbaoxoa", 0);
		List<The> theGT = theService.selectByMaGT(maGT);
		// Check Tr???ng Th??i GT = KDK ? , khong -> xoa
		if (theGT.size() == 0) {

			goiTapService.delete(maGT);
			mw.addObject("thongbaoxoa", 1);// tr??? v??? gi?? tr??? cho api => return = 1
		}
		return mw;
	}
	
	
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== ????NG K?? T???P
	 * ===================================================
	 */
	//=============== Hi???n th??? trang ????ng k?? kh??ch h??ng file sidebar tr??? v??? file dangky.jsp
	@RequestMapping("dangky")
	public String DangKyKH() {
		return "admin/dangky";
	}
	//================= Th??m m???i 1 kh??ch h??ng file dangky.jsp
	@RequestMapping(value = "dangkykhachhang", method = RequestMethod.POST)
	public ModelAndView ThemKhachHang(@RequestParam("hovaten") String hoVaTen, @RequestParam("ngaysinh") String ngaySinh, 
			@RequestParam("email") String email, @RequestParam("sdt")  String sdt, @RequestParam("diachi") String diaChi, 
			@RequestParam("gioitinh") String gioiTinh,@RequestParam("avatar") MultipartFile file) {
		
		// ki???m tra tr??ng email. m???i KH c?? 1 email duy nh???t
		List<KhachHang> emailKH = khachHangService.selectByEmail(email);
		if(emailKH.isEmpty() && !email.isEmpty() && !hoVaTen.trim().isEmpty()) {
			List<KhachHang> khachHangSort = khachHangService.selectSortMaKh(); 
			KhachHang khachHang = new KhachHang();
			//============== T??? ?????ng l???y m?? KH ============= 
			String maKHDV ="";
			int maxID =0;
			try {
				 maxID = Integer.parseInt(khachHangSort.get(0).getMaKH().split("KH")[1]);
				 for ( int i = 0; i < khachHangSort.size(); i++) {
			            if (Integer.parseInt(khachHangSort.get(i).getMaKH().split("KH")[1]) > maxID) {  
			            	maxID = Integer.parseInt(khachHangSort.get(i).getMaKH().split("KH")[1]); 
			            }
			        }
				 maKHDV = "KH "+(maxID+1);
			}catch(Exception e) {
				
				maKHDV = "KH1";
			}    
			// System.out.println("MA KHACH HANG NE= "+maKHDV);
			
		    //Upload File ???nh
			String thongbao = "";
			String extensionFile = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			if (!file.isEmpty() && (extensionFile.equals("jpg") || extensionFile.equals("png")) ) {
						
				try {
					// ?????nh d???ng t??n file: vd nv1_dotam.jpg
					String fileName = maKHDV + extensionFile;
					File dir = new File(servletContext.getRealPath("resources/images/"));
					System.out.println("Upload File = " + dir);
					File serverFile = new File(dir.getAbsolutePath()+ File.separator + fileName);
					file.transferTo(serverFile);
					thongbao= "Success!";
					 } 
				catch (Exception e) {
						 thongbao="Fail!! " + e.getMessage();
					 }
							 
			} 
			else 
				thongbao = "Kh??ng ph???i file ???nh";
			
			System.out.println("Upload File = " + thongbao);
		
			khachHang.setMaKH(maKHDV);
			khachHang.setTenKH(hoVaTen);
			khachHang.setEmail(email);
			khachHang.setGioiTinh(gioiTinh);
			khachHang.setSdt(sdt);
			khachHang.setNgaySinh(null);//cho ph??p null
			khachHang.setDiaChi(diaChi);
			
			//check file ???nh r???ng
			if(extensionFile.isEmpty()) 
				khachHang.setAnh(null);
			else 
				khachHang.setAnh(maKHDV +  "." + extensionFile);
			
			khachHangService.save(khachHang);
			ModelAndView mw =new ModelAndView("redirect:dichvu?id="+maKHDV);
			mw.addObject("thongbao", "????ng k?? Kh??ch H??ng th??nh c??ng");//th??nh c??ng th?? tr??? k???t qu??? thongbao v??? JS file dichvu.jsp
			return mw;
		}
		
		ModelAndView mw = new ModelAndView("admin/dangky");
		mw.addObject("thongbaoloi", "Email ho???c Th??ng Tin kh??ng ch??nh x??c!");//th???t b???i th?? tr??? k???t qu??? thongbaoloi v??? JS file dangky.jsp
		mw.addObject("hoVaTen",hoVaTen);
		mw.addObject("ngaySinh",ngaySinh);
		mw.addObject("sdt",sdt);
		mw.addObject("gioiTinh",gioiTinh);
		mw.addObject("diaChi",diaChi);
		return mw;
	}
	
	//================= ????ng k?? d???ch v??? sau khi ????ng k?? kh??ch h??ng th??nh c??ng file dichvu.jsp
	@RequestMapping(value = "dangkydichvu", method = RequestMethod.POST)
	public ModelAndView DangKyDVKH(@RequestParam("maKH") String maKH, @RequestParam("lopDV") String tenLopDV, 
			@RequestParam("goiTap") String goiTap) {
		ModelAndView mw;
		
		// Ki???m tra g??i t???p c?? tr???ng th??i  = 1 
			int flag = 0;
			List<GoiTap> checkGoiTap = goiTapService.selectByTenLopTrangThai(tenLopDV);
			
			for(GoiTap tenGoiTap:checkGoiTap)
				if(tenGoiTap.getTenGoiTap().equals(goiTap.trim())) flag=1;
			
			List<KhachHang> khachHangs = khachHangService.selectByMaKH(maKH.trim());
			
			if(flag == 1 && khachHangs.size() == 1 ) {
				List<The> TheSort = theService.selectSortMaThe();
				//T??? ?????ng l???y m?? th??? m???i
				String maTDV = "";
				int maxID =0;
				try {
					 maxID = Integer.parseInt(TheSort.get(0).getMaThe().split("TT")[1]);
					 for ( int i = 0; i < TheSort.size(); i++) {
				            if (Integer.parseInt(TheSort.get(i).getMaThe().split("TT")[1]) > maxID) {  
				            	maxID = Integer.parseInt(TheSort.get(i).getMaThe().split("TT")[1]); 
				            }
				        }
					 maTDV = "TT"+(maxID+1);
				}
				catch(Exception e) {
					maTDV = "TT1";
				}	        
				// System.out.println("MA KHACH HANG NE = "+ maKHDV);
				// Lay khach hang theo ma
				
				// L???y G??i T???p theo M?? L???p
				goiTap = goiTap.trim();
				List<LopDV> lopDVs = lopDVService.selectByTenLop(tenLopDV);
				 
				List<GoiTap> goiTaps = goiTapService.selectByMaLopTenGoiTap(lopDVs.get(0).getMaLop(), goiTap);
				
				//T???o th???c th???
				Date date = new Date();
				
				The the = new The();
				the.setMaThe(maTDV);
				the.setKhachHang(khachHangs.get(0));
				the.setGoiTap(goiTaps.get(0));
				the.setNgayDK(date);
				the.setTrangThai("Ch??a Thanh To??n");
				
				theService.save(the);
				 mw = new ModelAndView("redirect:hoadon?id=" + maTDV);
			}
			else  mw = new ModelAndView("redirect:logout");//???
		return mw;
	}
	
	//================= Thanh To??n sau khi ????ng k?? v?? x??t c??? Khi ????ng k?? v?? ch??a thanh to??n JS hoadon?id=maThe file user.jsp
	@RequestMapping(value = "hoadon", params = {"id"}, method = RequestMethod.GET)
	public ModelAndView LapHoaDon(@RequestParam("id") String maThe, HttpServletResponse response) {
		
		ModelAndView mw = new ModelAndView("admin/hoadon");//tr??? v??? chi ti???t h??a ????n file hoadon.jsp
		List<The> thes = theService.selectByMaThe(maThe);
		mw.addObject("thes", thes);
		return mw;
	}
	
	//================ Th???c Hi???n thanh to??n h??a ????n file hoadon.jsp khi nh???n Thanh To??n
	//?????nh d???ng th???i gian theo month/day/year
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="MM/DD/YYYY")
	@RequestMapping(value = "hoadon", method = RequestMethod.POST)
	public ModelAndView ThanhToanHoaDon(HttpSession session, ModelMap modelMap,@RequestParam("id") String maThe, HttpServletResponse response) throws MessagingException {
		
		//l???y t??n t??i kho???n ????ng nh???p c???a nh??n vi??n ????? thanh to??n h??a ????n (username trong login.jsp)
		List<TaiKhoan> taiKhoans = taiKhoanService.selectByUserName(session.getAttribute("username").toString());
		List<The> thes = theService.selectByMaThe(maThe);
		List<HoaDon> hoaDons = hoaDonService.selectSortMaSoHD();
		String maHDMail = "";
			HoaDon hoaDon = new HoaDon();
			The the = new The();
			NhanVien nhanVien = new NhanVien();
			GoiTap goiTap = new GoiTap();
			Date date = new Date();
					
			nhanVien = taiKhoans.get(0).getNhanVien();
			the = thes.get(0);
			hoaDon.setNhanVien(nhanVien);
			hoaDon.setThehd(the);
			hoaDon.setNgayHD(date);
			
			//T??? ?????ng l???y m?? h??a ????n
			int maxID =0;
			try {
				 maxID = Integer.parseInt(hoaDons.get(0).getMaSoHD().split("HD")[1]);
				 for ( int i = 0; i < hoaDons.size(); i++)
			            if (Integer.parseInt(hoaDons.get(i).getMaSoHD().split("HD")[1]) > maxID)   
			            	maxID = Integer.parseInt(hoaDons.get(i).getMaSoHD().split("HD")[1]); 
			            
			        
				 maHDMail = "HD" + (maxID+1);
			}
			catch(Exception e) {
				
				maHDMail = "HD1";
			}
			// System.out.println("MA KHACH HANG NE= "+maKHDV);
			
			hoaDon.setMaSoHD(maHDMail);
			hoaDonService.save(hoaDon);
			int updateTT= theService.updateByMaThe("Ho???t ?????ng", maThe);
			
			// G???i mail th??ng b??o Thanh To??n H??a ????n
			List<The> thesMail= theService.selectByMaThe(maThe);
			MimeMessage messages = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(messages, true, "UTF-8");
	       
	        helper.setTo(thesMail.get(0).getKhachHang().getEmail());
	        helper.setSubject("Thanh To??n D???ch V???");
	        String html_HoaDon="\r\n"
	        		+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /><title>NDTGYM Confirm</title><style type=\"text/css\">\r\n"
	        		+ "    /* Take care of image borders and formatting, client hacks */\r\n"
	        		+ "    img { max-width: 600px; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;}\r\n"
	        		+ "    a img { border: none; }\r\n"
	        		+ "    table { border-collapse: collapse !important;}\r\n"
	        		+ "    #outlook a { padding:0; }\r\n"
	        		+ "    .ReadMsgBody { width: 100%; }\r\n"
	        		+ "    .ExternalClass { width: 100%; }\r\n"
	        		+ "    .backgroundTable { margin: 0 auto; padding: 0; width: 100% !important; }\r\n"
	        		+ "    table td { border-collapse: collapse; }\r\n"
	        		+ "    .ExternalClass * { line-height: 115%; }\r\n"
	        		+ "    .container-for-gmail-android { min-width: 600px; }\r\n"
	        		+ "\r\n"
	        		+ "\r\n"
	        		+ "    /* General styling */\r\n"
	        		+ "    * {\r\n"
	        		+ "      font-family: Helvetica, Arial, sans-serif;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    body {\r\n"
	        		+ "      -webkit-font-smoothing: antialiased;\r\n"
	        		+ "      -webkit-text-size-adjust: none;\r\n"
	        		+ "      width: 100% !important;\r\n"
	        		+ "      margin: 0 !important;\r\n"
	        		+ "      height: 100%;\r\n"
	        		+ "      color: #676767;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    td {\r\n"
	        		+ "      font-family: Helvetica, Arial, sans-serif;\r\n"
	        		+ "      font-size: 14px;\r\n"
	        		+ "      color: #777777;\r\n"
	        		+ "      text-align: center;\r\n"
	        		+ "      line-height: 21px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    a {\r\n"
	        		+ "      color: #676767;\r\n"
	        		+ "      text-decoration: none !important;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .pull-left {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .pull-right {\r\n"
	        		+ "      text-align: right;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .header-lg,\r\n"
	        		+ "    .header-md,\r\n"
	        		+ "    .header-sm {\r\n"
	        		+ "      font-size: 32px;\r\n"
	        		+ "      font-weight: 700;\r\n"
	        		+ "      line-height: normal;\r\n"
	        		+ "      padding: 35px 0 0;\r\n"
	        		+ "      color: #4d4d4d;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .header-md {\r\n"
	        		+ "      font-size: 24px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .header-sm {\r\n"
	        		+ "      padding: 5px 0;\r\n"
	        		+ "      font-size: 18px;\r\n"
	        		+ "      line-height: 1.3;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .content-padding {\r\n"
	        		+ "      padding: 20px 0 5px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-header-padding-right {\r\n"
	        		+ "      width: 290px;\r\n"
	        		+ "      text-align: right;\r\n"
	        		+ "      padding-left: 10px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-header-padding-left {\r\n"
	        		+ "      width: 290px;\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      padding-left: 10px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .free-text {\r\n"
	        		+ "      width: 100% !important;\r\n"
	        		+ "      padding: 10px 60px 0px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .button {\r\n"
	        		+ "      padding: 30px 0;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "\r\n"
	        		+ "    .mini-block {\r\n"
	        		+ "      border: 1px solid #e5e5e5;\r\n"
	        		+ "      border-radius: 5px;\r\n"
	        		+ "      background-color: #ffffff;\r\n"
	        		+ "      padding: 12px 15px 15px;\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      width: 253px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mini-container-left {\r\n"
	        		+ "      width: 278px;\r\n"
	        		+ "      padding: 10px 0 10px 15px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mini-container-right {\r\n"
	        		+ "      width: 278px;\r\n"
	        		+ "      padding: 10px 14px 10px 15px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .product {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      vertical-align: top;\r\n"
	        		+ "      width: 175px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .total-space {\r\n"
	        		+ "      padding-bottom: 8px;\r\n"
	        		+ "      display: inline-block;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .item-table {\r\n"
	        		+ "      padding: 50px 20px;\r\n"
	        		+ "      width: 560px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .item {\r\n"
	        		+ "      width: 300px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-hide-img {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      width: 125px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-hide-img img {\r\n"
	        		+ "      border: 1px solid #e6e6e6;\r\n"
	        		+ "      border-radius: 4px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .title-dark {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      border-bottom: 1px solid #cccccc;\r\n"
	        		+ "      color: #4d4d4d;\r\n"
	        		+ "      font-weight: 700;\r\n"
	        		+ "      padding-bottom: 5px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .item-col {\r\n"
	        		+ "      padding-top: 20px;\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      vertical-align: top;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .force-width-gmail {\r\n"
	        		+ "      min-width:600px;\r\n"
	        		+ "      height: 0px !important;\r\n"
	        		+ "      line-height: 1px !important;\r\n"
	        		+ "      font-size: 1px !important;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "  </style><style type=\"text/css\" media=\"screen\">\r\n"
	        		+ "    @import url(http://fonts.googleapis.com/css?family=Oxygen:400,700);\r\n"
	        		+ "  </style><style type=\"text/css\" media=\"screen\">\r\n"
	        		+ "    @media screen {\r\n"
	        		+ "      /* Thanks Outlook 2013! */\r\n"
	        		+ "      * {\r\n"
	        		+ "        font-family: 'Oxygen', 'Helvetica Neue', 'Arial', 'sans-serif' !important;\r\n"
	        		+ "      }\r\n"
	        		+ "    }\r\n"
	        		+ "  </style><style type=\"text/css\" media=\"only screen and (max-width: 480px)\">\r\n"
	        		+ "    /* Mobile styles */\r\n"
	        		+ "    @media only screen and (max-width: 480px) {\r\n"
	        		+ "\r\n"
	        		+ "      table[class*=\"container-for-gmail-android\"] {\r\n"
	        		+ "        min-width: 290px !important;\r\n"
	        		+ "        width: 100% !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      img[class=\"force-width-gmail\"] {\r\n"
	        		+ "        display: none !important;\r\n"
	        		+ "        width: 0 !important;\r\n"
	        		+ "        height: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      table[class=\"w320\"] {\r\n"
	        		+ "        width: 320px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "\r\n"
	        		+ "      td[class*=\"mobile-header-padding-left\"] {\r\n"
	        		+ "        width: 160px !important;\r\n"
	        		+ "        padding-left: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class*=\"mobile-header-padding-right\"] {\r\n"
	        		+ "        width: 160px !important;\r\n"
	        		+ "        padding-right: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"header-lg\"] {\r\n"
	        		+ "        font-size: 24px !important;\r\n"
	        		+ "        padding-bottom: 5px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"content-padding\"] {\r\n"
	        		+ "        padding: 5px 0 5px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "       td[class=\"button\"] {\r\n"
	        		+ "        padding: 5px 5px 30px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class*=\"free-text\"] {\r\n"
	        		+ "        padding: 10px 18px 30px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"mobile-hide-img\"] {\r\n"
	        		+ "        display: none !important;\r\n"
	        		+ "        height: 0 !important;\r\n"
	        		+ "        width: 0 !important;\r\n"
	        		+ "        line-height: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"item\"] {\r\n"
	        		+ "        width: 140px !important;\r\n"
	        		+ "        vertical-align: top !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"quantity\"] {\r\n"
	        		+ "        width: 50px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"price\"] {\r\n"
	        		+ "        width: 90px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"item-table\"] {\r\n"
	        		+ "        padding: 30px 20px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"mini-container-left\"],\r\n"
	        		+ "      td[class=\"mini-container-right\"] {\r\n"
	        		+ "        padding: 0 15px 15px !important;\r\n"
	        		+ "        display: block !important;\r\n"
	        		+ "        width: 290px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "    }\r\n"
	        		+ "  </style></head><body bgcolor=\"#f7f7f7\"><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"container-for-gmail-android\" width=\"100%\"><tr><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#ffffff\" background=\"http://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg\" style=\"background-color:transparent\"><tr><td width=\"100%\" height=\"80\" valign=\"top\" style=\"text-align: center; vertical-align:middle;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"pull-left mobile-header-padding-left\" style=\"vertical-align: middle;\"><a class=\"header-md\" href=\"\">Xin ch??o, " + thesMail.get(0).getKhachHang().getTenKH() + "</a></td></tr></table></center></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7;\" class=\"content-padding\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"header-lg\">\r\n"
	        		+ "              Thanh To??n Th??nh C??ng!\r\n"
	        		+ "            </td></tr><tr><td class=\"free-text\">\r\n"
	        		+ "              Ch??n th??nh c???m ??n Qu?? Kh??ch ???? ?????ng h??nh c??ng FITNESSGYM.<br> Ch??c Qu?? Kh??ch h??ng c?? m???t tr???i nghi???m th???t t???t v?? th?? v???!!\r\n"
	        		+ "            </td></tr><tr><td class=\"button\"><div><a href=\"http://\"\r\n"
	        		+ "              style=\"background-color:#28a745;border-radius:5px;color:#ffffff;display:inline-block;font-family:'Cabin', Helvetica, Arial, sans-serif;font-size:14px;font-weight:regular;line-height:45px;text-align:center;text-decoration:none;width:155px;-webkit-text-size-adjust:none;mso-hide:all;\">????ng k?? d???ch v??? m???i</a></div></td></tr><tr><td class=\"w320\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-container-left\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Th??ng tin kh??ch h??ng</span><br />\r\n"
	        		+ "                                " + thesMail.get(0).getKhachHang().getTenKH() + " <br />\r\n"
	        		+ "                                " + thesMail.get(0).getKhachHang().getSdt() + " <br />\r\n"
	        		+ "                                " + thesMail.get(0).getKhachHang().getEmail() + " \r\n"
	        		+ "                              </td></tr></table></td></tr></table></td><td class=\"mini-container-right\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Th??ng Tin D???ch V???</span><br />\r\n"
	        		+ "                                Ng??y ????ng K??: " + thesMail.get(0).getNgayDK() + " <br /><span class=\"header-sm\">M?? H??a ????n</span><br />\r\n"
	        		+ "                                #" + maHDMail + "\r\n"
	        		+ "                              </td></tr></table></td></tr></table></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #ffffff;  border-top: 1px solid #e5e5e5; border-bottom: 1px solid #e5e5e5;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"item-table\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td class=\"title-dark\" width=\"300\">\r\n"
	        		+ "                       D???ch V???\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"163\">\r\n"
	        		+ "                      G??i T???p\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"97\">\r\n"
	        		+ "                      Gi??\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item\"><span style=\"color: #4d4d4d; font-weight:bold;\">"+thesMail.get(0).getGoiTap().getLopDV().getTenLop()+"</span></td><td class=\"item-col quantity\">\r\n"
	        		+ "                     " + thesMail.get(0).getGoiTap().getTenGoiTap() + "\r\n"
	        		+ "                    </td><td class=\"item-col\">\r\n"
	        		+ "                      ???" + thesMail.get(0).getGoiTap().getGia() + "\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item mobile-row-padding\"></td><td class=\"item-col quantity\"></td><td class=\"item-col price\"></td></tr><tr><td class=\"item-col item\"></td><td class=\"item-col quantity\" style=\"text-align:right; padding-right: 10px; border-top: 1px solid #cccccc;\"><span class=\"total-space\">T???ng chi ph??</span><br /><span class=\"total-space\">Thu???</span><br /><span class=\"total-space\" style=\"font-weight: bold; color: #4d4d4d\">Th??nh Ti???n</span></td><td class=\"item-col price\" style=\"text-align: left; border-top: 1px solid #cccccc;\"><span class=\"total-space\">???" + thesMail.get(0).getGoiTap().getGia() + "</span><br /><span class=\"total-space\">0.00???</span><br /><span class=\"total-space\" style=\"font-weight:bold; color: #4d4d4d\">"+thesMail.get(0).getGoiTap().getGia()+"???</span></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7; height: 100px;\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td style=\"padding: 5px 0 10px\"><strong>97 Man Thi???n</strong><br />\r\n"
	        		+ "              Th??nh ph??? Th??? ?????c <br />\r\n"
	        		+ "              Th??nh Ph??? H??? Ch?? Minh <br /><br /></td></tr></table></center></td></tr></table></div></body></html>";
	        helper.setText(html_HoaDon, true);
	        this.javaMailSender.send(messages);
			
			ModelAndView mw = new ModelAndView("admin/hoadon");
			mw.addObject("updateTT", updateTT);//tr??? v??? JS trong hoadon.jsp
			System.out.println(updateTT);
			List<The> thes1 = theService.selectByMaThe(maThe);
			mw.addObject("thes", thes1);
			
			return mw;
		}

	
	
	//================= L???y t??n G??i T???p thu???c D???ch V??? JS file dichvu.jsp
	@RequestMapping(value = "laytengoitap", method = RequestMethod.POST)
	public ModelAndView LayTenGoiTap(@RequestParam("lopDV") String lopDV ) throws IOException {
		ModelAndView mw = new ModelAndView("apilaytengoitap");//api
		
		List<GoiTap> checkGoiTap = goiTapService.selectByTenLopTrangThai(lopDV);
		String tenCacGoiTap = "";
		for (GoiTap goiTap:checkGoiTap) {
			tenCacGoiTap += goiTap.getTenGoiTap() + ",";
		}
		mw.addObject("tengoitaps", tenCacGoiTap.substring(0,tenCacGoiTap.length()-1));//g??n cho api sau ???? return v??? mw
		
		return mw;
		
	}
	
	//================= L???y gi?? G??i T???p thu???c D???ch V??? JS file dichvu.jsp
	@RequestMapping(value = "laygiagoitap", method=RequestMethod.POST)
	public ModelAndView LayGiaGoiTap(@RequestParam("lopDV") String lopDV, @RequestParam("goiTap") String goiTap ) {
		ModelAndView mw = new ModelAndView("apigetgiatien");//api
		
		goiTap = goiTap.trim();
		
		List<LopDV> lopDVs = lopDVService.selectByTenLop(lopDV);
		List<GoiTap> goiTaps = goiTapService.selectByMaLopTenGoiTap(lopDVs.get(0).getMaLop(), goiTap);
		
		mw.addObject("getGiaTien", goiTaps.get(0).getGia());//g??n cho api sau ???? return v??? mw
		return mw;
	}
	
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== H??A ????N
	 * ===================================================
	 */
	//================= Hi???n th??? danh s??ch h??a ????n
	@RequestMapping("banghoadon")
	public ModelAndView DanhSachHoaDon() {
		ModelAndView mw = new ModelAndView("admin/banghoadon");
		List<HoaDon> hoaDons = hoaDonService.listAll();
		mw.addObject("hoaDons", hoaDons);
		return mw;
	}
	
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== KH??CH H??NG
	 * ===================================================
	 */
	//================= Hi???n th??? danh s??ch Kh??ch H??ng file bangusers.jsp => hi???n th??? list user
	@RequestMapping("bangusers")
	public ModelAndView BangKhachHang() {
		ModelAndView mw = new ModelAndView("admin/bangusers");
		
		List<KhachHang> khachHangServices = khachHangService.listAll();
		mw.addObject("khachHangServices", khachHangServices);
		return mw;
	}
	
	//================= Ch???nh s???a d???ch v??? KH file banguser.jsp nh???n D???ch V???
	@RequestMapping(value = "dichvu" , params = {"id"}, method = RequestMethod.GET)
	public ModelAndView ChinhSuaDVKH(@RequestParam("id") String maKH) {
		List<The> TheSort = theService.selectSortMaThe();
		List<GoiTap> goiTaps = goiTapService.listAll();
		List<LopDV> lopDVs = lopDVService.listAll();

		String maTDV = "";
		LocalDate localDate = LocalDate.now(); //m???c ?????nh ??ang l?? th???i gian hi???n t???i
		   
		//T??? ?????ng l???y m?? th??? t???p
		int maxID =0;
		try {
			 maxID = Integer.parseInt(TheSort.get(0).getMaThe().split("TT")[1]);
			 for ( int i = 0; i < TheSort.size(); i++) {
		            if (Integer.parseInt(TheSort.get(i).getMaThe().split("TT")[1]) > maxID) {  
		            	maxID = Integer.parseInt(TheSort.get(i).getMaThe().split("TT")[1]); 
		            }
		        }
			 maTDV = "TT"+(maxID+1);
		}catch(Exception e) {
			
			maTDV = "TT1";
		}
		
		ModelAndView mw = new ModelAndView("admin/dichvu");
		mw.addObject("maTDV", maTDV);
		mw.addObject("maKH", maKH);
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("localDate", localDate);
		
		return mw;
	}
	
	//================= Ch???nh s???a th??ng tin KH file banguser.jsp nh???n Ch???nh S???a
	@RequestMapping(value = "user", params = {"id"}, method = RequestMethod.GET)
	public ModelAndView SuaKhachHang(@RequestParam("id") String maKH) {
		ModelAndView mw = new ModelAndView("admin/user");
		
		List <KhachHang> khachHangs = khachHangService.selectByMaKH(maKH);
		List <The> thes = theService.selectByMaKH(maKH);
		
		mw.addObject("khachhangs", khachHangs);
		mw.addObject("avatar", khachHangs.get(0).getAnh());
		mw.addObject("tenKH", khachHangs.get(0).getTenKH());
		mw.addObject("thes", thes);

		return mw;
	}
	
	//================= Update th??ng tin KH file banguser.jsp khi nh???n btn C???p nh???t
	@RequestMapping(value = "updateuser", method = RequestMethod.POST)
	public ModelAndView UpdateKhachHang(@RequestParam("makh") String maKH, @RequestParam("sdt") String sdt, @RequestParam("hovaten") String hoVaTen,@RequestParam("gioitinh") String gioiTinh, @RequestParam("email") String email, @RequestParam("diachi") String diaChi, @RequestParam("ngaysinh") String ngaySinh, @RequestParam("avatar") MultipartFile file) throws ParseException {

		ModelAndView mw = new ModelAndView("redirect:user?id=" + maKH);//th??nh c??ng tr??? v??? thongbao JS trong file user.jsp
		mw.addObject("thongbao", "0");//g??n l?? fail
		
		// ki???m tra tr??ng email. m???i KH c?? 1 email duy nh???t
		KhachHang khachHang = new KhachHang();
		List<KhachHang> emailKH = khachHangService.selectByEmail(email);
		List<KhachHang> khachHangMaKH = khachHangService.selectByMaKH(maKH);
		
		if((emailKH.isEmpty()||khachHangMaKH.get(0).getEmail().equals(email) )&& !email.isEmpty()) {
			// C???p nh???t th??ng tin
			try {
				String sDate1 = ngaySinh.replace("-", "/"); //nh???p ???????c ??? 2 d???ng 
				Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1); //n??m/th??ng/ng??y 
				khachHang.setNgaySinh(date1);
			}
			catch(Exception e) {}
			
		    khachHang.setMaKH(maKH);
			khachHang.setTenKH(hoVaTen);
			khachHang.setEmail(email);
			khachHang.setSdt(sdt);
			khachHang.setDiaChi(diaChi);
			khachHang.setGioiTinh(gioiTinh);
			
			
			 //Upload File ???nh
			String thongbao = "";
			String extensionFile = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			if (!file.isEmpty() && (extensionFile.equals("jpg") || extensionFile.equals("png")) ) {
					
					try {
						// X??a Avatar c?? tr?????c khi upload l???i
						File avatar = new File(servletContext.getRealPath("resources/images/" + maKH + ".jpg"));
						if(avatar.exists()) 
							avatar.delete();
						else {
							avatar =new File(servletContext.getRealPath("resources/images/" + maKH + ".png"));
							avatar.delete();
						}
			
						// ?????nh d???ng t??n file: vd nv1_dotam.jpg
						String fileName = maKH + extensionFile;
						File dir = new File(servletContext.getRealPath("resources/images/"));
						System.out.println("Upload File = " + dir);
						File serverFile = new File(dir.getAbsolutePath()+ File.separator + fileName);
						file.transferTo(serverFile);
						thongbao= "Success!";
						 } 
					catch (Exception e) 
					{
							 thongbao = "Fail!! " + e.getMessage();
					}
							 
			} 
			else 
				thongbao = "Kh??ng ph???i file ???nh";
			
			if(extensionFile.isEmpty()) 
				khachHang.setAnh(khachHangMaKH.get(0).getAnh());
			else 
				khachHang.setAnh(maKH + "."+ extensionFile);
			
			khachHangService.save(khachHang);
			
			//mw = new ModelAndView("redirect:user?id=" + maKH);
			mw.addObject("thongbao", "1");//th??nh c??ng
			return mw;
		}
		
		return mw;
		
		
	}
	
	
	//================= X??a Th??ng tin kh??ch h??ng file banguser.jsp => th??? t???p x??a => tr??? v??? user.jsp
	//================= X??a Kh??ch H??ng use JS v?? api file  banguser.jsp
	@RequestMapping(value = "xoakhachhang", method = RequestMethod.POST)
	public ModelAndView XoaKhachHang(@RequestParam("maKH") String maKH) {
		ModelAndView mw = new ModelAndView("apixoakhachhang");//tr??? v??? k???t qu??? thongbaoxoa = mw
		
		List<The> theKH = theService.selectByMaKHNotSort(maKH);
		
		// N???u kh??ch h??ng ch??a ????ng k?? th??? t???p th?? x??a
		if(theKH.isEmpty()) {
			//X??a avatar tr?????c n???u c??
			File avatar =new File(servletContext.getRealPath("resources/images/" + maKH + ".jpg"));
			if(avatar.exists()) 
				avatar.delete();

			else {
				avatar = new File(servletContext.getRealPath("resources/images/" + maKH + ".png"));
				avatar.delete();
			}
			
			khachHangService.delete(maKH);
			mw.addObject("thongbaoxoa", "1");
			
		}
		else mw.addObject("thongbaoxoa", "0");
		return mw;
	} 
	
	/*
	 * =============================================================================
	 * ============================
	 */


	/*
	 * ==================================================== NH??N VI??N
	 * ===================================================
	 */
	 //================= Hi???n th??? danh s??ch Nh??n Vi??n file sidebar.jsp tr??? v??? file nhanvien.jsp
	@RequestMapping("bangnhanvien")
	public ModelAndView DanhSachNV( HttpServletResponse response , HttpSession session) throws IOException {
		ModelAndView mw = new ModelAndView("admin/nhanvien");
		
		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		
		List<NhanVien> nhanViens = nhanVienService.listAll();
		mw.addObject("nhanViens", nhanViens);
		return mw;
	}
	
	//================== Th??m Nh??n Vi??n khi nh???n onclick them file nhanvien.jsp
	@RequestMapping(value = "dangkynhanvien", method = RequestMethod.POST)
	public ModelAndView DangKyNhanVien(HttpServletResponse response, HttpSession session, @RequestParam("hovaten") String hoVaTen, 
			@RequestParam("email") String email, @RequestParam("sdt")  String sdt, @RequestParam("diachi") String diaChi, 
			@RequestParam("gioitinh") String gioiTinh, @RequestParam("chucvu") String chucVu, @RequestParam("username") String userName, @RequestParam("password") String password, @RequestParam("xnpassword") String xnpassword) throws IOException {
		
		ModelAndView mw = new ModelAndView("admin/nhanvien");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}
				
		if(password.equals(xnpassword) && password.trim().length()>5 && !chucVu.trim().isEmpty() && !hoVaTen.trim().isEmpty()&& !userName.trim().isEmpty() ){
			List<NhanVien> ktEmail = nhanVienService.selectByEmail(email);
			List<NhanVien> ktUserName = nhanVienService.selectByUserName(userName);
			
			//ki???m tra tr??ng email & username
			if(ktEmail.size()==0 && ktUserName.size()==0 ) {
				TaiKhoan taiKhoan = new TaiKhoan();
				List<PhanQuyen> phanQuyen;
				if(chucVu.equals("Qu???n L??")) phanQuyen = phanQuyenService.selectByMaQuyen(0);
				else phanQuyen = phanQuyenService.selectByMaQuyen(1);
				
			
				taiKhoan.setUserName(userName);
				taiKhoan.setPassWord(password);
				taiKhoan.setTrangThai(1);//nh??n vi??n
				taiKhoan.setPhanQuyen(phanQuyen.get(0));
				taiKhoanService.save(taiKhoan);
				
				
				
				NhanVien nhanVien = new NhanVien();
				List<NhanVien> nhanViens = nhanVienService.listAll();
				
				//T??? ?????ng l???y m?? nh??n vi??n 
				String maNV ="";
				int maxID =0;
				try {
					 maxID = Integer.parseInt(nhanViens.get(0).getMaNV().split("NV")[1]);
					 for ( int i = 0; i < nhanViens.size(); i++) {
				            if (Integer.parseInt(nhanViens.get(i).getMaNV().split("NV")[1]) > maxID) {  
				            	maxID = Integer.parseInt(nhanViens.get(i).getMaNV().split("NV")[1]); 
				            }
				        }
					 maNV = "NV"+(maxID+1);
				}
				catch(Exception e) {
					
					maNV = "NV1";
				}
				        
				nhanVien.setMaNV(maNV);
				nhanVien.setDiaChi(diaChi);
				nhanVien.setEmail(email);
				nhanVien.setGioiTinh(gioiTinh);
				nhanVien.setSdt(sdt);
				nhanVien.setTaiKhoan(taiKhoan);
				nhanVien.setTenNV(hoVaTen);
				nhanVienService.save(nhanVien);
				
				mw.addObject("thongbao", "0");//Th??m Nh??n Vi??n th??nh c??ng
				
			}else mw.addObject("thongbao", "1");//tr??ng usename ho???c email
				
		}
		else 
			mw.addObject("thongbao", "2");//Sai Password ho???c x??c nh???n password
		
		List<NhanVien> nhanViens = nhanVienService.listAll();
		mw.addObject("nhanViens", nhanViens);
		
		return mw;
	}
	
	//================== Ch???nh S???a th??ng tin Nh??n Vi??n theo maNV khi nh???n nhavien?id=maNV file nhanvien.jsp
	@RequestMapping(value = "nhanvien", params = {"id"}, method = RequestMethod.GET)
	public ModelAndView ChinhSuaNhanVien( HttpSession session, HttpServletResponse response, @RequestParam("id") String maNV) throws IOException {
		ModelAndView mw =new ModelAndView("admin/chitietnhanvien");
		
		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		List<NhanVien> nhanViens = nhanVienService.selectByMaNV(maNV);
		mw.addObject("nhanVien", nhanViens);
		return mw;
	}
	
	//=================== Update Nh??n Vi??n sau khi ch???nh s???a xong v?? nh???n btn save file chitietnhanvien.jsp
	@RequestMapping(value = "updatenhanvien", method = RequestMethod.POST)
	public ModelAndView UpdateNhanVien(HttpServletResponse response, HttpSession session, @RequestParam("manv") String maNV, @RequestParam("hovaten") String hoVaTen, 
			@RequestParam("email") String email, @RequestParam("sdt") String sdt, @RequestParam("diachi") String diaChi, 
			@RequestParam("gioitinh") String gioiTinh,@RequestParam("chucvu") String chucVu) throws IOException {
		
		ModelAndView mw = new ModelAndView("admin/chitietnhanvien");
		
		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}
		
		List<NhanVien> nhanViens = nhanVienService.selectByMaNV(maNV);
		mw.addObject("thongbao", "0");
		List<NhanVien> ktEmail = nhanVienService.selectByEmail(email);

		//ki???m tra n???u nh??n vi??n ch??a l???p h??a ????n ho???c ch??a c?? email th?? c?? th??? ch???nh s???a th??ng tin
		if(nhanViens.get(0).getHoaDons().size() == 0 && nhanViens.size() > 0 && ( nhanViens.get(0).getEmail().equals(email.trim()) || ktEmail.size() == 0 )) {
			NhanVien nhanVien = new NhanVien();
			nhanVien.setMaNV(maNV);
			nhanVien.setDiaChi(diaChi);
			nhanVien.setEmail(email);
			nhanVien.setGioiTinh(gioiTinh);
			nhanVien.setSdt(sdt);
			nhanVien.setTaiKhoan(nhanViens.get(0).getTaiKhoan());
			nhanVien.setTenNV(hoVaTen);
			
			nhanVienService.save(nhanVien);
			
			mw.addObject("thongbao", "1");
			
		}
		//Truy xu???t d??? li???u sau khi Update nV
	    nhanViens = nhanVienService.selectByMaNV(maNV);
		mw.addObject("nhanVien", nhanViens);
		return mw;
		
		
	}
	
	//=================== Qu???n L?? c?? quy???n Kh??a T??i Kho???n Nh??n Vi??n khi nh???n JS kh??a t??i kho???n file nhanvien.jsp
	@RequestMapping(value = "khoataikhoan", method = RequestMethod.POST)
	public ModelAndView KhoaTaiKhoan(HttpSession session,HttpServletResponse response,@RequestParam("maNV")String maNV,@RequestParam("checked")String checked ) throws IOException {
		ModelAndView mw = new ModelAndView("apikhoataikhoan");//tr??? v??? thongbaoupdate cho api v?? result kq = mw
		
		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
		if(!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		List<NhanVien> nhanVien = nhanVienService.selectByMaNV(maNV);
		if(!nhanVien.get(0).getTaiKhoan().getPhanQuyen().getChucVu().equals("Qu???n L??")) {
			try {
				if(checked.trim().equals("true")) 
					taiKhoanService.updateByUserName(0, nhanVien.get(0).getTaiKhoan().getUserName()); 
				else if (checked.trim().equals("false")) 
					taiKhoanService.updateByUserName(1, nhanVien.get(0).getTaiKhoan().getUserName());
				
				mw.addObject("thongbaoupdate", "1");
			}
			catch(Exception e){
				mw.addObject("thonguaoupdate", "0");
				
			}
		}
		return mw;
	}
	
	//=================== Update T??i Kho???n Nh??n Vi??n sau khi ch???nh s???a xong  file chitietnhanvien.jsp
		//ch??? update t??i kho???n ch??a c?? ho???c ?????i quy???n, t??i kho???n ???? c?? th?? ko update
		@RequestMapping(value = "updatetaikhoan", method = RequestMethod.POST)
		public ModelAndView UpdateTaiKhoan(HttpServletResponse response, HttpSession session, @RequestParam("manv") String maNV, @RequestParam("username") String userName, @RequestParam("password") String passWord, @RequestParam("maquyen") String maQuyen, @RequestParam("trangthai") String trangThai ) throws IOException {
			ModelAndView mw = new ModelAndView("admin/chitietnhanvien");
			
			// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
					if(!session.getAttribute("maQuyen").equals("0")) {
						response.sendRedirect("dangky");
					}


			int tb = 4;// gi??? s??? c???p nh???t NV th??nh c??ng
			List<TaiKhoan> taiKhoans = taiKhoanService.selectByUserName(userName);
			List<NhanVien> nhanViens = nhanVienService.selectByMaNV(maNV);
			
			mw.addObject("thongbao", "3");//3: username define
		
			//check t??i kho???n ????? kh??ng b??? tr??ng , kh??c root
			if(taiKhoans.size() > 0 && !taiKhoans.get(0).getUserName().trim().equals("root") && taiKhoans.get(0).getUserName().equals(nhanViens.get(0).getTaiKhoan().getUserName().trim())) {
				TaiKhoan taiKhoan = new TaiKhoan();	
				
				//================================== Check Ph??n Quy???n=================
				List<PhanQuyen> phanQuyen;
				// B???t bu???c >2 Qu???n l?? m???i cho ?????i quy???n t??? 1->0 (Qu???n L?? -> Nh??n Vi??n)
				if(maQuyen.trim().equals("0")) 
					phanQuyen = phanQuyenService.selectByMaQuyen(0);
				else if (nhanViens.get(0).getTaiKhoan().getUserName().trim().equals(session.getAttribute("username"))) {
					phanQuyen = phanQuyenService.selectByMaQuyen(0);//qly
					tb += 1;//5: kh??ng thay ?????i quy???n c???a ch??nh b???n
				}
				else phanQuyen = phanQuyenService.selectByMaQuyen(1);//nvien
				
				
				//================================== Check T??i Kho???n=================
				taiKhoan.setUserName(userName);
				if(passWord.trim().length() > 5 ) { 
					taiKhoan.setPassWord(passWord);
					mw.addObject("thongbaopass", "1");//1: change pass success
				}
				else {
					taiKhoan.setPassWord(nhanViens.get(0).getTaiKhoan().getPassWord());
					tb += 2;//6: pass ko h???p l??? <=5
				}
				
				//================================== Check Tr???ng Th??i (username: t??i kho???n c???a b???n ??ang ????ng nh???p) 0-Kh??a; 1- H??=================
				if( trangThai.trim().equals("1") || (trangThai.trim().equals("0") && !nhanViens.get(0).getTaiKhoan().getUserName().trim().equals(session.getAttribute("username"))  ) ) 
					taiKhoan.setTrangThai(Integer.parseInt(trangThai));
			    else 
			    {
			    	taiKhoan.setTrangThai(nhanViens.get(0).getTaiKhoan().getTrangThai());
			    	/*8: kh??ng thay ?????i ???????c tr???ng th??i c???a ch??nh b???n
			    	 *5 + 4 = 9: kh??ng thay ?????i quy???n & kh??ng thay ?????i ???????c tr???ng th??i c???a ch??nh b???n
			    	 *6 + 4 = 10: kh??ng thay ?????i ???????c tr???ng th??i c???a ch??nh b???n & pass ko h???p l??? */
			    	tb += 4;//x+4
			    }

				taiKhoan.setPhanQuyen(phanQuyen.get(0));
				taiKhoanService.save(taiKhoan);
				mw.addObject("thongbao", "" + tb);//
			}
			
			  nhanViens = nhanVienService.selectByMaNV(maNV);
				mw.addObject("nhanVien", nhanViens);
			return mw;	
		}
		
		
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== TH???NG K??
	 * ===================================================
	 */
	//======================= V??o Th???ng K?? file thongke.jsp
	@RequestMapping("thongke")
	public ModelAndView ThongKe(HttpSession session,HttpServletResponse response) throws ParseException, IOException {
		ModelAndView mw = new ModelAndView("admin/thongke");

		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		float tongTien=0;	
		LocalDate date = LocalDate.now();//l???y th???i gian hi???n t???i
		
		String []dates = ("" + date).split("-");
		String namBD = dates[0] + "/" + dates[1] + "/01";//b???t ?????u t??? n??m ..01
		String namKT = dates[0] + "/" + dates[1] + "/" + dates[2];
		
		Date dateBD = new SimpleDateFormat("yyyy/MM/dd").parse(namBD);
		Date dateKT = new SimpleDateFormat("yyyy/MM/dd").parse(namKT);
		
		List<The> theServices = theService.findBetweenNgayDK(dateBD,dateKT );//t??? ng??y... ?????n ng??y...
		List<The> theServicess = new ArrayList<The>();
		
		for(The theService1:theServices ) {
			if(theService1.getTrangThai().trim().equals("Ho???t ?????ng")||theService1.getTrangThai().trim().equals("H???t H???n") ) {
				tongTien += theService1.getGoiTap().getGia();//Th??? h???t h???n + th??? ho???t ?????ng
				theServicess.add(theService1);
			}
			
		}
		System.out.println(""+dates[0]+"-"+dates[1]);
		
		List<LopDV> lopDVs = lopDVService.listAll();
		
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("theServices", theServicess);
		mw.addObject("tongTien", tongTien);
		mw.addObject("namBD", namBD.replace('/', '-'));//?????nh d???ng ??? c??? 2 / & -
		mw.addObject("namKT", namKT.replace('/', '-'));
		
		return mw;
	}
	
	//======================= Th???ng k?? doanh thu btn Doanh Thu file thongke.jsp
	@RequestMapping(value = "thongkeDT", method = RequestMethod.POST)
	public ModelAndView ThongKeDoanhThu(HttpServletResponse response, HttpSession session,@RequestParam("ngayBD") String ngayBD,@RequestParam("ngayKT") String ngayKT) throws ParseException, IOException {
		ModelAndView mw = new ModelAndView("admin/thongke");


		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}


		float tongTien = 0;
		
		Date dateBD = new SimpleDateFormat("yyyy/MM/dd").parse(ngayBD.replace("-", "/"));
		Date dateKT = new SimpleDateFormat("yyyy/MM/dd").parse(ngayKT.replace("-", "/"));
		
		List<The> theServices = theService.findBetweenNgayDK(dateBD,dateKT );
		List<The> theServicess = new ArrayList<The>();
		
		for(The theService1:theServices ) {
			if(theService1.getTrangThai().trim().equals("Ho???t ?????ng")||theService1.getTrangThai().trim().equals("H???t H???n") ) {
				tongTien += theService1.getGoiTap().getGia();
				theServicess.add(theService1);
			}
			
		}   
		
		List<LopDV> lopDVs = lopDVService.listAll();
		
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("theServices", theServicess);
		mw.addObject("tongTien", tongTien);
		mw.addObject("namBD", ngayBD);
		mw.addObject("namKT", ngayKT);
		
		return mw;
	}
	
	//======================= Th???ng k?? Kh??ch H??ng btn Kh??ch H??ng file thongke.jsp
	@RequestMapping(value = "thongkeKH", method = RequestMethod.POST)
	public ModelAndView thongkeKH(HttpServletResponse response,HttpSession session,@RequestParam("ngayBD") String ngayBD,@RequestParam("ngayKT") String ngayKT,@RequestParam("tenKH") String tenKH) throws ParseException, IOException {
		ModelAndView mw=new ModelAndView("thongke");


		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}
				
		Date dateBD=new SimpleDateFormat("yyyy/MM/dd").parse(ngayBD.replace("-", "/"));
		Date dateKT=new SimpleDateFormat("yyyy/MM/dd").parse(ngayKT.replace("-", "/"));
		
		List <The> theServices = theService.findBetweenNgayDKTenKH(dateBD, dateKT,tenKH);
		List<LopDV> lopDVs = lopDVService.listAll();
		
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("theServiceKH", theServices);
		mw.addObject("flag", "kh");
		mw.addObject("slTheKH", ""+theServices.size());
		mw.addObject("tenKH", tenKH);
		mw.addObject("namBDDV", ngayBD);
		mw.addObject("namKTDV", ngayKT);
		
		return mw;
		
	}
	
	//======================= Th???ng k?? Th??? D???ch V??? btn D???ch V??? file thongke.jsp
	@RequestMapping(value = "thongkeDV", method = RequestMethod.POST)
	public ModelAndView ThongKeDichVu(HttpServletResponse response,HttpSession session,@RequestParam("ngayBD") String ngayBD,@RequestParam("ngayKT") String ngayKT,@RequestParam("tenLopDV") String tenLopDV) throws ParseException, IOException {
		ModelAndView mw = new ModelAndView("admin/thongke");
		
		// check : Ph??n Quy???n 0: Qu???n L??, 1:Nh??n Vi??n. Ch???n Nh??n Vi??n Th???y
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		int slThe = 0;
		Date dateBD=new SimpleDateFormat("yyyy/MM/dd").parse(ngayBD.replace("-", "/"));
		Date dateKT=new SimpleDateFormat("yyyy/MM/dd").parse(ngayKT.replace("-", "/"));
		
		List<The> theServices = theService.findBetweenNgayDKTenLop(dateBD,dateKT,tenLopDV);
		
		for(The theService1:theServices )						
				slThe += 1;
		
		List<LopDV> lopDVs = lopDVService.listAll();
		
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("flag", "dv");
		mw.addObject("theServiceDV", theServices);
		mw.addObject("slThe", slThe);
		mw.addObject("tenLopDV", tenLopDV);
		mw.addObject("namBDDV", ngayBD);
		mw.addObject("namKTDV", ngayKT);
		
		return mw;
	}
	
	
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== T??I KHO???N
	 * ===================================================
	 */

	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== UPLOAD FILE - C?? Ph??p SEND EMAIL - CHECK DATE - CHECK H???T H???N TH??? T???P
	 * ===================================================
	 */
	//
	//================ Check h???t h???n th??? t???p kh??ch h??ng ????ng k??
	@RequestMapping(value = "kiemtrahethan", method = RequestMethod.POST)
	public ModelAndView KiemTraTheTapHH() {
		ModelAndView mw = new ModelAndView("apikiemtrahethan");
		Date date = new Date();
		List<The> checkNgayHH = theService.selectByNgayHH(date);
		
		for(The checkHH:checkNgayHH)
			if(checkHH.getTrangThai().trim().equals("Ho???t ?????ng")) theService.updateByMaThe("H???t H???n",checkHH.getMaThe());
		
		return mw;
	}
	
	//================ C?? Ph??p G???i Mail
	public String sendSimpleEmail() throws MessagingException{
	        MimeMessage messages = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(messages, true, "UTF-8");
	       
	        helper.setTo("dotam5020@gmail.com");
	        helper.setSubject("A file for you");
	        String html_HoaDon="\r\n"
	        		+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /><title>NDTGYM Confirm</title><style type=\"text/css\">\r\n"
	        		+ "    /* Take care of image borders and formatting, client hacks */\r\n"
	        		+ "    img { max-width: 600px; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;}\r\n"
	        		+ "    a img { border: none; }\r\n"
	        		+ "    table { border-collapse: collapse !important;}\r\n"
	        		+ "    #outlook a { padding:0; }\r\n"
	        		+ "    .ReadMsgBody { width: 100%; }\r\n"
	        		+ "    .ExternalClass { width: 100%; }\r\n"
	        		+ "    .backgroundTable { margin: 0 auto; padding: 0; width: 100% !important; }\r\n"
	        		+ "    table td { border-collapse: collapse; }\r\n"
	        		+ "    .ExternalClass * { line-height: 115%; }\r\n"
	        		+ "    .container-for-gmail-android { min-width: 600px; }\r\n"
	        		+ "\r\n"
	        		+ "\r\n"
	        		+ "    /* General styling */\r\n"
	        		+ "    * {\r\n"
	        		+ "      font-family: Helvetica, Arial, sans-serif;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    body {\r\n"
	        		+ "      -webkit-font-smoothing: antialiased;\r\n"
	        		+ "      -webkit-text-size-adjust: none;\r\n"
	        		+ "      width: 100% !important;\r\n"
	        		+ "      margin: 0 !important;\r\n"
	        		+ "      height: 100%;\r\n"
	        		+ "      color: #676767;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    td {\r\n"
	        		+ "      font-family: Helvetica, Arial, sans-serif;\r\n"
	        		+ "      font-size: 14px;\r\n"
	        		+ "      color: #777777;\r\n"
	        		+ "      text-align: center;\r\n"
	        		+ "      line-height: 21px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    a {\r\n"
	        		+ "      color: #676767;\r\n"
	        		+ "      text-decoration: none !important;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .pull-left {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .pull-right {\r\n"
	        		+ "      text-align: right;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .header-lg,\r\n"
	        		+ "    .header-md,\r\n"
	        		+ "    .header-sm {\r\n"
	        		+ "      font-size: 32px;\r\n"
	        		+ "      font-weight: 700;\r\n"
	        		+ "      line-height: normal;\r\n"
	        		+ "      padding: 35px 0 0;\r\n"
	        		+ "      color: #4d4d4d;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .header-md {\r\n"
	        		+ "      font-size: 24px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .header-sm {\r\n"
	        		+ "      padding: 5px 0;\r\n"
	        		+ "      font-size: 18px;\r\n"
	        		+ "      line-height: 1.3;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .content-padding {\r\n"
	        		+ "      padding: 20px 0 5px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-header-padding-right {\r\n"
	        		+ "      width: 290px;\r\n"
	        		+ "      text-align: right;\r\n"
	        		+ "      padding-left: 10px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-header-padding-left {\r\n"
	        		+ "      width: 290px;\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      padding-left: 10px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .free-text {\r\n"
	        		+ "      width: 100% !important;\r\n"
	        		+ "      padding: 10px 60px 0px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .button {\r\n"
	        		+ "      padding: 30px 0;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "\r\n"
	        		+ "    .mini-block {\r\n"
	        		+ "      border: 1px solid #e5e5e5;\r\n"
	        		+ "      border-radius: 5px;\r\n"
	        		+ "      background-color: #ffffff;\r\n"
	        		+ "      padding: 12px 15px 15px;\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      width: 253px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mini-container-left {\r\n"
	        		+ "      width: 278px;\r\n"
	        		+ "      padding: 10px 0 10px 15px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mini-container-right {\r\n"
	        		+ "      width: 278px;\r\n"
	        		+ "      padding: 10px 14px 10px 15px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .product {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      vertical-align: top;\r\n"
	        		+ "      width: 175px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .total-space {\r\n"
	        		+ "      padding-bottom: 8px;\r\n"
	        		+ "      display: inline-block;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .item-table {\r\n"
	        		+ "      padding: 50px 20px;\r\n"
	        		+ "      width: 560px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .item {\r\n"
	        		+ "      width: 300px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-hide-img {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      width: 125px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .mobile-hide-img img {\r\n"
	        		+ "      border: 1px solid #e6e6e6;\r\n"
	        		+ "      border-radius: 4px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .title-dark {\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      border-bottom: 1px solid #cccccc;\r\n"
	        		+ "      color: #4d4d4d;\r\n"
	        		+ "      font-weight: 700;\r\n"
	        		+ "      padding-bottom: 5px;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .item-col {\r\n"
	        		+ "      padding-top: 20px;\r\n"
	        		+ "      text-align: left;\r\n"
	        		+ "      vertical-align: top;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "    .force-width-gmail {\r\n"
	        		+ "      min-width:600px;\r\n"
	        		+ "      height: 0px !important;\r\n"
	        		+ "      line-height: 1px !important;\r\n"
	        		+ "      font-size: 1px !important;\r\n"
	        		+ "    }\r\n"
	        		+ "\r\n"
	        		+ "  </style><style type=\"text/css\" media=\"screen\">\r\n"
	        		+ "    @import url(http://fonts.googleapis.com/css?family=Oxygen:400,700);\r\n"
	        		+ "  </style><style type=\"text/css\" media=\"screen\">\r\n"
	        		+ "    @media screen {\r\n"
	        		+ "      /* Thanks Outlook 2013! */\r\n"
	        		+ "      * {\r\n"
	        		+ "        font-family: 'Oxygen', 'Helvetica Neue', 'Arial', 'sans-serif' !important;\r\n"
	        		+ "      }\r\n"
	        		+ "    }\r\n"
	        		+ "  </style><style type=\"text/css\" media=\"only screen and (max-width: 480px)\">\r\n"
	        		+ "    /* Mobile styles */\r\n"
	        		+ "    @media only screen and (max-width: 480px) {\r\n"
	        		+ "\r\n"
	        		+ "      table[class*=\"container-for-gmail-android\"] {\r\n"
	        		+ "        min-width: 290px !important;\r\n"
	        		+ "        width: 100% !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      img[class=\"force-width-gmail\"] {\r\n"
	        		+ "        display: none !important;\r\n"
	        		+ "        width: 0 !important;\r\n"
	        		+ "        height: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      table[class=\"w320\"] {\r\n"
	        		+ "        width: 320px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "\r\n"
	        		+ "      td[class*=\"mobile-header-padding-left\"] {\r\n"
	        		+ "        width: 160px !important;\r\n"
	        		+ "        padding-left: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class*=\"mobile-header-padding-right\"] {\r\n"
	        		+ "        width: 160px !important;\r\n"
	        		+ "        padding-right: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"header-lg\"] {\r\n"
	        		+ "        font-size: 24px !important;\r\n"
	        		+ "        padding-bottom: 5px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"content-padding\"] {\r\n"
	        		+ "        padding: 5px 0 5px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "       td[class=\"button\"] {\r\n"
	        		+ "        padding: 5px 5px 30px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class*=\"free-text\"] {\r\n"
	        		+ "        padding: 10px 18px 30px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"mobile-hide-img\"] {\r\n"
	        		+ "        display: none !important;\r\n"
	        		+ "        height: 0 !important;\r\n"
	        		+ "        width: 0 !important;\r\n"
	        		+ "        line-height: 0 !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"item\"] {\r\n"
	        		+ "        width: 140px !important;\r\n"
	        		+ "        vertical-align: top !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"quantity\"] {\r\n"
	        		+ "        width: 50px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class~=\"price\"] {\r\n"
	        		+ "        width: 90px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"item-table\"] {\r\n"
	        		+ "        padding: 30px 20px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "\r\n"
	        		+ "      td[class=\"mini-container-left\"],\r\n"
	        		+ "      td[class=\"mini-container-right\"] {\r\n"
	        		+ "        padding: 0 15px 15px !important;\r\n"
	        		+ "        display: block !important;\r\n"
	        		+ "        width: 290px !important;\r\n"
	        		+ "      }\r\n"
	        		+ "    }\r\n"
	        		+ "  </style></head><body bgcolor=\"#f7f7f7\"><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"container-for-gmail-android\" width=\"100%\"><tr><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#ffffff\" background=\"http://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg\" style=\"background-color:transparent\"><tr><td width=\"100%\" height=\"80\" valign=\"top\" style=\"text-align: center; vertical-align:middle;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"pull-left mobile-header-padding-left\" style=\"vertical-align: middle;\"><a class=\"header-md\" href=\"\">Xin ch??o, "+"" +"</a></td></tr></table></center></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7;\" class=\"content-padding\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"header-lg\">\r\n"
	        		+ "              Thanh To??n Th??nh C??ng!\r\n"
	        		+ "            </td></tr><tr><td class=\"free-text\">\r\n"
	        		+ "             Ch??n th??nh c???m ??n Qu?? Kh??ch ???? ?????ng h??nh c??ng FITNESSGYM.<br> Ch??c Qu?? Kh??ch h??ng c?? m???t tr???i nghi???m th???t t???t v?? th?? v???!!\r\n"
	        		+ "            </td></tr><tr><td class=\"button\"><div><a href=\"http://\"\r\n"
	        		+ "              style=\"background-color:#28a745;border-radius:5px;color:#ffffff;display:inline-block;font-family:'Cabin', Helvetica, Arial, sans-serif;font-size:14px;font-weight:regular;line-height:45px;text-align:center;text-decoration:none;width:155px;-webkit-text-size-adjust:none;mso-hide:all;\">????ng k?? d???ch v??? m???i</a></div></td></tr><tr><td class=\"w320\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-container-left\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Th??ng tin kh??ch h??ng</span><br />\r\n"
	        		+ "                                ????? T??m <br />\r\n"
	        		+ "                                0352615020 <br />\r\n"
	        		+ "                                dotam5020@gmail.com \r\n"
	        		+ "                              </td></tr></table></td></tr></table></td><td class=\"mini-container-right\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Th??ng Tin D???ch V???</span><br />\r\n"
	        		+ "                                Ng??y ????ng K??: 10-05-2021 <br /><span class=\"header-sm\">M?? H??a ????n</span><br />\r\n"
	        		+ "                                HD1\r\n"
	        		+ "                              </td></tr></table></td></tr></table></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #ffffff;  border-top: 1px solid #e5e5e5; border-bottom: 1px solid #e5e5e5;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"item-table\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td class=\"title-dark\" width=\"300\">\r\n"
	        		+ "                       D???ch V???\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"163\">\r\n"
	        		+ "                      G??i T???p\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"97\">\r\n"
	        		+ "                      T???ng Ti???n\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item\"><span style=\"color: #4d4d4d; font-weight:bold;\">Fitness</span></td><td class=\"item-col quantity\">\r\n"
	        		+ "                     th??ng\r\n"
	        		+ "                    </td><td class=\"item-col\">\r\n"
	        		+ "                      ???150,000.00\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item mobile-row-padding\"></td><td class=\"item-col quantity\"></td><td class=\"item-col price\"></td></tr><tr><td class=\"item-col item\"></td><td class=\"item-col quantity\" style=\"text-align:right; padding-right: 10px; border-top: 1px solid #cccccc;\"><span class=\"total-space\">T???ng ph???</span><br /><span class=\"total-space\">Thu???</span><br /><span class=\"total-space\" style=\"font-weight: bold; color: #4d4d4d\">Th??nh ti???n</span></td><td class=\"item-col price\" style=\"text-align: left; border-top: 1px solid #cccccc;\"><span class=\"total-space\">???150,000.00</span><br /><span class=\"total-space\">???0.00</span><br /><span class=\"total-space\" style=\"font-weight:bold; color: #4d4d4d\">???150,000.00</span></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7; height: 100px;\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td style=\"padding: 5px 0 10px\"><strong>97 Man Thi???n</strong><br />\r\n"
	        		+ "              Th??nh ph??? Th??? ?????c <br />\r\n"
	        		+ "              H??? Ch?? Minh <br /><br /></td></tr></table></center></td></tr></table></div></body></html>";
	        
	        		helper.setText(html_HoaDon, true);
	        this.javaMailSender.send(messages);
	        
			return "admin/sendemail";
		}
		
	//================ Test update Ng??y
	 @RequestMapping("testupdatengay")
	public String TestUpdateNgay() throws ParseException {
			
			LocalDate ngayBD1 = LocalDate.now();
			LocalDate ngayKT1 = ngayBD1.plusDays(1);
			System.out.print("ngayBD1==="+ngayBD1);
			System.out.print("ngayKT1==="+ngayKT1);
			Date ngayBD=new SimpleDateFormat("yyyy/MM/dd").parse((""+ngayBD1).replace("-", "/"));
			Date ngayKT= new SimpleDateFormat("yyyy/MM/dd").parse((""+ngayKT1).replace("-", "/"));
			
			
			theService.updateNgayByMaThe(ngayBD,ngayKT, "T1");
			
			return "test/testupdatengay";
		}
	
	 //=============== Test Session
	 @RequestMapping("testsession")
		public ModelAndView testsession(HttpSession session, HttpServletResponse response) {
			ModelAndView mw = new ModelAndView("testsession");
			if(!session.getAttribute("maQuyen").equals("0")) 
				mw.addObject("session", "1");
			if( !((""+session.getAttribute("maQuyen")).equals("0")))
				mw.addObject("session",session.getAttribute("maQuyen") );
			else 
			{
				try {
					response.sendRedirect("dangky");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return mw;
		}
	 
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== 
	 * ===================================================
	 */
	/*
	 * =============================================================================
	 * ============================
	 */

}
