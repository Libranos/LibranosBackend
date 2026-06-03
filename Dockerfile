# ──────────────────────────────────────────────────────────────────────────────
# Stage 1 — Build
#   Usa a imagem oficial Maven + JDK 17 (Alpine) para montar o fat-JAR.
#   O truque de copiar o pom.xml antes do src garante que o layer de
#   dependências só seja invalidado quando o pom.xml mudar, não a cada
#   alteração no código-fonte.
# ──────────────────────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# 1) Baixa todas as dependências declaradas (layer cacheável)
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# 2) Copia o código e empacota, pulando testes (executados em CI, não aqui)
COPY src ./src
RUN mvn package -DskipTests -B --no-transfer-progress

# ──────────────────────────────────────────────────────────────────────────────
# Stage 2 — Runtime
#   eclipse-temurin:17-jre-alpine é ~100 MB vs ~450 MB do JDK completo.
#   Cria usuário não-privilegiado; a aplicação nunca roda como root.
# ──────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Usuário de sistema sem shell de login e sem home directory
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copia apenas o fat-JAR produzido no stage anterior
COPY --from=builder /build/target/Libranos-0.0.1-SNAPSHOT.jar app.jar

# Garante que o dono do artefato é o usuário não-privilegiado
RUN chown spring:spring app.jar

USER spring

# Porta definida em application.properties (server.port=8005)
EXPOSE 8005

# Exec form: permite passar flags de JVM via JAVA_TOOL_OPTIONS sem sobrescrever
# o entrypoint. Exemplo:
#   docker run -e JAVA_TOOL_OPTIONS="-Xmx512m -Xms256m" libranos-app
#
# -Djava.security.egd acelera a geração de tokens em Alpine (pouca entropia)
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
