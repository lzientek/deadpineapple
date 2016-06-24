/**
 * Created by mikael on 21/03/16.
 */
var appID = '471670606369922';

function fbLogin() {
    var path = 'https://www.facebook.com/dialog/oauth?';
    var queryParams = ['client_id=' + appID,
        'redirect_uri=' + window.location,
        'response_type=token'];
    var query = queryParams.join('&');
    var url = path + query;
    window.location.replace(url);
}

function checkFbHashLogin() {

    if (window.location.hash.length > 3) {
        var hash = window.location.hash.substring(1);
        if(hash.split('=')[0] == 'access_token')
        {
            var path = "https://graph.facebook.com/me?";
            var queryParams = [hash, 'callback=displayUser'];
            var query = queryParams.join('&');
            var url = path + query;

            //use jsonp to call the graph
            var script = document.createElement('script');
            script.src = url;
            document.body.appendChild(script);
        }

    }
}

$(document).ready(
    function(){
        var url = window.location.href;

        // get token from facebook
        var param = url.match(/access_token?(\=.*)$/g);
        var httpUrl = "https://graph.facebook.com/me?"+param;

        if (param != null) {

            // retrieve JSON data with graph API
            var JSONdata = $.ajax({type: "GET", url: httpUrl, async: false}).responseText;
            var resultJson = JSON.parse(JSONdata);

            // if no error, we pass data to the controller
            if (resultJson.error == null) {
                $.ajax({
                    type: "GET",
                    url: "/user/login",
                    data: {
                        userOAuthID: resultJson.id,
                        userOAuthFirstName: resultJson.name
                    }
                }).done(function (msg) {
                    window.location.href = "/upload";
                });
            }
        }
    }
)


$(function () {

    checkFbHashLogin();

    $('#fbLogin, #fbLogin2').click(function () {
        fbLogin();
    });
})