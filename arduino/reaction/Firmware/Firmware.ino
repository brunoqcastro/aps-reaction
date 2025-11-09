#include "Arduino.h"
#include "LED.h"
#include "Button.h"

// --- PINOS (mantidos do seu código anterior) ---
#define LEDG_PIN_VIN 4
#define LEDR_PIN_VIN 14
#define PUSHBUTTON_PIN_2 5
#define SEVENSEGSINGLE_PIN_DP 15
#define SEVENSEGSINGLE_PIN_C 12
#define SEVENSEGSINGLE_PIN_D 13
#define SEVENSEGSINGLE_PIN_E 16
#define SEVENSEGSINGLE_PIN_B 2
#define SEVENSEGSINGLE_PIN_A 0
#define SEVENSEGSINGLE_PIN_F 9
#define SEVENSEGSINGLE_PIN_G 10

// --- OBJETOS ---
LED ledG(LEDG_PIN_VIN);
LED ledR(LEDR_PIN_VIN);
Button button(PUSHBUTTON_PIN_2);

// --- VARIÁVEIS ---
int SevenSegSinglePins[] = {
  SEVENSEGSINGLE_PIN_DP, SEVENSEGSINGLE_PIN_C, SEVENSEGSINGLE_PIN_D,
  SEVENSEGSINGLE_PIN_E, SEVENSEGSINGLE_PIN_B, SEVENSEGSINGLE_PIN_A,
  SEVENSEGSINGLE_PIN_F, SEVENSEGSINGLE_PIN_G
};

unsigned long reactionStart = 0;
bool gameRunning = false;
bool waitingForGreen = false;
bool burnedStart = false;
int lastScore = -1;

// Escala de notas (em milissegundos)
int scoreRanges[] = {400, 800, 1200, 1600, 2000, 2500, 3000, 4000, 5000};

// --- FUNÇÕES AUXILIARES ---

// Desliga todos os segmentos do display
void clearDisplay() {
  for (int i = 0; i < 8; i++) digitalWrite(SevenSegSinglePins[i], HIGH);
}

// Mostra um número (0-9) no display
void showDigit(int num) {
  clearDisplay();
  // Tabela simples de 7 segmentos (sem DP)
  const bool digits[10][7] = {
    {0,0,0,0,0,0,1},  // 0
    {1,0,0,1,1,1,1},  // 1
    {0,0,1,0,0,1,0},  // 2
    {0,0,0,0,1,1,0},  // 3
    {1,0,0,1,1,0,0},  // 4
    {0,1,0,0,1,0,0},  // 5
    {0,1,0,0,0,0,0},  // 6
    {0,0,0,1,1,1,1},  // 7
    {0,0,0,0,0,0,0},  // 8
    {0,0,0,0,1,0,0}   // 9
  };
  for (int i = 0; i < 7; i++) {
    digitalWrite(SevenSegSinglePins[i + 1], digits[num][i] ? LOW : HIGH);
  }
}

// Pisca o LED vermelho N vezes
void blinkRed(int times, int delayMs) {
  for (int i = 0; i < times; i++) {
    ledR.on();
    delay(delayMs);
    ledR.off();
    delay(delayMs);
  }
}

// Calcula a nota com base no tempo
int calculateScore(unsigned long reactionTime) {
  for (int i = 0; i < 9; i++) {
    if (reactionTime <= scoreRanges[i]) return 9 - i;
  }
  return 0;
}

// --- CONFIGURAÇÃO ---
void setup() {
  Serial.begin(115200);
  Serial.println("Jogo de Reação iniciado!");
  
  button.init();
  ledG.off();
  ledR.off();

  for (int i = 0; i < 8; i++) pinMode(SevenSegSinglePins[i], OUTPUT);
  clearDisplay();
}

// --- LOOP PRINCIPAL ---
void loop() {
  // Espera o botão para iniciar o jogo
  if (!gameRunning && button.onPress()) {
    Serial.println("Iniciando o jogo!");
    burnedStart = false;
    gameRunning = true;
    waitingForGreen = true;

    // Contagem regressiva com LED vermelho
    Serial.println("Contagem regressiva...");
    for (int i = 0; i < 3; i++) {
      ledR.on();
      delay(500);
      ledR.off();
      delay(500);
    }

    // Espera aleatória entre 1 e 4 segundos
    unsigned long waitTime = random(1000, 4000);
    unsigned long startWait = millis();
    Serial.println("Aguardando para acender o verde...");

    // Enquanto espera o momento aleatório
    while (millis() - startWait < waitTime) {
      if (button.onPress()) {
        Serial.println("⚠️ Queimou a largada!");
        burnedStart = true;
        blinkRed(10, 250);
        gameRunning = false;
        return;
      }
    }

    // Acende o verde e começa o tempo de reação
    ledG.on();
    reactionStart = millis();
    waitingForGreen = false;
    Serial.println("Agora! Aperte o botão!");

  }

  // Se o verde estiver aceso e o jogo em andamento
  if (gameRunning && !waitingForGreen && !burnedStart) {
    if (button.onPress()) {
      unsigned long reactionTime = millis() - reactionStart;
      ledG.off();
      gameRunning = false;

      int score = calculateScore(reactionTime);
      lastScore = score;
      showDigit(score);

      Serial.print("Tempo de reação: ");
      Serial.print(reactionTime);
      Serial.println(" ms");
      Serial.print("Nota: ");
      Serial.println(score);
      Serial.println("Aperte o botão para jogar novamente.");
    }
  }

  // Permite reiniciar o jogo depois de terminar
  if (!gameRunning && lastScore != -1 && button.onPress()) {
    clearDisplay();
    lastScore = -1;
    Serial.println("Novo jogo iniciado!");
  }
}
