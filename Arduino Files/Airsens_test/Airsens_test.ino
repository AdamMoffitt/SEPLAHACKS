/* Detector air quality with Arduino Yun and          **
** Optical Dust Sensor (GP2Y1010AU0F)              */

int dustPin = 1; // dust sensor - Arduino A0 pin
int ledP = 2;    

float voMeasured = 0;
float calcVoltage = 0;
float dustDensity = 0;
  
void setup(){
  Serial.begin(9600);
  pinMode(ledP,OUTPUT);
}
  
void loop(){
  digitalWrite(ledP,LOW); // power on the LED
  delayMicroseconds(280);
  
  voMeasured = analogRead(dustPin); // read the dust value
  
  delayMicroseconds(40);
  digitalWrite(ledP,HIGH); // turn the LED off
  delayMicroseconds(9680);
  
  // 0 - 5V mapped to 0 - 1023 integer values
  // recover voltage
  calcVoltage = voMeasured * (5.0 / 1024.0);
  dustDensity = 0.17 * calcVoltage - 0.1;
  dustDensity *= 1000;
  
  Serial.print("Raw Signal Value(0-1023) : ");
  Serial.print(voMeasured);
  
  Serial.print(" - Voltage: ");
  Serial.print(calcVoltage);
  
  Serial.print(" - Dust Density: ");
  Serial.println(dustDensity); // microg/m3
  
  delay(1000);
}
