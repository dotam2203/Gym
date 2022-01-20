<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!-- Navbar -->

      <nav class="navbar navbar-expand-lg navbar-absolute navbar-transparent">
        <div class="container-fluid">
          <div class="navbar-wrapper">
            <div class="navbar-toggle d-inline">
              <button type="button" class="navbar-toggler">
                <span class="navbar-toggler-bar bar1"></span>
                <span class="navbar-toggler-bar bar2"></span>
                <span class="navbar-toggler-bar bar3"></span>
              </button>
            </div>
          </div>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navigation" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-bar navbar-kebab"></span>
            <span class="navbar-toggler-bar navbar-kebab"></span>
            <span class="navbar-toggler-bar navbar-kebab"></span>
          </button>
          <div class="collapse navbar-collapse" id="navigation">
            <ul class="navbar-nav ml-auto">
              <li class="search-bar input-group">
                
              </li>
              <li class="dropdown nav-item">
                
                  <div class="notification d-none d-lg-block d-xl-block"></div>
                  <a href="./taikhoan" class="dropdown-toggle nav-link" ><i style="text-shadow: activeborder;">${sessionScope.username}</i></a>
                  <p class="d-lg-none">
                    Notifications
                  </p>
              </li>
              <a href="logout" class="dropdown-toggle nav-link" data-toggle="dropdown">
              <li >
                  <div>
                    <!-- <img onclick="logout()" width="150px" src="../resources/assets/img/logout.png" alt="Profile Photo"> -->
                    <i onclick="logout()" class="tim-icons icon-button-power" style="margin-top: 7.5px;"></i>
                  </div>
              </li>
               </a>
              <li class="separator d-lg-none"></li>
            </ul>
          </div>
        </div>
      </nav>
      <script>
		function logout(){
			window.location.href="logout";
		}
      </script>
      <div class="modal modal-search fade" id="searchModal" tabindex="-1" role="dialog" aria-labelledby="searchModal" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <input type="text" class="form-control" id="inlineFormInputGroup" placeholder="SEARCH">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <i class="tim-icons icon-simple-remove"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- End Navbar -->