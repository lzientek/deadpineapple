<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lzientek
  Date: 14/03/2016
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="glissiere">
    <div id="poussoirG"></div>
    <div id="leftbar">
        <ul class="menu-vertical">

            <c:choose>
                <c:when test="${sessionScope.LOGGEDIN_USER == null}">

                    <form:form action="/user/login" method="post" modelAttribute="loginAttribute">
                        <li id="creer" class="mv-item"><a>Se connecter</a></li>
                        <li id="login" class="connect"><form:input type="text" class="login-inp" path="username"
                                                                   placeholder="login"/></li>
                        <li id="mdp" class="connect"><form:input type="password" onfocus="this.value=''"
                                                                 class="login-inp" path="password"
                                                                 placeholder="mot de passe"/></li>
                        <c:if test="${not empty param.error}">
                            <div class="alert alert-error">
                                Invalid username and password.
                            </div>
                        </c:if>
                        <c:if test="${not empty param.logout}">
                            <div class="alert alert-success">
                                You have been logged out.
                            </div>
                        </c:if>


                        <input type="checkbox" class="checkbox-size connect" id="login-check"/>
                        <label class="connect"
                               for="login-check">Se souvenir de moi</label>
                        <div class="connect"><input type="submit" class="btn btn-success" value="connexion"/></div>
                        <li class="connect" style="margin:5px;">
                            <img type="subtmit" value="Facebook login" id="fbLogin" onclick="" class="login"
                                 src="/resources/img/F_icon.svg.png">
                            <img type="submit" value="Google login" id="googlelogin" class="login"
                                 src="/resources/img/google-icon.png">
                        </li>
                        <li class="mv-item"><a href="<spring:url value='/user/add'/>"> S'incrire! </a></li>


                    </form:form>
                </c:when>
                <c:otherwise>
                    <div>
                        <a href="<spring:url value='/user/logOff'/>" class="btn btn-warning mv-item">Se déconnecter</a>
                    </div>
                    <div>
                        Bienvenue <c:out value="${USER_INFORMATIONS.firstName}"/>
                    </div>
                    <li class="mv-item"><a href="<spring:url value='/dashboard'/>"> Mon espace </a></li>

                </c:otherwise>
            </c:choose>
            <li class="mv-item"><a href="<spring:url value='/upload'/>"> Convertir une video </a></li>
            <li id="kesako" class="mv-item"><a href="<spring:url value='/index'/>#whats">Que faisons nous</a></li>
            <li id="qui" class="mv-item"><a href="<spring:url value='/index'/>#whos">Qui sommes nous</a></li>
            <li id="call" class="mv-item"><a href="<spring:url value='/index'/>#contact">Nous contacter</a></li>
        </ul>

        <div id="repli">
            <img src="/resources/img/lucas_svg/employee.svg" height="30" class="icon">
            <!--<img src="/resources/img/lucas_svg/user.svg" height="30" class="icon">-->
            <img src="/resources/img/lucas_svg/speedometer.svg" height="30" class="icon">

            <img src="/resources/img/lucas_svg/team.svg" height="30" class="icon">
            <img src="/resources/img/lucas_svg/chat.svg" height="30" class="icon">
        </div>

    </div>
    <div id="poussoirD"></div>
</div>
