$(document).ready(function () {

    $("#uploaded").hide();
    $("#addagainbtn").hide();
    $("#convertbtn").hide();
    $("#deleteallbtn").hide();

//lorsque l'on clic sur  l'upload


    $("#uploadbtn").click(function () {
        $("#uploadbtn").hide();
        $("#cancelbtn").hide();
        $("#depot").hide();
        $("#uploaded").show();
        $("#convertbtn").show();
        $("#deleteallbtn").show();
        $("#addagainbtn").show();

    });
//lorsque l'on clique sur addagain
    $("#addagainbtn").click(function () {
        console.log("addagain");
        $("#addagainbtn").hide();
        $("#uploaded").hide();
        $("#depot").show();
        $("#uploadbtn").show();
        $("#cancelbtn").show();
        $("#convertbtn").hide();
        $("#deleteallbtn").hide();
    });


});
