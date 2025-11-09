#include "Arduino.h"
#include "LED.h"
#include "Button.h"

#define LEDG_PIN_VIN	4
#define LEDR_PIN_VIN	14
#define PUSHBUTTON_PIN_2	5

LED ledG(LEDG_PIN_VIN);
LED ledR(LEDR_PIN_VIN);
Button pushButton(PUSHBUTTON_PIN_2);

bool jogoAtivo = false;
bool aguardandoReacao = false;
unsigned long inicioTempo = 0;

void setup() {
  Serial.begin(115200);

  while (!Serial) ; // wait for serial port to connect. Needed for native USB
  Serial.println("start");

  pushButton.init();

  Serial.println("=== Jogo de Reação ===");
  Serial.println("Digite '1' no Serial Monitor para começar o jogo.");
}

void loop() {
  // Lê entrada serial
  if (Serial.available() > 0) {
    char comando = Serial.read();
    if (comando == '1' && !jogoAtivo) {
      iniciarJogo();
    }
  }

  bool pushButtonVal = pushButton.read();

  // Se o jogador apertar o botão
  if (jogoAtivo && pushButtonVal == 0) {
    delay(50); // debounce

    if (pushButtonVal == 0) {
        // Reação correta
        unsigned long tempoReacao = millis() - inicioTempo;
        ledG.off();
        Serial.print("Tempo de reação: ");
        Serial.print(tempoReacao);
        Serial.println(" ms ⏱️");
        jogoAtivo = false;
        aguardandoReacao = false;
        Serial.println("Digite '1' para jogar novamente.");
    }
  }
}

void iniciarJogo() {
  jogoAtivo = true;

  Serial.println("Prepare-se...");
  for (int i = 3; i > 0; i--){
    Serial.println(i);
    delay(500);
    ledR.on();
    delay(500);
    ledR.off();
  }
  
  
  delay(random(500, 5000)); // tempo aleatório

  // Acende o LED verde
  Serial.println("Aperte!!");
  ledG.on();
  inicioTempo = millis();
}
