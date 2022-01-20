<%@ include file="/resources/include/sidebar.jsp" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!- -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style type="text/css">
/* The switch - the box around the slider */
.switch {
  position: relative;
  display: inline-block;
  width: 48px;
  height: 24px;
}

/* Hide default HTML checkbox */
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

/* The slider */
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 16px;
  width: 16px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

.slider.round {
  border-radius: 24px;
}

.slider.round:before {
  border-radius: 50%;
}


</style>

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

 <head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/JNKKKK/MoreToggles.css@0.2.1/output/moretoggles.min.css">
  </head>

  <%@ page contentType="text/html" pageEncoding="UTF-8" %>    
      <%@ include file="/resources/include/navbarmn.jsp" %>
      <div class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="card">
              <div class="card-header ">
                <div class="row">
                  <div class="col-sm-6 text-left">
                    
                    <h2 class="card-title"> Dịch Vụ</h2>
                  </div>
                  <div class="col-sm-6">
                    <div class="btn-group btn-group-toggle float-right" data-toggle="buttons">
                      <label  class="tablinks btn btn-sm btn-primary btn-simple active" id="defaultOpen" onclick="dangky(event, 'doanhthu')">
                        <input type="radio" name="options" checked>
                       <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Danh Sách Gói Tập</span></form>
                        <span class="d-block d-sm-none">
                          <i class="tim-icons icon-single-02"></i>
                        </span>
                      </label>               
                    </div>
                  </div>
                </div>
                
              </div>

              <!-- ========================Nội dung ======================== -->
              <!-- ========================Chỉnh sửa lớp dịch vụ============ -->
              <div id="chinhsua" class="card-body tabcontent">
					<form>
						<c:forEach var="info" items="${lopDVs}">
                  
                       <div class="row">
											<div class="col-md-3 pr-md-1">
												<div  class="form-group">
										<label>Mã Dịch Vụ</label> <input style="text-align: center;color:white" type="text"
											class="form-control" disabled="" placeholder="ID" name="maLop" id="maLop"
											value=" ${info.maLop}">
									</div>
											</div>
											<div class="col-md-3 pr-md-1">
												<label>Tên Dịch Vụ</label> 
												<input type="text" list="ide"
														class="form-control" placeholder="tenLop" name="tenLop"
														value="${info.tenLop}">
												<datalist id="ide">
													<option value="Boxing" label="mặc định" />
												    <option value="Aerobic" label="mặc định" />
												    <option value="Fitness"label="mặc định"/>
												    <option value="Yoga" label="mặc định"/>
												</datalist>
												</div>
							</div>
							 </c:forEach> 
						</form>	 
					<!-- ================================end lớp dv=============================== -->
					
	<!-- ==========================================Thêm mới gói tập========================================= -->						 
							 <form action="updatelopdv" method="post">
							
							<div class="row">
												
											<div class="col-md-3 pr-md-1">
												<div class="form-group">
													<label>Loại Gói Tập</label>
													<select id="loaigoitap"  onchange="chonloaigoitap()" placeholder="Hãy chọn loại gói tập" style="background: #27293d" class="form-control form-select">
														<option hidden checked="" "></option>
														<option>Ngày</option>
														<option>Tuần</option>
														<option>Tháng</option>
														<option>Năm</option>
														
													</select>
												</div>
											</div>
											 <div  class="col-md-2 pr-md-1">
											      <div class="form-group">
											           <label>Tên Gói Tập</label>
											          <select id="optiongoitap" name="tengoitap"  style="background: #27293d" class="form-control form-select"></select>
											      </div>
											</div> 

								
											<script>

												function layoption(start,end,ten){
													var options="";
													for(var  loaigoitap = start ; loaigoitap <=end; loaigoitap++){ 
														options += "<option>"+loaigoitap+" "+ten +"</option>";
													}
													return options;
												}
												function  chonloaigoitap(){
													
													if(document.getElementById("loaigoitap").value.trim()=="Ngày"){
														
														document.getElementById("optiongoitap").innerHTML=layoption(1,6,"ngày");
													}else if(document.getElementById("loaigoitap").value.trim()=="Tháng"){
														
														document.getElementById("optiongoitap").innerHTML=layoption(1,11,"tháng");
													}else if(document.getElementById("loaigoitap").value.trim()=="Tuần"){
													
														document.getElementById("optiongoitap").innerHTML=layoption(1,3,"tuần");
													}else if(document.getElementById("loaigoitap").value.trim()=="Năm"){
														
														document.getElementById("optiongoitap").innerHTML=layoption(1,10,"năm");
													}
													
												}
											</script>
									
										
										<div class="col-md-3 pr-md-1">
												<div class="form-group">
													<label>Giá Tiền</label> <input type="number"
														class="form-control" placeholder="gia" name="gia">
												</div>
											</div>
									
									<input name = "malop" value="" id = "maLop1" hidden="">	
									<div class="card-footer">
										<button type="submit" class="btn btn-fill btn-primary">Thêm</button>
									</div>
						</div>	
						
					</form>
					
					
					<script type="text/javascript">
						document.getElementById("maLop1").value = document.getElementById("maLop").value;
						
					</script>
				
                 <!-- end title --> 
              
              <!-- =================================end==================================================== -->
              <!-- =================================Bảng gói tập=========================================== -->
              
              <div class="card-body">
                <div class="table-responsive ps">
                  <table class="table tablesorter " id="">
                    <thead class=" text-primary">
                      <tr>
                      <th>
                          Mã Gói Tập
                        </th>
                        <th>
                          Tên Gói tập
                        </th>
                        
                        <th class="text-center">
                          Thời Hạn
                        </th>
                        <th class="text-center">
                          Giá Tiền
                        </th>
                       <th class="text-center">Trạng Thái</th>
                        
                        <th class="text-center">
                          Xóa
                        </th>
                        
                        
                      </tr>
                    </thead>
                    <tbody id="myTable">
						<c:forEach var="info" items="${goiTapServices}">
                      <tr id="tr_${info.maGoiTap}">
                        <td>
                          ${info.maGoiTap}
                        </td>
                        <td>
                          ${info.tenGoiTap}
                        </td>
                        <td class="text-center">
                          ${info.thoiHan} ngày
                        </td>
                        <td class="text-center">
                          ${info.gia} VND
                        </td>
                        <td id="td_${info.maGoiTap}" class="text-center">
                        	<span id="ttgt_${info.maGoiTap}"> 	
                        		<c:forEach var="the" items="${info.the}">
                        			${the.maThe},
                        		</c:forEach>
							</span>
                        	<script>
								var trangthai = "";
								var trangthais = document
										.getElementById("ttgt_${info.maGoiTap}").innerHTML
										.trim();
								if (trangthais == "") {
									trangthai = "KDK";
								} else {
									trangthai = "DK";
								}
								
								document.getElementById("td_${info.maGoiTap}").innerHTML = trangthai.trim();
								
								
							</script>
                        
                        </td>
                        
                         <td  class="text-center text-danger"><a href="#myModal${info.maGoiTap}" data-toggle="modal"  id="xoa1_${info.maGoiTap}" style="color:red;cursor: pointer;" class="tim-icons icon-simple-remove"></a></td>
                      	 <!-- Modal HTML -->
							<div id="myModal${info.maGoiTap}" class="modal fade">
								<div class="modal-dialog modal-confirm">
									<div class="modal-content">
										
													<br>		
											<center><h3 style="color: black">Bạn muốn tiếp tục?</h3></center>	
							                
										
										<div class="modal-body">
											<center><p> BẠN chắc chắn muốn xóa gói tập <b style="font-weight: bold;">${info.tenGoiTap}</b> ?</p></center>
										</div>
										<div class="modal-footer">
											<button id="thoat${info.maGoiTap}" style="margin-left: 100px" type="button" class="btn btn-info" data-dismiss="modal">Thoát</button>
											<button onclick='ajax_xoa_GT("${info.maGoiTap}")' style="margin-right: 100px" type="button" class="btn btn-danger">Xóa</button>
										</div>
									</div>
								</div>
							</div>     
                      </tr>
                      <script>
                         if(document.getElementById("td_${info.maGoiTap}").innerHTML.trim()!="KDK") document.getElementById("xoa1_${info.maGoiTap}").remove(); 
					 </script>
                      </c:forEach>
                    </tbody>
                  </table>
            </div>
          </div>
   </div>
 </div>       
 
 					<script>
						
						function ajax_xoa_GT(maGT){
							$.ajax({
			                    url : "xoagoitap",
			                    type : "post",
			                    dataType:"text",
			                    data : {
			                         "maGT": maGT
			                         
			                    },
			                    success : function (result){
				                    
			                        if(result=="1"){
			                        	document.getElementById("tr_"+maGT).remove();
			                        	document.getElementById("thoat"+maGT).click()
			                        	demo.showNotification('top','right','Xóa Gói Tập thành công!','2');
			                        	
			                        	
			                        	
				                     }else {demo.showNotification('top','right','Xóa Gói Tập thất bại!','3');}
			                    	
			                    }
			                });  
	
						}

                    </script>
 
 
 
 
           <!-- =================================end==================================================== -->

        </div>
      </div>

    </div>
  <script>
	if("${thongbao}".trim()=="1") demo.showNotification('top','right','Thêm Gói Tập thành công!','2');
	else if("${thongbao}".trim()=="0")demo.showNotification('top','right','Thêm Gói Tập thất bại!','3');

  </script>
  <%@include file="/resources/include/endsidebar.jsp" %>
</body>

