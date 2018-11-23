<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="adonias" content="">
<link rel="icon" href="https://getbootstrap.com/favicon.ico">

<title>Starter Template for Bootstrap</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/starter-template.css" rel="stylesheet">

<style type="text/css">
th, td {
	text-align: center
}
</style>

</head>

<body>

	<main role="main" class="container">

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-8 mx-auto">

				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>Horário/Data</th>
								<th>Nome</th>
								<th>Telefone</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="emergency" items="${pendent}">
							<tr class="table-warning">
								<td>
									<fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" />
								</td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td>PENDENTE</td>
							</tr>
							</c:forEach>
							
							<c:forEach var="emergency" items="${progress}">
							<tr class="table-success">
								<td>
									<fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" />
								</td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td>PROGRESSO</td>
							</tr>
							</c:forEach>
							
							<c:forEach var="emergency" items="${finished}">
							<tr>
								<td>
									<fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" />
								</td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td>CONCLUÍDO</td>
							</tr>
							</c:forEach>
							
							<c:forEach var="emergency" items="${canceled}">
							<tr>
								<td>
									<fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" />
								</td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td>CANCELADO</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>
		</div>
	</div>

	</main>
	<!-- /.container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/jquery-3.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')
	</script>
	<script src="js/popper.js"></script>
	<script src="js/bootstrap.js"></script>

</body>
</html>