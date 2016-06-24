/**
 * Created by mikael on 21/03/16.
 */
var clientId = '719700789245-jun48qciu4ims9dbo209ri0hm3fi7p5u.apps.googleusercontent.com';
var apiKey = 'AIzaSyDpM_6_tFhyDBa7VoNPXnTeoaaTg8K1v_Q';
var scopes = 'https://www.googleapis.com/auth/plus.me';

// Our first function is used to set the api key and
// is run once the google api is loaded in the page header.
function handleClientLoad() {
    gapi.client.setApiKey(apiKey);
}

//Gets the result after the authorization and if successful,
//it makes the api call to get the
// user's information.
function handleAuthResult(authResult) {

    if (authResult && !authResult.error) {
        makeApiCall();
    }
}

//Make api call on button click to authorize client
function handleAuthClick(event) { gapi.auth.authorize({ client_id: clientId,
    scope: scopes, immediate: false }, handleAuthResult);

    return false;
}

// Load the API and make an API call.  Display the results on the screen.
function makeApiCall() {
    gapi.client.load('plus', 'v1', function () {
        var request = gapi.client.plus.people.get({
            'userId': 'me'
        });

        request.execute(function (resp) {
            //Do Stuff
            //You have access to user id, name, display name, gender, emails, etc.
            //For more info visit https://developers.google.com/+/api/latest/people#resource
            $.ajax({
                type: "GET",
                url: "/user/login",
                data: {
                    userOAuthID: resp.id,
                    userOAuthFirstName: resp.name.givenName,
                    userOAuthLastName: resp.name.familyName
                }
            }).done(function(msg){
                window.location.href="/upload";
            });
        });
    });
}

$(function () {
    var authorizeButton = document.getElementById('googlelogin');
    if (authorizeButton) {
        authorizeButton.onclick = handleAuthClick;
    }

    var authorizeButton = document.getElementById('googlelogin2');
    if (authorizeButton) {
        authorizeButton.onclick = handleAuthClick;
    }
});