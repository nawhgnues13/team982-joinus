<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>홈</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="/css/index.css">
    </head>
    <body class="d-flex align-items-center justify-content-center pt-5 my-4 bg-info-subtle">
		<div class="container-sm row">
			<div class="col-6 d-flex align-items-center justify-content-center">
				<img class="me-5" src="/images/JOINUS.png" alt=""/>
			</div>
			<div class="col-4 mt-5 bg-white p-5 rounded-5 border">
				<form action="/member/login.do" method="post">
					<h1 class="h3 mb-5 fw-normal"><strong>JOINUS</strong>에<br>오신걸 환영합니다.</h1>
					<div class="form-floating">
						<input type="email" name="email" class="form-control no-bottom-radius" id="floatingInput" placeholder="name@example.com" autocomplete="email" required>
						<label for="floatingInput">Email address</label>
					</div>
					<div class="form-floating">
						<input type="password" name="password" class="form-control no-top-radius" id="floatingPassword" placeholder="Password" required>
						<label for="floatingPassword">Password</label>
					</div>
					<c:if test="${not empty error && error eq '1'}">
						<p class="text-danger mt-2 fw-semibold">아이디 또는 비밀번호가 틀렸습니다.</p>
					</c:if>
					<c:if test="${empty error}">
						<p class="mt-2 text-white">1</p>
					</c:if>
					<button class="btn btn-primary w-100 py-2 mt-2" type="submit">Sign in</button>
				</form>
			</div>
			<div class="col-2"></div>
		</div>        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>