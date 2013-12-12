const int LED = 13; // LED connected to
                    // digital pin 13

void setup()
{
  pinMode(LED, OUTPUT);    // sets the digital
                           // pin as output
}


/**1. Turns on the led
   2. Wait one second
   3. Tuns off the led
   4. Wait one second
**/
void loop()
{
  digitalWrite(LED, HIGH); 
  delay(1000);               
  digitalWrite(LED, LOW);  
  delay(1000);              
}

