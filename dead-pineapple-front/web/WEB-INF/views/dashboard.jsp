<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: saziri
  Date: 16/05/2016
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="<spring:url value='/resources/js/video.js'/>"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/video-js.min.css'/>">

<div class="container dashboard-container">
    <br/>
    <h1>
        Mon espace
    </h1>
    <hr/>
    <div class="progress">
        <span id="progressStatus"><c:out value="${userSize} "/> MB / 10 GB</span>
        <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="${spacePercent}"
             aria-valuemin="0" aria-valuemax="100" style="width: ${spacePercent}%">
        </div>
    </div>
    <div class="row">
        <div class="col-lg-5 col-lg-offset-1">
            <h3>Historique</h3>
            <c:if test="${not empty invoices}">
                <div class="invoices">
                    <c:forEach items="${invoices}" var="invoice" varStatus="loop">
                        <fmt:formatDate value="${invoice.date}" var="formattedDate" type="date" pattern="MM/dd/yyyy"/>
                        <div class="panel-group" style="margin-bottom: 5px">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" href="#collapse${loop.index}"> Facture du <c:out
                                                value="${formattedDate}"/> au prix de <c:out
                                                value="${invoice.price}"/> &euro;</a>
                                    </h4>
                                </div>
                                <div id="collapse${loop.index}" class="panel-collapse collapse">
                                    <ul class="list-group">
                                        <c:forEach items="${invoice.convertedFiles}" var="file">
                                            <li class="list-group-item"><c:out value="${file.originalName}"/>
                                                <div class="actions">
                                                    <c:choose>
                                                        <c:when test="${not empty file.filePath}">
                                                            <c:choose>

                                                                <c:when test="${file.inConvertion != true}">
                                                                    En cours de conversion...
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <button class="btn btn-primary"
                                                                            onclick="location.href='<spring:url
                                                                                    value='dashboard/downloadFile?fileName=${file.originalName}'/>'">
                                                                        <img src="/resources/img/icons/download.png">
                                                                    </button>
                                                                    <button class="btn btn-info"
                                                                            onclick="location.href='<spring:url
                                                                                    value='dashboard/downloadFileDb?fileName=${file.originalName}'/>'">
                                                                        <img src="/resources/img/icons/dropbox.png">
                                                                    </button>
                                                                    <button class="btn btn-danger"
                                                                            onclick="location.href='<spring:url
                                                                                    value='dashboard/deleteFile?fileName=${file.originalName}&invoiceNumber=${loop.index}'/>'">
                                                                        <img src="/resources/img/icons/delete.png">
                                                                    </button>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            Vidéo supprimée
                                                        </c:otherwise>

                                                    </c:choose>


                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <div class="panel-footer"><a
                                            href="<spring:url value='dashboard/getInvoice?invoiceId=${invoice.invoiceId}'/>"><b>Télécharger
                                        la facture</b></a></div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>
        <div class="col-lg-6">
            <h3>Lecteur video</h3>
            <video id="my-video" class="video-js" controls preload="auto" width="550" height="200"
                   poster="<spring:url value='/resources/img/preview.png'/>" data-setup="{}">
                <source src="${videoStream}" type='video/mp4'>
                <p class="vjs-no-js">
                    To view this video please enable JavaScript, and consider upgrading to a web browser that
                    <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
                </p>
            </video>
        </div>
    </div>
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
            <h3>Mes informations personnelles</h3>
            <form:form method="POST" action="/user/edit" id="editForm" cssClass="col-md-10 col-md-offset-1"
                       data-toggle="validator" modelAttribute="userAccount" disabled="true">
                <div class="form-group">
                    <form:label path="lastName" class="control-label">Votre nom</form:label>
                    <form:input path="lastName" value="${USER_INFORMATIONS.lastName}" id="name" type="text" name="name"
                                placeholder="Nom" maxlength="50" class="form-control" required="true"/>
                </div>
                <div class="form-group">
                    <form:label class="control-label" path="firstName">Votre prénom</form:label>
                    <form:input path="firstName" value="${USER_INFORMATIONS.firstName}" id="firstName" type="text"
                                name="firstname" placeholder="Prenom" maxlength="50" class="form-control"
                                required="true"/>
                </div>
                <div class="form-group">
                    <form:label class="control-label" path="email">Email</form:label>
                    <form:input path="email" value="${USER_INFORMATIONS.email}" id="signupEmail" type="email"
                                maxlength="50" placeholder="Email" name="mail" class="form-control" disabled="true"
                                required="true"/>
                    <span class="erreur">${ erreurs['emailCheck']}</span>
                </div>
                <div class="form-group">
                    <label class="control-label" for="adresse">Adresse</label>
                    <form:input id="adresse" value="${USER_INFORMATIONS.adresse}" name="adresse" path="adresse"
                                type="text" placeholder="Adresse" class="form-control" required="true"/>
                </div>
                <div class="col-sm-6 form-group" style="padding-left:0">
                    <form:input id="codePostal" value="${USER_INFORMATIONS.codePostal}" path="codePostal"
                                name="codePostal" type="text" placeholder="Code Postal" class="form-control"
                                required="true"/>
                </div>
                <div class="col-sm-6 form-group" style="padding-right:0">
                    <form:input id="ville" value="${USER_INFORMATIONS.ville}" path="ville" name="ville" type="text"
                                placeholder="Ville" class="form-control" required="true"/>
                </div>

                <div class="form-group">
                    <label class="control-label" for="phone">Telephone</label>
                    <form:input id="phone" path="phone" value="${USER_INFORMATIONS.phone}" type="text"
                                class="form-control" placeholder="Téléphone (Optional)"/>
                </div>
                <div class="form-group">
                    <label class="control-label" for="password">Password</label>
                    <input id="password" type="password" name="password" maxlength="25" class="form-control"
                           placeholder="Au moins 6 caractères" length="40" required="true">
                </div>
                <div class="form-group">
                    <label class="control-label" for="signupPasswordagain">Password again</label>
                    <input id="signupPasswordagain" type="password" data-match="#password" maxlength="25"
                           name="passwordConfirm" class="form-control" required="true">
                    <span class="erreur">${ erreurs['passwordCheck']}</span>
                </div>
                <div class="form-group">
                    <button id="signupSubmit" type="submit" class="btn btn-info btn-block">Modifier</button>
                </div>
            </form:form>
        </div>
    </div>
</div>