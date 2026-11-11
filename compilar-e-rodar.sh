#!/bin/bash
# Plano B: compila e roda o sistema pelo terminal, sem precisar do IntelliJ.
# Uso:  ./compilar-e-rodar.sh

cd "$(dirname "$0")" || exit 1

JAR="src/libs/mysql-connector-j-9.1.0.jar"
OUT="out/production/SistemaFaculdade"

echo "Compilando..."
mkdir -p "$OUT"
javac -encoding UTF-8 -cp "$JAR" -d "$OUT" $(find src -name '*.java') || {
    echo "Falha na compilacao."
    exit 1
}

echo "Iniciando o sistema..."
echo
java -cp "$OUT:$JAR" Main
