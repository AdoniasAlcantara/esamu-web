<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="icon" href="https://getbootstrap.com/favicon.ico">

<title>e-SAMU - Emergências</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="${root}/css/bootstrap.css">

<!-- Custom styles -->
<link rel="stylesheet" href="${root}/css/style.css">

<style type="text/css">
.text-muted {
	color: #b3b3b3 !important;
}
</style>

</head>

<body class="bg-light">

	<header class="container">
		<nav class="navbar">
			<h3>Emergências</h3>
		</nav>
	</header>

	<main role="main" class="container">

	<div class="row">
		<div class="col-md-12">

			<div class="card">
				<table class="table table-card table-striped table-hover">

					<thead>
						<tr>
							<th>Horário/Data</th>
							<th>Nome</th>
							<th>Telefone</th>
							<th>Status</th>
						</tr>
					</thead>

					<tbody id="tableBody">

						<c:forEach var="emergency" items="${pendent}">
							<tr data-id="${emergency.id}">
								<td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" /></td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td class="text-warning">PENDENTE</td>
							</tr>
						</c:forEach>
						
						<c:forEach var="emergency" items="${progress}">
							<tr data-id="${emergency.id}">
								<td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" /></td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td class="text-primary">PROGRESSO</td>
							</tr>
						</c:forEach>

						<c:forEach var="emergency" items="${finished}">
							<tr data-id="${emergency.id}">
								<td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" /></td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td class="text-dark">CONCLUÍDO</td>
							</tr>
						</c:forEach>

						<c:forEach var="emergency" items="${canceled}">
							<tr data-id="${emergency.id}">
								<td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - d/M/y" /></td>
								<td>${emergency.user.name}</td>
								<td>${emergency.user.phone}</td>
								<td class="text-muted">CANCELADO</td>
							</tr>
						</c:forEach>

					</tbody>

				</table>
			</div>
			<!-- /.card -->

		</div>
	</div>

	</main>
	<!-- /.container -->

	<!-- Bootstrap core JavaScript -->
	<script src="${root}/js/jquery-3.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous"></script>
	<script>
		window.jQuery
			|| document
				.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')
	</script>
	<script src="${root}/js/popper.js"></script>
	<script src="${root}/js/bootstrap.js"></script>
	<script>
        $(document).ready(function () {
            var tBody = $("#tableBody");

            if (tBody.children().length == 0)
                tBody.append('<tr><td colspan="4" style="text-align: center">Não há emergências ainda</td></tr>');
            
            $(".table tbody tr").click(function() {
            	window.location.href = "${root}/emergency?action=details&id=" + $(this).data("id");
            });
            
            var currentTime = new Date().getTime();
        	window.setInterval(function(){
        		$.ajax({
                    url: "${root}/service/last",
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