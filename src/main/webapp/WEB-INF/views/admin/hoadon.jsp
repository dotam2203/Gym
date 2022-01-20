<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/resources/include/sidebar.jsp"%>
<%@ include file="/resources/include/navbarmn.jsp"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>

<c:forEach var="the" items="${thes}">

<div class="content">
	<div class="row">
		<div class="col-md-10">
			<div class="card">
				<div class="card-header">
					<h3 class="title">HÓA ĐƠN</h3>
				</div>
				<div class="card-body">
					<form action="hoadon" method="post">
						
						<div class="row">
							<div class="col-md-3 pr-md-1">
								<div class="form-group">
									<label>Mã Thẻ Tập</label> <input name="id" type="text"
										class="form-control" style="color: #FFFFFFCC"  placeholder="Company"
										value="${the.maThe}" >
								</div>
							</div>
							<div class="col-md-5 px-md-1">
								<div class="form-group">
									<label>Khách Hàng</label> <input type="text" class="form-control"
										placeholder="Username" value="${the.khachHang.tenKH}">
								</div>
							</div>
							<div class="col-md-4 pl-md-1">
								<div class="form-group">
									<label for="exampleInputEmail1">Ngày Đăng Ký</label> <input
										type="date" value="${the.ngayDK}" class="form-control">
								</div>
							</div>
						</div>
						<div class="row">
						<div class="col-md-4 px-md-1">
								<div class="form-group">
									<label>Email</label> <input type="email" class="form-control"
										placeholder="Email" value="${the.khachHang.email}">
								</div>
							</div>
							<div class="col-md-4 pr-md-1">
								<div class="form-group">
									<label>Số Điện Thoại</label> <input type="text" class="form-control"
										placeholder="SĐT" value="${the.khachHang.sdt}">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label>Địa Chỉ</label> <input type="text" class="form-control"
										placeholder="Home Address"
										value="${the.khachHang.diaChi}">
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6 pr-md-1">
								<div class="form-group">
									<label>Dịch Vụ</label> 
									<input  type="text"
										class="form-control" style="color: #FFFFFFCC" disabled="" 
										value="${the.goiTap.lopDV.tenLop}">
								</div>
							</div>
							<div class="col-md-6 pl-md-1">
								<div class="form-group">
									<label>Gói Tập</label> 
									<input  type="text" class="form-control" style="color: #FFFFFFCC" disabled="" value="${the.goiTap.tenGoiTap}">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-8">
								<div class="form-group">
									<label>Tổng Tiền</label>
									<h3>${the.goiTap.gia} VNĐ</h3>
								</div>
							</div>
						</div>
							<div class="card-footer">
								<button  id="button"  type="submit" class="btn btn-fill btn-primary">Thanh
									Toán</button>
							</div>
							
				</form>
						
						<script >
						
							if ("Hoạt Động" == "${the.trangThai}"||"Hết Hạn" == "${the.trangThai}"){
								document.getElementById("button").innerHTML = "Đã Thanh Toán";
								document.getElementById("button").setAttribute("disabled", " ");
								if("1" == "${updateTT}" ){
									demo.showNotification('top','right','Thanh Toán Thành Công','2');	
									
								}
							}
						
							/* document.getElementById("thongbao").removeAttribute("disabled");
							document.getElementById("thongbao").click(); */

						</script>
											
							
				</div>
				
			</div>
		</div>

	</div>
</div>
</c:forEach>

</div>
</div>
<%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>