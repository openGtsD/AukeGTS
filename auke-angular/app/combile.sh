#!/bin/bash

if [ "$1" == "f" ]; then
  uglifyjs ./scripts/app.js ./scripts/bootstrap/routes.js ./scripts/components/**/*.js ./scripts/ui/**/*.js  -o ./core.min.js
fi

if [ "$1" == "m" ]; then
  uglifyjs ./scripts/app.js ./scripts/bootstrap/map.js ./scripts/components/**/*.js ./scripts/ui/**/*.js  -o ./m.min.js
fi

if [ "$1" == "b" ]; then
  uglifyjs ./scripts/app.js ./scripts/bootstrap/tracker.js ./scripts/components/**/*.js ./scripts/ui/**/*.js  -o ./b.min.js
fi