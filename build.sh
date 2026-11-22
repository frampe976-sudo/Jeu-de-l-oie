#!/bin/sh
# 1) index.html : page autonome pour le web (three.js et polices depuis les CDN)
# 2) android/app/src/main/assets/www : la même page, hors ligne, pour l'application Android TV
set -e
cd "$(dirname "$0")"
head='<!doctype html>\n<html lang="fr">\n<head>\n<meta charset="utf-8">\n<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">\n'
{ printf "$head"; cat src/game.html; printf '\n</html>\n'; } > index.html

www=android/app/src/main/assets/www
rm -rf "$www" && mkdir -p "$www/vendor"
cp -r android/web-vendor/. "$www/vendor/"
{ printf "$head"; sed \
    -e 's#<link rel="preconnect" href="https://fonts.googleapis.com">##' \
    -e 's#<link rel="stylesheet" href="https://fonts.googleapis.com/[^"]*">#<link rel="stylesheet" href="vendor/fonts.css">#' \
    -e 's#https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js#vendor/three.min.js#' \
    -e 's#https://cdn.jsdelivr.net/npm/three@0.128.0/examples/js/controls/OrbitControls.js#vendor/OrbitControls.js#' \
    src/game.html; printf '\n</html>\n'; } > "$www/index.html"
if grep -q 'https://' "$www/index.html"; then echo "Ressource distante restante dans $www/index.html" >&2; grep -n 'https://' "$www/index.html" >&2; exit 1; fi
