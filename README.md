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

### Properties
| Environment variable  | Required | Default | Example |
| ------------- | ------------- | ------------- | ------------- |
| HOST_URI  | no  |  | https://192.168.0.55:2376 |
| HOST_CERTIFICATES  | no  |  | /home/avidesit/.docker/machine/certs |
| REGISTRY_USERNAME  | no  |  | avides |
| REGISTRY_EMAIL  | no  |  | info@avides.com  |
| REGISTRY_PASSWORD  | no  |  | 123456  |
| PULL_MODE | no | RUNNING | (ALL, RUNNING, NONE) |
