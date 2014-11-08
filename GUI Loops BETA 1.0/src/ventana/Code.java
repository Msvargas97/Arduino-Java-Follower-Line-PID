/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventana;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.text.Caret;


/**
 *
 * @author Familia Vargas
 */
public class Code extends javax.swing.JFrame {

    /**
     * Creates new form Code
     */
    public Code() {
        initComponents();
        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setBounds((ancho / 2) - (this.getWidth() / 2), (alto / 2) - (this.getHeight() / 2), 560, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Código Fuente Arduino: ");
        setLocationRelativeTo(null);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        codeino = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        button = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        codeino.setColumns(20);
        codeino.setRows(5);
        codeino.setText("// Control PID Seguidor de Linea Carro de carreras Loops BETA V 1.0 Arduino + Java \n// Powered By Michael Vargas 2014\n//creditos: www.panamahitek.com\n#include <QTRSensors.h>\n#include \"math.h\"\n//------------------------------------------------------------------------------------//\n#define NUM_SENSORS             8            // Numero de sensores que usa\n#define TIMEOUT               2500           // waits for 2500 us for sensor outputs to go low\n#define EMITTER_PIN    QTR_NO_EMITTER_PIN    // desactivar LED_ON QTR_RC\nconst byte STBY =   A2; //standby // uso de pines analogos A0-A5 como digitales\n//Motor A\nconst byte  PWMA =  11; //Speed control  \nconst byte  AIN1 =  A1; //Direction\nconst byte  AIN2 =  A0; //Direction\n//Motor B\nconst byte  PWMB =  10; //Speed control\nconst byte  BIN1 =  A3; //Direction\nconst byte  BIN2 =  A4; //Direction\n//LEDS RGB\nconst byte  AZUL =  13;\nconst byte  VERDE = A5;\nconst byte  ROJO =  12;\nQTRSensorsRC qtrrc((unsigned char[]) {  2, 3, 4, 5, 6, 7, 8, 9} ,NUM_SENSORS, TIMEOUT, EMITTER_PIN); // Matriz QTR_RC pines en PCB ( D2-D9)\nunsigned int sensorValues[NUM_SENSORS];\n//VALORES PD\nfloat VProporcional = 0.1;\nint VDerivativo = 10;\n//##############################\nunsigned int ref_line=3500; // posicion linea negra de referencia\nint maximum =255; // maxima velocidad \nint time_calibracion=300; // tiempo de calibracion\nint x=1; // velocidad calibracion\nint inputString; // Lectura de datos Java\nconst int timer = 250; // Tiempo de delays\nchar junk = ' ';   //Lectura de datos manteninedo el Buffer\nvoid stop(){\n//enable standby  \n  digitalWrite(STBY, LOW); \n}\nvoid CELESTE_ON(){\n   digitalWrite(13, HIGH); \n  digitalWrite(A5, HIGH);   // turn the LED on (HIGH is the voltage level) \n}\nvoid CELESTE_OFF(){\n   digitalWrite(13, LOW); \n  digitalWrite(A5, LOW);   // turn the LED on (HIGH is the voltage level) \n}\nvoid MORADO_ON(){\n  digitalWrite(13, HIGH); \n  digitalWrite(12, HIGH); \n}\nvoid MORADO_OFF(){\n  digitalWrite(13, LOW); \n  digitalWrite(12, LOW); \n}\n\nvoid LEDS_OFF(){\n  digitalWrite(13, LOW); \n  digitalWrite(12, LOW);\n  digitalWrite(A5, LOW);\n  MORADO_OFF;\n  CELESTE_OFF;\n}\nvoid QTR_calibration(){\n   \n//-------------Instrucciones para Empezar a hacer la Calibracion de Sensores--------------------------------------//\n  x=1;\nMORADO_ON();\n  for (int i = 1; i < time_calibracion; i++)  // make the calibration take about 6.25 seconds\n  {\n    if (( i >= 1) && ( i<=127 )){\n    setSpeed(i,-i);\n    }\n    else{\n      if (( x >= 1) && ( x<=64 )){\n      setSpeed(-x,x);\n      x++;\n      }\n    }\n    qtrrc.calibrate();       // reads all sensors 10 times at 2500 us per read (i.e. ~25 ms per call)\n    if ((i == 127) || (x == 64)){\nstop();\n      } \n  }\n \n MORADO_OFF();\n  //---------------------------Fin Calibracion de Sensores----------------------------------------------------//\n  for (int i=0; i <=6; i++){  // parpadeo para indicar que termino la calibracion\n       CELESTE_ON();\n       delay(timer);\n       CELESTE_OFF();\n       delay(timer);\n  } \n}\nvoid RESET_ALL(){\n stop();// Motores detenidos  \n LEDS_OFF();\n Serial.flush(); \n}\n//--------------------------------------------------------------------------------------//\nvoid setup()\n{\n  Serial.begin(9600);\n \n  pinMode(STBY, OUTPUT);\n\n  pinMode(PWMA, OUTPUT);\n  pinMode(AIN1, OUTPUT);\n  pinMode(AIN2, OUTPUT);\n\n  pinMode(PWMB, OUTPUT);\n  pinMode(BIN1, OUTPUT);\n  pinMode(BIN2, OUTPUT);\n  \n  pinMode(AZUL,OUTPUT);\n  pinMode(VERDE,OUTPUT);\n  pinMode(ROJO,OUTPUT);\n RESET_ALL();\n};\n\nunsigned int last_proportional = 0;\nlong integral = 0;\nvoid loop()\n{\nmenu_Arduino_Java();\n};\n//<------------------------------------------------------------------------>\n// ################# FUNCIONES ###########################################\nvoid menu_Arduino_Java(){\n  boolean sensores=false,motores=false;\n  if(Serial.available() > 0){ \n       do{\n            delay(5);   // Mejora la recepcion de datos\n           inputString = Serial.read();\n           switch (inputString){\n              case '1':\n                digitalWrite(ROJO,HIGH);\n                   break;\n              case '2':\n               digitalWrite(VERDE,HIGH);\n                   break;\n              case '3':\n               digitalWrite(AZUL,HIGH);\n                   break;\n              case '4':\n                  MORADO_ON();\n                   break;\n              case '5':\n                 CELESTE_ON();\n                   break;\n              case '6':\n                  LEDS_OFF();             //apagar leds\n                   break;\n              case '7':\n                 \n                     QTR_calibration();  // Iniciar Calibracion\n                     stop();\n                   break;  \n              case '8':\n                  sensores = true;              // Lectura de sensores para graficar\n                  while ( sensores == true ){\n                   read_IR();\n                   inputString=Serial.read();\n                   if( inputString >= '0' ){\n                     sensores=false;\n                  }\n                  }\n                  break;\n              case '9':                // MOTORES ADELANTE\n                    motores = true;\n                    while(motores==true){\n                    setSpeed(255,255); \n                    delay(100);\n                    inputString= Serial.read();\n                    if( inputString >= '0' ){\n                      motores=false;\n                      stop();\n                  }\n                   }\n                    break;\n        \n              case 'a':\n                    motores = true;\n                    while(motores==true){\n                     setSpeed(-255,-255); \n                     delay(100);\n                     inputString= Serial.read();\n                     if( inputString >= '0' ){\n                      motores=false;\n                      stop();\n                     }\n                   }\n                   break;\n             case 'b':\n                      Serial.println(VProporcional);\n                      delay(250);\n                      Serial.println(VDerivativo);\n                      delay(250);\n                      Serial.println(maximum);\n                      delay(250);\n                   /* while (Serial.available() == 0) ;  // Wait here until input buffer has a character\n                      {\n                        VProporcional = Serial.parseFloat();      \n                        while (Serial.available() > 0)  // .parseFloat() can leave non-numeric characters\n                              { junk = Serial.read() ; }      // clear the keyboard buffer\n                           }\n                 \n                      while (Serial.available() == 0) ;  // Wait here until input buffer has a character\n                      {\n                        VDerivativo = Serial.parseInt();      \n                        while (Serial.available() > 0)  // .parseFloat() can leave non-numeric characters\n                              { junk = Serial.read() ; }      // clear the keyboard buffer\n                           }\n            \n                         while (Serial.available() == 0) ;  // Wait here until input buffer has a character\n         \n                      {\n                        maximum= Serial.parseInt();      \n                        while (Serial.available() > 0)  \n                              { junk = Serial.read() ; }   \n                           }*/\n                  break;\n             case 'c' :\n                     while (Serial.available() == 0) ;  // Wait here until input buffer has a character\n                      {\n                        VProporcional = Serial.parseFloat();      \n                        while (Serial.available() > 0)  // .parseFloat() can leave non-numeric characters\n                              { junk = Serial.read() ; }      // clear the keyboard buffer\n                           }\n                         break;\n             case 'e':\n                      RESET_ALL();            // REINICAR PROGRAMA\n                      software_Reset();\n                      break;\n             case 'f': \n                   // delay(3000); // Espera 3 segundos antes de empezar a calibrar\n                     QTR_calibration();       // MODO COMPETENCIA\n                     while(true){\n                      Line_follower_PID();\n                     }\n                   break;\n               }; // FIN SWITCH MENU\n           }while( inputString == 0 );\n         \n      }    \n /*   else{\n     QTR_calibration();     \n     while(true){\n     Line_follower_PID();\n      }*/\n    }  \n\n void software_Reset() // Restarts program from beginning but does not reset the peripherals and registers\n{\nasm volatile (\"  jmp 0\");  \n}  \nvoid read_IR() {\n unsigned int position = qtrrc.readLine(sensorValues); // leemos posicion de la linea en la variable position\n Serial.println(position); \n delay(timer);\n}\n\nvoid Line_follower_PID(){\nunsigned int position = qtrrc.readLine(sensorValues); // leemos posicion de la linea en la variable position\n  int proportional = (int)position - ref_line;  // Referencia donde seguira la linea, mitad sensores.\n  // Calculos PD\n  int derivative = proportional - last_proportional;\n  integral += proportional;\n  last_proportional = proportional;\n  \n  int power_difference = proportional/VProporcional + integral*0 + derivative*VDerivativo;\n  \n  if (power_difference > maximum)\n    power_difference = maximum;\n  if (power_difference < -maximum)\n    power_difference = -maximum;\n  \n    if (power_difference < 0)\n    setSpeed(maximum, maximum + power_difference);\n  else\n    setSpeed(maximum - power_difference,maximum); \n}\nvoid setSpeed(int velocidad_1,int velocidad_2){\n\nif ((velocidad_1 < 0) || (velocidad_2 < 0)){\n  if ((velocidad_1 < 0) && (velocidad_2 <0)){\n     velocidad_1= abs(velocidad_1); //Motor A atras\n     velocidad_2= abs(velocidad_2); //Motor B atras\n      move(1,velocidad_1, 0); \n      move(2,velocidad_2, 0);\n  }\n    if (velocidad_1 < 0){\n      velocidad_1= abs(velocidad_1); //Motor A atras\n      move(1,velocidad_1, 0); \n      move(2,velocidad_2, 1); \n    }\n    if (velocidad_2 < 0){\n      velocidad_2= abs(velocidad_2);//Motor B atras\n      move(1,velocidad_1, 1); \n      move(2,velocidad_2, 0); \n    } \n  }\n  else{\n     move(1, velocidad_1, 1); \n     move(2,velocidad_2, 1); \n  }\n}\nvoid move(int motor, int speed, int direction){\n  digitalWrite(STBY, HIGH); \n\n  boolean inPin1 = LOW;\n  boolean inPin2 = HIGH;\n\n  if(direction == 1){\n    inPin1 = HIGH;\n    inPin2 = LOW;\n  }\n\n  if(motor == 1){\n    digitalWrite(AIN1, inPin1);\n    digitalWrite(AIN2, inPin2);\n    analogWrite(PWMA, speed);\n  }else{\n    digitalWrite(BIN1, inPin1);\n    digitalWrite(BIN2, inPin2);\n    analogWrite(PWMB, speed);\n  }\n}\n\n");
        codeino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codeinoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(codeino);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei Light", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setText("Código Arduino:");
        jLabel1.setName(""); // NOI18N

        button.setText("Guardar ");
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonMouseClicked(evt);
            }
        });
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText("Escoger la carpeta :/Codigo Arduino y a continuación asignarle un nombre al archivo.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button)
                                .addGap(21, 21, 21))
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codeinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codeinoKeyPressed


        // TODO add your handling code here:
    }//GEN-LAST:event_codeinoKeyPressed

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
   
// TODO add your handling code here:
    }//GEN-LAST:event_buttonActionPerformed

    private void buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonMouseClicked
         
 String Texto = codeino.getText();
try{
//System.getProperty("user.dir") Abre el JFileChooser donde esta el ejecutable

 
  
File directorio;

     JFileChooser fc=new JFileChooser(System.getProperty("user.dir"));
        directorio = new File("Codigo Arduino");
           directorio.mkdir();
  
      fc.showSaveDialog(this); //Muestra el diálogo
  File Guardar =fc.getSelectedFile();
  if(Guardar !=null){
       try (FileWriter Guardx = new FileWriter(Guardar+".ino")) {
        
           Guardx.write(Texto);
           Guardx.close(); //Cierra el fichero
       }
   }
 }
 catch(IOException ioe){
 System.out.println(ioe); //Muestra por consola los errores
} 
   
        
    }//GEN-LAST:event_buttonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Code.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Code.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Code.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Code.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Code().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button;
    private javax.swing.JTextArea codeino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}