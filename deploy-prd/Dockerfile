FROM cas-j8

ENV TZ=America/Sao_Paulo

COPY target/brinks-portal-1.1.0-SNAPSHOT.jar /deployments/

#RUN echo "ipv6" >> /etc/modules
#RUN echo http://mirror.yandex.ru/mirrors/alpine/v3.5/main > /etc/apk/repositories; \
#    echo http://mirror.yandex.ru/mirrors/alpine/v3.5/community >> /etc/apk/repositories
#
#RUN apk update \
# && apk add --no-cache \
#
#RUN apk add tzdata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT java -DproxySet=true -Dhttps.proxySet=true -Dhttp.proxySet=true -Dhttps.proxyHost=192.168.130.240 -Dhttps.proxyPort=3128 -Dhttp.proxyHost=192.168.130.240 -Dhttp.proxyPort=3128 -Dhttp.nonProxyHosts="localhost|*.accesstage.com.br|*.accestage.net.br|192.168.44.10" -Xmx512m -Xms512m -Djava.security.egd=file:/dev/./urandom -jar /deployments/brinks-portal-1.1.0-SNAPSHOT.jar