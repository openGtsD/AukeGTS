#!/bin/bash

if [ "$1" == "f" ]; then
  uglifyjs ./app/scripts/app.js ./app/scripts/bootstrap/routes.js ./app/scripts/components/**/*.js ./app/scripts/ui/**/*.js  -o ./app/core.min.js
fi

if [ "$1" == "m" ]; then
  uglifyjs ./app/scripts/app.js ./app/scripts/bootstrap/map.js ./app/scripts/components/**/*.js ./app/scripts/ui/**/*.js  -o ./app/m.min.js
fi

if [ "$1" == "b" ]; then
  uglifyjs ./app/scripts/app.js ./app/scripts/bootstrap/tracker.js ./app/scripts/components/**/*.js ./app/scripts/ui/**/*.js  -o ./app/b.min.js
fi