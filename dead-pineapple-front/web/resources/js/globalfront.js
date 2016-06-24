$(document).ready(function () {
    var cible;
    var position;
    var placementLEFT;
    var placementTOP;
//definition dimentions fenetre
    var largeur_fenetre, hauteur_fenetre, conteneur_acceuil;


//redimentionnement du conteneur_acceuil
    function misAjourDesTaillesConteneur() {
        largeur_fenetre = $(window).width();
        hauteur_fenetre = $(window).height();
        if (hauteur_fenetre < 480) {
            hauteur_fenetre = 480;
        }
        conteneur_acceuil = hauteur_fenetre * 4;

        $('#conteneur').height(conteneur_acceuil);
        $('#ajoutez').height(hauteur_fenetre);
        $('#whats').height(hauteur_fenetre);
        $('#whos').height(hauteur_fenetre);
        $('#contact').height(hauteur_fenetre);
    }

    //si on est sur du paralax
    var actualPosition = -1;
    if ($('#conteneur').length > 0) {
        //grossissement des icones sur le scroll
        var grossiment = function () {
            var position = parseInt($(window).scrollTop() / hauteur_fenetre);
            if (position != actualPosition) {
                actualPosition = position;
                var icons = $('#repli .icon');
                for (var i = 0; i < icons.length; i++) {
                    if (i == actualPosition) {
                        $(icons[i]).addClass('bigger');
                    } else {
                        $(icons[i]).removeClass('bigger');
                    }
                }
            }
        };

        //sur le scroll et au start

        $(window).resize(misAjourDesTaillesConteneur);
        misAjourDesTaillesConteneur();

        $(window).scroll(grossiment);
        grossiment();
    }


//placement des lunettes
    $('.lunnette').mouseover(function () {
        $(this).parent().children('.etp').addClass('etphv');
    }, function () {
        $(this).parent().children('.etp').removeClass('etphv');
    });
    $(".membre img").mouseover(function () {
        cible = $(this).parent().children('.lunnette');
        $('.lunnette').each(function () {
            if ($(this).parent().attr('id') != cible.parent().attr('id')) {
                $(this).stop(true, true).animate({opacity: 1});
            }
        });
        cible.animate({opacity: 0});
    });

    //affichage de description des membres
    $("#pablo").mouseover(function () {
        $("#description h1").html('Pablo MERIDJEN<br/>Principaux travaux<span class="small"> Front design, Mise ne place des outils agile, scrum master</span><br/>Bio <span class="small">Ayant un parcours assez atypique, Pablo a fait des études aux thématiques différentes en abordant le multimedia, le communication et la programmation web et 3D. En alternance à ICDC et ccupant le poste dassistant MOA dans un équipe Agile, il cest naturrelement proposé pour mettre en place une gestion agile du projet et soccuper du front </span><br/>');
    });

    $("#thomas").mouseover(function () {
        $("#description h1").html('Thomas GERARDIN<br/> Principaux travaux <span class="small">DevOps, Linux, Open Source</span> Ingénieur systèmes passionné par l\'Open source et GNU Linux. Il a rejoint Deadpineapple bien décidé à exploiter pleinnement les nouvelles technologies Open source au sein de cette entreprise innovante.<br/>Bio <span class="small">DUT Informatique (spécialité réseaux et gestion de projet)<br/>Licence, Gestion de ressources informatiques<br/>Master of Science, Ingénierie informatique</span><br/>' +
        'Retrouvez mon parcours complet sur <a href="https://www.linkedin.com/in/tgerardin">Linkedin</a></span>');
    });
    $("#sofiane").mouseover(function () {
        $("#description h1").html('Sofiane AZIRI <br/> Principaux travaux <span class="small">Architecture Java Framework Spring, API Dropbox , Manipulation & Upload fichiers Asynchrones</span><br/>Bio <span class="small">BTS Services Informatique aux Organisations <br/> Licence Profesionnelle SIL - Systèmes Informatiques et Logicieles <br/> Master 1 & 2 : Supinfo Expert en informatique <br/>' +
            'Passionné par le développement d\'architectures et d\'infrastructures web, j\'apporte aux projets mon expertise sur le fonctionnement et la navigation d\'un site' +
            'Retrouvez moi sur <a href="https://fr.linkedin.com/in/sofiane-aziri-b45761104">Linkedin</a></span>');
    });
    $("#lucas").mouseover(function () {
        $("#description h1").html('Lucas ZIENTEK<br/> Principaux travaux <span class="small">Worker ffmpeg, architecture logiciel, front, manequin officiel!</span><br/>Bio <span class="small">Après avoir travailler chez google et facebook pendant 5ans Lucas est un ingénieurs qui déchire! Il apporte toutes ses connaissances en algorythme et programmation a l\'équipe junior.</span><br/>');
    });

    $("#mikael").mouseover(function () {
        $("#description h1").html('Mikaël LE FALHER<br/> Principaux travaux <span class="small">Gestion et intégration de la base de donnée, API facebook & Google, API PayPal</span><br/>Bio <span class="small">DUT Informatique (spécialité réseaux et gestion de projet)<br/>Licence Profesionnelle SIL - Système d\'Information et Service Web <br/>Master of Science, Ingénierie informatique</span><br/>    ');
    });


//navi
//$("#help").hide();

//déclenches les infos steps
    $("#1upload").mouseover(function () {
        $("#explicatif h1").html('Uploadez jusqu\'à 10 gigas de videos à convertir. <br/> <span class="small"> N\'importe ou, n\'importe quand!</span>');
    });
    $("#2convert").mouseover(function () {
        $("#explicatif h1").html('Convertissez vers tout type de formats et encodages!<br/> <span class="small">14 formats et 5 encodages disponibles.</span>');
    });
    $("#3paye").mouseover(function () {
        $("#explicatif h1").html('Payez un maximum de <strong>1€</strong>! <br/> <span class="small">Pour un film 1 heure.</span>');
    });
    $("#4download").mouseover(function () {
        $("#explicatif h1").html('Télécharger vos conversions depuis toute les plateforme! <br/> <span class="small">Depuis votre boite mail ou votre espace dédié en ligne.</span>');
    });

});
