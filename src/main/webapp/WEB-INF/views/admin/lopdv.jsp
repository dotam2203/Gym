<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ include file="/resources/include/sidebar.jsp"%>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<link href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<style type="text/css">

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
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.8); /* Black w/ opacity */
	padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
	background-color: #fefefe;
	margin: 5% auto 15% auto;
	/* 5% from the top, 15% from the bottom and centered */
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

.close:hover, .close:focus {
	color: red;
	cursor: pointer;
}

/* Add Zoom Animation */
.animate {
	-webkit-animation: animatezoom 0.6s;
	animation: animatezoom 0.6s
}

@
-webkit-keyframes animatezoom {
	from {-webkit-transform: scale(0)
}

to {
	-webkit-transform: scale(1)
}

}
@
keyframes animatezoom {
	from {transform: scale(0)
}

to {
	transform: scale(1)
}

}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
	span.psw {
		display: block;
		float: none;
	}
}
</style>
<!--   Core JS Files   -->
<script src="../resources/assets/js/core/jquery.min.js"></script>
<script src="../resources/assets/js/core/popper.min.js"></script>
<script src="../resources/assets/js/core/bootstrap.min.js"></script>
<script
	src="../resources/assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
<!--  Google Maps Plugin    -->
<!-- Place this tag in your head or just before your close body tag. -->
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
<!-- Chart JS -->
<script src="../resources/assets/js/plugins/chartjs.min.js"></script>
<!--  Notifications Plugin    -->
<script src="../resources/assets/js/plugins/bootstrap-notify.js"></script>
<!-- Control Center for Black Dashboard: parallax effects, scripts for the example pages etc -->
<script src="../resources/assets/js/black-dashboard.min.js?v=1.0.0"></script>
<!-- Black Dashboard DEMO methods, don't include it in your project! -->
<script src="../resources/assets/demo/demo.js"></script>
<script>
/* Hàm Search */
	$(document).ready(
			function() {
				$("#myInput").on(
						"keyup",
						function() {
							var value = $(this).val().toLowerCase();
							$("#myTable tr").filter(
									function() {
										$(this).toggle(
												$(this).text().toLowerCase()
														.indexOf(value) > -1)
									});
						});
			});
</script>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/resources/include/navbarmn.jsp"%>
<div class="content">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header ">
					<div class="row">
						<div class="col-sm-6 text-left">

							<h2 class="card-title">Dịch Vụ</h2>
						</div>
						<div class="col-sm-6">
							<div class="btn-group btn-group-toggle float-right"
								data-toggle="buttons">
								<label class="tablinks btn btn-sm btn-primary btn-simple active"
									id="defaultOpen"> <input type="radio" name="options"
									checked> <span
									class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Bảng Dịch Vụ</span> <span class="d-block d-sm-none"> <i
										class="tim-icons icon-single-02"></i>
								</span>
								</label>
							</div>
						</div>
					</div>

				</div>
				<div class="row">
					<div class="col-md-3 pl-md-3">
						<div class="card-footer">
							<button type="submit" class="btn btn-fill btn-primary"
								onclick="document.getElementById('them').style.display='block'"
								class="button">
								<span> Thêm </span>
							</button>
						</div>
					</div>
					<div class="col-md-6 pl-md-1"></div>
					<div class="col-md-2 pr-md-1 ">
						<div class="form-group tim-icons icon-zoom-split ">
							<label>Search</label> <input class=" form-control" id="myInput"
								type="text" placeholder="">
						</div>
					</div>
				</div>
				<!-- ========================Nội dung ======================== -->
				<div id="lopdv" class="card-body tabcontent">

					<!-- end title -->
					<div class="card-body">
						<div class="table-responsive ps">
							<table class="table tablesorter " id="myTable1">
								<thead class=" text-primary">
									<tr>
										<th>Mã Dịch Vụ</th>

										<th>Tên Dịch Vụ</th>
										
										<!-- <th>Trạng Thái</th> -->

										<th>Chỉnh Sửa</th>
										
										<th>Xóa</th>
									</tr>
								</thead>
								<tbody id="myTable">
									<c:forEach var="info" items="${lopDVServices}">

										<tr id = "tr_${info.maLop}">
											<td>${info.maLop}</td>
											
											<td>${info.tenLop}</td>
											
											<%-- <td id = "td_${info.maLop}" class="text-center">
												<span id="ttdv_${info.maLop}">	
					                        		<c:forEach var="goitap" items="${info.goiTap}">
					                        			${goitap.trangThai},
					                        		</c:forEach>
												</span>
												<script>
													var trangthai = "";
													var trangthais = document
															.getElementById("ttdv_${info.maLop}").innerHTML
															.trim();
													if (trangthais == "") {
														trangthai = "KGT";
													} else {
														if (trangthais.search(" Có Gói Tập") > -1){
															trangthai = trangthai + " CGT";
														}
														if (trangthais.search("Không Gói Tập") > -1)
														{	trangthai = trangthai + " KGT";
															document.getElementById("tr_${info.maLop}")
															
														}
													}
													
													document.getElementById("td_${info.maLop}").innerHTML = trangthai.trim();
												</script>
											</td> --%>
											
											<td><a href="goitap?id=${info.maLop}" class="tim-icons icon-pencil"></a></td>
											
											<td ><a href="#myModal${info.maLop}" data-toggle="modal"  id="xoa1_${info.maLop}" style="color:red;cursor: pointer;" class="tim-icons icon-simple-remove"></a></td>
											<!-- Modal HTML -->
											<div id="myModal${info.maLop}" class="modal fade">
												<div class="modal-dialog modal-confirm">
													<div class="modal-content">
														<br>		
														<center><h3 style="color: black">Bạn muốn tiếp tục?</h3></center>	
														<div class="modal-body">
															<center><p> Bạn chắc chắn muốn xóa Dịch Vụ <b style="font-weight: bold;">${info.tenLop}</b> ?</p></center>
														</div>
														<div class="modal-footer">
															<button id="thoat${info.maLop}" style="margin-right: 100px" type="button" class="btn btn-info" data-dismiss="modal">Thoát</button>
															<button onclick='ajax_xoa_DV("${info.maLop}")' style="margin-left: 100px" type="button" class="btn btn-danger">Xóa</button>
														</div>
													</div>
												</div>
											</div> 
											<!-- <script>
                         						if(document.getElementById("td_${info.maLop}").innerHTML.trim()!="KGT") document.getElementById("xoa1_${info.maLop}").remove(); 
                         					</script>  -->     
			                      	</tr>
			                   </c:forEach>
                      
                      <!--Xóa dịch vụ  -->
                      <script>
						function ajax_xoa_DV(maLop){
							$.ajax({
			                    url : "xoalopdv",
			                    type : "post",
			                    dataType:"text",
			                    data : {
			                         "maLop": maLop
			                         
			                    },
			                    success : function (result){
				                    
			                        if(result == "1"){
			                        	document.getElementById("tr_"+maLop).remove();
			                        	document.getElementById("thoat"+maLop).click()
			                        	demo.showNotification('top','right','Xóa Dịch Vụ thành công!','2');
			                        	
			                        	
			                        	
				                    }
			                        else {demo.showNotification('top','right','Xóa Dịch Vụ thất bại!','3');}
			                    	
			                    }
			                });  
	
						}

                    </script>
                     
								</tbody>
							</table>
							<div class="ps__rail-x" style="left: 0px; bottom: 0px;">
								<div class="ps__thumb-x" tabindex="0"
									style="left: 0px; width: 0px;"></div>
							</div>
							<div class="ps__rail-y" style="top: 0px; right: 0px;">
								<div class="ps__thumb-y" tabindex="0"
									style="top: 0px; height: 0px;"></div>
							</div>
						</div>
					</div>

				</div>
				<script>
					function sortTable() {

						var table, rows, switching, i, x, y, shouldSwitch;
						table = document.getElementById("myTable1");
						switching = true;
						/* Make a loop that will continue until
						no switching has been done: */
						while (switching) {
							// Start by saying: no switching is done:
							switching = false;
							rows = table.rows;
							/* Loop through all table rows (except the
							first, which contains table headers): */
							for (i = 1; i < (rows.length - 1); i++) {
								// Start by saying there should be no switching:
								shouldSwitch = false;
								/* Get the two elements you want to compare,
								one from current row and one from the next: */
								x = rows[i].getElementsByTagName("TD")[0];
								y = rows[i + 1].getElementsByTagName("TD")[0];
								// Check if the two rows should switch place:
								// alert(x.innerHTML.trim().substr(2));
								if (parseInt(x.innerHTML.trim().substr(2)) < parseInt(y.innerHTML
										.trim().substr(2))) {
									// If so, mark as a switch and break the loop:
									shouldSwitch = true;
									break;
								}
							}
							//    break;
							if (shouldSwitch) {
								/* If a switch has been marked, make the switch
								and mark that a switch has been done: */
								rows[i].parentNode.insertBefore(rows[i + 1],
										rows[i]);
								switching = true;
							}
						}
					}
					sortTable();
				</script>

				<!-- =========================================== end2 =========== -->

				<!-- ==========================Form Thêm======================= -->

				<div id="them" class="modal row">

					<div class="modal-content animate card-body  col-md-5"
						style="background: #27293d">
						<div class="card-header">
							<div class="imgcontainer">
								<span
									onclick="document.getElementById('them').style.display='none'"
									class="close" title="Close Modal">&times;</span>
							</div>
							<div class="col-sm-6 text-left">
								<h5 class="title">Dịch Vụ Mới</h5>
							</div>

							<div class="col-sm-12"></div>
						</div>


						<!-- =============================== Form Lớp DV========================= -->
						<div id="goitap" class="card-body tabcontent">

							<form action="lopdv" method="post">

								<div class="row">

									<div class="col-md-6 px-md-1">
										<label>Tên Dịch Vụ</label> <input type="text" list="ide"
											class="form-control" placeholder="Tên lớp" name="tenlop"
											value="" required pattern="\S+.*">

									</div>
								</div>

								<div class="row">
									<div class="col-md-12 pr-md-1">
										<label> Vui Lòng Chọn Gói Tập</label>
										<ul>
											<li>
												<div class="row">

													<div style="margin-top: 10px" class="col-md-3">

														<input id="checkboxngay"
															onchange="hienthigia('checkboxngay','removengay')"
															style="width: 15px; height: 15px" name="ngay"
															value="ngay" type="checkbox" /> <span>1 Ngày</span>

													</div>



													<div id="removengay" hidden class="col-md-7"
														style="width: 200px" class="form-group">
														<input type="number" class="form-control"
															placeholder="gia" name="giangay">
													</div>
												</div>


											</li>
											<br>

											<li>

												<div class="row">

													<div style="margin-top: 10px" class="col-md-3">

														<input id="checkboxtuan"
															onchange="hienthigia('checkboxtuan','removetuan')"
															style="width: 15px; height: 15px" name="tuan"
															value="tuan" type="checkbox" /> <span>1 Tuần</span>

													</div>



													<div id="removetuan" hidden class="col-md-7"
														style="width: 200px" class="form-group">
														<input type="number" class="form-control"
															placeholder="gia" name="giatuan">
													</div>
												</div>
											</li>
											<br>

											<li>

												<div class="row">

													<div style="margin-top: 10px" class="col-md-3">

														<input id="checkboxthang"
															onchange="hienthigia('checkboxthang','removethang')"
															style="width: 15px; height: 15px" name="thang"
															value="thang" type="checkbox" /> <span>1 Tháng</span>

													</div>



													<div id="removethang" hidden class="col-md-7"
														style="width: 200px" class="form-group">
														<input type="number" class="form-control"
															placeholder="gia" name="giathang">
													</div>
												</div>
											</li>
											<br>

											<li>

												<div class="row">

													<div style="margin-top: 10px" class="col-md-3">

														<input id="checkboxnam"
															onchange="hienthigia('checkboxnam','removenam')"
															style="width: 15px; height: 15px" name="nam" value="nam"
															type="checkbox" /> <span>1 Năm</span>

													</div>



													<div id="removenam" hidden class="col-md-7"
														style="width: 200px" class="form-group">
														<input type="number" class="form-control"
															placeholder="gia" name="gianam">
													</div>
												</div>
											</li>
										</ul>
									</div>

									<script>
										function hienthigia(id, idremove) {
											if (document.getElementById(id).checked) {
												document.getElementById(
														idremove)
														.removeAttribute(
																"hidden");

											} else
												document.getElementById(
														idremove).setAttribute(
														"hidden", "");

										}
									</script>

								</div>
								<div class="card-footer">
									<button type="submit" class="btn btn-fill btn-primary">Thêm</button>
								</div>
							</form>

						</div>
						<!-- ===================end FormlớpDV============================== -->


					</div>
				</div>
			</div>
		</div>
	</div>
</div>

</div>
</div>

<script>
	if ("${thongbao}".trim() == "1")
		demo.showNotification('top', 'right', 'Thêm Dịch Vụ thành công!', '2');
	else if ("${thongbao}".trim() == "0")
		demo.showNotification('top', 'right', 'Thêm Dịch Vụ thất bại!', '3');
</script>

<%@include file="/resources/include/endsidebar.jsp" %>
</body>

