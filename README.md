# Jeu de l'Oie 3D

Le jeu de l'oie traditionnel (63 cases) sur un plateau en spirale en 3D, jouable dans le navigateur.

- 2 à 4 joueurs, humains ou robots
- Dés lancés en 3D, pions qui sautent de case en case
- Caméra libre : glisser pour tourner, molette ou pincement pour zoomer
- Survol (ou toucher) d'une case pour lire sa règle
- Barre d'espace pour lancer les dés

## Jouer

Ouvrir `index.html` dans un navigateur (connexion Internet nécessaire pour charger three.js et les polices).

## Règles

- Oies (5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59) : on avance encore du même nombre.
- 6 Pont → case 12 · 19 Hôtel : 2 tours sans jouer · 31 Puits et 52 Prison : bloqué jusqu'à ce qu'un autre joueur arrive
- 42 Labyrinthe → case 30 · 58 La Mort → retour au départ
- Premier lancer : 6 + 3 → case 26, 5 + 4 → case 53
- Tomber sur une case occupée renvoie l'autre pion à la case d'où l'on vient.
- Il faut arriver pile sur la 63, sinon on recule du surplus.

## Développement

Le code source est dans `src/game.html`. `./build.sh` génère `index.html`.
