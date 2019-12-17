FROM registry-prd1.accesstage.com.br/java-generic-oraclelinux

LABEL maintainer="Murilo Feltrin <murilo.pontes@accesstage.com.br>"

COPY target/trustion-servicos-1.0-SNAPSHOT.jar /deployments/

CMD java $JVM_CONFIG_DEFAULT $JVM_MEMORY_SIZE $JVM_CONFIG $JVM_GARBAGE_CONF $JVM_RMI $JAVA_SECURITY $JAVA_PROXY_ACCESSTAGE -jar /deployments/trustion-servicos-1.0-SNAPSHOT.jar
