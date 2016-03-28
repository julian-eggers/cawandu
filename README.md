Cawandu
============

## !Under development!


## Docker
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
-e REGISTRY_USERNAME=test \
-e REGISTRY_EMAIL="test@test.de" \
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
-e HOST_URI=https://192.168.2.1:2376 \
-e HOST_CERTIFICATES="/home/ubuntu/.docker/machine/certs" \
-e REGISTRY_USERNAME=test \
-e REGISTRY_EMAIL="test@test.de" \
-e REGISTRY_PASSWORD="123456" \
jeggers/cawandu:latest
```