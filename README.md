# Jeu de l'Oie 3D

Le jeu de l'oie traditionnel (63 cases) sur un plateau en spirale en 3D, jouable dans le navigateur.

- 2 à 4 joueurs, humains ou robots
- Dés lancés en 3D, pions qui sautent de case en case
- Caméra libre : glisser pour tourner, molette ou pincement pour zoomer
- Survol (ou toucher) d'une case pour lire sa règle
- Barre d'espace pour lancer les dés
- Résolution adaptative jusqu'à la 4K (bouton « Qualité » : auto, 720p, 1080p, 1440p, 4K)
- Animations travaillées : sauts avec écrasement et rebond, dés qui rebondissent, oies qui s'envolent, confettis, secousses de caméra
- Caméra qui suit et se rapproche du pion pendant ses déplacements
- Sons synthétisés (dés, sauts, oie, fanfare) et réglage de vitesse (lente, normale, rapide)

## Mode compétitif (tout en local)

- **Cartes pouvoirs** : les cases violettes font piocher une carte (3 au maximum). Une carte par tour, avant de lancer : ⚡ Turbo (+3 cases), 🛡️ Bouclier (contre les pièges et les délogements), 🎲 3 dés (on garde les 2 meilleurs), 🔄 Échange (avec le leader), 🧊 Gel (le leader passe son tour). Chaque joueur commence avec une carte.
- **Robots** Facile, Normal ou Expert : les robots Expert jouent leurs cartes au meilleur moment.
- **Trophées et rangs** (Bronze → Légende) : une victoire en rapporte (davantage contre des Expert et en série), une défaite en coûte.
- **Niveaux, pièces et boutique** : l'expérience et les pièces gagnées à chaque partie débloquent des chapeaux pour son pion.
- **Défis du jour** (3 par jour) et **bonus de connexion** qui grandit avec la série de jours.
- Le profil est celui du premier joueur humain ; il est enregistré sur l'appareil, sans compte ni connexion.

## Jouer

Ouvrir `index.html` dans un navigateur (connexion Internet nécessaire pour charger three.js et les polices).

## Règles

- Oies (5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59) : on avance encore du même nombre.
- 6 Pont → case 12 · 19 Hôtel : 2 tours sans jouer
- 31 Puits et 52 Prison : on lance quand même les dés, un double fait sortir et avancer ; un autre joueur qui arrive vous délivre ; sinon on sort d'office (après 1 essai en partie rapide, 2 en classique)
- Partie rapide (par défaut) : hôtel 1 tour, la Mort fait reculer à la case 38, et il suffit d'atteindre ou dépasser la 63. Partie classique : règles traditionnelles
- 42 Labyrinthe → case 30 · 58 La Mort → retour au départ
- Premier lancer : 6 + 3 → case 26, 5 + 4 → case 53
- Tomber sur une case occupée renvoie l'autre pion à la case d'où l'on vient.
- Il faut arriver pile sur la 63, sinon on recule du surplus.

## Développement

Le code source est dans `src/game.html`. `./build.sh` génère `index.html`.

## Application Android TV (TCL, Google TV…)

Le dossier `android/` contient une application Android TV qui embarque le jeu hors ligne (three.js et polices inclus) dans une WebView plein écran.

- Télécommande : flèches pour se déplacer entre les boutons, **OK** pour lancer les dés ou valider, **Retour** pour ouvrir le menu (un second **Retour** depuis le menu quitte l'application).
- Résolution : en mode « auto », le rendu suit la définition réelle du téléviseur (jusqu'à 3840 × 2160) et baisse si l'image saccade. L'application demande le mode d'affichage 4K quand le téléviseur le propose.
- L'APK est construit automatiquement par GitHub Actions (workflow « Application Android TV ») à chaque modification, et publié dans la release `android-tv` sous le nom `jeu-de-l-oie-tv.apk`.

### Installer sur un téléviseur TCL

1. Sur le téléviseur, autoriser les applications de sources inconnues : Paramètres → Applications → Sécurité et restrictions → Sources inconnues. Sur Google TV, il faut d'abord activer les options développeur (Paramètres → Système → À propos → appuyer 7 fois sur « Build de l'OS Android TV »).
2. Installer l'application **Downloader** (Play Store du téléviseur), autoriser Downloader à installer des applications inconnues, y saisir l'adresse de téléchargement de `jeu-de-l-oie-tv.apk` (page Releases du dépôt), puis installer.
   Autre méthode : copier l'APK sur une clé USB et l'ouvrir avec le gestionnaire de fichiers du téléviseur.
3. Le jeu apparaît dans la liste des applications avec sa bannière.

### Signature et mises à jour

Sans configuration, chaque construction est signée avec une clé de débogage différente : pour installer une nouvelle version, il faut d'abord désinstaller l'ancienne.
Pour des mises à jour directes, créer une clé (`keytool -genkeypair -keystore release.jks -alias jeudeloie -keyalg RSA -validity 10000`) et l'ajouter dans les secrets du dépôt (Settings → Secrets and variables → Actions) : `ANDROID_KEYSTORE_BASE64` (contenu de `release.jks` en base64), `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.

### Construire soi-même

```sh
./build.sh                                   # génère android/app/src/main/assets/www
gradle -p android assembleRelease            # nécessite le SDK Android (compileSdk 34)
```
