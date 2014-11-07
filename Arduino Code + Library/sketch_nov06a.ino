// Control PID Seguidor de Linea Carro de carreras Loops BETA V 1.0 Arduino + Java 
// Powered By Michael Vargas 2014
#include <QTRSensors.h>
#include "math.h"
//------------------------------------------------------------------------------------//
#define NUM_SENSORS             8            // Numero de sensores que usa
#define TIMEOUT               2500           // waits for 2500 us for sensor outputs to go low
#define EMITTER_PIN    QTR_NO_EMITTER_PIN    // desactivar LED_ON QTR_RC
const byte STBY =   A2; //standby // uso de pines analogos A0-A5 como digitales
//Motor A
const byte  PWMA =  11; //Speed control  
const byte  AIN1 =  A1; //Direction
const byte  AIN2 =  A0; //Direction
//Motor B
const byte  PWMB =  10; //Speed control
const byte  BIN1 =  A3; //Direction
const byte  BIN2 =  A4; //Direction
//LEDS RGB
const byte  AZUL =  13;
const byte  VERDE = A5;
const byte  ROJO =  12;
QTRSensorsRC qtrrc((unsigned char[]) {  2, 3, 4, 5, 6, 7, 8, 9} ,NUM_SENSORS, TIMEOUT, EMITTER_PIN); // Matriz QTR_RC pines en PCB ( D2-D9)
unsigned int sensorValues[NUM_SENSORS];
//VALORES PD
float VProporcional = 0.1;
int VDerivativo = 10;
//##############################
unsigned int ref_line=3500; // posicion linea negra de referencia
int maximum =255; // maxima velocidad 
int time_calibracion=300; // tiempo de calibracion
int x=1; // velocidad calibracion
int inputString; // Lectura de datos Java
const int timer = 250; // Tiempo de delays
char junk = ' ';   //Lectura de datos manteninedo el Buffer
void stop(){
//enable standby  
  digitalWrite(STBY, LOW); 
}
void CELESTE_ON(){
   digitalWrite(13, HIGH); 
  digitalWrite(A5, HIGH);   // turn the LED on (HIGH is the voltage level) 
}
void CELESTE_OFF(){
   digitalWrite(13, LOW); 
  digitalWrite(A5, LOW);   // turn the LED on (HIGH is the voltage level) 
}
void MORADO_ON(){
  digitalWrite(13, HIGH); 
  digitalWrite(12, HIGH); 
}
void MORADO_OFF(){
  digitalWrite(13, LOW); 
  digitalWrite(12, LOW); 
}

void LEDS_OFF(){
  digitalWrite(13, LOW); 
  digitalWrite(12, LOW);
  digitalWrite(A5, LOW);
  MORADO_OFF;
  CELESTE_OFF;
}
void QTR_calibration(){
   
//-------------Instrucciones para Empezar a hacer la Calibracion de Sensores--------------------------------------//
  x=1;
MORADO_ON();
  for (int i = 1; i < time_calibracion; i++)  // make the calibration take about 6.25 seconds
  {
    if (( i >= 1) && ( i<=127 )){
    setSpeed(i,-i);
    }
    else{
      if (( x >= 1) && ( x<=64 )){
      setSpeed(-x,x);
      x++;
      }
    }
    qtrrc.calibrate();       // reads all sensors 10 times at 2500 us per read (i.e. ~25 ms per call)
    if ((i == 127) || (x == 64)){
stop();
      } 
  }
 
 MORADO_OFF();
  //---------------------------Fin Calibracion de Sensores----------------------------------------------------//
  for (int i=0; i <=6; i++){  // parpadeo para indicar que termino la calibracion
       CELESTE_ON();
       delay(timer);
       CELESTE_OFF();
       delay(timer);
  } 
}
void RESET_ALL(){
 stop();// Motores detenidos  
 LEDS_OFF();
 Serial.flush(); 
}
//--------------------------------------------------------------------------------------//
void setup()
{
  Serial.begin(9600);
 
  pinMode(STBY, OUTPUT);

  pinMode(PWMA, OUTPUT);
  pinMode(AIN1, OUTPUT);
  pinMode(AIN2, OUTPUT);

  pinMode(PWMB, OUTPUT);
  pinMode(BIN1, OUTPUT);
  pinMode(BIN2, OUTPUT);
  
  pinMode(AZUL,OUTPUT);
  pinMode(VERDE,OUTPUT);
  pinMode(ROJO,OUTPUT);
 RESET_ALL();
};

unsigned int last_proportional = 0;
long integral = 0;
void loop()
{
menu_Arduino_Java();
};
//<------------------------------------------------------------------------>
// ################# FUNCIONES ###########################################
void menu_Arduino_Java(){
  boolean sensores=false,motores=false;
  if(Serial.available() > 0){ 
       do{
           inputString = Serial.read();
           switch (inputString){
              case '1':
                digitalWrite(ROJO,HIGH);
                   break;
              case '2':
               digitalWrite(VERDE,HIGH);
                   break;
              case '3':
               digitalWrite(AZUL,HIGH);
                   break;
              case '4':
                  MORADO_ON();
                   break;
              case '5':
                 CELESTE_ON();
                   break;
              case '6':
                  LEDS_OFF();             //apagar leds
                   break;
              case '7':
                     QTR_calibration();  // Iniciar Calibracion
                     stop();
                   break;  
              case '8':
                  sensores = true;              // Lectura de sensores para graficar
                  while ( sensores == true ){
                   read_IR();
                   inputString=Serial.read();
                   if( inputString >= '0' ){
                     sensores=false;
                  }
                  }
                  break;
              case '9':                // MOTORES ADELANTE
                    motores = true;
                    while(motores==true){
                    setSpeed(255,255); 
                    delay(100);
                    inputString= Serial.read();
                    if( inputString >= '0' ){
                      motores=false;
                      stop();
                  }
                   }
                    break;
        
              case 'a':
                    motores = true;
                    while(motores==true){
                     setSpeed(-255,-255); 
                     delay(100);
                     inputString= Serial.read();
                     if( inputString >= '0' ){
                      motores=false;
                      stop();
                     }
                   }
                   break;
             case 'b':
                     while (Serial.available() == 0) ;  // Wait here until input buffer has a character
                      {
                        VProporcional = Serial.parseFloat();      
                        while (Serial.available() > 0)  // .parseFloat() can leave non-numeric characters
                              { junk = Serial.read() ; }      // clear the keyboard buffer
                           }
                  break;
              case 'c':
                     while (Serial.available() == 0) ;  // Wait here until input buffer has a character
                      {
                        VDerivativo = Serial.parseInt();      
                        while (Serial.available() > 0)  // .parseFloat() can leave non-numeric characters
                              { junk = Serial.read() ; }      // clear the keyboard buffer
                           }
                  break;
             
             case 'd':
                      while (Serial.available() == 0) ;  // Wait here until input buffer has a character
                      {
                        maximum= Serial.parseInt();      
                        while (Serial.available() > 0)  
                              { junk = Serial.read() ; }   
                           }
                  break; 
                      break;
             case 'e':
                      RESET_ALL();            // REINICAR PROGRAMA
                      software_Reset();
                      break;
             case 'f': 
                     QTR_calibration();       // MODO COMPETENCIA
                     while(true){
                      Line_follower_PID();
                     }
                   break;
               }; // FIN SWITCH MENU
           }while( inputString == 0 );
         
      }      
}
 void software_Reset() // Restarts program from beginning but does not reset the peripherals and registers
{
asm volatile ("  jmp 0");  
}  
void read_IR() {
 unsigned int position = qtrrc.readLine(sensorValues); // leemos posicion de la linea en la variable position
 Serial.println(position); 
 delay(timer);
}

void Line_follower_PID(){
unsigned int position = qtrrc.readLine(sensorValues); // leemos posicion de la linea en la variable position
  int proportional = (int)position - ref_line;  // Referencia donde seguira la linea, mitad sensores.
  // Calculos PD
  int derivative = proportional - last_proportional;
  integral += proportional;
  last_proportional = proportional;
  
  int power_difference = proportional/VProporcional + integral*0 + derivative*VDerivativo;
  
  if (power_difference > maximum)
    power_difference = maximum;
  if (power_difference < -maximum)
    power_difference = -maximum;
  
    if (power_difference < 0)
    setSpeed(maximum, maximum + power_difference);
  else
    setSpeed(maximum - power_difference,maximum); 
}
void setSpeed(int velocidad_1,int velocidad_2){

if ((velocidad_1 < 0) || (velocidad_2 < 0)){
  if ((velocidad_1 < 0) && (velocidad_2 <0)){
     velocidad_1= abs(velocidad_1); //Motor A atras
     velocidad_2= abs(velocidad_2); //Motor B atras
      move(1,velocidad_1, 0); 
      move(2,velocidad_2, 0);
  }
    if (velocidad_1 < 0){
      velocidad_1= abs(velocidad_1); //Motor A atras
      move(1,velocidad_1, 0); 
      move(2,velocidad_2, 1); 
    }
    if (velocidad_2 < 0){
      velocidad_2= abs(velocidad_2);//Motor B atras
      move(1,velocidad_1, 1); 
      move(2,velocidad_2, 0); 
    } 
  }
  else{
     move(1, velocidad_1, 1); 
     move(2,velocidad_2, 1); 
  }
}
void move(int motor, int speed, int direction){
  digitalWrite(STBY, HIGH); 

  boolean inPin1 = LOW;
  boolean inPin2 = HIGH;

  if(direction == 1){
    inPin1 = HIGH;
    inPin2 = LOW;
  }

  if(motor == 1){
    digitalWrite(AIN1, inPin1);
    digitalWrite(AIN2, inPin2);
    analogWrite(PWMA, speed);
  }else{
    digitalWrite(BIN1, inPin1);
    digitalWrite(BIN2, inPin2);
    analogWrite(PWMB, speed);
  }
}


