#define BUTTON_PIN D1
#define LED_PIN D2

bool jogoIniciado = false;

void setup() {
  Serial.begin(115200);
  pinMode(BUTTON_PIN, INPUT_PULLUP);  // botão ligado ao GND
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);

  Serial.println("Pronto! Pressione o botão para iniciar o jogo de reação.");
}

void loop() {
  // Espera o botão ser pressionado (nível LOW)
  if (digitalRead(BUTTON_PIN) == LOW && !jogoIniciado) {
    delay(50); // debounce
    if (digitalRead(BUTTON_PIN) == LOW) {
      jogoIniciado = true;
      iniciarJogo();
    }
  }
}

void iniciarJogo() {
  Serial.println("Jogo de Reação iniciado!");
  delay(random(2000, 5000)); // espera aleatória

  digitalWrite(LED_PIN, HIGH);
  unsigned long start = millis();

  // Espera o jogador reagir
  while (digitalRead(BUTTON_PIN) == HIGH) {
    delay(1); // evita WDT reset
  }

  unsigned long tempoReacao = millis() - start;
  digitalWrite(LED_PIN, LOW);
  Serial.print("Tempo de reação: ");
  Serial.print(tempoReacao);
  Serial.println(" ms");

  delay(2000);
  jogoIniciado = false;
  Serial.println("Pressione o botão para jogar novamente.");
}
