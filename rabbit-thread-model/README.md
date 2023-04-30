# rabbitmq-code-repo
Rabbitmq realated examples repo

Before starting run rabbitmq

docker run -d --name myrabbit -it --rm  -p 5672:5672 -p 15672:15672 rabbitmq:3-management
ya da
docker run -d --name myrabbit -it --rm  -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine

Her ikisi de luan için 3.11.14'ü indiriyor. 12c beta release
http://localhost:15672/ üzerinden guest/guest bilgileriyle rabbitmq web admin console açılabilir