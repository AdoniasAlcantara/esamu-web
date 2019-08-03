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

    <title>e-SAMU - Detalhes da Emergência</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css">

    <!-- Custom styles -->
    <link rel="stylesheet" href="css/style.css">

    <style type="text/css">
        th {
            width: 30%;
            text-align: right;
        }
    </style>

</head>

<body class="bg-light">

    <header class="container my-3">
        <nav class="breadcrumb">
            <div class="container">
                <div class="row">

                    <div class="col-md-8 col-lg-10 mb-2 mb-md-0 text-center text-md-left">
                        <h3 class="my-0 text-secondary font-weight-normal">Detalhes da Emergência</h3>
                    </div>

                    <div class="col-md-4 col-lg-2">
                        <a class="btn btn-primary btn-block" href="emergency">&laquo; Voltar</a>
                    </div>

                </div>
            </div>
        </nav>
    </header>

    <main class="container">
        <div class="card shadow-sm">

            <h5 class="card-header">PENDENTE</h5>

            <div class="card-body">

                <div class="row">

                    <table class="table table-borderless col-md-5">
                        <tbody>

                            <tr>
                                <th>ID</th>
                                <td>${emergency.id}</td>
                            </tr>

                            <tr>
                                <th>Iní­cio</th>
                                <td><fmt:formatDate value="${emergency.start}" type="BOTH" pattern="HH:mm - dd/MM/yy" /></td>
                            </tr>

							<c:choose><c:when test="${emergency.status == 'FINISHED'}">
                            	<tr>
	                                <th>Término</th>
	                                <td><fmt:formatDate value="${emergency.end}" type="BOTH" pattern="HH:mm - dd/MM/yy" /></td>
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

                        </tbody>
                    </table><!-- /.col-md-6 -->

                    <div class="col-md-7" id="map" style="height: 400px"></div>

                </div><!-- /.row -->

                <div class="row mt-4">

                    <div class="col-md-8 mb-3 mb-md-0 text-center">
                        <img class="img-fluid rounded clickable" data-toggle="modal" data-target="#modalImg" src="img/res/${emergency.id}.jpg" alt="Imagem da emergÃªncia">
                        <p class="text-muted" style="text-align: center; margin: 0;">Clique na imagem para ampliar</p>
                    </div>

                    <div class="col-md-4">
                        <div class="card bg-light">
                            <div class="card-body">
                            
                            <c:choose>
                            	
                            	<c:when test="${emergency.status == 'PENDENT'}">
                            		<a href="#" class="card-link" data-toggle="modal" data-target="#modalAttach">Anexar procedimento</a>
                                	<p id="textAttach" class="text-muted">Nenhum procedimento selecionado.</p><br>
                                	<p class="card-text">Deseja confirmar a ocorrência?</p>
                                	
                                	<c:set var="negativeButtonValue" value="canceled"/>
                                	<c:set var="negativeButtonText" value="Recusar"/>
                                	<c:set var="display" value="d-inline-block"/>
                                	
                                	<c:set var="positiveButtonValue" value="progress"/>
                                	<c:set var="positiveButtonText" value="Confirmar"/>
                            	</c:when>
                            	
                            	<c:when test="${emergency.status == 'PROGRESS'}">
                            		<p class="card-text">Clique em "Concluir" para encerrar a ocorrência.</p>
                            		
                            		<c:set var="negativeButtonValue" value="canceled"/>
                                	<c:set var="negativeButtonText" value="Anular"/>
                                	<c:set var="display" value="d-inline-block"/>
                                	
                                	<c:set var="positiveButtonValue" value="finished"/>
                                	<c:set var="positiveButtonText" value="Concluir"/>
                            	</c:when>
                            	
                            	<c:when test="${emergency.status == 'CANCELED' || emergency.status == 'FINISHED'}">
                            		<p class="card-text">A ocorrência ficará disponível na aba "Ativas" com status "PENDENTE" caso deseja reabrí-la.</p>
                            		
                     				<c:set var="positiveButtonValue" value="pendent"/>
                                	<c:set var="positiveButtonText" value="Reabrir"/>
                                	<c:set var="display" value="d-none"/>
                            	</c:when>
                            
                            </c:choose>

                                <form action="emergency?action=update&id=${emergency.id}" method="post" class="text-center">

                                    <div class="form-row">

                                        <div class="col-lg-5 mb-2 mb-lg-0 ${display}">
                                            <button name="status" type="submit" value="${negativeButtonValue}" class="btn btn-secondary btn-block">${negativeButtonText}</button>
                                        </div>

                                        <div class="col-lg-7">
                                            <button name="status" type="submit" value="${positiveButtonValue}" class="btn btn-primary btn-block">${positiveButtonText}</button>
                                        </div>

                                    </div>

                                    <input id="attach" name="attach" type="hidden" value="-1">

                                </form>

                            </div>
                        </div>
                    </div>

                </div><!-- /.row -->
            </div><!-- /.card-body -->
        </div><!-- /.card -->

    </main><!-- /.container -->

    <footer class="container my-3">
        <nav class="breadcrumb">
            <div class="container">
                <div class="row">
                    <div class="col-md-4 col-lg-2 offset-md-8 offset-lg-10">
                        <a class="btn btn-primary btn-block" href="#" onclick="emergency">&laquo; Voltar</a>
                    </div>
                </div>
            </div>
        </nav>
    </footer>

    <!-- Modal for picture -->
    <div id="modalImg" class="modal" role="dialog" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered" role="document" style="max-width: 80%; margin-left: auto; margin-right: auto;">
            <div class="modal-content">

                <div class="modal-header py-2">

                    <h5 class="modal-title font-weight-normal">Imagem enviada pelo usuário</h5>

                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>

                </div>

                <img src="img/res/${emergency.id}.jpg" alt="Imagem da emergÃªncia" style="width:100%">

            </div>
        </div>
    </div>

    <!-- Modal for attach first aid procedure -->
    <div id="modalAttach" class="modal" role="dialog" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
            <div class="modal-content">

                <div class="modal-header py-2">

                    <h5 class="modal-title font-weight-normal">Anexar procedimento</h5>

                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span>
                    </button>

                </div>

                <div class="modal-body">
                    <div class="row">

                        <div class="col-md-6">
                            <form>
                                <div class="form-group">
                                    <label for="selectAttach">Selecione um procedimento de primeiros socorros:</label>
                                    <select id="selectAttach" class="form-control" size="10" onchange="changeImgPreview(this.value)">
                                        <option value="0">Choque elétrico</option>
                                        <option value="1">Infarto e parada cardiorrespiratória</option>
                                        <option value="2">Envenenamento</option>
                                        <option value="3">Picada de cobra</option>
                                        <option value="4">Corpo estranho e asfixia</option>
                                        <option value="5">Queimadura</option>
                                        <option value="6">Sangramento</option>
                                        <option value="7">Fratura, luxação, contusÃ£o e entorce</option>
                                        <option value="8">Acidente de trânsito</option>
                                    </select>
                                </div>
                            </form>
                        </div>

                        <div class="col-md-6">
                            <p class="text-muted text-center mb-2">Pré-visualização</p>
                            <img id="imgPreview" class="img-fluid" src="img/attach-0.png">
                        </div>

                    </div>
                </div><!-- /.modal-body -->

                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="selectAttach()">Selecionar</button>
                </div>

            </div>
        </div>
    </div>

    <!-- Bootstrap core JavaScript -->
    <script src="js/jquery-3.js"></script>
    <script src="js/popper.js"></script>
    <script src="js/bootstrap.js"></script>
    <script>
        function selectAttach() {
            $("#textAttach").text($("#selectAttach :selected").text() + " selecionado.");
            $("#attach").val($("#selectAttach").val());
        }

        function changeImgPreview(imgUrl) {
            $("#imgPreview").attr("src", "img/attach-" + imgUrl + ".png");
        }

        var latLng = {
            lat: ${emergency.location.latitude},
            lng: ${emergency.location.longitude}
        };
        
        var marker;
        var map;

        function initMap() {
            map = new google.maps.Map(document.getElementById('map'), {
                center: {
                    lat: ${emergency.location.latitude},
                    lng: ${emergency.location.longitude}
                },
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