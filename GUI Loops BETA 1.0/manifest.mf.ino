int input;

void setup() {

 pinMode(13, OUTPUT); // Declaramos que utilizaremos el pin 13 como salida

 Serial.begin(9600);
}

void loop() {
 if (Serial.available() > 0) {

 input = Serial.read();

 if (input == '1') {

 digitalWrite(13, HIGH); //Si el valor de input es 1, se enciende el led

 }

 else

 {

 digitalWrite(13, LOW); //Si el valor de input es diferente de 1, se apaga el LED

 }

 }
}