<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ball Arena - Accueil</title>
    <style>
        /* Styles CSS pour rendre la page adaptée aux téléphones */
        body {
            overflow: hidden;
            font-family: 'Press Start 2P', 'sans-serif';
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #000;
            color: #fff;
        }
        .container {
            text-align: center;
        }
        h1 {
            font-size: 36px;
        }
        p {
            font-size: 20px;
            margin-top: 20px;
        }
        .btn-jeu {
            display: inline-block;
            background-color: #00ff00;
            color: #000;
            padding: 15px 30px;
            text-decoration: none;
            border-radius: 5px;
            font-size: 24px;
            margin-top: 40px;
            transition: background-color 0.3s;
        }
        .btn-jeu:hover {
            background-color: #0f0;
        }
        @media screen and (max-width: 600px) {
            h1 {
                font-size: 24px;
            }
            p {
                font-size: 16px;
            }
            .btn-jeu {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Capitalist Arena</h1>
    <p>Un jeu passionnant pour les amateurs de compétition et de capitalisme !</p>
    <a class="btn-jeu" href="hello-servlet">Jouer</a>
</div>
</body>
</html>


