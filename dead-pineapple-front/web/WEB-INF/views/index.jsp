<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 15256
  Date: 12/02/2016
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="se-pre-con"></div>

<div id="conteneur" class="col-lg-12 col-md-12 ">
    <div id="ajoutez" class="col-lg-12 col-md-12 page-para">

        <%--  <div id="seconnecterindex" class="col-xs-9 col-xs-offset-2">
              <form:form action="/user/login" method="post" modelAttribute="loginAttribute">

                  <h2>Se connecter</h2>
                  <div class="form-group col-md-5"><form:input type="text" class="form-control" path="username"
                                                               placeholder="login"/></div>
                  <div class="form-group col-md-5"><form:input type="password" onfocus="this.value=''"
                                                               class="form-control" path="password"
                                                               placeholder="mot de passe"/></div>

                  <div class="form-group col-md-2">
                      <button type="submit" class="btn btn-success">Connexion</button>
                  </div>
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
                  <hr class="col-xs-12">
                  <h3>Ou s'incrire/se connecter via:</h3>
                  <div class="form-group">
                      <img type="subtmit" value="Facebook login" id="fbLogin2" onclick="" class="login"
                           src="/resources/img/bigFbLogin.png">
                      <img type="submit" value="Google login" id="googlelogin2" class="login"
                           src="/resources/img/bigGoogleLogin.png">
                  </div>

                  <hr/>
                  <div><a href="<spring:url value='/user/add'/>"> Créer un nouveau compte deadpineapple </a></div>
              </form:form>
          </div>
  --%>
        <h1 class="col-md-offset-1">Bienvenue sur Deadpineapple
            <br><span class="small">Premier transcodeur français</span></h1>
        <a href="<spring:url value="/upload"/>" class="btn btn-default col-md-5 col-xs-offset-4">Uploader une video!</a>
        <div class="scroll-down-btn col-xs-3 col-xs-offset-5">
            <button type="button" goto="#whats">Qu'est ce que dead-pineapple ?</button>
        </div>
    </div>
    <div id="whats" class="col-lg-12 page-para">

        <div id="1upload" class="etapes col-xs-2 col-md-2 col-xs-offset-1 ">
            <img class="etp img-responsive"
                 src="/resources/img/upload.svg"></div>
        <div id="2convert" class="etapes col-xs-2 col-md-2 col-xs-offset-1">
            <img class="etp img-responsive"
                 src="/resources/img/transform.svg"></div>
        <div id="3paye" class="etapes col-xs-2 col-md-2 col-xs-offset-1">
            <img class="etp img-responsive"
                 src="/resources/img/paye.svg"></div>
        <div id="4download" class="etapes col-xs-2 col-md-2 col-xs-offset-1">
            <img class="etp img-responsive"
                 src="/resources/img/download.svg"></div>
        <div id="explicatif" class="msg col-xs-9 col-xs-offset-2"><h1>
            <span class="small">Selectionnez un module ci-dessus pour plus de détails!</span>
        </h1></div>
        <div class="scroll-down-btn col-xs-3 col-xs-offset-5">
            <button type="button" goto="#whos">Qui sommes nous ?</button>
        </div>
    </div>
    <div id="whos" class="col-lg-12 col-md-12 page-para ">
        <div id="pablo" class="membre col-xs-1 col-xs-offset-2">
            <img class="etp teamate img-responsive"
                 src="/resources/img/pablo.png">
            <img class="lunnette" src="/resources/img/lunettes.svg" height="100">

        </div>
        <div id="sofiane" class="membre col-xs-1 col-xs-offset-1">
            <img class="etp teamate img-responsive"
                 src="/resources/img/sofianeROND.png">
            <img class="lunnette" src="/resources/img/lunettes.svg" height="100">

        </div>
        <div id="mikael" class="membre col-xs-1 col-xs-offset-1">
            <img class="etp teamate img-responsive"
                 src="/resources/img/mikaROND.png">
            <img class="lunnette" src="/resources/img/lunettes.svg" height="100">

        </div>
        <div id="lucas" class="membre col-xs-1 col-xs-offset-1">
            <img class="etp teamate img-responsive"
                 src="/resources/img/lucasROND.png">
            <img class="lunnette" src="/resources/img/lunettes.svg" height="100">
        </div>
        <div id="thomas" class="membre col-xs-1 col-xs-offset-1">
            <img class="etp teamate img-responsive"
                 src="/resources/img/thomasROND.png">
            <img class="lunnette" src="/resources/img/lunettes.svg" height="100">

        </div>

        <div id="description" class="msg col-xs-9 col-xs-offset-2"><h1 style="font-size: 16pt;">
            <span class="small">Découvrez les fondateurs de Dead-pinneaple !</span>
        </h1></div>

        <div class="scroll-down-btn col-xs-3 col-xs-offset-5">
            <button type="button" goto="#contact">Nous contacter</button>
        </div>

    </div>
    <div id="contact" class="col-lg-12 col-md-12 page-para ">
        <div id="coord" class="col-xs-9 col-lg-offset-2">
            <div class="col-xs-10"><h2>Nous contacter</h2></div>
            <div class="adresses col-xs-12">
                <h4>N'hésitez pas nous contacter aux différentes adresses suivantes : </h4><br/><br/>
                <b>Pablo MERIDJEN </b><br/><A HREF="mailto:216369@supinfo.com"> 216369@supinfo.com </A><br/><br/>
                <b>Sofiane AZIRI </b><br/><A HREF="mailto:215546@supinfo.com"> 215546@supinfo.com</A><br/><br/>
                <b>Mikaël LE FALHER </b> <br/><A HREF="mailto:216369@supinfo.com"> 222242@supinfo.com</A><br/><br/>
                <b>Lucas ZIENTEK</b> <br/><A HREF="mailto:152565@supinfo.com"> 152565@supinfo.com</A><br/><br/>
                <b>Tomas GERARDIN</b> <br/><A HREF="mailto:216369@supinfo.com"> 221568@supinfo.com</A><br/><br/>
            </div>
        </div>
    </div>
</div>



