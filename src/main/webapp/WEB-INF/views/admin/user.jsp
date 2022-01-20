<%@ include file="/resources/include/sidebar.jsp"%>
<style type="text/css">
.hethan {
    /*  opacity: 0.75; */
     
      background:linear-gradient(#C0C18E,#C1C1C1); 
     
}
.hoatdong{
	background:linear-gradient(#2a6df5,#55acee);
	color: white;
	
}
.chuathanhtoan{
	
	color: white;
	background:linear-gradient(#f55c47,#ff7b54); 
}
/* The Modal (background) */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 6; /* Sit on top */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0,0.8); /* Black w/ opacity */
  padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
  background-color: #fefefe;
  margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
  border: 1px solid #888;
  width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
  position: absolute;
  right: 25px;
  top: 0;
  color: #000;
  font-size: 35px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: red;
  cursor: pointer;
}

/* Add Zoom Animation */
.animate {
  -webkit-animation: animatezoom 0.6s;
  animation: animatezoom 0.6s
}

@-webkit-keyframes animatezoom {
  from {-webkit-transform: scale(0)} 
  to {-webkit-transform: scale(1)}
}
  
@keyframes animatezoom {
  from {transform: scale(0)} 
  to {transform: scale(1)}
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
  span.psw {
     display: block;
     float: none;
  }
  
}

/*========= XOAY THE =========*/

.flip-card {
  border-radius: 5px;
  background-color: transparent;
  width: 150px;
  height: 250px;
  perspective: 1000px;
}

.flip-card-inner {
 border-radius: 5px;
  position: relative;
  width: 100%;
  height: 100%;
  text-align: center;
  transition: transform 0.6s;
  transform-style: preserve-3d;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
}

.flip-card:hover .flip-card-inner {
  transform: rotateY(180deg);
}

.flip-card-front, .flip-card-back {
 border-radius: 5px;
  position: absolute;
  width: 100%;
  height: 100%;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;
}

.flip-card-front {
  
  color: black;
}

.flip-card-back {
  background: linear-gradient(#282083,#595F9D);
  color: white;
  transform: rotateY(180deg);
}

/*============= button================*/
.button {
  border-radius: 4px;
  /*background: linear-gradient(#D41383,#D514DE);*/
  background: linear-gradient(#2630DE,#245CD4);
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 15px;
  padding: 10px;
  width: 100px;
  height:50px
  transition: all 0.5s;
  cursor: pointer;
  margin: 10px;
}

.button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}

.button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.button:hover span {
  padding-right: 20px;
}

.button:hover span:after {
  opacity: 1;
  right: 0;
}


.center {
  margin: 0;
  position: absolute;
  top: 50%;
  left: 50%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
</style>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/resources/include/navbarmn.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="content">
	<div class="row">
		<div class="col-md-8">
			<div class="card">
				<div class="card-header">
					<div class="col-sm-6 text-left">
						<h5 class="title">Khách hàng</h5>
					</div>

					<div class="col-sm-12">
						<div class="btn-group btn-group-toggle float-right"
							data-toggle="buttons">
							<label class=" tablinks btn btn-sm btn-primary btn-simple active"
								id="0" onclick="user(event, 'profile')"> <input
								type="radio" name="options" checked> <span
								class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Thông
									tin khách hàng</span> <span class="d-block d-sm-none"> <i
									class="tim-icons icon-single-02"></i>
							</span>
							</label> <label class="tablinks btn btn-sm btn-primary btn-simple" id="1"
								onclick="user(event, 'the')"> <input type="radio"
								name="options" checked> <span
								class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Thẻ</span>
								<span class="d-block d-sm-none"> <i
									class="tim-icons icon-gift-2"></i>
							</span>
							</label>
						</div>
					</div>
				</div>
				<!--               ==================nội dung======================= -->
				<div id="profile" class="card-body tabcontent">
					<form action="updateuser" method="post" enctype='multipart/form-data'>
						<c:forEach var="khachhang" items="${khachhangs}">
							<div class="row">
								<div class="col-md-5 pr-md-1">
									<div class="form-group">
										<label>Mã Khách Hàng </label> <input type="text"
											class="form-control" readonly="true" placeholder="ID"
											value="${khachhang.maKH}" name="makh" >
									</div>
								</div>
								<div class="col-md-3 px-md-1">
									<div class="form-group">
										<label>Họ & Tên</label> <input type="text"
											class="form-control" placeholder="Hoten"
											value="${khachhang.tenKH}"  required pattern="\S+.*"  name="hovaten">
									</div>
								</div>
								<div class="col-md-4 pl-md-1">
									<div class="form-group">
										<label>Số Điện Thoại</label> 
										
											<input type="text" class="form-control" placeholder="SĐT" value="${khachhang.sdt}" name = "sdt" pattern="(\+84|0)\d{9,10}"
									title="Số điện thoại gồm 10 chữ số (nếu nhập 0) hoặc 9 số (nếu nhập +84)">
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-8 pr-md-1">
									<div class="form-group">
										<label>Email</label> <input type="email" class="form-control"
											placeholder="City" value="${khachhang.email}" name="email">
									</div>
									
								</div>

								<div class="col-md-4 pl-md-1">
									<div>
									<label>Avatar</label> <input type="file" class="form-control" placeholder="Link ảnh" name="avatar">
									
								</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-6 pr-md-1">
									<div class="form-group">
										<label>Giới Tính</label> <select name="gioitinh" id="gioitinh"
											style="background: #27293d" class="form-control form-select"
											placeholder="Sex">
											<option id="op_Nu">Nữ</option>
											<option id="op_Nu" hidden="" selected="selected">${khachhang.gioiTinh}</option>
											<option id="op_Nam">Nam</option>
										</select>

									</div>
								</div>
								<div class="col-md-6 pl-md-1">
									<div class="form-group">
										<label>Ngày Sinh</label> <input onchange="TDate()" name="ngaysinh" id="userdate" type="date"
											value="${khachhang.ngaySinh}" class="form-control"
											placeholder="Date of birth">
									</div>
								</div>
							</div>
							<script>
									function TDate() {
										var dd="${khachhang.ngaySinh}";
									    var UserDate = document.getElementById("userdate").value;
									    var ToDate = new Date();

									    if (new Date(UserDate).getTime() > ToDate.getTime()) {
									    	demo.showNotification('top','right','Ngày sinh nhỏ hơn ngày hiện tại!','3');
									          document.getElementById("userdate").value=dd;
									     }
									    return true;
									}</script>
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label>Địa Chỉ</label> <input name="diachi" type="text" class="form-control"
											placeholder="Home Address" value="${khachhang.diaChi}">
									</div>
								</div>
							</div>
						</c:forEach>
						<div class="card-footer">
						<button type="submit" class="btn btn-fill btn-primary">Save</button>
					</div>
					</form>
					
				</div>
				<!--     ==============================end=====================================       -->



				<div id="the" class="card-body tabcontent">

					<div  class="row ">
						<c:forEach var="the" items="${thes}">
							
							
							<div  class="flip-card col-md-3 pr-md-1 mb-3">
							<div id="trangthai_${the.maThe}" class="flip-card-inner">
								<div class="flip-card-front">
									<div  
										
										class="btn btn-link  ">
										<i style="color: white ;font-size:30px;font-family: Poppins,sans-serif" class=" simple-text logo-normal tim-icons">${the.maThe} </i><br>
										<br>
										<div style="color: white ;font-size:13px">
											${the.goiTap.lopDV.tenLop}<br>${the.goiTap.tenGoiTap}
										</div>

									</div>
								</div>
								<div   class="flip-card-back ">
									<div id="tpthe_${the.maThe}" class="center">
									
									<button onclick="document.getElementById('id01_${the.maThe}').style.display='block'" class="button">
										<span>Chi Tiết </span>
									</button>
									</div>
								</div>
							</div>
						</div>
							
							
							
							
							
							
							
							
							
							
							<script type="text/javascript">
								if("${the.trangThai}"=="Hết Hạn"){
									document.getElementById("trangthai_${the.maThe}").classList.add("hethan");
									
								}else if("${the.trangThai}"=="Chưa Thanh Toán"){
									document.getElementById("tpthe_${the.maThe}").innerHTML+="<button onclick=\"window.location='hoadon?id=${the.maThe}'\" style=\"background: linear-gradient(#4eaaa9, #6369d3);\" class=\"button\"> <span>Thanh Toán </span> </button>";
									document.getElementById("trangthai_${the.maThe}").classList.add("chuathanhtoan");
								} 
								else document.getElementById("trangthai_${the.maThe}").classList.add("hoatdong");
								
									
							</script>
							<div id="id01_${the.maThe}" class="modal row">
								<div class="modal-content animate card-body  col-md-5"
									style="background: #083834">
									<form>
										<div class="imgcontainer">
											<span
												onclick="document.getElementById('id01_${the.maThe}').style.display='none'"
												class="close" title="Close Modal">&times;</span>
										</div>

										<div class="row">
											<div class="col-md-5 pr-md-1">
												<div id="thee" class="form-group">
													<label>Mã thẻ (disabled)</label> <input style="color:white"
														name="mathe_${the.maThe}" type="text" class="form-control"
														disabled="" placeholder="ID thẻ" value="${the.maThe}">

												</div>
											</div>
											<div class="col-md-3 px-md-1">
												<div class="form-group">
													<label>Lớp</label> <input style="color:white" readonly="true" type="text" class="form-control"
														placeholder="Class" value="${the.goiTap.lopDV.tenLop}">
												</div>
											</div>
											<div class="col-md-4 pl-md-1">
												<div class="form-group">
													<label>Thời gian kết thúc</label> <input type="date"
														class="form-control" placeholder="date"
														value="${the.ngayKT}">
												</div>
											</div>
										</div>
										<div class="row">
											
											
												
												
											<div class="col-md-5 pr-md-1">
												<div id="thee" class="form-group">
													<label>Trạng thái</label> <input style="color:white"
														 type="text" class="form-control"
														disabled="" placeholder="ID thẻ" value="${the.trangThai}">

												</div>
											</div>
											<div class="col-md-5 pr-md-1">
												<div id="thee" class="form-group">
													<label>Gói tập</label> <input style="color:white"
														 type="text" class="form-control"
														disabled="" placeholder="ID thẻ" value="${the.goiTap.tenGoiTap}">

												</div>
											</div>
										</div>

									</form>
									
								</div>
							</div>
							
						</c:forEach>
					</div>

					</div>
					<!-- =========================================== end2 ================================= -->


				<!--========================scirpt start 1 trong 2 mode================================ -->

				<script>
					function user(evt, cityName) {
						var i, tabcontent, tablinks;
						tabcontent = document
								.getElementsByClassName("tabcontent");
						for (i = 0; i < tabcontent.length; i++) {
							tabcontent[i].style.display = "none";
						}
						tablinks = document.getElementsByClassName("tablinks");
						for (i = 0; i < tablinks.length; i++) {
							tablinks[i].className = tablinks[i].className
									.replace(" active", "");
						}
						document.getElementById(cityName).style.display = "block";
						evt.currentTarget.className += " active";
					}

					// Get the element with id="defaultOpen" and click on it
					document.getElementById("0").click();
				</script>

				<!--======================= end script ======= -->
			</div>
		</div>


		<div class="col-md-4">
			<div class="card card-user">
				<div class="card-body">
					<p class="card-text">
					<div class="author">
						<div class="block block-one"></div>
						<div class="block block-two"></div>
						<div class="block block-three"></div>
						<div class="block block-four"></div>
						<a href="javascript:void(0)"> <img id="avatarid" class="avatar"
							src="../resources/images/" alt="...">
							<h5 class="title">${tenKH}</h5>
						</a>
						<script>

						if("${avatar}".trim()!="") document.getElementById("avatarid").src="../resources/images/"+"${avatar}".trim();
                        else document.getElementById("avatarid").src="../resources/assets/img/aerobic.png";

                        </script>
						<p class="description">KHÁCH HÀNG THÀNH VIÊN</p>
					</div>
					</p>
				<div class="card-description">"Sống Hết Mình & Tập Hết Khả Năng"</div>
				</div>
				<div class="card-footer">
					<div class="button-container">
						<button href="javascript:void(0)"
							class="btn btn-icon btn-round btn-facebook">
							<i class="fab fa-facebook"></i>
						</button>
						<button href="javascript:void(0)"
							class="btn btn-icon btn-round btn-twitter">
							<i class="fab fa-twitter"></i>
						</button>
						<button href="javascript:void(0)"
							class="btn btn-icon btn-round btn-google">
							<i class="fab fa-google-plus"></i>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--       ==========================end========================== -->


</div>
</div>

<script>
var url_string = window.location.href;
var url = new URL(url_string);
var check = url.searchParams.get("thongbao");
if(check == "0") demo.showNotification('top','right','Trùng Email!','3');
else if (check == "1") demo.showNotification('top','right','Thay Đổi Thành Công!','2');
</script>

<%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>