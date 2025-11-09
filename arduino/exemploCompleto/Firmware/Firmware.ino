
// Include Libraries
#include "Arduino.h"
#include "LED.h"
#include "Button.h"


// Pin Definitions
#define LEDG_PIN_VIN	4
#define LEDR_PIN_VIN	14
#define PUSHBUTTON_PIN_2	5
#define SEVENSEGSINGLE_PIN_DP	15
#define SEVENSEGSINGLE_PIN_C	12
#define SEVENSEGSINGLE_PIN_D	13
#define SEVENSEGSINGLE_PIN_E	16
#define SEVENSEGSINGLE_PIN_B	2
#define SEVENSEGSINGLE_PIN_A	0
#define SEVENSEGSINGLE_PIN_F	9
#define SEVENSEGSINGLE_PIN_G	10



// Global variables and defines
//define an array for all the segments
int SevenSegSinglePins[] = { SEVENSEGSINGLE_PIN_DP, SEVENSEGSINGLE_PIN_C, SEVENSEGSINGLE_PIN_D, SEVENSEGSINGLE_PIN_E, SEVENSEGSINGLE_PIN_B, SEVENSEGSINGLE_PIN_A, SEVENSEGSINGLE_PIN_F, SEVENSEGSINGLE_PIN_G };
// object initialization
LED ledG(LEDG_PIN_VIN);
LED ledR(LEDR_PIN_VIN);
Button pushButton(PUSHBUTTON_PIN_2);


// define vars for testing menu
const int timeout = 10000;       //define timeout of 10 sec
char menuOption = 0;
long time0;

// Setup the essentials for your circuit to work. It runs first every time your circuit is powered with electricity.
void setup() 
{
    // Setup Serial which is useful for debugging
    // Use the Serial Monitor to view printed messages
    Serial.begin(9600);
    while (!Serial) ; // wait for serial port to connect. Needed for native USB
    Serial.println("start");
    
    pushButton.init();
    pinMode(SEVENSEGSINGLE_PIN_DP, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_C, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_D, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_E, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_B, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_A, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_F, OUTPUT);
    pinMode(SEVENSEGSINGLE_PIN_G, OUTPUT);
    //turn off all segments:
    for (int i = 0; i < 8; i++) { 
    digitalWrite(SevenSegSinglePins[i],HIGH);
    }
    menuOption = menu();
    
}

// Main logic of your circuit. It defines the interaction between the components you selected. After setup, it runs over and over again, in an eternal loop.
void loop() 
{
    
    
    if(menuOption == '1') {
    // Basic Green LED 5mm - Test Code
    // The LED will turn on and fade till it is off
    for(int i=255 ; i> 0 ; i -= 5)
    {
    ledG.dim(i);                      // 1. Dim Led 
    delay(15);                               // 2. waits 5 milliseconds (0.5 sec). Change the value in the brackets (500) for a longer or shorter delay in milliseconds.
    }                                          
    ledG.off();                        // 3. turns off
    }
    else if(menuOption == '2') {
    // Basic Red LED 5mm - Test Code
    // The LED will turn on and fade till it is off
    for(int i=255 ; i> 0 ; i -= 5)
    {
    ledR.dim(i);                      // 1. Dim Led 
    delay(15);                               // 2. waits 5 milliseconds (0.5 sec). Change the value in the brackets (500) for a longer or shorter delay in milliseconds.
    }                                          
    ledR.off();                        // 3. turns off
    }
    else if(menuOption == '3') {
    // Mini Pushbutton Switch - Test Code
    //Read pushbutton state. 
    //if button is pressed function will return HIGH (1). if not function will return LOW (0). 
    //for debounce funtionality try also pushButton.onPress(), .onRelease() and .onChange().
    //if debounce is not working properly try changing 'debounceDelay' variable in Button.h
    bool pushButtonVal = pushButton.read();
    Serial.print(F("pushButtonVal: ")); Serial.println(pushButtonVal);

    }
    else if(menuOption == '4') {
    // 7 - Segment Display (Single Digit) - Test Code
    //This loop will turn on and off each segment in the array for 0.5 sec
    for (int i = 0; i < 8; i++) { 
    digitalWrite(SevenSegSinglePins[i],LOW);
    delay(500);
    }
    for (int i = 0; i < 8; i++) { 
    digitalWrite(SevenSegSinglePins[i],HIGH);
    }

    }
    
    if (millis() - time0 > timeout)
    {
        menuOption = menu();
    }
    
}



// Menu function for selecting the components to be tested
// Follow serial monitor for instrcutions
char menu()
{

    Serial.println(F("\nWhich component would you like to test?"));
    Serial.println(F("(1) Basic Green LED 5mm"));
    Serial.println(F("(2) Basic Red LED 5mm"));
    Serial.println(F("(3) Mini Pushbutton Switch"));
    Serial.println(F("(4) 7 - Segment Display (Single Digit)"));
    Serial.println(F("(menu) send anything else or press on board reset button\n"));
    while (!Serial.available());

    // Read data from serial monitor if received
    while (Serial.available()) 
    {
        char c = Serial.read();
        if (isAlphaNumeric(c)) 
        {   
            
            if(c == '1') 
    			Serial.println(F("Now Testing Basic Green LED 5mm"));
    		else if(c == '2') 
    			Serial.println(F("Now Testing Basic Red LED 5mm"));
    		else if(c == '3') 
    			Serial.println(F("Now Testing Mini Pushbutton Switch"));
    		else if(c == '4') 
    			Serial.println(F("Now Testing 7 - Segment Display (Single Digit)"));
            else
            {
                Serial.println(F("illegal input!"));
                return 0;
            }
            time0 = millis();
            return c;
        }
    }
    return 0;
}
