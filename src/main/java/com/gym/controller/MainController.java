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
	 * ==================================================== CẤP QUYỀN: Nhân Viên chỉ có quyền Ở mục ĐĂNG KÝ KHÁCH HÀNG, HÓA ĐƠN, KHÁCH HÀNG, Quản Lý thì full quyền
	 * ===================================================
	 */
	/*
	 * ==================================================== TRANG CHỦ
	 * ===================================================
	 */
	//==============Show thông tin tài khoản nhân viên đăng nhập
	@RequestMapping("taikhoan")
	public ModelAndView showTKDN() {
		ModelAndView mw = new ModelAndView("admin/taikhoan");
		return mw;
	}
	// ============= Đăng nhập vào trang chủ
	@RequestMapping("home")
	public ModelAndView TrangChu(HttpServletResponse response, HttpSession session) throws IOException {
		ModelAndView mw = new ModelAndView("admin/trangchu");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}
		List <The> thes = theService.selectByTrangThai("Chưa Thanh Toán");
		LocalDate localDate = LocalDate.now();
	      String date = "" + localDate;
	      String[] dates = date.split("-");
		
		
		String namHienTai = dates[0];
		String thangHienTai = dates[1];
		float[] danhThuT = {0,0,0,0,0,0,0,0,0,0,0,0};//mảng chứa 12 tháng
		float[] doanhThuTCN = {0,0,0};
		int[] soLuongDV = {0,0,0,0};//4 loại dv chính
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
			List<String> sLKHs = new ArrayList<>(); // tạo danh sách KH ảo
			List<String> sLKHs1 = new ArrayList<>();// list remove duplicate
			while(true) {
				try {
					//Cộng tổng tiền thu được trong tháng
					danhThuT[i-1]+=hoaDons.get(a).getThehd().getGoiTap().getGia();
					sLKHs.add(hoaDons.get(a).getThehd().getKhachHang().getMaKH());
					
					a++;
				}
				catch (Exception e){
					break;
				}
			}
				// duplicate đếm số lượng kh
				Set<String> store = new HashSet<>();
				
		        for (String sLKH : sLKHs) {
		            if (!store.add(sLKH) == false) {
		            	sLKHs1.add(""+sLKH);
		            }
		        }
		        // đếm số lượng
		        soLuongKHT[i-1] = sLKHs1.size();
			
		}
		
		//============== THỐNG KÊ DOANH THU ============= 
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
        	fieldBDNCN[0] = "'Tháng  " + (maxIndexDT + 1) + "'";
        	fieldBDNCN[1] = "'Tháng  " + (maxIndexDT + 2) + "'";
        	fieldBDNCN[2] = "'Tháng  " + (maxIndexDT + 3) + "'";
        	
        }
        else if(maxIndexDT == 11) {
        	doanhThuTCN[0] = danhThuT[maxIndexDT - 2];
        	doanhThuTCN[1] = danhThuT[maxIndexDT - 1];
        	doanhThuTCN[2] = danhThuT[maxIndexDT];
        	fieldBDNCN[0] = "'Tháng  "+ (maxIndexDT + 1) + "'";
        	fieldBDNCN[1] = "'Tháng  "+ (maxIndexDT) + "'";
        	fieldBDNCN[2] = "'Tháng  "+ (maxIndexDT-1) + "'";
        }else {
        	doanhThuTCN[0] = danhThuT[maxIndexDT - 1];
        	doanhThuTCN[1] = danhThuT[maxIndexDT];
        	doanhThuTCN[2] = danhThuT[maxIndexDT + 1];
        	fieldBDNCN[0] = "'Tháng  "+(maxIndexDT) + "'";
        	fieldBDNCN[1] = "'Tháng  "+(maxIndexDT + 1) + "'";
        	fieldBDNCN[2] = "'Tháng  "+(maxIndexDT + 2) + "'";
        }
        
        
        //=============== THỐNG KÊ DỊCH VỤ TRONG THÁNG ================
        List <HoaDon> hoaDons1;
        //Xét tháng <10 => 0X
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
        
        
		//=========== THỐNG KÊ TOP 5 KHÁCH HÀNG =========
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
        
        //Sắp xếp khách Hàng giảm dần theo thời gian
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
        
        //Lấy 5 khách hàng vip nhất tháng
        try {
	        for( int id = 0; id < 5; id++) {
	        	List <KhachHang> KH = khachHangService.selectByMaKH(khachHangs.get(id).split("-")[0]);
	        	top5KHTiemNang.add(KH.get(0));
	        }
        }
        catch(Exception e) {}
		//============================ Lấy dữ liệu vào file trangchu.jsp
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

	// ============= đăng xuất khỏi trang chủ
	@RequestMapping("logout")
	public String DangXuat(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		// Xóa session
		session.removeAttribute("username");
		// Xóa cookie
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
	 * ==================================================== DỊCH VỤ
	 * ===================================================
	 */
	// ======== Danh sách Dịch Vụ File sidebar.jsp trả về file lopdv.jsp
	@RequestMapping("lopdv")
	public ModelAndView ListDichVu(HttpSession session, HttpServletResponse response) throws IOException {
		ModelAndView mw = new ModelAndView("admin/lopdv");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}
		
		List<LopDV> lopDVServices = lopDVService.listAll();
		mw.addObject("lopDVServices", lopDVServices);
		
		return mw;
		
		
	}

	// ============= Thêm Dịch Vụ khi nhấn onclick = them File lopdv.jsp
	@RequestMapping(value = "lopdv", method = RequestMethod.POST)
	public ModelAndView ThemDichVu(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws IOException {

		ModelAndView mw = new ModelAndView("admin/lopdv");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		Enumeration<String> parameterNames = request.getParameterNames();
		List<String> paramNames = new ArrayList<>();
		int[] flagParams = { 0, 0, 0, 0, 0 }; // format {ten,goi1,goi2,goi3,goi4}

		// xác định các giá trị gửi lên từ browser
		// liệt kê hết thì trả về false, chưa hết thì true
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
		// check giá tiền là số
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

			// lấy 2 ký tự của mã lớp
			String prefixMaLop = request.getParameter("tenlop").trim().substring(0, 1) + request.getParameter("tenlop")
					.trim().substring(request.getParameter("tenlop").trim().length() - 1);

			List<LopDV> lopDVServices = lopDVService.listAll();
			// ============== Tự động lấy mã lớp =============
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

			// ================ Thêm lớp dv file lopdv.jsp btn_Thêm
			LopDV lopDV = new LopDV();
			lopDV.setMaLop(maLop);
			lopDV.setTenLop(request.getParameter("tenlop").trim());
			lopDV.setTrangThai(1);
			lopDVService.save(lopDV);

			// them gói tập nếu có
			if (flagParams[1] == 2) {
				GoiTap goiTapNgay = new GoiTap();
				// format MaGoiTap + MaLop + TenGoiTap + ThoiHan + Gia
				goiTapNgay.setMaGoiTap("GTNG" + maLop.substring(2));
				goiTapNgay.setLopDV(lopDV);
				goiTapNgay.setTenGoiTap("ngày");
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
				goiTapTuan.setTenGoiTap("tuần");
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
				goiTapThang.setTenGoiTap("tháng");
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
				goiTapNam.setTenGoiTap("năm");
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

	// ============= Thêm gói tập goitap?id file lopdv
	@RequestMapping(value = "goitap", params = { "id" }, method = RequestMethod.GET)
	public ModelAndView ThemGoiTapDV(@RequestParam("id") String maLop, HttpSession session,
			HttpServletResponse response) throws IOException {

		ModelAndView mw = new ModelAndView("admin/goitap");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
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
		// nếu chưa có dịch vụ thì out về lopdv.jsp
		mw = new ModelAndView("redirect:lopdv");
		return mw;
	}

	// ====================== Sau khi thêm gói tập or chỉnh sửa dịch vụ thì
	// updatelopdv file goitap.jsp
	@RequestMapping(value = "updatelopdv", method = RequestMethod.POST)
	public ModelAndView UpdateLopDV(HttpSession session, HttpServletResponse response,
			@RequestParam("malop") String maLop1, @RequestParam("tengoitap") String tenGoiTap,
			@RequestParam("gia") String gia) throws IOException {
		ModelAndView mw = new ModelAndView("admin/goitap");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		String maLop = maLop1.trim();
		mw.addObject("thongbao", 0);
		List<LopDV> checkLopDV = lopDVService.selectByMaLop(maLop.trim());

		kiemTraLoi: try {
			float giaGoiTap = Float.parseFloat(gia);

			// kiem tra dịch vụ có tồn tại không
			if (checkLopDV.size() > 0) {
				GoiTap goiTap = new GoiTap();
				int thoiHan = 0;

				// Tự động lấy mã gói tập
				// format GT + Thoihan + Tengoitap + số của mã Lớp
				String maGT = "GT";
				if (Integer.parseInt(tenGoiTap.trim().split(" ")[0]) > 1) {
					if (tenGoiTap.trim().split(" ")[1].equals("ngày")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 7) {
						maGT += tenGoiTap.trim().split(" ")[0] + "NG" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]);
					} else if (tenGoiTap.trim().split(" ")[1].equals("tuần")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 4) {
						maGT += tenGoiTap.trim().split(" ")[0] + "T" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]) * 7;
					} else if (tenGoiTap.trim().split(" ")[1].equals("tháng")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 12) {
						maGT += tenGoiTap.trim().split(" ")[0] + "TH" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]) * 30;
					} else if (tenGoiTap.trim().split(" ")[1].equals("năm")
							&& Integer.parseInt(tenGoiTap.trim().split(" ")[0]) < 11) {
						maGT += tenGoiTap.trim().split(" ")[0] + "N" + maLop.substring(2);
						thoiHan = Integer.parseInt(tenGoiTap.trim().split(" ")[0]) * 365;
					} else
						break kiemTraLoi;
				} else {
					if (tenGoiTap.trim().split(" ")[1].equals("ngày")) {
						maGT += "NG" + maLop.substring(2);
						thoiHan = 1;
					} else if (tenGoiTap.trim().split(" ")[1].equals("tuần")) {
						maGT += "T" + maLop.substring(2);
						thoiHan = 7;
					} else if (tenGoiTap.trim().split(" ")[1].equals("tháng")) {
						maGT += "TH" + maLop.substring(2);
						thoiHan = 30;
					} else if (tenGoiTap.trim().split(" ")[1].equals("năm")) {
						maGT += "N" + maLop.substring(2);
						thoiHan = 365;
					} else
						break kiemTraLoi;

				}

				// check trùng gói tập trong dv
				List<GoiTap> checkGoiTap = goiTapService.selectByMaGT(maGT);
				if (checkGoiTap.size() > 0)
					break kiemTraLoi;

				// nếu trùng thì nhập lại
				goiTap.setMaGoiTap(maGT);
				goiTap.setTenGoiTap(tenGoiTap.trim());
				goiTap.setThoiHan(thoiHan);
				goiTap.setTrangThai(1);// mặc định trạng thái sẽ bằng 1
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
	
	// ================= Xóa dịch vụ nếu chưa có gói tập xoalopdv?id file lopdv
	@RequestMapping(value = "xoalopdv", method = RequestMethod.POST)
	public ModelAndView XoaDichVu(HttpSession session,HttpServletResponse response,@RequestParam("maLop") String maLop) throws IOException{
		ModelAndView mw = new ModelAndView("apixoalopdv");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}
		
		List<GoiTap> goiTaps = goiTapService.selectByMaLop(maLop);
		if (goiTaps.isEmpty()) {
			lopDVService.delete(maLop);
			mw.addObject("thongbaoxoa", 1);// xóa thành công
		}
		else 
			mw.addObject("thongbaoxoa", 0);//xóa thất bại
		
		return mw;
	}

	// ================= Xóa Gói tập nếu khách hàng chưa đăng kí
	@RequestMapping(value = "xoagoitap", method = RequestMethod.POST)
	public ModelAndView XoaGoiTap(HttpSession session, HttpServletResponse response, @RequestParam("maGT") String maGT)
			throws IOException {
		ModelAndView mw = new ModelAndView("apixoagoitap");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if (!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		mw.addObject("thongbaoxoa", 0);
		List<The> theGT = theService.selectByMaGT(maGT);
		// Check Trạng Thái GT = KDK ? , khong -> xoa
		if (theGT.size() == 0) {

			goiTapService.delete(maGT);
			mw.addObject("thongbaoxoa", 1);// trả về giá trị cho api => return = 1
		}
		return mw;
	}
	
	
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== ĐĂNG KÝ TẬP
	 * ===================================================
	 */
	//=============== Hiển thị trang đăng kí khách hàng file sidebar trả về file dangky.jsp
	@RequestMapping("dangky")
	public String DangKyKH() {
		return "admin/dangky";
	}
	//================= Thêm mới 1 khách hàng file dangky.jsp
	@RequestMapping(value = "dangkykhachhang", method = RequestMethod.POST)
	public ModelAndView ThemKhachHang(@RequestParam("hovaten") String hoVaTen, @RequestParam("ngaysinh") String ngaySinh, 
			@RequestParam("email") String email, @RequestParam("sdt")  String sdt, @RequestParam("diachi") String diaChi, 
			@RequestParam("gioitinh") String gioiTinh,@RequestParam("avatar") MultipartFile file) {
		
		// kiểm tra trùng email. mỗi KH có 1 email duy nhất
		List<KhachHang> emailKH = khachHangService.selectByEmail(email);
		if(emailKH.isEmpty() && !email.isEmpty() && !hoVaTen.trim().isEmpty()) {
			List<KhachHang> khachHangSort = khachHangService.selectSortMaKh(); 
			KhachHang khachHang = new KhachHang();
			//============== Tự động lấy mã KH ============= 
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
			
		    //Upload File Ảnh
			String thongbao = "";
			String extensionFile = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			if (!file.isEmpty() && (extensionFile.equals("jpg") || extensionFile.equals("png")) ) {
						
				try {
					// Định dạng tên file: vd nv1_dotam.jpg
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
				thongbao = "Không phải file ảnh";
			
			System.out.println("Upload File = " + thongbao);
		
			khachHang.setMaKH(maKHDV);
			khachHang.setTenKH(hoVaTen);
			khachHang.setEmail(email);
			khachHang.setGioiTinh(gioiTinh);
			khachHang.setSdt(sdt);
			khachHang.setNgaySinh(null);//cho phép null
			khachHang.setDiaChi(diaChi);
			
			//check file ảnh rỗng
			if(extensionFile.isEmpty()) 
				khachHang.setAnh(null);
			else 
				khachHang.setAnh(maKHDV +  "." + extensionFile);
			
			khachHangService.save(khachHang);
			ModelAndView mw =new ModelAndView("redirect:dichvu?id="+maKHDV);
			mw.addObject("thongbao", "Đăng ký Khách Hàng thành công");//thành công thì trả kết quả thongbao về JS file dichvu.jsp
			return mw;
		}
		
		ModelAndView mw = new ModelAndView("admin/dangky");
		mw.addObject("thongbaoloi", "Email hoặc Thông Tin không chính xác!");//thất bại thì trả kết quả thongbaoloi về JS file dangky.jsp
		mw.addObject("hoVaTen",hoVaTen);
		mw.addObject("ngaySinh",ngaySinh);
		mw.addObject("sdt",sdt);
		mw.addObject("gioiTinh",gioiTinh);
		mw.addObject("diaChi",diaChi);
		return mw;
	}
	
	//================= Đăng kí dịch vụ sau khi đăng kí khách hàng thành công file dichvu.jsp
	@RequestMapping(value = "dangkydichvu", method = RequestMethod.POST)
	public ModelAndView DangKyDVKH(@RequestParam("maKH") String maKH, @RequestParam("lopDV") String tenLopDV, 
			@RequestParam("goiTap") String goiTap) {
		ModelAndView mw;
		
		// Kiểm tra gói tập có trạng thái  = 1 
			int flag = 0;
			List<GoiTap> checkGoiTap = goiTapService.selectByTenLopTrangThai(tenLopDV);
			
			for(GoiTap tenGoiTap:checkGoiTap)
				if(tenGoiTap.getTenGoiTap().equals(goiTap.trim())) flag=1;
			
			List<KhachHang> khachHangs = khachHangService.selectByMaKH(maKH.trim());
			
			if(flag == 1 && khachHangs.size() == 1 ) {
				List<The> TheSort = theService.selectSortMaThe();
				//Tự động lấy mã thẻ mới
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
				
				// Lấy Gói Tập theo Mã Lớp
				goiTap = goiTap.trim();
				List<LopDV> lopDVs = lopDVService.selectByTenLop(tenLopDV);
				 
				List<GoiTap> goiTaps = goiTapService.selectByMaLopTenGoiTap(lopDVs.get(0).getMaLop(), goiTap);
				
				//Tạo thực thể
				Date date = new Date();
				
				The the = new The();
				the.setMaThe(maTDV);
				the.setKhachHang(khachHangs.get(0));
				the.setGoiTap(goiTaps.get(0));
				the.setNgayDK(date);
				the.setTrangThai("Chưa Thanh Toán");
				
				theService.save(the);
				 mw = new ModelAndView("redirect:hoadon?id=" + maTDV);
			}
			else  mw = new ModelAndView("redirect:logout");//???
		return mw;
	}
	
	//================= Thanh Toán sau khi đăng kí và xét cả Khi đăng kí và chưa thanh toán JS hoadon?id=maThe file user.jsp
	@RequestMapping(value = "hoadon", params = {"id"}, method = RequestMethod.GET)
	public ModelAndView LapHoaDon(@RequestParam("id") String maThe, HttpServletResponse response) {
		
		ModelAndView mw = new ModelAndView("admin/hoadon");//trả về chi tiết hóa đơn file hoadon.jsp
		List<The> thes = theService.selectByMaThe(maThe);
		mw.addObject("thes", thes);
		return mw;
	}
	
	//================ Thực Hiện thanh toán hóa đơn file hoadon.jsp khi nhấn Thanh Toán
	//định dạng thời gian theo month/day/year
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="MM/DD/YYYY")
	@RequestMapping(value = "hoadon", method = RequestMethod.POST)
	public ModelAndView ThanhToanHoaDon(HttpSession session, ModelMap modelMap,@RequestParam("id") String maThe, HttpServletResponse response) throws MessagingException {
		
		//lấy tên tài khoản đăng nhập của nhân viên để thanh toán hóa đơn (username trong login.jsp)
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
			
			//Tự động lấy mã hóa đơn
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
			int updateTT= theService.updateByMaThe("Hoạt Động", maThe);
			
			// Gửi mail thông báo Thanh Toán Hóa Đơn
			List<The> thesMail= theService.selectByMaThe(maThe);
			MimeMessage messages = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(messages, true, "UTF-8");
	       
	        helper.setTo(thesMail.get(0).getKhachHang().getEmail());
	        helper.setSubject("Thanh Toán Dịch Vụ");
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
	        		+ "  </style></head><body bgcolor=\"#f7f7f7\"><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"container-for-gmail-android\" width=\"100%\"><tr><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#ffffff\" background=\"http://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg\" style=\"background-color:transparent\"><tr><td width=\"100%\" height=\"80\" valign=\"top\" style=\"text-align: center; vertical-align:middle;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"pull-left mobile-header-padding-left\" style=\"vertical-align: middle;\"><a class=\"header-md\" href=\"\">Xin chào, " + thesMail.get(0).getKhachHang().getTenKH() + "</a></td></tr></table></center></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7;\" class=\"content-padding\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"header-lg\">\r\n"
	        		+ "              Thanh Toán Thành Công!\r\n"
	        		+ "            </td></tr><tr><td class=\"free-text\">\r\n"
	        		+ "              Chân thành cảm ơn Quý Khách đã đồng hành cùng FITNESSGYM.<br> Chúc Quý Khách hàng có một trải nghiệm thật tốt và thú vị!!\r\n"
	        		+ "            </td></tr><tr><td class=\"button\"><div><a href=\"http://\"\r\n"
	        		+ "              style=\"background-color:#28a745;border-radius:5px;color:#ffffff;display:inline-block;font-family:'Cabin', Helvetica, Arial, sans-serif;font-size:14px;font-weight:regular;line-height:45px;text-align:center;text-decoration:none;width:155px;-webkit-text-size-adjust:none;mso-hide:all;\">Đăng ký dịch vụ mới</a></div></td></tr><tr><td class=\"w320\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-container-left\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Thông tin khách hàng</span><br />\r\n"
	        		+ "                                " + thesMail.get(0).getKhachHang().getTenKH() + " <br />\r\n"
	        		+ "                                " + thesMail.get(0).getKhachHang().getSdt() + " <br />\r\n"
	        		+ "                                " + thesMail.get(0).getKhachHang().getEmail() + " \r\n"
	        		+ "                              </td></tr></table></td></tr></table></td><td class=\"mini-container-right\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Thông Tin Dịch Vụ</span><br />\r\n"
	        		+ "                                Ngày Đăng Ký: " + thesMail.get(0).getNgayDK() + " <br /><span class=\"header-sm\">Mã Hóa Đơn</span><br />\r\n"
	        		+ "                                #" + maHDMail + "\r\n"
	        		+ "                              </td></tr></table></td></tr></table></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #ffffff;  border-top: 1px solid #e5e5e5; border-bottom: 1px solid #e5e5e5;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"item-table\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td class=\"title-dark\" width=\"300\">\r\n"
	        		+ "                       Dịch Vụ\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"163\">\r\n"
	        		+ "                      Gói Tập\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"97\">\r\n"
	        		+ "                      Giá\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item\"><span style=\"color: #4d4d4d; font-weight:bold;\">"+thesMail.get(0).getGoiTap().getLopDV().getTenLop()+"</span></td><td class=\"item-col quantity\">\r\n"
	        		+ "                     " + thesMail.get(0).getGoiTap().getTenGoiTap() + "\r\n"
	        		+ "                    </td><td class=\"item-col\">\r\n"
	        		+ "                      ₫" + thesMail.get(0).getGoiTap().getGia() + "\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item mobile-row-padding\"></td><td class=\"item-col quantity\"></td><td class=\"item-col price\"></td></tr><tr><td class=\"item-col item\"></td><td class=\"item-col quantity\" style=\"text-align:right; padding-right: 10px; border-top: 1px solid #cccccc;\"><span class=\"total-space\">Tổng chi phí</span><br /><span class=\"total-space\">Thuế</span><br /><span class=\"total-space\" style=\"font-weight: bold; color: #4d4d4d\">Thành Tiền</span></td><td class=\"item-col price\" style=\"text-align: left; border-top: 1px solid #cccccc;\"><span class=\"total-space\">₫" + thesMail.get(0).getGoiTap().getGia() + "</span><br /><span class=\"total-space\">0.00₫</span><br /><span class=\"total-space\" style=\"font-weight:bold; color: #4d4d4d\">"+thesMail.get(0).getGoiTap().getGia()+"₫</span></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7; height: 100px;\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td style=\"padding: 5px 0 10px\"><strong>97 Man Thiện</strong><br />\r\n"
	        		+ "              Thành phố Thủ Đức <br />\r\n"
	        		+ "              Thành Phố Hồ Chí Minh <br /><br /></td></tr></table></center></td></tr></table></div></body></html>";
	        helper.setText(html_HoaDon, true);
	        this.javaMailSender.send(messages);
			
			ModelAndView mw = new ModelAndView("admin/hoadon");
			mw.addObject("updateTT", updateTT);//trả về JS trong hoadon.jsp
			System.out.println(updateTT);
			List<The> thes1 = theService.selectByMaThe(maThe);
			mw.addObject("thes", thes1);
			
			return mw;
		}

	
	
	//================= Lấy tên Gói Tập thuộc Dịch Vụ JS file dichvu.jsp
	@RequestMapping(value = "laytengoitap", method = RequestMethod.POST)
	public ModelAndView LayTenGoiTap(@RequestParam("lopDV") String lopDV ) throws IOException {
		ModelAndView mw = new ModelAndView("apilaytengoitap");//api
		
		List<GoiTap> checkGoiTap = goiTapService.selectByTenLopTrangThai(lopDV);
		String tenCacGoiTap = "";
		for (GoiTap goiTap:checkGoiTap) {
			tenCacGoiTap += goiTap.getTenGoiTap() + ",";
		}
		mw.addObject("tengoitaps", tenCacGoiTap.substring(0,tenCacGoiTap.length()-1));//gán cho api sau đó return về mw
		
		return mw;
		
	}
	
	//================= Lấy giá Gói Tập thuộc Dịch Vụ JS file dichvu.jsp
	@RequestMapping(value = "laygiagoitap", method=RequestMethod.POST)
	public ModelAndView LayGiaGoiTap(@RequestParam("lopDV") String lopDV, @RequestParam("goiTap") String goiTap ) {
		ModelAndView mw = new ModelAndView("apigetgiatien");//api
		
		goiTap = goiTap.trim();
		
		List<LopDV> lopDVs = lopDVService.selectByTenLop(lopDV);
		List<GoiTap> goiTaps = goiTapService.selectByMaLopTenGoiTap(lopDVs.get(0).getMaLop(), goiTap);
		
		mw.addObject("getGiaTien", goiTaps.get(0).getGia());//gán cho api sau đó return về mw
		return mw;
	}
	
	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== HÓA ĐƠN
	 * ===================================================
	 */
	//================= Hiển thị danh sách hóa đơn
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
	 * ==================================================== KHÁCH HÀNG
	 * ===================================================
	 */
	//================= Hiển thị danh sách Khách Hàng file bangusers.jsp => hiển thị list user
	@RequestMapping("bangusers")
	public ModelAndView BangKhachHang() {
		ModelAndView mw = new ModelAndView("admin/bangusers");
		
		List<KhachHang> khachHangServices = khachHangService.listAll();
		mw.addObject("khachHangServices", khachHangServices);
		return mw;
	}
	
	//================= Chỉnh sửa dịch vụ KH file banguser.jsp nhấn Dịch Vụ
	@RequestMapping(value = "dichvu" , params = {"id"}, method = RequestMethod.GET)
	public ModelAndView ChinhSuaDVKH(@RequestParam("id") String maKH) {
		List<The> TheSort = theService.selectSortMaThe();
		List<GoiTap> goiTaps = goiTapService.listAll();
		List<LopDV> lopDVs = lopDVService.listAll();

		String maTDV = "";
		LocalDate localDate = LocalDate.now(); //mặc định đang là thời gian hiện tại
		   
		//Tự động lấy mã thẻ tập
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
	
	//================= Chỉnh sửa thông tin KH file banguser.jsp nhấn Chỉnh Sửa
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
	
	//================= Update thông tin KH file banguser.jsp khi nhấn btn Cập nhật
	@RequestMapping(value = "updateuser", method = RequestMethod.POST)
	public ModelAndView UpdateKhachHang(@RequestParam("makh") String maKH, @RequestParam("sdt") String sdt, @RequestParam("hovaten") String hoVaTen,@RequestParam("gioitinh") String gioiTinh, @RequestParam("email") String email, @RequestParam("diachi") String diaChi, @RequestParam("ngaysinh") String ngaySinh, @RequestParam("avatar") MultipartFile file) throws ParseException {

		ModelAndView mw = new ModelAndView("redirect:user?id=" + maKH);//thành công trả về thongbao JS trong file user.jsp
		mw.addObject("thongbao", "0");//gán là fail
		
		// kiểm tra trùng email. mỗi KH có 1 email duy nhất
		KhachHang khachHang = new KhachHang();
		List<KhachHang> emailKH = khachHangService.selectByEmail(email);
		List<KhachHang> khachHangMaKH = khachHangService.selectByMaKH(maKH);
		
		if((emailKH.isEmpty()||khachHangMaKH.get(0).getEmail().equals(email) )&& !email.isEmpty()) {
			// Cập nhật thông tin
			try {
				String sDate1 = ngaySinh.replace("-", "/"); //nhập được ở 2 dạng 
				Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1); //năm/tháng/ngày 
				khachHang.setNgaySinh(date1);
			}
			catch(Exception e) {}
			
		    khachHang.setMaKH(maKH);
			khachHang.setTenKH(hoVaTen);
			khachHang.setEmail(email);
			khachHang.setSdt(sdt);
			khachHang.setDiaChi(diaChi);
			khachHang.setGioiTinh(gioiTinh);
			
			
			 //Upload File Ảnh
			String thongbao = "";
			String extensionFile = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			if (!file.isEmpty() && (extensionFile.equals("jpg") || extensionFile.equals("png")) ) {
					
					try {
						// Xóa Avatar cũ trước khi upload lại
						File avatar = new File(servletContext.getRealPath("resources/images/" + maKH + ".jpg"));
						if(avatar.exists()) 
							avatar.delete();
						else {
							avatar =new File(servletContext.getRealPath("resources/images/" + maKH + ".png"));
							avatar.delete();
						}
			
						// Định dạng tên file: vd nv1_dotam.jpg
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
				thongbao = "Không phải file ảnh";
			
			if(extensionFile.isEmpty()) 
				khachHang.setAnh(khachHangMaKH.get(0).getAnh());
			else 
				khachHang.setAnh(maKH + "."+ extensionFile);
			
			khachHangService.save(khachHang);
			
			//mw = new ModelAndView("redirect:user?id=" + maKH);
			mw.addObject("thongbao", "1");//thành công
			return mw;
		}
		
		return mw;
		
		
	}
	
	
	//================= Xóa Thông tin khách hàng file banguser.jsp => thẻ tập xóa => trả về user.jsp
	//================= Xóa Khách Hàng use JS và api file  banguser.jsp
	@RequestMapping(value = "xoakhachhang", method = RequestMethod.POST)
	public ModelAndView XoaKhachHang(@RequestParam("maKH") String maKH) {
		ModelAndView mw = new ModelAndView("apixoakhachhang");//trả về kết quả thongbaoxoa = mw
		
		List<The> theKH = theService.selectByMaKHNotSort(maKH);
		
		// Nếu khách hàng chưa đăng kí thẻ tập thì xóa
		if(theKH.isEmpty()) {
			//Xóa avatar trước nếu có
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
	 * ==================================================== NHÂN VIÊN
	 * ===================================================
	 */
	 //================= Hiển thị danh sách Nhân Viên file sidebar.jsp trả về file nhanvien.jsp
	@RequestMapping("bangnhanvien")
	public ModelAndView DanhSachNV( HttpServletResponse response , HttpSession session) throws IOException {
		ModelAndView mw = new ModelAndView("admin/nhanvien");
		
		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		
		List<NhanVien> nhanViens = nhanVienService.listAll();
		mw.addObject("nhanViens", nhanViens);
		return mw;
	}
	
	//================== Thêm Nhân Viên khi nhấn onclick them file nhanvien.jsp
	@RequestMapping(value = "dangkynhanvien", method = RequestMethod.POST)
	public ModelAndView DangKyNhanVien(HttpServletResponse response, HttpSession session, @RequestParam("hovaten") String hoVaTen, 
			@RequestParam("email") String email, @RequestParam("sdt")  String sdt, @RequestParam("diachi") String diaChi, 
			@RequestParam("gioitinh") String gioiTinh, @RequestParam("chucvu") String chucVu, @RequestParam("username") String userName, @RequestParam("password") String password, @RequestParam("xnpassword") String xnpassword) throws IOException {
		
		ModelAndView mw = new ModelAndView("admin/nhanvien");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}
				
		if(password.equals(xnpassword) && password.trim().length()>5 && !chucVu.trim().isEmpty() && !hoVaTen.trim().isEmpty()&& !userName.trim().isEmpty() ){
			List<NhanVien> ktEmail = nhanVienService.selectByEmail(email);
			List<NhanVien> ktUserName = nhanVienService.selectByUserName(userName);
			
			//kiểm tra trùng email & username
			if(ktEmail.size()==0 && ktUserName.size()==0 ) {
				TaiKhoan taiKhoan = new TaiKhoan();
				List<PhanQuyen> phanQuyen;
				if(chucVu.equals("Quản Lý")) phanQuyen = phanQuyenService.selectByMaQuyen(0);
				else phanQuyen = phanQuyenService.selectByMaQuyen(1);
				
			
				taiKhoan.setUserName(userName);
				taiKhoan.setPassWord(password);
				taiKhoan.setTrangThai(1);//nhân viên
				taiKhoan.setPhanQuyen(phanQuyen.get(0));
				taiKhoanService.save(taiKhoan);
				
				
				
				NhanVien nhanVien = new NhanVien();
				List<NhanVien> nhanViens = nhanVienService.listAll();
				
				//Tự động lấy mã nhân viên 
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
				
				mw.addObject("thongbao", "0");//Thêm Nhân Viên thành công
				
			}else mw.addObject("thongbao", "1");//trùng usename hoặc email
				
		}
		else 
			mw.addObject("thongbao", "2");//Sai Password hoặc xác nhận password
		
		List<NhanVien> nhanViens = nhanVienService.listAll();
		mw.addObject("nhanViens", nhanViens);
		
		return mw;
	}
	
	//================== Chỉnh Sửa thông tin Nhân Viên theo maNV khi nhấn nhavien?id=maNV file nhanvien.jsp
	@RequestMapping(value = "nhanvien", params = {"id"}, method = RequestMethod.GET)
	public ModelAndView ChinhSuaNhanVien( HttpSession session, HttpServletResponse response, @RequestParam("id") String maNV) throws IOException {
		ModelAndView mw =new ModelAndView("admin/chitietnhanvien");
		
		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		List<NhanVien> nhanViens = nhanVienService.selectByMaNV(maNV);
		mw.addObject("nhanVien", nhanViens);
		return mw;
	}
	
	//=================== Update Nhân Viên sau khi chỉnh sửa xong và nhấn btn save file chitietnhanvien.jsp
	@RequestMapping(value = "updatenhanvien", method = RequestMethod.POST)
	public ModelAndView UpdateNhanVien(HttpServletResponse response, HttpSession session, @RequestParam("manv") String maNV, @RequestParam("hovaten") String hoVaTen, 
			@RequestParam("email") String email, @RequestParam("sdt") String sdt, @RequestParam("diachi") String diaChi, 
			@RequestParam("gioitinh") String gioiTinh,@RequestParam("chucvu") String chucVu) throws IOException {
		
		ModelAndView mw = new ModelAndView("admin/chitietnhanvien");
		
		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}
		
		List<NhanVien> nhanViens = nhanVienService.selectByMaNV(maNV);
		mw.addObject("thongbao", "0");
		List<NhanVien> ktEmail = nhanVienService.selectByEmail(email);

		//kiểm tra nếu nhân viên chưa lập hóa đơn hoặc chưa có email thì có thể chỉnh sửa thông tin
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
		//Truy xuất dữ liệu sau khi Update nV
	    nhanViens = nhanVienService.selectByMaNV(maNV);
		mw.addObject("nhanVien", nhanViens);
		return mw;
		
		
	}
	
	//=================== Quản Lý có quyền Khóa Tài Khoản Nhân Viên khi nhấn JS khóa tài khoản file nhanvien.jsp
	@RequestMapping(value = "khoataikhoan", method = RequestMethod.POST)
	public ModelAndView KhoaTaiKhoan(HttpSession session,HttpServletResponse response,@RequestParam("maNV")String maNV,@RequestParam("checked")String checked ) throws IOException {
		ModelAndView mw = new ModelAndView("apikhoataikhoan");//trả về thongbaoupdate cho api và result kq = mw
		
		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
		if(!session.getAttribute("maQuyen").equals("0")) {
			response.sendRedirect("dangky");
		}

		List<NhanVien> nhanVien = nhanVienService.selectByMaNV(maNV);
		if(!nhanVien.get(0).getTaiKhoan().getPhanQuyen().getChucVu().equals("Quản Lý")) {
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
	
	//=================== Update Tài Khoản Nhân Viên sau khi chỉnh sửa xong  file chitietnhanvien.jsp
		//chỉ update tài khoản chưa có hoặc đổi quyền, tài khoản đã có thì ko update
		@RequestMapping(value = "updatetaikhoan", method = RequestMethod.POST)
		public ModelAndView UpdateTaiKhoan(HttpServletResponse response, HttpSession session, @RequestParam("manv") String maNV, @RequestParam("username") String userName, @RequestParam("password") String passWord, @RequestParam("maquyen") String maQuyen, @RequestParam("trangthai") String trangThai ) throws IOException {
			ModelAndView mw = new ModelAndView("admin/chitietnhanvien");
			
			// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
					if(!session.getAttribute("maQuyen").equals("0")) {
						response.sendRedirect("dangky");
					}


			int tb = 4;// giả sử cập nhật NV thành công
			List<TaiKhoan> taiKhoans = taiKhoanService.selectByUserName(userName);
			List<NhanVien> nhanViens = nhanVienService.selectByMaNV(maNV);
			
			mw.addObject("thongbao", "3");//3: username define
		
			//check tài khoản để không bị trùng , khác root
			if(taiKhoans.size() > 0 && !taiKhoans.get(0).getUserName().trim().equals("root") && taiKhoans.get(0).getUserName().equals(nhanViens.get(0).getTaiKhoan().getUserName().trim())) {
				TaiKhoan taiKhoan = new TaiKhoan();	
				
				//================================== Check Phân Quyền=================
				List<PhanQuyen> phanQuyen;
				// Bắt buộc >2 Quản lý mới cho đổi quyền từ 1->0 (Quản Lý -> Nhân Viên)
				if(maQuyen.trim().equals("0")) 
					phanQuyen = phanQuyenService.selectByMaQuyen(0);
				else if (nhanViens.get(0).getTaiKhoan().getUserName().trim().equals(session.getAttribute("username"))) {
					phanQuyen = phanQuyenService.selectByMaQuyen(0);//qly
					tb += 1;//5: không thay đổi quyền của chính bạn
				}
				else phanQuyen = phanQuyenService.selectByMaQuyen(1);//nvien
				
				
				//================================== Check Tài Khoản=================
				taiKhoan.setUserName(userName);
				if(passWord.trim().length() > 5 ) { 
					taiKhoan.setPassWord(passWord);
					mw.addObject("thongbaopass", "1");//1: change pass success
				}
				else {
					taiKhoan.setPassWord(nhanViens.get(0).getTaiKhoan().getPassWord());
					tb += 2;//6: pass ko hợp lệ <=5
				}
				
				//================================== Check Trạng Thái (username: tài khoản của bạn đang đăng nhập) 0-Khóa; 1- HĐ=================
				if( trangThai.trim().equals("1") || (trangThai.trim().equals("0") && !nhanViens.get(0).getTaiKhoan().getUserName().trim().equals(session.getAttribute("username"))  ) ) 
					taiKhoan.setTrangThai(Integer.parseInt(trangThai));
			    else 
			    {
			    	taiKhoan.setTrangThai(nhanViens.get(0).getTaiKhoan().getTrangThai());
			    	/*8: không thay đổi được trạng thái của chính bạn
			    	 *5 + 4 = 9: không thay đổi quyền & không thay đổi được trạng thái của chính bạn
			    	 *6 + 4 = 10: không thay đổi được trạng thái của chính bạn & pass ko hợp lệ */
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
	 * ==================================================== THỐNG KÊ
	 * ===================================================
	 */
	//======================= Vào Thống Kê file thongke.jsp
	@RequestMapping("thongke")
	public ModelAndView ThongKe(HttpSession session,HttpServletResponse response) throws ParseException, IOException {
		ModelAndView mw = new ModelAndView("admin/thongke");

		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}

		float tongTien=0;	
		LocalDate date = LocalDate.now();//lấy thời gian hiện tại
		
		String []dates = ("" + date).split("-");
		String namBD = dates[0] + "/" + dates[1] + "/01";//bắt đầu từ năm ..01
		String namKT = dates[0] + "/" + dates[1] + "/" + dates[2];
		
		Date dateBD = new SimpleDateFormat("yyyy/MM/dd").parse(namBD);
		Date dateKT = new SimpleDateFormat("yyyy/MM/dd").parse(namKT);
		
		List<The> theServices = theService.findBetweenNgayDK(dateBD,dateKT );//từ ngày... đến ngày...
		List<The> theServicess = new ArrayList<The>();
		
		for(The theService1:theServices ) {
			if(theService1.getTrangThai().trim().equals("Hoạt Động")||theService1.getTrangThai().trim().equals("Hết Hạn") ) {
				tongTien += theService1.getGoiTap().getGia();//Thẻ hết hạn + thẻ hoạt động
				theServicess.add(theService1);
			}
			
		}
		System.out.println(""+dates[0]+"-"+dates[1]);
		
		List<LopDV> lopDVs = lopDVService.listAll();
		
		mw.addObject("lopDVs", lopDVs);
		mw.addObject("theServices", theServicess);
		mw.addObject("tongTien", tongTien);
		mw.addObject("namBD", namBD.replace('/', '-'));//định dạng ở cả 2 / & -
		mw.addObject("namKT", namKT.replace('/', '-'));
		
		return mw;
	}
	
	//======================= Thống kê doanh thu btn Doanh Thu file thongke.jsp
	@RequestMapping(value = "thongkeDT", method = RequestMethod.POST)
	public ModelAndView ThongKeDoanhThu(HttpServletResponse response, HttpSession session,@RequestParam("ngayBD") String ngayBD,@RequestParam("ngayKT") String ngayKT) throws ParseException, IOException {
		ModelAndView mw = new ModelAndView("admin/thongke");


		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
				if(!session.getAttribute("maQuyen").equals("0")) {
					response.sendRedirect("dangky");
				}


		float tongTien = 0;
		
		Date dateBD = new SimpleDateFormat("yyyy/MM/dd").parse(ngayBD.replace("-", "/"));
		Date dateKT = new SimpleDateFormat("yyyy/MM/dd").parse(ngayKT.replace("-", "/"));
		
		List<The> theServices = theService.findBetweenNgayDK(dateBD,dateKT );
		List<The> theServicess = new ArrayList<The>();
		
		for(The theService1:theServices ) {
			if(theService1.getTrangThai().trim().equals("Hoạt Động")||theService1.getTrangThai().trim().equals("Hết Hạn") ) {
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
	
	//======================= Thống kê Khách Hàng btn Khách Hàng file thongke.jsp
	@RequestMapping(value = "thongkeKH", method = RequestMethod.POST)
	public ModelAndView thongkeKH(HttpServletResponse response,HttpSession session,@RequestParam("ngayBD") String ngayBD,@RequestParam("ngayKT") String ngayKT,@RequestParam("tenKH") String tenKH) throws ParseException, IOException {
		ModelAndView mw=new ModelAndView("thongke");


		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
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
	
	//======================= Thống kê Thẻ Dịch Vụ btn Dịch Vụ file thongke.jsp
	@RequestMapping(value = "thongkeDV", method = RequestMethod.POST)
	public ModelAndView ThongKeDichVu(HttpServletResponse response,HttpSession session,@RequestParam("ngayBD") String ngayBD,@RequestParam("ngayKT") String ngayKT,@RequestParam("tenLopDV") String tenLopDV) throws ParseException, IOException {
		ModelAndView mw = new ModelAndView("admin/thongke");
		
		// check : Phân Quyền 0: Quản Lý, 1:Nhân Viên. Chặn Nhân Viên Thấy
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
	 * ==================================================== TÀI KHOẢN
	 * ===================================================
	 */

	/*
	 * =============================================================================
	 * ============================
	 */

	/*
	 * ==================================================== UPLOAD FILE - Cú Pháp SEND EMAIL - CHECK DATE - CHECK HẾT HẠN THẺ TẬP
	 * ===================================================
	 */
	//
	//================ Check hết hạn thẻ tập khách hàng đăng ký
	@RequestMapping(value = "kiemtrahethan", method = RequestMethod.POST)
	public ModelAndView KiemTraTheTapHH() {
		ModelAndView mw = new ModelAndView("apikiemtrahethan");
		Date date = new Date();
		List<The> checkNgayHH = theService.selectByNgayHH(date);
		
		for(The checkHH:checkNgayHH)
			if(checkHH.getTrangThai().trim().equals("Hoạt Động")) theService.updateByMaThe("Hết Hạn",checkHH.getMaThe());
		
		return mw;
	}
	
	//================ Cú Pháp Gửi Mail
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
	        		+ "  </style></head><body bgcolor=\"#f7f7f7\"><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"container-for-gmail-android\" width=\"100%\"><tr><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#ffffff\" background=\"http://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg\" style=\"background-color:transparent\"><tr><td width=\"100%\" height=\"80\" valign=\"top\" style=\"text-align: center; vertical-align:middle;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"pull-left mobile-header-padding-left\" style=\"vertical-align: middle;\"><a class=\"header-md\" href=\"\">Xin chào, "+"" +"</a></td></tr></table></center></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7;\" class=\"content-padding\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"header-lg\">\r\n"
	        		+ "              Thanh Toán Thành Công!\r\n"
	        		+ "            </td></tr><tr><td class=\"free-text\">\r\n"
	        		+ "             Chân thành cảm ơn Quý Khách đã đồng hành cùng FITNESSGYM.<br> Chúc Quý Khách hàng có một trải nghiệm thật tốt và thú vị!!\r\n"
	        		+ "            </td></tr><tr><td class=\"button\"><div><a href=\"http://\"\r\n"
	        		+ "              style=\"background-color:#28a745;border-radius:5px;color:#ffffff;display:inline-block;font-family:'Cabin', Helvetica, Arial, sans-serif;font-size:14px;font-weight:regular;line-height:45px;text-align:center;text-decoration:none;width:155px;-webkit-text-size-adjust:none;mso-hide:all;\">Đăng ký dịch vụ mới</a></div></td></tr><tr><td class=\"w320\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-container-left\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Thông tin khách hàng</span><br />\r\n"
	        		+ "                                Đỗ Tâm <br />\r\n"
	        		+ "                                0352615020 <br />\r\n"
	        		+ "                                dotam5020@gmail.com \r\n"
	        		+ "                              </td></tr></table></td></tr></table></td><td class=\"mini-container-right\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td class=\"mini-block-padding\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"border-collapse:separate !important;\"><tr><td class=\"mini-block\"><span class=\"header-sm\">Thông Tin Dịch Vụ</span><br />\r\n"
	        		+ "                                Ngày Đăng Ký: 10-05-2021 <br /><span class=\"header-sm\">Mã Hóa Đơn</span><br />\r\n"
	        		+ "                                HD1\r\n"
	        		+ "                              </td></tr></table></td></tr></table></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #ffffff;  border-top: 1px solid #e5e5e5; border-bottom: 1px solid #e5e5e5;\"><center><table cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"w320\"><tr><td class=\"item-table\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td class=\"title-dark\" width=\"300\">\r\n"
	        		+ "                       Dịch Vụ\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"163\">\r\n"
	        		+ "                      Gói Tập\r\n"
	        		+ "                    </td><td class=\"title-dark\" width=\"97\">\r\n"
	        		+ "                      Tổng Tiền\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item\"><span style=\"color: #4d4d4d; font-weight:bold;\">Fitness</span></td><td class=\"item-col quantity\">\r\n"
	        		+ "                     tháng\r\n"
	        		+ "                    </td><td class=\"item-col\">\r\n"
	        		+ "                      ₫150,000.00\r\n"
	        		+ "                    </td></tr><tr><td class=\"item-col item mobile-row-padding\"></td><td class=\"item-col quantity\"></td><td class=\"item-col price\"></td></tr><tr><td class=\"item-col item\"></td><td class=\"item-col quantity\" style=\"text-align:right; padding-right: 10px; border-top: 1px solid #cccccc;\"><span class=\"total-space\">Tổng phụ</span><br /><span class=\"total-space\">Thuế</span><br /><span class=\"total-space\" style=\"font-weight: bold; color: #4d4d4d\">Thành tiền</span></td><td class=\"item-col price\" style=\"text-align: left; border-top: 1px solid #cccccc;\"><span class=\"total-space\">₫150,000.00</span><br /><span class=\"total-space\">₫0.00</span><br /><span class=\"total-space\" style=\"font-weight:bold; color: #4d4d4d\">₫150,000.00</span></td></tr></table></td></tr></table></center></td></tr><tr><td align=\"center\" valign=\"top\" width=\"100%\" style=\"background-color: #f7f7f7; height: 100px;\"><center><table cellspacing=\"0\" cellpadding=\"0\" width=\"600\" class=\"w320\"><tr><td style=\"padding: 5px 0 10px\"><strong>97 Man Thiện</strong><br />\r\n"
	        		+ "              Thành phố Thủ Đức <br />\r\n"
	        		+ "              Hồ Chí Minh <br /><br /></td></tr></table></center></td></tr></table></div></body></html>";
	        
	        		helper.setText(html_HoaDon, true);
	        this.javaMailSender.send(messages);
	        
			return "admin/sendemail";
		}
		
	//================ Test update Ngày
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
