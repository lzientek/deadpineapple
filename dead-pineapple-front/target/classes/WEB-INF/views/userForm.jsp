<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--
  Created by IntelliJ IDEA.
  User: saziri
  Date: 14/03/2016
  Time: 09:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    body{/*c'est sal mais pratique!*/
        background: url(/resources/img/ananasmort2.svg) center 0 no-repeat fixed;
    }
</style>

<form:form method="POST" action="/user/add" id="subscribeForm" cssClass="col-md-offset-4 col-md-5 col-xs-offset-2 col-xs-10 col-md-7"
           data-toggle="validator" modelAttribute="userAccount">
    <div class="form-group">
        <h2>Créer un compte</h2>
    </div>
    <div class="form-group">
        <form:label path="lastName" class="control-label">Votre nom</form:label>
        <form:input path="lastName" id="name" type="text" name="name" placeholder="Nom" maxlength="50"
                    class="form-control" required="true"/>
    </div>
    <div class="form-group">
        <form:label class="control-label" path="firstName">Votre prénom</form:label>
        <form:input path="firstName" id="firstName" type="text" name="firstname" placeholder="Prenom" maxlength="50"
                    class="form-control" required="true"/>
    </div>
    <div class="form-group">
        <form:label class="control-label" path="email">Email</form:label>
        <form:input path="email" id="signupEmail" type="email" maxlength="50" placeholder="Email" name="mail"
                    class="form-control" required="true"/>
        <span class="erreur">${ erreurs['emailCheck']}</span>
    </div>

    <div class="form-group">
        <label class="control-label" for="adresse">Adresse</label>
        <input id="adresse" name="adresse" path="adresse" type="text" placeholder="Adresse" class="form-control" required="true">
    </div>

    <div class="col-sm-6 form-group" style="padding-left:0">
        <input id="codePostal" path="codePostal" name="codePostal" type="text" placeholder="Code Postal" class="form-control" name="zipcode" required="true">
    </div>
    <div class="col-sm-6 form-group" style="padding-right:0">
        <input id="ville" path="ville" name="ville" type="text" placeholder="Ville" class="form-control" name="city" required="true">
    </div>

    <div class="form-group">
        <label class="control-label" for="phone" >Telephone</label>
        <input id="phone" path="phone" type="text" class="form-control" placeholder="Téléphone (Optional)">
    </div>
    <div class="form-group">
        <label class="control-label" for="password">Mot de passe</label>
        <input id="password" type="password" name="password" maxlength="25" class="form-control"
               placeholder="Au moins 6 caractères" length="40" required="true">
    </div>
    <div class="form-group">
        <label class="control-label" for="signupPasswordagain">Encore</label>
        <input id="signupPasswordagain" type="password" data-match="#password" maxlength="25"
               placeholder="Confirmez votre mot de passe" name="passwordConfirm" class="form-control" required="true">
        <span class="erreur">${ erreurs['passwordCheck']}</span>
    </div>
    <div class="form-group col-md-12">
        <button id="signupSubmit" type="submit" class="btn btn-info col-md-offset-3 col-md-6">Create your account
        </button>
    </div>
    <p class="form-group">By creating an account, you agree to our <a href="/bartering/termsOfService.jsp">Terms of
        Use</a> and our <a href="/bartering/privacypolicy.jsp">Privacy Policy</a>.</p>

    <hr>
    <p class="form-group">Déja un compte? Connectez vous <a href="#">Sign in</a></p>

</form:form>

