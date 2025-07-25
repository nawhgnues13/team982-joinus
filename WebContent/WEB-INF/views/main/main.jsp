<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>홈</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <c:import url="/WEB-INF/views/common/header.jsp"/>
        <div class="d-flex">
	        <c:import url="/WEB-INF/views/common/sidebar.jsp"/>
	        <div class="w-100">
		        <div class="./container mt-5">
		            main<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>		        
		        </div>
        		<c:import url="/WEB-INF/views/common/footer.jsp"/>
	        </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>