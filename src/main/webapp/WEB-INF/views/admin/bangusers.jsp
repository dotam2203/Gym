   <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/resources/include/sidebar.jsp" %>
    <%@ page contentType="text/html" pageEncoding="UTF-8" %>  
      <%@ include file="/resources/include/navbarmn.jsp" %>
   
     
<link href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round" rel="stylesheet">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"> -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"> -->
<style>


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



</script>


      
      <div class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="card ">
              <div class="card-header">
					<div class="row">
						<div class="col-md-3 pr-md-1"><h4 class="card-title"> Khách Hàng</h4></div>
						<div class="col-md-3 pr-md-1"></div>
						<div class="col-md-3 pr-md-1"></div>
						<div class="col-md-2 pr-md-1 ">
							<div class="form-group tim-icons icon-zoom-split ">
								<label>Search</label> <input class=" form-control"
									id="myInput" type="text" placeholder="">
							</div>
						</div>

					</div>
					
              </div>
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table tablesorter " id="myTable1">
                    <thead class=" text-primary">
                      <tr>
                        <th>
                          Mã Khách Hàng
                        </th>
                        <th>
                          Họ & Tên
                        </th>
                        <th>
                          Email
                        </th>
                        <th>
                          Giới Tính
                        </th>
                        <th class="text-center">
                          Số Điện Thoại
                        </th>
                        
                        <th class="text-center">Trạng Thái</th>
                        
                        <th class="text-center">Dịch Vụ</th>
                        <th class="text-center">Chỉnh Sửa</th>
                        <th class="text-center">Xóa</th>
                      </tr>
                    </thead>
                    <tbody id="myTable" >
                    <c:forEach var="info" items="${khachHangServices}">
                      <tr  id="tr_${info.maKH}" >
                        <td>
                          ${info.maKH}
                        </td>
                        <td>
                          ${info.tenKH}
                        </td>
                         <td >
                         ${info.email}
                        </td>
                        <td >
                          ${info.gioiTinh}
                        </td>
                       <td class="text-center">
                          ${info.sdt}
                       </td >
                       <td id="td_${info.maKH}" class="text-center">
                        	<span id="ttdv_${info.maKH}">	
                        		<c:forEach var="the" items="${info.the}">
                        			${the.trangThai},
                        		</c:forEach>
							</span>
							<script>
								var trangthai = "";
								var trangthais = document
										.getElementById("ttdv_${info.maKH}").innerHTML
										.trim();
								if (trangthais == "") {
									trangthai = "CDK";
								} else {
									if (trangthais.search("Hoạt Động") > -1){
										trangthai = trangthai + " HD";
									}
									if (trangthais.search("Chưa Thanh Toán") > -1)
									{	trangthai = trangthai + " CTT";
										document.getElementById("tr_${info.maKH}").setAttribute("class", "alert alert-primary");
										
									}
									if (trangthais.search("Hết Hạn") > -1){
										trangthai = trangthai + " HH";
									}
								}
								
								document.getElementById("td_${info.maKH}").innerHTML = trangthai.trim();
								
							</script>
					   </td >
                        
                        <td class="text-center"><a href="dichvu?id=${info.maKH}" class="tim-icons icon-badge"></a></td>
                        <td class="text-center text-success"><a  href="user?id=${info.maKH}" class="tim-icons icon-pencil"></a></td>
                        
                         <td class="text-center text-danger"><a href="#myModal${info.maKH}" data-toggle="modal"  id="xoa1_${info.maKH}" style="color:red;cursor: pointer;" class="tim-icons icon-simple-remove"></a></td>
                         
							<!-- Modal HTML -->
							<div id="myModal${info.maKH}" class="modal fade">
								<div class="modal-dialog modal-confirm">
									<div class="modal-content">
										
													<br>		
											<center><h3 style="color: black">Cảnh Báo!!!</h3></center>	
							                
										
										<div class="modal-body">
											<center><p> Bạn chắc chắn muốn xóa Khách Hàng <b style="font-weight: bold;">${info.tenKH}</b> ?</p></center>
										</div>
										<div class="modal-footer">
											<button id="thoat${info.maKH}" style="margin-right: 100px" type="button" class="btn btn-info" data-dismiss="modal">Thoát</button>
											<button onclick='ajax_xoa_KH("${info.maKH}")' style="margin-left: 100px" type="button" class="btn btn-danger">Xóa</button>
										</div>
									</div>
								</div>
							</div>     
                         
                         
                         <script>
                         	if(document.getElementById("td_${info.maKH}").innerHTML.trim()!="CDK") document.getElementById("xoa1_${info.maKH}").remove(); 
                         </script>                      
                      </tr>
                    </c:forEach>
                    
                    <script>
						function ajax_xoa_KH(maKH){
							
							$.ajax({
			                    url : "xoakhachhang",
			                    type : "post",
			                    dataType:"text",
			                    data : {
			                         "maKH": maKH
			                         
			                    },
			                    success : function (result){
				                    
			                        if(result=="1"){
			                        	demo.showNotification('top','right','Xóa thành công!','2');
			                        	document.getElementById("tr_"+maKH).remove();
			                        	document.getElementById("thoat"+maKH).click()
			                        	
			                        	
				                     }else {demo.showNotification('top','right','Xóa thất bại!','3');}
			                    	
			                    }
			                });
						}
                    </script>
                   </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        
        </div>
      </div>
 
    </div>
  </div>
  
  <script>
function sortTable() {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById("myTable1");
  switching = true;
  /*Make a loop that will continue until
  no switching has been done:*/
  while (switching) {
    //start by saying: no switching is done:
    switching = false;
    rows = table.rows;
    /*Loop through all table rows (except the
    first, which contains table headers):*/
    for (i = 1; i < (rows.length - 1); i++) {
      //start by saying there should be no switching:
      shouldSwitch = false;
      /*Get the two elements you want to compare,
      one from current row and one from the next:*/
      x = rows[i].getElementsByTagName("TD")[0];
      y = rows[i + 1].getElementsByTagName("TD")[0];
      //alert(parseInt(x.innerHTML.split('KH')[1]));
      //alert(parseInt(y.innerHTML.split('KH')[1]));
      //check if the two rows should switch place:
      if (parseInt(x.innerHTML.split('KH')[1]) < parseInt(y.innerHTML.split('KH')[1])) {
      	//alert(parseInt(x.innerHTML.split('KH')[1]));
        //if so, mark as a switch and break the loop:
        shouldSwitch = true;
        break;
      }
    }
    //break;
    if (shouldSwitch) {
      /*If a switch has been marked, make the switch
      and mark that a switch has been done:*/
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
  }
}
sortTable();
</script>
  
  
 <%@include file="/resources/include/endsidebar.jsp" %>
</body>

</html>