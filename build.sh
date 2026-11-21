#!/bin/sh
# Assemble index.html (page autonome) à partir de src/game.html
cd "$(dirname "$0")"
{ printf '<!doctype html>\n<html lang="fr">\n<head>\n<meta charset="utf-8">\n<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">\n'; cat src/game.html; printf '\n</html>\n'; } > index.html
