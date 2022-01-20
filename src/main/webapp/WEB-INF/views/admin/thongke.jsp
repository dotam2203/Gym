<%@ include file="/resources/include/sidebar.jsp" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!- -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
  $("#myInput").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#myTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
$(document).ready(function(){
	  $("#myInput1").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#myTable1 tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});
$(document).ready(function(){
	  $("#myInput2").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#myTable2 tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});
</script>

  <%@ page contentType="text/html" pageEncoding="UTF-8" %>    
      <%@ include file="/resources/include/navbarmn.jsp" %>
      <div class="content">
        <div class="row">
          <div class="col-md-8">
            <div class="card">
              <div class="card-header ">
                <div class="row">
                  <div class="col-sm-6 text-left">
                    <h5 class="card-category">Bảng</h5>
                    <h2 class="card-title">Thống Kê Trong 1 Tháng</h2>
                  </div>
                  <div class="col-sm-6">
                    <div class="btn-group btn-group-toggle float-right" data-toggle="buttons">
                      <!-- Doanh Thu -->
                      <label name="flagDT"  class="tablinks btn btn-sm btn-primary btn-simple flagDT  " id="1" onclick="dangky(event, 'doanhthu')">
                        <input type="radio" name="options" checked>
                        <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Doanh Thu</span>
                        <span class="d-block d-sm-none">
                          <i class="tim-icons icon-single-02"></i>
                        </span>
                      </label>
                      <!-- Dịch Vụ -->
                      <label name="flagDV" class="tablinks btn btn-sm btn-primary btn-simple "   onclick="dangky(event, 'dichvu')">
                        <input type="radio" class="d-none d-sm-none" name="options">
                        <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Dịch Vụ</span>
                        <span class="d-block d-sm-none">
                          <i class="tim-icons icon-gift-2"></i>
                        </span>
                      </label>
                      <!-- Khách Hàng -->
                       <label name="flagKH" class="tablinks btn btn-sm btn-primary btn-simple flagKH" id="1" onclick="dangky(event, 'khachhang')">
                        <input type="radio" class="d-none d-sm-none" name="options">
                        <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Khách Hàng</span>
                        <span class="d-block d-sm-none">
                          <i class="tim-icons icon-gift-2"></i>
                        </span>
                      </label>
                      
                    </div>
                  </div>
                </div>
                
              </div>
              <!-- ============================= noi dung ====================== -->
              
              
              
              <!-- =================btn Doanh Thu================ -->
              <!-- Thống Kê doanh Thu -->
              <div id="doanhthu" class="card-body tabcontent">
              
              <form action="thongkeDT" method="post">
              <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>Từ Ngày</label>
                        <input onchange="ktngay()" id="startDate" type="date" class="form-control" placeholder="Company" value="${namBD}" name="ngayBD">
                      </div>
                    </div>
                    
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>Đến Ngày</label>
                        <input onchange="ktngay()" id="endDate" type="date" class="form-control" placeholder="Last Name" value="${namKT}" name="ngayKT">
                      </div>
                   
                    </div>
                     <div class="col-md-3 pl-md-1">
	                	<div class="card-footer">
	                	<button  type="submit" id="timkiem" class=" btn btn-fill btn-primary">Search</button>
	              		</div>
	              	</div>
                  </div>
				</form>

					<div class="row">
						<div class="col-md-3 pr-md-1"></div>
						<div class="col-md-3 pr-md-1"></div>
						<div class="col-md-3 pr-md-1"></div>
						<div class="col-md-2 pr-md-1 ">
							<div class="form-group tim-icons icon-zoom-split ">
								<label>Search</label> <input class=" form-control"
									id="myInput" type="text" placeholder="">
							</div>
						</div>

					</div>
					
				
				<script>
                  function ktngay(){
	                  var startDate = new Date( document.getElementById("startDate").value);
	                  var endDate = new Date(document.getElementById("endDate").value);
	
	                  if (startDate > endDate){
	                	  demo.showNotification('top','right','Ngày không đúng. Vui lòng nhập lại!','3');
	                	  document.getElementById("timkiem").setAttribute("disabled", "");
	                  }else  document.getElementById("timkiem").removeAttribute("disabled")  ;
                  }
                  </script>	
					<!-- end title --> 
               
                 
                 
                <div class="card-body">
                <div class="table-responsive ps">
                  <table class="table tablesorter " id="">
                    <thead class=" text-primary">
                      <tr>
                      <th>
                          Mã Thẻ Tập
                        </th>
                        <th>
                          Họ & Tên
                        </th>
                        
                        <th>
                          Dịch Vụ
                        </th>
                        <th>
                          Gói Tập
                        </th>
                        <th>
                          Ngày Đăng Ký <br>(YYYY/MM/DD)
                        </th>
                        <th class="text-center">
                          Giá Tiền
                        </th>
                      </tr>
                    </thead>
                    <tbody id="myTable">
                    <c:forEach var="info" items="${theServices}">
                      <tr>
                        <td>
                          ${info.maThe}
                        </td>
                        <td>
                          ${info.khachHang.tenKH}
                        </td>
                        <td>
                          ${info.goiTap.lopDV.tenLop}
                        </td>
                        <td>
                          1 ${info.goiTap.tenGoiTap}
                        </td>
                        <td>
                          ${info.ngayDK}
                        </td>
                        <td class="text-center">
                          ${info.goiTap.gia} VNĐ
                        </td>
                      </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
              </div>
           
              <div class="row">
                    <div style="text-align: center" class="col-md-12 pr-md-3">
                      <div class="form-group">
                        <label>Thành Tiền</label>
                        <input id="tongtien" style="text-align: right;font-size: 20px;color:pink" type="text" class="form-control" disabled="" placeholder="Company" value="00 VND">
                        
                      </div>
                    </div>
                    
                    
                  </div>
              </div>
              
              
              <!-- =================================== end================= -->
              
              
               <!-- =================btn Dịch Vụ================ -->
              <!-- Thống Kê Dịch Vụ -->
              
              <div id="dichvu" class="card-body tabcontent">
               <!-- title -->
               <form action="thongkeDV", method="POST">
              <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>Từ Ngày</label>
                        <input onchange="ktngayDV()" id="startDateDV" name="ngayBD"  type="date" class="form-control" placeholder="Company" value="${namBDDV}">
                      </div>
                    </div>
                    
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>Đến Ngày</label>
                        <input onchange="ktngayDV()" id="endDateDV" name="ngayKT" type="date" class="form-control" placeholder="Last Name" value="${namKTDV}">
                      </div>
                   
                    </div>
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>Dịch Vụ</label>
                        <select  style="background: #27293d" class="form-control form-select" name="tenLopDV" id="cars">
                        	<option hidden selected="selected">${tenLopDV}</option>
                        	 <c:forEach var="info" items="${lopDVs}">
						   		<option>${info.tenLop}</option>
						    </c:forEach>
						  </select>
                        <!-- <input type="text"  placeholder="Last Name" value="dropdown"> -->
                      </div>
                   
                    </div>
                     <div class="col-md-3 pl-md-1">
	                	<div class="card-footer">
	                	<button id="timkiemDV" type="submit" class="btn btn-fill btn-primary">Search</button>
	              		</div>
	              	</div>
                  </div>
                  </form>
                  <script>
                  function ktngayDV(){
	                  var startDate = new Date( document.getElementById("startDateDV").value);
	                  var endDate = new Date(document.getElementById("endDateDV").value);
	
	                  if (startDate > endDate){
	                	  demo.showNotification('top','right','Ngày không đúng. Vui lòng nhập lại!','3');
	                	  document.getElementById("timkiemDV").setAttribute("disabled", "");
	                  }else  document.getElementById("timkiemDV").removeAttribute("disabled")  ;
                  }
                  </script>	
                  
                 <!-- end title SEARCH --> 
               <div class="row">
                    <div  class="col-md-3 pr-md-1">
                      
                    </div>
                    <div  class="col-md-3 pr-md-1">
                      
                    </div>
                    <div  class="col-md-3 pr-md-1">
                      
                    </div>
                    <div  class="col-md-2 pr-md-1 ">
                      <div  class="form-group tim-icons icon-zoom-split "  >
                        <label>Search</label>
                        <input class=" form-control"  id="myInput1"  type="text" placeholder="" >
                      </div>
                    </div>
                   
                  </div>
                 <!-- end title --> 
               
                 
                 
                <div class="card-body">
                <div class="table-responsive ps">
                  <table class="table tablesorter " id="">
                    <thead class=" text-primary">
                      <tr>
                      <th>
                          Mã Thẻ Tập
                        </th>
                        <th>
                          Họ & Tên
                        </th>
                        <th>
                          Gói Tập
                        </th>
                       <!--  <th class="text-center">
                          Số lần tập
                        </th> -->
                        <th >
                          Trạng Thái
                        </th>
                       
                      </tr>
                    </thead>
                    <tbody id="myTable1">
                    <c:forEach var="info" items="${theServiceDV}">
                      <tr>
                        <td>
                          ${info.maThe}
                        </td>
                        <td>
                          ${info.khachHang.tenKH}
                        </td>
                        <td>
                         ${info.goiTap.tenGoiTap}
                        </td>
                        <td >
                          ${info.trangThai}
                        </td>
                        
                      </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
              </div>
           
              <div class="row">
                    <div style="text-align: center" class="col-md-12 pr-md-3">
                      <div class="form-group">
                        <label>Số Lượng Thẻ Tập</label>
                        <input style="text-align: center;font-size: 20px;color:pink" type="text" class="form-control" disabled="" placeholder="số lượng thẻ" value="${slThe}">
                      </div>
                    </div>
                    
                    
                  </div>
              </div>
              
              <!-- =========================================== end2 =========== -->
              
              <!-- =================================== end3================= -->
              
               <!-- =================btn Khách Hàng================ -->
              <!-- Thống Kê Khách Hàng -->
              
              <div id="khachhang" class="card-body tabcontent">
               	 <!-- title -->
               <form action="thongkeKH" method="post">
              <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>Từ Ngày</label>
                        <input onchange="ktngayKH()" id="startDateKH" type="date" class="form-control" placeholder="Company" name="ngayBD" value="${namBDDV}">
                      </div>
                    </div>
                    
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>Đến Ngày</label>
                        <input onchange="ktngayKH()" id="endDateKH" type="date" class="form-control" placeholder="Last Name" name="ngayKT" value="${namKTDV}">
                      </div>
                   
                    </div>
                     
	              	
	              	
	              	<div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>Tên Khách Hàng</label>
                        <input type="text" class="form-control" placeholder="Last Name" value="${tenKH}" name="tenKH">
                      </div>
                  </div>
                  <div class="col-md-3 pl-md-1">
	                	<div class="card-footer">
	                	<button id="timkiemKH" type="submit" class="btn btn-fill btn-primary">Search</button>
	              		</div>
	              	</div>
                  <!-- end title --> 
                </div>
                
                </form>
                
                <script>
                  function ktngayKH(){
	                  var startDate = new Date( document.getElementById("startDateKH").value);
	                  var endDate = new Date(document.getElementById("endDateKH").value);
	
	                  if (startDate > endDate){
	                	  demo.showNotification('top','right','Ngày không đúng. Vui lòng nhập lại!','3');
	                	  document.getElementById("timkiemKH").setAttribute("disabled", "");
	                  }else  document.getElementById("timkiemKH").removeAttribute("disabled")  ;
                  }
                  </script>	
               <div class="row">
                    <div  class="col-md-3 pr-md-1">
                      
                    </div>
                    <div  class="col-md-3 pr-md-1">
                      
                    </div>
                    <div  class="col-md-3 pr-md-1">
                      
                    </div>
                    <div  class="col-md-2 pr-md-1 ">
                      <div  class="form-group tim-icons icon-zoom-split "  >
                        <label>Search</label>
                        <input class=" form-control"  id="myInput2"  type="text" placeholder="" >
                      </div>
                    </div>
                   
                  </div>
                 <!-- end title --> 
                  
                 <!-- end title --> 
               <div class="card-body">
                <div class="table-responsive ps">
                  <table class="table tablesorter " id="">
                    <thead class=" text-primary">
                      <tr>
                        <th>
                          Mã Thẻ Tập
                        </th>
                        <th>
                        Mã Khách Hàng
                          
                        </th>
                        <th>
                          Họ & Tên
                        </th>
                        <th >
                          Email
                        </th>
                        <th>
                          Gói Tập
                        </th>
                        
                        
                      </tr>
                    </thead>
                    <tbody id="myTable2">
                    <c:forEach var="info" items="${theServiceKH}">
                   
                      <tr>
                        <td>
                        	${info.maThe}
                          
                        </td>
                        <td>
                          ${info.khachHang.maKH}
                        </td>
                        <td>
                         ${info.khachHang.tenKH}
                        </td>
                        <td >
                         ${info.khachHang.email}
                        </td>
                        <td>
                          ${info.goiTap.lopDV.tenLop}
                        </td>
                        
                      </tr>
                      </c:forEach>
                      
                    </tbody>
                  </table>
                <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
              </div>
              <div class="row">
                    <div style="text-align: center" class="col-md-12 pr-md-3">
                      <div class="form-group">
                        <label>Số Lượng Khách Hàng Đăng Kí</label>
                        <input style="text-align: center;font-size: 20px;color:pink" type="text" class="form-control" disabled="" placeholder="Số lượng khách hàng" value="${slTheKH}">
                      </div>
                    </div>
                    
                    
                  </div>
              </div>
              <!-- kiem tra cac nút thống kê -->
              <script>
					if("${flag}"=="kh"){
						document.getElementsByName("flagKH")[0].setAttribute("id", "defaultOpen");
						document.getElementsByName("flagKH")[0].classList.add("active");
					}else if("${flag}"=="dv"){
						document.getElementsByName("flagDV")[0].setAttribute("id", "defaultOpen");
						document.getElementsByName("flagDV")[0].classList.add("active");
					}
					else{
						document.getElementsByName("flagDT")[0].setAttribute("id", "defaultOpen");
						document.getElementsByName("flagDT")[0].classList.add("active");
						}


              </script>
              
              
              <!-- =========================================== end2 =========== -->
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
          <div class="col-md-4">
            <div class="card card-user">
              <div class="card-body">
                <p class="card-text">
                  <div class="author">
                    <div class="block block-one"></div>
                    <div class="block block-two"></div>
                    <div class="block block-three"></div>
                    <div class="block block-four"></div>
                    <a href="javascript:void(0)">
                      <p id="tongtienlon" style="color:white;font-size: 40px"></p>
                      <h5 class="title"></h5>
                      <script>
	                        const formatter = new Intl.NumberFormat('en-US', {
	          				  style: 'currency',
	          				  currency: 'VND',
	          				  minimumFractionDigits: 2
	          				})
	          				if("${tongTien}".trim()!="") {
	                        	document.getElementById("tongtien").value= formatter.format(${tongTien});
	          					document.getElementById("tongtienlon").innerHTML= formatter.format(${tongTien});
	          				}else if("${slTheKH}".trim()!="") document.getElementById("tongtienlon").innerHTML="${slTheKH}";
	          				else document.getElementById("tongtienlon").innerHTML="${slThe}";
	          				
                        </script>
                    </a>
                    <p class="description">
                      Thành Tiền
                    </p>
                  </div>
                </p>
              </div>
              
            </div>
          </div>
        </div>
      </div>
     
    </div>
  </div>
  
  <%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>