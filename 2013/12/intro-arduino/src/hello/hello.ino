const int LED = 13; // LED connected to
                    // digital pin 13


void setup()
{
  pinMode(LED, OUTPUT);    // sets the digital
                           // pin as output
}

void point(){
  digitalWrite(LED, HIGH); 
  delay(800);               
  digitalWrite(LED, LOW);                  
  delay(200);
}

void dash(){
  digitalWrite(LED, HIGH); 
  delay(1600);               
  digitalWrite(LED, LOW);                
  delay(200);
}



void space(){
  delay(500);               
}

/**print "hello world" in morse code
**/
void sayHello(){
  point();
  point();
  point();
  point();
  space();
  point();
  space();
  point();
  dash();
  dash();
  dash();
  space();
  point();
  dash();
  dash();
  dash();
  space();
  dash();
  dash();
  dash();
  space();
  space();
  space();
  point();
  dash();
  dash();
  space();
  dash();
  dash();
  dash();
  space();
  point();
  dash();
  point();
  space();
  point();
  dash();
  dash();
  dash();
  space();
  dash();
  point();
  point();
  space();
  space();
  space();
    

}

void loop()
{
  sayHello();
}

