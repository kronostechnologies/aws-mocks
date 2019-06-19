#!/usr/bin/env bash

/usr/local/bin/docker-entrypoint.sh &

./run.sh &

touch ./awsmocks.log
tail -qf ./awsmocks.log
