<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ include file="/resources/include/sidebar.jsp" %>
  <%@ page contentType="text/html" pageEncoding="UTF-8" %>    
      <%@ include file="/resources/include/navbarmn.jsp" %>
<link href="../resources/assets/css/black-dashboard.css?v=1.0.0">
<div class="content">
	<div class="row">
		<div class="col-md-10">
			<div class="card">
				<div class="card-header ">
					<div class="row">
						<div class="col-sm-6 text-left">
							<h3 class="card-title">ĐĂNG KÝ</h3>
						</div>
						<div class="col-sm-6">
							<div class="btn-group btn-group-toggle float-right"
								data-toggle="buttons">
								<label class="tablinks btn btn-sm btn-primary btn-simple active"
									id="defaultOpen" onclick="dangky(event, 'profile')"> <input
									type="radio" name="options" checked> <span
									class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Khách Hàng</span> <span class="d-block d-sm-none"> <i
										class="tim-icons icon-single-02"></i>
								</span>
								</label> <!-- <label class="tablinks btn btn-sm btn-primary btn-simple"
									id="1" onclick="dangky(event, 'dichvu')"> <input
									type="radio" class="d-none d-sm-none" name="options"> <span
									class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Dịch
										Vụ</span> <span class="d-block d-sm-none"> <i
										class="tim-icons icon-gift-2"></i>
								</span> -->
								</label>

							</div>
						</div>
					</div>
				</div>
				<!-- ============================= noi dung ====================== -->
				<div id="profile" class="card-body tabcontent">
					<form action="dangkykhachhang" enctype="multipart/form-data" method="POST">
						<div class="row">
							<div class="col-md-6 pr-md-1">
								<div class="form-group">
									<label>Họ & Tên</label> <input type="text"
										class="form-control" placeholder="Họ và tên" value="${hoVaTen}" name = "hovaten">
								</div>
							</div>
							<div class="col-md-6 pl-md-1">
								<div class="form-group">
									<label>Email</label> <input type="email" class="form-control"
										placeholder="pipi@gmail.com" name = "email">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 pr-md-1">
								<div class="form-group">
									<label>Số Điện Thoại</label> 
									
									<input type="text" class="form-control" placeholder="SĐT" value="${sdt}" name = "sdt" pattern="(\+84|0)\d{9,10}"
									title="Số điện thoại gồm 10 chữ số (nếu nhập 0) hoặc 9 số (nếu nhập +84)">
								</div>
							</div>
							<div class="col-md-6 pl-md-1">
								<div class="form-group">
									<label>Ngày Sinh</label> <input type="date"
										class="form-control" placeholder="Ngày sinh" value="${ngaySinh}" id="userdate" onchange="TDate()" name = "ngaysinh">
								</div>
							</div>
						</div>
						<script>
									function TDate() {
										
									    var UserDate = document.getElementById("userdate").value;
									    var ToDate = new Date();

									    if (new Date(UserDate).getTime() > ToDate.getTime()) {
									    	demo.showNotification('top','right','Ngày sinh bé hơn ngày hiện tại!','3');
									          document.getElementById("userdate").value="";
									     }
									    return true;
									}</script>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label>Địa Chỉ</label> <input type="text" class="form-control"
										placeholder="Địa chỉ" value="${diaChi}" name = "diachi">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-5 pr-md-1">
								<div class="form-group">
									<label>Giới tính</label> 
									<select class="form-control form-select" name = "gioitinh">
										<option selected="selected" hidden style="background: #27293d" class="form-group">${gioiTinh}</option>
										<option style="background: #27293d" class="form-group">Nam</option>
										<option style="background: #27293d" class="form-group">Nữ</option>
									</select>
								</div>
							</div>
							<div >
								<div >
									<label>Ảnh</label> <input type="file" class="form-control" placeholder="Link ảnh" name="avatar">
									
								</div>
							</div>
						</div>
						<div class="card-footer">
						<button type="submit" class="btn btn-fill btn-primary">Lưu</button>
					</div>
					</form>
					
				</div>

				<!-- =================================== end================= -->
				
				<!--========================scirpt start 1 trong 2 mode -->

				<script>
				function dangky(evt, cityName) {
				  var i, tabcontent, tablinks;
				  tabcontent = document.getElementsByClassName("tabcontent");
				  for (i = 0; i < tabcontent.length; i++) {
				    tabcontent[i].style.display = "none";
				  }
				  tablinks = document.getElementsByClassName("tablinks");
				  for (i = 0; i < tablinks.length; i++) {
				    tablinks[i].className = tablinks[i].className.replace(" active", "");
				  }
				  document.getElementById(cityName).style.display = "block";
				  evt.currentTarget.className += " active";
				}
				
				// Get the element with id="defaultOpen" and click on it
				document.getElementById("defaultOpen").click();
				</script>

				<!--======================= end script ======= -->

			</div>
		</div>
	
	</div>
</div>

</div>
</div>


<script> 
	if("${thongbaoloi}" == "Email đã tồn tại"){
		demo.showNotification('top','right','Email đã tồn tại!','3');

	}

</script>

<%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>