   <%@ taglib  uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ include file="/resources/include/sidebar.jsp" %>
    <%@ page contentType="text/html" pageEncoding="UTF-8" %>  
      <%@ include file="/resources/include/navbarmn.jsp" %>
      <div class="content">
        <div class="row">
          <div class="col-md-8">
            <div class="card">
              <div class="card-header">
                <h5 class="title">Thông Tin Tài Khoản</h5>
              </div>
              <div class="card-body">
                  <div class="row">
                    <div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>Mã Nhân Viên</label>
                        <input type="text" class="form-control" disabled="disabled" placeholder="MaNV" name="manv" value="${sessionScope.manv}">
                      </div>
                    </div>
                    <div class="col-md-5 px-md-1">
                      <div class="form-group">
                        <label>Họ & Tên</label>
                        <input type="text" class="form-control" disabled="disabled" placeholder="Họ và tên" name="hovaten" value="${sessionScope.tennv}">
                      </div>
                    </div>
                    <div class="col-md-5 pl-md-1">
                      <div class="form-group">
                        <label for="exampleInputEmail1">Email</label>
                        <input type="email" name="email" disabled="disabled" class="form-control" placeholder="Email" value="${sessionScope.email}">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4 pr-md-1">
                      <div class="form-group">
                        <label>Số Điện Thoại</label>
                        <input type="text" class="form-control" disabled="disabled" placeholder="Số điện thoại" name="sdt" value="${sessionScope.sdt}">
                      </div>
                    </div>
                    <div class="col-md-3 pr-md-1">
                    	<div class="form-group">
									<label>Giới tính</label> 
									<input type="text" class="form-control" disabled="disabled" placeholder="Giới Tính" name="giotinh" value="${sessionScope.gioitinh}">
						</div>
					</div>
                    <div class="col-md-5 pl-md-1">
                      <div class="form-group">
                        <label>Địa Chỉ</label>
                        <input type="text" class="form-control" disabled="disabled" placeholder="Địa chỉ" name="diachi" value="${sessionScope.diachi}">
                      </div>
                    </div>
                  </div>
                 
                  
                  <div class="row">
                  <div class="col-md-4 pr-md-1">
                      <div class="form-group">
                        <label>Chức Vụ</label>
                        <input id="chucvu" type="text" class="form-control" disabled="disabled" placeholder="Chức vụ" name="chucvu" value="${sessionScope.chucvu}">
                      </div>
                    </div>
                    
                  </div>
                  <div class="card-footer">
              </div>
           </div>
              
          </div>
          </div>
          <div class="col-md-4">
            
            <div class="card">
              <div class="card-header">
                <h5 class="title">Tài Khoản Đăng Nhập</h5>
              </div>
              <div class="card-body">

		<!-- Cập nhật lại tài khoản sau khi chỉnh sửa nhân viên -->
                  <div class="row">
                    <div class="col-md-6 pr-md-1">
                      <div class="form-group">
                        <label>Username</label>
                        <input name="username" type="text" class="form-control" disabled="disabled" placeholder="Username" value="${sessionScope.username}">
                       </div>
                    </div>
                    <div class="col-md-6 px-md-1">
                      <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" class="form-control" disabled="disabled" placeholder="" value="${sessionScope.password}">
                      </div>
                    </div>
                    
                  </div>
                  <div class="row">
                    <div class="col-md-6 pr-md-1">
                      <div class="form-group">
                        <label>Mã quyền</label>
                        <input id="maquyen11" disabled="disabled" onchange = "xacnhandoiquyen()"  type="text" name="maQuyen" class="form-control" placeholder="Mã quyền" value="${sessionScope.maQuyen}">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                  <div class="col-md-12 pr-ml-13" style="color: #FFFFFF99;">
	                  	<table  style = "width:80%; margin-left:40px;border-color:purple;">
	                    	<tr >
	                    		
	                    		<th class=" text-center"><i class="tim-icons icon-attach-87"></i>Ghi Chú</th>
	                    		<th>Mã Quyền</th>
	                    	</tr>
	                    	<tr>
	                    		<td class=" text-center">0</td>
	                    		<td>Quản Lý</td>
	                    	</tr>
	                    	<tr>
	                    		<td class=" text-center">1</td>
	                    		<td>Nhân Viên</td>
	                    	</tr>
	                    </table>
                    </div>
                  </div>
                  <div class="card-footer">
              </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    
      <script>
              document.write(new Date().getFullYear())
            </script>
    </div>
  </div>
   <%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>