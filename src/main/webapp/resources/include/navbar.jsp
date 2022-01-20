<!-- Top Bar Start -->
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!-- Nav Bar Start -->
        <div class="navbar navbar-expand-lg bg-dark navbar-dark">
            <div class="container-fluid">
                <a href="index.html" class="navbar-brand"><div class="logo"><img src="resources/img/logo.png" alt="Fitness"></div></a>
                <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
                    <span class="navbar-toggler-icon"></span>
                </button>
				<!-- Hiển thị List Button -->
                <div class="collapse navbar-collapse justify-content-between" id="navbarCollapse">
                    <div class="navbar-nav ml-auto">
                    <c:forEach var="item" items="${menus}">
						<a href="${item}" class="nav-item nav-link ">${item}</a>
					</c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <!-- Nav Bar End -->