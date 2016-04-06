Cawandu
============

[![Codacy Badge](https://api.codacy.com/project/badge/grade/289236a7e7b046eb89a6a0ae63863a28)](https://www.codacy.com/app/eggers-julian/cawandu)
[![Coverage Status](https://coveralls.io/repos/julian-eggers/cawandu/badge.svg?branch=master&service=github)](https://coveralls.io/github/julian-eggers/cawandu?branch=master)
[![Build Status](https://travis-ci.org/julian-eggers/cawandu.svg?branch=master)](https://travis-ci.org/julian-eggers/cawandu)


## Features
### Container
- Search container by id, name, state and image
- Start, stop, restart, remove and kill containers
- Recreate containers (remove old and start new container)
- Update container (Pull current image and start new container)
- Switch image-tag (Select tag from registry, pull and start new container)
- Rename container

### Images
- Search images by id and name
- Automatically pull updates of used images
- Pull and delete images
- Search images by id and name


# Stack
[Spring](https://spring.io/)
[Zkoss](http://www.zkoss.org/)
[Docker-Client](https://github.com/spotify/docker-client)


# Docker
[Dockerhub](https://hub.docker.com/r/jeggers/cawandu/)

### Local
```
docker run \
-d \
--name=cawandu \
--restart=always \
-p 7777:7777 \
-v /var/log/apps:/var/log/apps \
-v /var/run/docker.sock:/var/run/docker.sock \
-e REGISTRY_USERNAME=jeggers \
-e REGISTRY_EMAIL="eggers.julian@gmail.com" \
-e REGISTRY_PASSWORD="123456" \
jeggers/cawandu:latest
```

### Remote
```
docker run \
-d \
--name=cawandu \
--restart=always \
-p 7777:7777 \
-v /var/log/apps:/var/log/apps \
-v /home/ubuntu/.docker/machine/certs/:/home/ubuntu/.docker/machine/certs/ \
-e HOST_URI=https://192.168.0.55:2376 \
-e HOST_CERTIFICATES="/home/ubuntu/.docker/machine/certs" \
-e REGISTRY_USERNAME=jeggers \
-e REGISTRY_EMAIL="eggers.julian@gmail.com" \
-e REGISTRY_PASSWORD="123456" \
jeggers/cawandu:latest
```

### Properties
| Environment variable  | Required | Default | Example |
| ------------- | ------------- | ------------- | ------------- |
| HOST_URI  | no  |  | https://192.168.0.55:2376 |
| HOST_CERTIFICATES  | no  |  | /home/ubuntu/.docker/machine/certs |
| REGISTRY_USERNAME  | no  |  | jeggers |
| REGISTRY_EMAIL  | no  |  | eggers.julian@gmail.com  |
| REGISTRY_PASSWORD  | no  |  | 123456  |
| PULL_MODE | no | RUNNING | (ALL, RUNNING, NONE) |
