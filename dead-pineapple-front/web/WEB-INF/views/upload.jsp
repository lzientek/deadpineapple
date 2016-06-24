<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    Document   : upload
    Date: 14/03/2016
    User: saziri
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <div class="container dashboard-container">
            <br/>
            <h1>
                Conversion
            </h1>
            <hr/>
            <!-- The file upload form used as target for the file upload widget -->
            <form id="fileupload" action="<spring:url value='/upload/add'/>" method="POST" enctype="multipart/form-data">
                <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
                <div class="fileupload-buttonbar">
                    <div class="col-lg-7" style="padding-left: 0px;">
                        <!-- The fileinput-button span is used to style the file input field as button -->
                        <span class="btn btn-success fileinput-button">
                            <i class="glyphicon glyphicon-plus"></i>
                            <span>Ajouter un fichier</span>
                            <input type="file" name="files[]" multiple>
                        </span>
                        <button type="submit" class="btn btn-primary start">
                            <i class="glyphicon glyphicon-upload"></i>
                            <span>Uploader</span>
                        </button>
                        <span class="btn btn-info" onclick="location.href='${dropboxUrl}'" >
                            <i class="glyphicon glyphicon-upload"></i>
                            <span>Depuis dropbox</span>
                        </span>
                        <button type="reset" class="btn btn-warning cancel">
                            <i class="glyphicon glyphicon-ban-circle"></i>
                            <span>Annuler</span>
                        </button>
                        <!--
                        <button class="btn btn-info" class="btn btn-info btn-lg" data-toggle="modal" data-target="#ModalDropBox" >
                            <i class="glyphicon glyphicon-upload"></i>
                            <span>Parcourir votre Dropbox</span>
                        </button>!-->
                        <!-- Modal -->
                        <div id="ModalDropBox" class="modal fade" role="dialog">
                            <div class="modal-dialog">

                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <img class="etp" src="/resources/img/dropbox2.svg" height="30" >
                                        <h4 class="modal-title">Choix d'upload depuis Cloud Dropbox</h4>
                                    </div>
                                    <div class="modal-body">

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <!-- The global progress information -->
                    <div class="col-lg-5 fileupload-progress fade">
                        <!-- The global progress bar -->
                        <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                            <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                        </div>


                        <!-- The extended global progress state -->
                        <div class="progress-extended">&nbsp;</div>
                    </div>
                </div>
                <!-- The loading indicator is shown during file processing -->
                <div class="fileupload-loading"></div>
                <br>
                <!-- The table listing the files available for upload/download -->
                <table role="presentation" id="uploadedFiles" class="table table-striped"><tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>

                </table>
            </form>
            <br>
                <a href="<spring:url value='/upload/facture'/>" class="btn btn-primary start" id="payButton" price="" disabled>Payer et Convertir</a>
            <c:if test="${not empty dropboxFiles}">
                <div id="dropbox">
                    <h2>Mes vidéos </h2>
                    <ul>
                        <c:forEach var="listValue" items="${dropboxFiles}">
                            <li><a href="#" class="dropbox_file">${listValue}</a></li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            <!-- Modal Start here-->
            <div class="modal fade bs-example-modal-sm" id="myPleaseWait" tabindex="-1"
                 role="dialog" aria-hidden="true" data-backdrop="static">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">
                    <img src="/resources/img/dropbox/dropbox.ico">
                    </span>Upload dropbox en cours...
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="progress">
                                <div class="progress-bar progress-bar-info
                    progress-bar-striped active"
                                     style="width: 100%">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Modal ends Here -->
            <div class="well">
                <h3>Information sur l'upload</h3>
                <ul>
                    <li>The maximum file size for uploads is <strong>10 GB</strong></li>
                    <li>Only video files (<strong>".avi", "mp4", ".ogg", ".flv",".swf",".dv",".mov"</strong>) are allowed.</li>
                    <li>Uploaded files will be deleted automatically after <strong>5 minutes</strong>.</li>
                    <li>You can <strong>drag &amp; drop</strong> files from your desktop on this webpage with Google Chrome, Mozilla Firefox and Apple Safari.</li>
                </ul>
            </div>

        </div>

        <!-- The template to display files available before upload -->
        <script id="template-upload" type="text/x-tmpl">
            {% console.log("Suppression file");
            for (var i=0, file; file=o.files[i]; i++) { %}
        <div class="row template-upload fade" style="margin-top: 10px">
            <div class="col-sm-1">
            <span class="preview">
                <img src="/resources/img/video-player.png" style="margin-top: 5px;"/>
            </span></div>
            <div class="col-sm-3 name"><span>{%=file.name%}</span></div>
            <div class="col-sm-1 size"><span>{%=o.formatFileSize(file.size)%}</span></div>
            {% if (file.error) { %}
            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
            {% } else if (o.files.valid && !i) { %}
            <div class="col-sm-3 progress_style">
                <div class="progress">
                            <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar"
                                 aria-valuemin="0" aria-valuemax="100">
                            </div>
                </div>
            </div>

            <div class="col-sm-2 start">{% if (!o.options.autoUpload) { %}
                <button class="btn btn-primary">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Démarrer</span>
                </button>
                {% } %}</div>
            {% } else { %}
            {% } %}
            <div class="col-sm-2 cancel">{% if (!i) { %}
                <button class="btn btn-warning">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Annuler</span>
                </button>
                {% } %}
            </div>
        </div>
        {% } %}
    </script>
    <!-- The template to display files before conversion -->
    <script id="template-download" type="text/x-tmpl">
        {% var totalPrice = 0.0;
        for (var i=0, file; file=o.files[i]; i++) {
        totalPrice +=parseFloat(file.price);%}
        <div class="row template-download fade">
            {% if (file.error) { %}
            <td ><span class="name">{%=file.name%}</span></td>
            <td ><span class="size">{%=o.formatFileSize(file.size)%}</span></td>
            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
            {% } else { %}
            <div class='download-content'>
            <div class="col-sm-1 image">
            <span class="preview">{% if (file.thumbnail_url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" rel="gallery" download="{%=file.name%}"><img src="{%=file.thumbnail_url%}" style="margin-top: 5px;"></a>
                {% } %}</span></div>
            <div class="col-sm-3 filename">
                <span class="name">{%=file.name%}</span>
            </div>
            <div class="col-sm-1"><span class="size">{%=o.formatFileSize(file.size*1000000)%}</span></div>
            <div class="col-sm-1"><span class="duration">{%=file.duration%}</span></div>

            <div class="col-sm-1"><span class="price">{%=file.price%} &euro;</span></div>
            <div class="col-sm-1">
                 <div class="form-group">
                      <select class="form-control formats" filename="{%=file.name%}"required>
                        <option value="avi">.avi</option>
                        <option value="mp4">.mp4</option>
                        <option value="mp3">.mp3</option>
                        <option value="aac">.aac</option>
                        <option value="wav">.wav</option>
                        <option value="wma">.wma</option>
                        <option value="wmv">.wmv</option>
                        <option value="ogg">.ogg</option>
                        <option value="flv">.flv</option>
                        <option value="swf">.swf</option>
                        <option value="dv">.dv</option>
                        <option value="mov">.mov</option>
                     </select>
                </div>
            </div>
            <div class="col-sm-1">
                  <div class="form-group">
                      <select class="form-control" filename="{%=file.name%}" required>
                        <option> </option>
                        <option>ffv1</option>
                        <option>h.264</option>
                        <option>vp8</option>
                        <option>vp9</option>
                        <option>xvid</option>
                      </select>
                  </div>
            </div>
            {% } %}
            <div class="col-sm-2 col-xs-1 delete">
                <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}"{% if (file.delete_with_credentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                        <i class="glyphicon glyphicon-trash"></i>
                    <span>Supprimer</span>
                </button>
            </div>
            <div class="col-sm-12"><hr> </div>
            </div>
        </div>
        {% } %}
        {%
        var priceString = document.getElementById("payButton").getAttribute("price");
        if(priceString != ""){
            var price = parseFloat(priceString)
            totalPrice += price;
        }
        document.getElementById("payButton").setAttribute("price", totalPrice);
        document.getElementById("payButton").innerHTML = "Payer ( "+totalPrice.toFixed(2)+"€ ) et convertir";
        document.getElementById("payButton").removeAttribute("disabled");
        %}
    </script>
    <script src="<spring:url value='/resources/js/vendor/jquery.ui.widget.js'/>"></script>
    <script src="<spring:url value='/resources/js/tmpl.min.js'/>"></script>
    <script src="<spring:url value='/resources/js/load-image.min.js'/>"></script>
    <script src="<spring:url value='/resources/js/jquery.iframe-transport.js'/>"></script>
    <script src="<spring:url value='/resources/js/jquery.fileupload.js'/>"></script>
    <script src="<spring:url value='/resources/js/jquery.fileupload-fp.js'/>"></script>
    <script src="<spring:url value='/resources/js/jquery.fileupload-ui.js'/>"></script>
    <script src="<spring:url value='/resources/js/locale.js'/>"></script>
    <script src="<spring:url value='/resources/js/main.js'/>"></script>
    <script type="text/javascript">

        function setFormat(format, fileName){
            // When user choose a format for the file, send it to the bdd
            console.log("Envoi format vers serveur ");
            console.log(format, fileName);
            $.ajax({
            type:"GET",
            url: "/upload/setFormat",
            data: {     format: format,
                        file: fileName}
            }).done(function(msg){
                // Format set (display price)
            });

        };
        $(document).on('change', '.formats', function() {
            var format = $('option:selected',this).text();
            var filename = $('option:selected',this).parent().attr("filename");
            setFormat(format, filename);

        });
        $(document).on('change', '.encodage', function() {
            var encodage = $('option:selected',this).text();
            var filename = $('option:selected',this).parent().attr("filename");
            setEncodage(encodage, filename);

        });
        function setEncodage(encodage, fileName){
            // When user choose a format for the file, send it to the bdd
            $.ajax({
                type:"GET",
                url: "/upload/setEncodage",
                data: {     encodage: encodage,
                    file: fileName}
            }).done(function(msg){
                // Format set (display price)
            });

        };

        $(document).ready( function() {
            function openFile(file) {
                if (confirm("Souhaitez-vous uploader le fichier :"+ file+" ?")) {
                    // Save it!
                    $.ajax({
                        type:"GET",
                        url: "/upload/setFormat",
                        data: { fileName: file}
                    }).done(function(msg){
                        // Format set (display price)
                    });
                } else {
                    // Do nothin…
                }

            }
            // Dropbox
            $(document).on('click', '.dropbox_file', function() {
                $('#myPleaseWait').modal('show');
                var filename = $(this).text();
                getFiles("/upload/uploadDb?fileName="+filename);
            });

            // Load files
            function getFiles(url){
                $.getJSON(url, function (data) {
                    if(url != "/upload/getFiles"){
                        $('#myPleaseWait').modal('hide');
                    }
                    var totalPrice = 0;
                    var fileRow = "";
                    for(var i = 0;i < data.length;i++){
                        fileRow = document.createElement('div');
                        $(fileRow).addClass('row template-download fade in');
                        var downloadContent = document.createElement('div');
                        $(downloadContent).addClass('download-content');

                        var col1, col2, col3, col4, col5, col6, col7, col8;
                        var separateLine = document.createElement('div');
                        $(separateLine).addClass('col-sm-12').html('<hr>');
                        var fileName = "";
                        for(var attr in data[i]){
                            if(attr == "thumbnail_url") {
                                // Create thumbnail element
                                col1 = document.createElement('div');
                                var previewSpan = document.createElement('span');
                                var previewImg = document.createElement('img');
                                $(previewImg).attr('src',data[i][attr]);
                                $(previewImg).attr('style','margin-top: 5px;');
                                $(previewSpan).addClass('preview').html(previewImg);
                                $(col1).addClass('col-lg-1 col-sm-3 col-xs-6 image').html(previewSpan);
                            }
                            else if(attr == "name"){
                                // Create name html element
                                fileName = data[i][attr];
                                var col2 = document.createElement('div');
                                var fileNameSpan = document.createElement('span');
                                $(fileNameSpan).addClass('name').html(data[i][attr]);
                                $(col2).addClass('col-lg-3 col-sm-4 col-xs-6 filename').html(fileNameSpan);
                            }
                            else if(attr == "size") {
                                // Create size html element
                                col3 = document.createElement('div');
                                var spanSize = document.createElement('span');
                                $(spanSize).addClass(attr).html(formatFileSize(parseFloat(data[i][attr])*1000000));
                                $(col3).addClass('col-lg-1 col-sm-4 col-xs-6').html(spanSize);
                            }
                            else if(attr == "duration") {
                                // Create duration html element
                                col4 = document.createElement('div');
                                var spanDuration = document.createElement('span');
                                $(spanDuration).addClass(attr).html(data[i][attr]);
                                $(col4).addClass('col-lg-1 col-sm-4 col-xs-6').html(spanDuration);
                            }
                            else if(attr == "price"){
                                // Create price html element
                                priceValue = data[i][attr];
                                // Create price html element
                                col5 = document.createElement('div');
                                var priceSpan = document.createElement('span');
                                $(priceSpan).addClass('price').html(priceValue+"€");
                                $(col5).addClass('col-lg-1 col-sm-4 col-xs-6').html(priceSpan);

                                // Save the total price
                                totalPrice += parseFloat(priceValue);
                            }
                            else if(attr == "delete_url") {
                                // Create delete html button
                                col8 = document.createElement('div');
                                var deleteButton = document.createElement('button');
                                var deleteI = document.createElement('i');
                                $(deleteI).addClass('glyphicon glyphicon-trash').html('<span>Supprimer</span>');
                                $(deleteButton).attr('data-type','GET');
                                $(deleteButton).attr('data-url', data[i][attr]);
                                $(deleteButton).addClass('btn btn-danger').html(deleteI);
                                $(col8).addClass('col-lg-2 col-sm-1 col-xs-1 delete').html(deleteButton);
                            }
                        }


                        // Create format element
                        var formatArray = [ "avi", "mp4", "mp3", "aac", "wav", "wma", 'wmv','ogg','flv','swf','dv','mov'];
                        col6 = document.createElement('div');
                        var formatSelect = document.createElement('select');
                        $(formatSelect).addClass('form-control formats').attr('fileName', fileName);
                        jQuery.each( formatArray, function( i, val ) {
                            var option = document.createElement('option');
                            $(option).attr('value', val);
                            $(option).html('.'+val);
                            $(formatSelect).append(option);
                        });
                        $(col6).addClass('col-lg-1 col-sm-4 col-xs-6 form-group').html(formatSelect);

                        // Create encodage select element
                        col7 = document.createElement('div');
                        var encodageArray = ['', 'ffv1','h.264', 'vp8', 'vp9', 'xvid' ];
                        var encodageDiv = document.createElement('div');
                        var encodageSelect = document.createElement('select');
                        $(encodageSelect).addClass('form-control encodage').attr('fileName', fileName);
                        jQuery.each( encodageArray, function( i, val ) {
                            var option = document.createElement('option');
                            $(option).attr('value', val);
                            $(option).html(val);
                            $(encodageSelect).append(option);
                        });
                        $(col7).addClass('col-lg-1 col-sm-4 col-xs-6 form-group').html(encodageSelect);

                        $(downloadContent).append(col1, col2, col3, col4, col5, col6, col7, col8, separateLine)
                        $(fileRow).append(downloadContent).appendTo(".files");
                    }
                    if(totalPrice > 0){
                        $("#payButton").attr("price", totalPrice.toFixed(2));
                        $("#payButton").html("Payer ( "+totalPrice.toFixed(2)+"€ ) et convertir");
                        $("#payButton").removeAttr("disabled");
                    }

                });
            };
            getFiles("/upload/getFiles");

            function formatFileSize(bytes) {
                    if (typeof bytes !== 'number') {
                        return '';
                    }
                    if (bytes >= 1000000000) {
                        return (bytes / 1000000000).toFixed(2) + ' GB';
                    }
                    if (bytes >= 1000000) {
                        return (bytes / 1000000).toFixed(2) + ' MB';
                    }
                    return (bytes / 1000).toFixed(2) + ' KB';
            };
            $(document).on('click', '.delete', function() {
                var totalPrice = 0.0;
                setTimeout(function(){
                    $( ".price" ).each(function( index ) {
                        totalPrice += parseFloat($( this ).text())
                    });
                    if(totalPrice == 0){
                        $("#payButton").attr("disabled", true);
                        $("#payButton").html("Payer et convertir");
                    }
                    else{
                        $("#payButton").html("Payer ( "+totalPrice.toFixed(2)+"€ ) et convertir");
                    }
                    document.getElementById("payButton").setAttribute("price",  totalPrice.toFixed(2));
                },300);
            });
        });

        </script>