<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

	<base href="${root}/">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Adonias A. S. Neto">

	<title>e-SAMU - Emergências</title>

	<!-- Bootstrap core CSS -->
	<link rel="stylesheet" href="css/bootstrap.css">

	<!-- Custom styles -->
	<link rel="stylesheet" href="css/style.css">

</head>

<body class="bg-light">

	<header class="container my-3">
		<nav class="breadcrumb">
			<div class="container">
				<div class="row">

					<div class="col-md-8 mb-2 mb-md-0 text-center text-md-left">
						<h3 class="my-0 text-secondary font-weight-normal">Emergências</h3>
					</div>
						
					<ul class="nav nav-pills nav-fill col-md-4">
					
						<c:choose>
							<c:when test="${active}">
								
								<li class="nav-item">
									<a class="nav-link active" href="emergency?action=index&active=true">Ativas</a>
								</li>
								
								<li class="nav-item">
									<a class="nav-link" href="emergency?action=index&active=false">Inativas</a>
								</li>
								
							</c:when>
							<c:otherwise>
							
								<li class="nav-item">
									<a class="nav-link" href="emergency?action=index&active=true">Ativas</a>
								</li>
								
								<li class="nav-item">
									<a class="nav-link active" href="emergency?action=index&active=false">Inativas</a>
								</li>
							
							</c:otherwise>			
						</c:choose>
						
					</ul>

				</div>
			</div>
		</nav>
	</header>

	<main class="container">

		<div class="row">
			<div class="col-md-12">

				<div class="card">
					<table class="table table-striped table-responsive-sm table-hover my-0 shadow-sm">

						<thead>
							<tr>
								<th>Horário/Data</th>
								<th>Nome</th>
								<th>Telefone</th>
								<th>Status</th>
							</tr>
						</thead>

						<tbody id="tableBody">
						
							<c:forEach var="emergency" items="${list1}">
								<tr data-id="${emergency.id}">
									<td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="HH:mm - dd/MM/yy" /></td>
									<td>${emergency.user.name}</td>
									<td>${emergency.user.phone}</td>
									<td class="${color1}">${status1}</td>
								</tr>
							</c:forEach>
	
							<c:forEach var="emergency" items="${list2}">
								<tr data-id="${emergency.id}">
									<td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="HH:mm - dd/MM/yy" /></td>
									<td>${emergency.user.name}</td>
									<td>${emergency.user.phone}</td>
									<td class="${color2}">${status2}</td>
								</tr>
							</c:forEach>

						</tbody>

					</table>
				</div><!-- /.card -->

			</div>
		</div>

	</main><!-- /.container -->

	<!-- Bootstrap core JavaScript -->
	<script src="js/jquery-3.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
	<script src="js/popper.js"></script>
	<script src="js/bootstrap.js"></script>
	<script>
		$(document).ready(function () {
			var tBody = $("#tableBody");

			if (tBody.children().length == 0) {				
				tBody.append('<tr><td colspan="4" class="text-center">Não há emergências ainda</td></tr>');
			}
			
			$(".table tbody tr").click(function () {
				if ($(this).data("id")) {
					window.location.href = "emergency?action=details&id=" + $(this).data("id");	
				}
			});

			var currentTime = new Date().getTime();
			window.setInterval(function () {
				$.ajax({
					url: "api/emergencies/last",
					type: "GET",
					success: function (result) {
						last = Number(result);
						console.log(result);

						if (currentTime < last)
							location.reload();
					},
					error: function (error) {
						console.log(error);
					}
				});
			}, 5000);
		});
	</script>

</body>

</html>