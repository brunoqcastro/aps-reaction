#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include "LED.h"
#include "Button.h"

#define LEDG_PIN_VIN 4
#define LEDR_PIN_VIN 14
#define PUSHBUTTON_PIN_2 5

LED ledG(LEDG_PIN_VIN);
LED ledR(LEDR_PIN_VIN);
Button pushButton(PUSHBUTTON_PIN_2);

const char* ssid = "wifi";
const char* password = "senha";
const char* serverUrl = "http://192.168.0.40:5000";
WiFiClient client;

bool jogoAtivo = false;
unsigned long inicioTempo = 0;
int minDelay = 500;
int maxDelay = 5000;

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);

  Serial.print("Conectando ao WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConectado ao WiFi!");

  pushButton.init();

  Serial.println("=== Jogo de Reação ===");
  Serial.println("Digite '1' para jogar ou '2' para ver o Top 10.");
}

void loop() {
  if (Serial.available() > 0) {
    char comando = Serial.read();
    if (comando == '1' && !jogoAtivo) iniciarJogo();
    else if (comando == '2') listarTop10();
  }

  if (jogoAtivo && pushButton.read() == 0) {
    delay(50);
    if (pushButton.read() == 0) {
      unsigned long tempoReacao = millis() - inicioTempo;
      ledG.off();
      enviarReacao(tempoReacao);
      jogoAtivo = false;
      Serial.println("Digite '1' para jogar novamente.");
    }
  }
}

void iniciarJogo() {
  obterConfig();
  jogoAtivo = true;

  Serial.println("Prepare-se...");
  for (int i = 3; i > 0; i--) {
    Serial.println(i);
    ledR.on(); delay(300);
    ledR.off(); delay(300);
  }

  delay(random(minDelay, maxDelay));
  Serial.println("Aperte!!");
  ledG.on();
  inicioTempo = millis();
}

void enviarReacao(unsigned long tempo) {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(client, String(serverUrl) + "/reacao");
    http.addHeader("Content-Type", "application/json");

    String body = "{\"tempo_ms\": " + String(tempo) + "}";
    int code = http.POST(body);

    if (code > 0) {
      Serial.println("Tempo enviado com sucesso!");
    } else {
      Serial.println("Falha ao enviar tempo.");
    }
    http.end();
  }
}

void listarTop10() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(client, String(serverUrl) + "/reacoes");
    int code = http.GET();

    if (code == 200) {
      String payload = http.getString();
      Serial.println("Top 10 tempos:");
      Serial.println(payload);
    } else {
      Serial.println("Erro ao buscar top 10.");
    }
    http.end();
  }
}

void obterConfig() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(client, String(serverUrl) + "/config");
    int code = http.GET();

    if (code == 200) {
      String payload = http.getString();
      DynamicJsonDocument doc(256);
      deserializeJson(doc, payload);
      minDelay = doc["min_ms"];
      maxDelay = doc["max_ms"];
      Serial.printf("Tempo aleatório entre %d e %d ms\n", minDelay, maxDelay);
    } else {
      Serial.println("Usando configuração padrão (500–5000 ms).");
    }
    http.end();
  }
}