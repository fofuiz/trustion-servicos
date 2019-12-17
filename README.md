# Table of Contents
- [Execução do projeto](#execucao)
- [Tecnologias](#tecnologias)
- [Configuração](#configuracao)

## Execução do projeto <a name="execucao"></a>
Execute os seguintes comandos no diretório do projeto `trustion-model`.

```
mvn clean install
```

Execute os seguintes comandos no diretório do projeto, `../trustion-servicos`.

- Execução

```
mvn clean install spring-boot:run -Dspring.profiles.active=develop -Dmaven.test.skip=true
```

- Teste

```
mvn test
```

### Observações
- Certifique-se de que o projeto trustion-model esteja atualizado e compilado.
- Caso falte alguma dependência, fale com um desenvolvedor.



# Tecnologias Utilizadas <a name="tecnologias"></a>
- JDK 1.8;
- Maven;



# Configuração Ambiente <a name="configuracao"></a>

## Linux
- Crie a pasta `/data/trustion` e dê permissão de escrita;
- Copie o arquivo `trustion-servicos/src/main/resources/config.properties` e cole na pasta criada no passo anterior.

## Windows
