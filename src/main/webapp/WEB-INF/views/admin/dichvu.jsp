<%@ include file="/resources/include/sidebar.jsp"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/resources/include/navbarmn.jsp"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
								<!-- <label class="tablinks btn btn-sm btn-primary btn-simple active"
									id="defaultOpen" onclick="dangky(event, 'profile')"> <input
									type="radio" name="options" checked> <span
									class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Thông
										tin</span> <span class="d-block d-sm-none"> <i
										class="tim-icons icon-single-02"></i>
								</span>
								</label> --> <label  class="tablinks btn btn-sm btn-primary btn-simple active"
									id="1" onclick="dangky(event, 'dichvu')"> <input
									type="radio" class="d-none d-sm-none" name="options"> <span
									class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Dịch
										Vụ</span> <span class="d-block d-sm-none"> <i
										class="tim-icons icon-gift-2"></i>
								</span>
								</label>

							</div>
						</div>
					</div>
				</div>
				<!-- ============================= noi dung ====================== -->
				

				<!-- =================================== end================= -->
				<div id="dichvu" class="card-body tabcontent">
					<form action="dangkydichvu" method="post">
						<div class="row">
							<div class="col-md-6 pr-md-1">
								<div class="form-group">
									<label>Mã Thẻ Tập</label> <input type="text"
										class="form-control" style="color: white"  readonly="true" value="${maTDV}">
								</div>
							</div>
							
							<div class="col-md-6 pl-md-1">
								<div class="form-group">
									<label for="exampleInputEmail1">Mã Khách Hàng</label> <input style="color: white"  readonly="true"
										class="form-control" value="${maKH}" name="maKH" >
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4 pr-md-1">
								<div class="form-group">
									<label>Dịch Vụ</label> 
									<select onchange="laylopdv()" id="lopdvss" style="background: #27293d" class="form-control form-select" name="lopDV">
									<option selected="selected" hidden>Vui lòng chọn Dịch Vụ</option>
										<c:forEach var="lopDV" items="${lopDVs}">
											<option>${lopDV.tenLop}</option>										
										</c:forEach>
										
									</select>
								</div>
							</div>
							<div class="col-md-4 pl-md-1">
								<div class="form-group">
									<label>Gói Tập</label> 
									<select onchange="laygoitap()" id="goitapss" style="background: #27293d" class="form-control form-select" name="goiTap">
										
										
										
									</select>
									
								</div>
							</div>
							<div class="col-md-4 pl-md-1">
								<div class="form-group">
									<label for="exampleInputEmail1">Ngày Đăng Ký</label> <input
										type="date" value="${localDate}" style="color: white"  readonly="true" class="form-control">
								</div>
							</div>
						</div>
						</div>
						<div class="row">
							<div class="col-md-4 pr-md-1">
								<div class="form-group">
									<label>Thành Tiền</label> <input style="color: white" id="giatien" type="text" class="form-control"
										readonly="true" value="0 VNĐ">
								</div>
							</div>
						</div>
                  <div class="card-footer">
						<button disabled id="nutluu" type="submit" class="btn btn-fill btn-primary">Lưu
							và Xuất hóa đơn</button>
					</div>
					</form>
					
				</div>
				
				<script>

				const formatter = new Intl.NumberFormat('en-US', {
					  style: 'currency',
					  currency: 'VND',
					  minimumFractionDigits: 2
					})
					
										function  laygoitap(){
											//alert(document.getElementById("lopdvss").value);
											//alert(document.getElementById("goitapss").value);
											$.ajax({
							                    url : "laygiagoitap",
							                    type : "post",
							                    dataType:"text",
							                    data : {
							                         "lopDV": document.getElementById("lopdvss").value,
							                         "goiTap": document.getElementById("goitapss").value
							                    },
							                    success : function (result){
							                    	if(result.search(/<|>|html/i) == -1 && result.trim() != ""  ){
							                    		document.getElementById("giatien").value=formatter.format(result);
							                    		document.getElementById("nutluu").removeAttribute("disabled");
							                    	} else document.getElementById("nutluu").setAttribute("disabled", "");
							                        
							                    }
							                });
											}
										function  laylopdv(){
											//alert(document.getElementById("lopdvss").value);
											//alert(document.getElementById("goitapss").value);
												function layoption(start,end,ten){
													var options="<option hidden checked> </option>";
													for(var  loaigoitap = start ; loaigoitap <=end; loaigoitap++){ 
														options += "<option>"+ten[loaigoitap] +"</option>";
														
													}
													return options;
												}
												//alert(document.getElementById("lopdvss").value);
											
												$.ajax({
								                    url : "laytengoitap",
								                    type : "post",
								                    dataType:"text",
								                    data : {
								                         "lopDV": document.getElementById("lopdvss").value
								                         
								                    },
								                    success : function (result){   
								                    	//alert(result);
								                    	if(result.search(/<|>|html/i)>-1 || result.trim() == ""  ){
									                    	result="";
									                    	demo.showNotification('top','right','Dịch Vụ hiện tại không có Gói Tập!','3');
									                    	
									                    	
									                    	
								                    	}
								                    	document.getElementById("nutluu").setAttribute("disabled", "");
								                    	document.getElementById("goitapss").innerHTML=layoption(0,result.split(",").length-1,result.split(","));
								                    	document.getElementById("giatien").value=formatter.format("0");
								                    }
								                });
												
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
		
	</div>
</div>

</div>
</div>


<script>
  if("${thongbao}" == "Đăng ký Khách Hàng thành công"){

	  demo.showNotification('top','right','Đăng ký Khách Hàng thành công!','2');
	  }
</script>

<%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>