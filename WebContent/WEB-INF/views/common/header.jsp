<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 border-bottom">
    <div class="col-md-3 mb-2 mb-md-0">
        <a href="/" class="fs-1 ms-5 text-decoration-none">
            JOINUS
        </a>
    </div>
   <!--  <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><a href="#" class="nav-link px-2 link-secondary">Home</a></li>
        <li><a href="#" class="nav-link px-2">Features</a></li>
        <li><a href="#" class="nav-link px-2">Pricing</a></li>
        <li><a href="#" class="nav-link px-2">FAQs</a></li>
        <li><a href="#" class="nav-link px-2">About</a></li>
    </ul> -->
    <div class="col-md-3 text-end me-5">
        <!-- <button type="button" class="btn btn-outline-primary me-2">Login</button> -->
        <button type="button" class="btn btn-primary" onclick="location.href='/member/logout.do'">Logout</button>
    </div>
</header>