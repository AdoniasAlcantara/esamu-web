<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="icon" href="https://getbootstrap.com/favicon.ico">

    <title>e-SAMU - Detalhes</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${root}/css/bootstrap.css">

    <!-- Custom styles -->
    <link rel="stylesheet" href="${root}/css/style.css">

    <style type="text/css">
        .text-muted {
            color: #b3b3b3 !important;
        }

        .table-rotated>tbody th {
            width: 150px;
        }
    </style>

</head>

<body class="bg-light">

    <header class="container">
        <nav class="navbar">

            <h3>Detalhes da Emergência</h3>
            <a href="${root}/emergency" class="btn btn-secondary" style="width: 100px">Voltar</a>

        </nav>
    </header>

    <main role="main" class="container">

        <div class="row">
            <div class="col-md-12">
                <div class="card">

                    <div class="card-header">
                        <h5>Pendente</h5>
                    </div>

                    <table class="table table-card table-rotated">
                        <tbody>
                            <tr>
                                <th>ID</th>
                                <td>${emergency.id}</td>
                            </tr>
                            <tr>
                                <th>Horário/Data</th>
                                <td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="H:m - dd/MM/y" /></td>
                            </tr>                           
                            <c:choose><c:when test="${emergency.status == 'FINISHED'}">
                            <tr>
                            	<th>Horário/Data (término)</th>
                            	<td><fmt:formatDate value="${emergency.end}" type="BOTH" pattern="H:m - dd/MM/y" /></td>
                            </tr>
                            </c:when></c:choose>
                            <tr>
                                <th>Nome</th>
                                <td>${emergency.user.name}</td>
                            </tr>
                            <tr>
                                <th>Telefone</th>
                                <td>${emergency.user.phone}</td>
                            </tr>
                            <tr>
                                <th>IMEI</th>
                                <td>${emergency.imei}</td>
                            </tr>
                            <tr>
                                <th>Endereço</th>
                                <td>${emergency.location.address}</td>
                            </tr>
                            <tr>
                                <td colspan="2" style="padding: 0">
                                	<div id="map" style="width: 100%; height: 400px;"></div>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <div class="card-body">
                        <div class="row">

                            <div class="col-md-8">
                            	
                                <img class="img-center img-clickable rounded" data-toggle="modal" data-target="#modalImg" src="${root}/img/${emergency.id}.jpg" alt="Imagem da emergência enviada pelo usuário">
                                <p class="text-muted" style="text-align: center; margin: 0;">Clique na imagem para ampliar</p>

                            </div>

                            <div class="col-md-4">
                                <div class="card card-controls">
                                    <div class="card-content">

                                        <div class="card-body">
                                       	<c:choose>
                                       	
	                                       	<c:when test="${emergency.status == 'PENDENT'}">
	                                       		<a href="#" class="card-link" data-toggle="modal" data-target="#modalAttach">Anexar procedimento</a>
	                                            <p id="textAttach" class="text-muted">Nenhum procedimento selecionado.</p><br>
	                                            <p class="card-text">Deseja confirmar a ocorrência?</p>
	
	                                            <form action="${root}/emergency?action=update&id=${emergency.id}" method="post" class="text-center">
	                                                <button name="status" type="submit" value="canceled" class="btn btn-secondary" style="width: 35%">Recusar</button>
	                                                <button name="status" type="submit" value="progress" class="btn btn-primary" style="width: 60%">Aceitar</button>
	                                                <input id="attach" name="attach" type="hidden" value="-1">
	                                            </form>
	                                       	</c:when>
	                                       	
	                                       	<c:when test="${emergency.status == 'PROGRESS'}">
	                                       		<p class="card-text">Deseja concluir a ocorrência?</p>
	                                       		
	                                       		<form action="${root}/emergency?action=update&id=${emergency.id}" method="post" class="text-center">
	                                                <button name="status" type="submit" value="canceled" class="btn btn-secondary" style="width: 35%">Abortar</button>
	                                                <button name="status" type="submit" value="finished" class="btn btn-primary" style="width: 60%">Concluir</button>                                                
	                                            </form>
	                                       	</c:when>
	                                       	
	                                       	<c:when test="${emergency.status == 'FINISHED' || emergency.status == 'CANCELED'}">
	                                       		<p class="card-text">A emergência pode ser reaberta.</p>
	                                       		
	                                       		<form action="${root}/emergency?action=update&id=${emergency.id}" method="post" class="text-center">
	                                                <button name="status" type="submit" value="progress" class="btn btn-primary" style="width: 60%">Reabrir</button>                                                
	                                            </form>
	                                       	</c:when>
                                       	
                                       	</c:choose>
                                        </div>

                                    </div>
                                </div><!-- /Inner .card-->
                            </div>

                        </div><!-- /Inner .row-->

                    </div>
                </div><!-- /.card -->
            </div>
        </div> <!-- /.row -->

    </main><!-- /.container -->

    <!-- Modal for picture magnification -->
    <div id="modalImg" aria-hidden="true" aria-labelledby="emergencyImage" class="modal" role="dialog" tabindex="-1">
        <div class="modal-dialog" role="document" style="max-width: 80%">
            <div class="modal-content">

                <div class="modal-header">

                    <h5 class="modal-title">Imagem obtida do usuário</h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>

                </div>

                <div class="modal-body">
                    <img src="${root}/img/${emergency.id}.jpg" alt="Imagem da Emergência" style="width:100%">
                </div>

            </div>
        </div>
    </div>

    <!-- Modal for attach first aid procedure -->
    <div id="modalAttach" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">

                <div class="modal-header">

                    <h5 class="modal-title">Anexo</h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>

                </div>

                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="selectAttach">Selecione um procedimento de primeiros socorros</label>
                            <select id="selectAttach" class="form-control" multiple style="height: 300px">
                                <option value="0">Choque elétrico</option>
                                <option value="1">Infarto e parada cardiorrespiratória</option>
                                <option value="2">Envenenamento</option>
                                <option value="3">Picada de cobra</option>
                                <option value="4">Corpos estranhos e asfixia</option>
                                <option value="5">Queimaduras</option>
                                <option value="6">Sangramentos</option>
                                <option value="7">Transporte de vítimas</option>
                                <option value="8">Fraturas, luxações, contusões e entorces</option>
                                <option value="9">Acidentes de trânsito</option>
                            </select>
                        </div>
                    </form>
                </div><!-- /.modal-body -->

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" style="width: 160px" onclick="confirmAttach()">Salvar</button>
                </div>

            </div>
        </div>
    </div>

    <!-- Bootstrap core JavaScript -->
    <script src="${root}/js/jquery-3.js"></script>
    <script>
        window.jQuery || document.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')
    </script>
    <script src="js/popper.js"></script>
    <script src="js/bootstrap.js"></script>
    <script>
        function confirmAttach() {
            $("#textAttach").text($("#selectAttach :selected").text() + " selecionado.");
            $("#attach").val($("#selectAttach").val());
        }
        
        var latLng = {lat: ${emergency.location.latitude}, lng: ${emergency.location.longitude}};
        var marker;
        var map;
        
        function initMap() {
        	map = new google.maps.Map(document.getElementById('map'), {
            	center: {lat: ${emergency.location.latitude}, lng: ${emergency.location.longitude}},
            	zoom: 17
          	});
          
          	marker = new google.maps.Marker({
				position: latLng,
				map: map,
				title: 'Local da emergência'
			});
        }
    </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAbSiLG_0CxGCbhZE5kQHSq4p7kqNfWoLM&callback=initMap" async defer></script>
</body>

</html>