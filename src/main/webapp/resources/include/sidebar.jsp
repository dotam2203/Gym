<%@ page contentType="text/html" pageEncoding="UTF-8" %>  
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="apple-touch-icon" sizes="76x76" href="../resources/assets/img/apple-icon.png">
  <link href="../resources/img/logo1.png" rel="icon">
  <title>
   FITNESS
  </title>
  <!--     Fonts and icons     -->
  <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,600,700,800" rel="stylesheet" />
  <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
  
  <!-- Nucleo Icons -->
  <link href="../resources/assets/css/nucleo-icons.css" rel="stylesheet" />
  <!-- CSS Files -->
  <link href="../resources/assets/css/black-dashboard.css?v=1.0.0" rel="stylesheet" />
  <!-- CSS Just for demo purpose, don't include it in your project -->
  <link href="../resources/assets/demo/demo.css" rel="stylesheet" />
    <!--   Core JS Files   -->
  <script src="../resources/assets/js/core/jquery.min.js"></script>
  <script src="../resources/assets/js/core/popper.min.js"></script>
  <script src="../resources/assets/js/core/bootstrap.min.js"></script>
  <script src="../resources/assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
  <!--  Google Maps Plugin    -->
  <!-- Place this tag in your head or just before your close body tag. -->
  <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
  <!-- Chart JS -->
  <script src="../resources/assets/js/plugins/chartjs.min.js"></script>
  <!--  Notifications Plugin    -->
  <script src="../resources/assets/js/plugins/bootstrap-notify.js"></script>
  <!-- Control Center for Black Dashboard: parallax effects, scripts for the example pages etc -->
  <script src="../resources/assets/js/black-dashboard.min.js?v=1.0.0"></script><!-- Black Dashboard DEMO methods, don't include it in your project! -->
  <script src="../resources/assets/demo/demo.js"></script>
</head>

<body onload="kiemtrahethan()" class="">
	
 <script>
/* Kiểm tra thẻ tập đã hết hạn hay chưa? */
 function kiemtrahethan(){

		$.ajax({
			 url : "kiemtrahethan",
             type : "post",
             dataType:"text",
             data : {
             },
             success : function (result){
             }
         });

	}
 
 init_reload();
 function init_reload(){
     setInterval( function() {
     	kiemtrahethan();

       },43200000);
 }

 </script>
  <div class="wrapper">
    <div class="sidebar" >
      <!--
        Tip 1: You can change the color of the sidebar using: data-color="blue | green | orange | red"
    -->
      <div class="sidebar-wrapper" style="background-color:rgb(186, 84, 245);border-left-style: dashed;">
        <div class="logo">
          <a href="javascript:void(0)" >
            <img src="../resources/img/logo.png" alt="FITNESS">
          </a>
        </div>
        <ul class="nav">
          <li id="trangChuCheck" class="active ">
            <a href="./home">
              <i class="tim-icons icon-chart-pie-36"></i>
              <p>Trang Chủ</p>
            </a>
          </li>
           <li id="lopdvCheck" >
            <a href="./lopdv">
              <i class="tim-icons icon-credit-card"></i>
              <p>Dịch Vụ</p>
            </a>
          </li>
          <li>
            <a href="./dangky">
              <i class="tim-icons icon-pencil"></i>
              <p>Đăng Ký Tập</p>
            </a>
          </li>
          <li >
            <a href="./banghoadon">
              <i class="tim-icons icon-notes"></i>
              <p>Hóa Đơn</p>
            </a>
          </li>
          <li>
            <a href="./bangusers">
              <i class="tim-icons icon-badge"></i>
              <p>Khách Hàng</p>
            </a>
          </li>
          
         
          <li id="nhanVienCheck" >
            <a href="./bangnhanvien">
              <i class="tim-icons icon-single-02"></i>
              <p>Nhân Viên</p>
            </a>
          </li>
          
          <li id ="thongKeCheck" >
            <a href="./thongke">
              <i class="tim-icons icon-chart-bar-32"></i>
              <p>Thống Kê</p>
            </a>
          </li>
          
          <script>
          function checkAdmins(id) {
        	  var list = document.getElementById(id);
        	  while (true){
        	  if (list.hasChildNodes() && list.hasChildNodes()!=null ) {
        	    list.removeChild(list.childNodes[0]);
        	  }else break;
        	  }
        	}
          /* Là Quản Lý mới có quyền use full */
          if("${sessionScope.maQuyen}"!="0"){
        	  checkAdmins("trangChuCheck");
        	  checkAdmins("thongKeCheck");
        	  checkAdmins("nhanVienCheck");
        	  checkAdmins("lopdvCheck");
             }
          
          
          </script>
        </ul>
      </div>
    </div>
    <div class="main-panel">