<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Joystick</title>
    <style>
        body {
            overflow: hidden;
        }

        #container {
            position: absolute;
            bottom: 10px;
            left: 50%;
            transform: translateX(-50%);
            width: 200px;
            height: 200px;
            background-color: #000000;
            border-radius: 50%;
            margin: 100px auto;
            touch-action: none;
        }

        #joystick {
            position: absolute;
            width: 50px;
            height: 50px;
            background-color: #ff0000;
            border-radius: 50%;
            cursor: pointer;
            user-select: none;
        }

        #buttonContainer {
            position: absolute;
            top: 200px;
            width: 100%;
            text-align: center;
        }

        button {
            padding: 20px 100px;
            font-size: 20px;
            cursor: pointer;
            touch-action: manipulation;
            background-color: #ffffff;
            color: black;
            border: 2px solid black;
            border-radius: 15px;
        }

        #numJ {
            position: absolute;
            bottom: 30px;
            left: 50%;
            transform: translateX(-50%);
            background-color: #ffffff;
            padding: 5px 10px;
            border: 2px solid black;
            border-radius: 15px;
            font-size: 25px;
            touch-action: none;
        }

        #score {
            position: absolute;
            bottom: 30px;
            left: 40px;
            font-size: 20px;
            background-color: #ffffff;
            padding: 5px 10px;
            border: 2px solid black;
            border-radius: 15px;
            touch-action: none;
        }

        .closeButton {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            background-color: #ffffff;
            color: black;
            border: 2px solid black;
            border-radius: 10px;
            margin-top: 10px;
            left : 50%;
        }
    </style>

</head>
<body>

<div id="popup" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 9999;">
    <button class="voteButton" data-vote="1">1</button>
    <button class="voteButton" data-vote="2">2</button>
    <button class="voteButton" data-vote="3">3</button>
    <button class="voteButton" data-vote="4">4</button>
    <span class="closeButton" onclick="closePopup('popup')">Fermer</span>
</div>

<button id="popupButton" style="position: fixed; top: 10px; left: 50%; transform: translateX(-50%);">Voter</button>

<div id="popupShop" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 9999;">
    <button class="shopButton" data-item="1">Vitesse +</button>
    <button class="shopButton" data-item="2">Vitesse -</button>
    <button class="shopButton" data-item="3">Taille +</button>
    <button class="shopButton" data-item="4">Taille -</button>
    <span class="closeButton" onclick="closePopup('popupShop')">Fermer</span>
</div>

<button id="shopButton" style="position: fixed; top: 90px; left: 50%; transform: translateX(-50%);">Shop 5$</button>

<div id="buttonContainer">
    <button id="actionButton">REPOUSSER 10$</button>
</div>

<div id="container">
    <div id="joystick"></div>
</div>

<div id="score"><span id="scoreSpan">0</span>$</div>
<div id="numJ">Joueur n°<%= session.getAttribute("numJoueur") %></div>

<script>

    var mainLoopId = setInterval(function(){
        // AJAX
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "http://192.168.144.10:8080/serveurWeb_war_exploded/demande/info/score", true);
        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                // La requête a reussi faut traiter la reponse
                document.getElementById("scoreSpan").innerHTML = xhr.responseText;
            } else {
                console.error("erreur 2");
            }
        };
        xhr.onerror = function () {
            console.error("erreur ajax");
        };
        xhr.setRequestHeader("Content-Type", "application/json");
        // send la requete
        xhr.send();

    }, 1000);
    // FIN AJAX

    var nouvelleDistance = 0;
    var distanceSeuil = 0;
    var lastX = 0;
    var lastY = 0;

    var delay = 500;

    // Couleur background
    var rouge = '<%= session.getAttribute("rouge") %>';
    var vert =  '<%= session.getAttribute("vert") %>';
    var bleu = '<%= session.getAttribute("bleu") %>';
    document.body.style.backgroundColor = 'rgb(' + rouge + ',' + vert + ',' + bleu + ')';

    // Magnifique fonction
    function sendRequest(direction) {
        console.log('sendRequest: ' + direction);
        fetch('/serveurWeb_war_exploded/move?direction=' + direction, { method: 'POST' });
    }

    // FERMER LES POPUP
    function closePopup(popupId) {
        document.getElementById(popupId).style.display = 'none';
    }

    // VOTE
    document.addEventListener('DOMContentLoaded', function() {
        var popupButton = document.getElementById('popupButton');
        var popup = document.getElementById('popup');
        var isVoteDisabled = false;

        popupButton.addEventListener('click', function() {
            popup.style.display = 'block';
        });

        var voteButtons = document.querySelectorAll('.voteButton');
        voteButtons.forEach(function(button) {
            button.addEventListener('click', function() {
                if (!isVoteDisabled) {
                    isVoteDisabled = true;
                    setTimeout(function() {
                        isVoteDisabled = false;
                    }, delay);
                    var voteValue = button.getAttribute('data-vote');
                    sendVoteRequest(voteValue);
                    popup.style.display = 'none';
                }
            });
        });

        function sendVoteRequest(vote) {
            fetch('/serveurWeb_war_exploded/vote?target=' + vote, { method: 'POST' })
                .then(function(response) {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la requête vers la servlet.');
                    }
                    console.log('Vote envoyé avec succès');
                })
                .catch(function(error) {
                    console.error('Erreur:', error);
                });
        }
    });

    // SHOP
    document.addEventListener('DOMContentLoaded', function() {
        var shopButton = document.getElementById('shopButton');
        var popupShop = document.getElementById('popupShop');
        var isShopDisabled = false;

        shopButton.addEventListener('click', function() {
            popupShop.style.display = 'block';
        });

        var shopButtons = document.querySelectorAll('.shopButton');
        shopButtons.forEach(function(button) {
            button.addEventListener('click', function() {
                if (!isShopDisabled) {
                    isShopDisabled = true; // Désactivez les achats pour éviter les clics multiples
                    setTimeout(function() {
                        isShopDisabled = false; // Réactivez les achats après le délai
                    }, delay);

                    var item = button.getAttribute('data-item');
                    acheterItem(item);
                    popupShop.style.display = 'none';
                }
            });
        });

        function acheterItem(item) {
            fetch('/serveurWeb_war_exploded/shop?target=' + item, { method: 'POST' })
                .then(function(response) {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la requête vers la servlet.');
                    }
                })
                .catch(function(error) {
                    console.error('Erreur:', error);
                });
        }
    });

    // REPOUSSER / TIR
    document.addEventListener('DOMContentLoaded', function() {
        var actionButton = document.getElementById('actionButton');
        var isButtonDisabled = false;

        actionButton.addEventListener('click', function() {
            if (!isButtonDisabled) {
                isButtonDisabled = true; // Désactivez le bouton pour éviter les clics multiples
                setTimeout(function() {
                    isButtonDisabled = false; // Réactivez le bouton après le délai
                }, delay);
                fetch('/serveurWeb_war_exploded/tir?', { method: 'POST' });
            }
        });
    });


    // JOYSTICK
    document.addEventListener('DOMContentLoaded', function() {
        var container = document.getElementById('container');
        var joystick = document.getElementById('joystick');
        var containerRect = container.getBoundingClientRect();

        // Mettre à jour la position du joystick
        function updatePosJoystick(x, y) {
            joystick.style.left = x - joystick.offsetWidth / 2 + 'px';
            joystick.style.top = y - joystick.offsetHeight / 2 + 'px';
        }

        // Obtenir la position du joystick par rapport au conteneur
        function getPosJoystick(event) {
            var x = event.clientX - containerRect.left - joystick.offsetWidth / 2;
            var y = event.clientY - containerRect.top - joystick.offsetHeight / 2;

            // Calculer la distance entre le centre et la position actuelle
            var distance = Math.sqrt(Math.pow(x - (container.offsetWidth / 2), 2) + Math.pow(y - (container.offsetHeight / 2), 2));

            // Limiter la distance au rayon du cercle
            if (distance > (container.offsetWidth / 2)) {
                var ratio = (container.offsetWidth / 2) / distance;
                x = (x - (container.offsetWidth / 2)) * ratio + (container.offsetWidth / 2);
                y = (y - (container.offsetHeight / 2)) * ratio + (container.offsetHeight / 2);
            }

            return {x: x, y: y};
        }

        // Direction en fonction de la position initiale et de la position actuelle
        function calculDirection(initialPosition) {
            //Pos actuelle joystick
            var currentX = (joystick.offsetLeft-75)/100;
            var currentY = -((joystick.offsetTop-75)/100);

            //Calcul de la distance entre la nouvelle position et la dernière pos enregistrée
            var dx = currentX - lastX;
            var dy = currentY - lastY;

            nouvelleDistance = Math.sqrt(dx * dx + dy * dy);

            var dirX = (joystick.offsetLeft-75)/100;
            var dirY = -((joystick.offsetTop-75)/100);

            return dirX+";"+dirY;
        }

        // Événement pour la souris
        container.addEventListener('mousedown', function(event) {
            var initialPosition = getPosJoystick(event);

            // Quand le joystick est en mouvement
            function mouvement(event) {
                var newPosition = getPosJoystick(event);
                updatePosJoystick(newPosition.x, newPosition.y);
                var newDirection = calculDirection(initialPosition);
                var split = newDirection.split(";");
                var newX = parseFloat(split[0]);
                var newY = parseFloat(split[1]);

                if (nouvelleDistance > distanceSeuil){
                    lastX = newX;
                    lastY = newY;
                    sendRequest(newDirection);
                }
            }

            // Quand le joystick est à l'arrêt
            function arret() {
                document.removeEventListener('mousemove', mouvement);
                document.removeEventListener('mouseup', arret);

                // Stop lorsque l'utilisateur relâche le joystick
                lastX = 0;
                lastY = 0;
                sendRequest('0;0');
                updatePosJoystick(container.offsetWidth / 2, container.offsetHeight / 2);
            }

            document.addEventListener('mousemove', mouvement);
            document.addEventListener('mouseup', arret);
        });

        // Événements pour les écrans tactiles
        container.addEventListener('touchstart', function(event) {
            var initialTouch = event.touches[0];
            var initialPosition = getPosJoystick(initialTouch);

            // Quand le joystick est en mouvement
            function mouvement(event) {
                var touch = event.touches[0];
                var newPosition = getPosJoystick(touch);
                updatePosJoystick(newPosition.x, newPosition.y);
                var newDirection = calculDirection(initialPosition);
                var split = newDirection.split(";");
                var newX = parseFloat(split[0]);
                var newY = parseFloat(split[1]);

                if (nouvelleDistance > distanceSeuil){
                    lastX = newX;
                    lastY = newY;
                    sendRequest(newDirection);

                    // Si le bouton est enfoncé, envoyer également la demande de tir
                    if (isButtonDown) {
                        sendTirRequest();
                    }
                }
            }

            // Quand le joystick est à l'arrêt
            function arret() {
                document.removeEventListener('touchmove', mouvement);
                document.removeEventListener('touchend', arret);

                // Stop lorsque l'utilisateur relâche le joystick
                lastX = 0;
                lastY = 0;
                sendRequest('0;0');
                updatePosJoystick(container.offsetWidth / 2, container.offsetHeight / 2);
            }

            document.addEventListener('touchmove', mouvement);
            document.addEventListener('touchend', arret);
        });
        updatePosJoystick(container.offsetWidth / 2, container.offsetHeight / 2);
    });
</script>

</body>
</html>