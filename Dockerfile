# Estágio 1: Build da aplicação com Maven e JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de configuração do Maven para aproveitar o cache de dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Baixa as dependências do projeto
RUN ./mvnw dependency:go-offline

# Copia o código fonte da aplicação
COPY src ./src

# --- A CORREÇÃO CRÍTICA ESTÁ AQUI ---
# Define a variável de ambiente MAVEN_OPTS para forçar a codificação UTF-8
# para a JVM que executa o Maven.
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"

# Compila o projeto e gera o arquivo .jar, pulando os testes
RUN ./mvnw package -DskipTests

# Estágio 2: Criação da imagem final de execução
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o .jar gerado no estágio anterior para a imagem final
COPY --from=build /app/target/serramineira-sistemadeleite-0.0.1-SNAPSHOT.jar /app/app.jar

# Expõe a porta em que a aplicação Spring Boot roda
EXPOSE 8080

# Comando para iniciar a aplicação quando o container for executado
CMD ["java", "-jar", "app.jar"]