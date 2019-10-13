#include <EEPROM.h>
int ttlcycles=0,cycletime=0,lutime=0,stoptime=0,currcycle=0;
int ttlcycleadd=1;
int cycletimeadd=3;
int lutimeadd=5;
int stoptimeadd=7;
int currcycleadd=9;
String input;
int irrcycles = 0;
boolean inputcomp=false;
int inputread[4]; 
boolean jobrunning=false;
boolean cyclerunning=false;
boolean lurunning=false;
boolean cyclecomp=false;
boolean lucomplete=false;
boolean stopcomplete=false;
unsigned long currMillis;
unsigned long prevMillis;
unsigned long cycleprevMillis;
unsigned long cycleMillis;
int cyclestartdelay=5000;

int jobstartpin=7;
int jobendpin=8;
int cyclepin=9;

boolean manualmode=false;




void setup() {
  Serial.begin(38400);
  pinMode(jobstartpin,OUTPUT);
  pinMode(jobendpin,OUTPUT);
  pinMode(cyclepin,OUTPUT);
  digitalWrite(cyclepin,HIGH);
  digitalWrite(jobstartpin,HIGH);
  digitalWrite(jobendpin,HIGH);
  
  initData();
  

}

void loop() {
  if(Serial.available() >0)
  {
     char c=Serial.read();
     if(c!=';')
     {
      input+=c;
     }
     else
     {
      inputcomp=true;
      Serial.println("input complete");
      Serial.println("User input -----> "+input+";");
     }
  }
  if(inputcomp&&(input.charAt(input.length()-1)=='$')&&!manualmode)
  {
    manualmode=true;
    //inputcomp=false;
    endCycle();
    endJob();
  }
  else if(inputcomp&&!manualmode)
  {
    serialread();
    currcycle=0;
    lurunning=false;
    endCycle();
    endJob();
    delay(5000);
    saveData();
    initData();
    input="";
    inputcomp=false;
    return;
  }


  if(manualmode&&inputcomp)
  {
    if(input=="js$")
    {
      startJob();
    }
    else if(input=="je$")
    {
      endJob();
    }
    else if(input=="cs$")
    {
      startCycle();
    }
    else if(input=="ce$")
    {
      endCycle();
    }
    else if(input=="end$")
    {
      Serial.println("manualmode ended");
      manualmode=false;
      input="";
      inputcomp=false;
      currcycle=0;
      saveData();
      initData();
      endCycle();
      endJob();
      return;
    }
    input="";
    inputcomp=false;
    
  }
  
  if(ttlcycles!=0&&!manualmode)
  {
    if(!jobrunning&&currcycle==0)
    {
      startJob();
      delay(5000);
    }
    else{jobrunning=true;}
  
    if(!cyclerunning&&jobrunning&&!lurunning)
    {
      startCycle();
      prevMillis=millis();
    }
    currMillis=millis();
    if(cyclerunning&&((currMillis-prevMillis)>=cycletime))
    {
      endCycle();
      currcycle++;
      saveCurrCycle();
      if(currcycle==ttlcycles)
      {
        currcycle=0;
        saveCurrCycle();
        delay(5000);
        endJob();
        delay(5000);
        
      }
    }
    if(!cyclerunning && jobrunning&&!lurunning)
    {
      lurunning=true;
      Serial.println("lu running");
      prevMillis=millis();
    }
    currMillis=millis();
    if(((currMillis-prevMillis)>=lutime)&&lurunning)
    {
      lurunning=false;
      Serial.println("lu ended");
    }
  }

  
 
  

}

void startJob()
{
  jobrunning=true;
  Serial.println("job started;");
  digitalWrite(jobstartpin,LOW);
  delay(300);
  digitalWrite(jobstartpin,HIGH);

}

void endJob()
{
  jobrunning=false;
  Serial.println("job ended;");
  digitalWrite(jobendpin,LOW);
  delay(300);
  digitalWrite(jobendpin,HIGH);

}

void startCycle()
{
  cyclerunning=true;
  Serial.println("cycle no-"+String(currcycle+1)+" started");
  digitalWrite(cyclepin,LOW);

}

void endCycle()
{
  cyclerunning=false;
  Serial.println("cycle no-"+String(currcycle+1)+" ended");
  digitalWrite(cyclepin,HIGH);
}

void serialread()
     { 
     int a=0;
     int p=0;
     for(int i=0;i<input.length();i++)
     {
     if(input[i]==',')
     { 
     inputread[a]=(input.substring(p,i)).toInt();
     a++;
     p=i+1;}
     }
     inputread[a]=(input.substring(p,input.length())).toInt();
     ttlcycles=inputread[0];
     cycletime=inputread[1]*1000;
     lutime=inputread[2]*1000;
     irrcycles=inputread[3];
     }

void saveCurrCycle()
{
  EEPROMWriteInt(currcycleadd,currcycle);
}
void saveData()
{
  EEPROMWriteInt(ttlcycleadd,ttlcycles);
  EEPROMWriteInt(cycletimeadd,cycletime);
  EEPROMWriteInt(lutimeadd,lutime);
  //EEPROMWriteInt(stoptimeadd,stoptime);
  EEPROMWriteInt(currcycleadd,currcycle);
}


void initData()
{
  ttlcycles=EEPROMReadInt(ttlcycleadd);
  cycletime=EEPROMReadInt(cycletimeadd);
  lutime=EEPROMReadInt(lutimeadd);
  //stoptime=EEPROMReadInt(stoptimeadd);
  currcycle=EEPROMReadInt(currcycleadd);
  Serial.println("ttlcycles="+String(ttlcycles)+";");
  Serial.println("cycletime="+String(cycletime)+";");
  Serial.println("lutime="+String(lutime)+";");
  Serial.println("currcycle="+String(currcycle)+";");
}




void EEPROMWriteInt(int address, int value)
{
  byte two = (value & 0xFF);
  byte one = ((value >> 8) & 0xFF);
  
  EEPROM.update(address, two);
  EEPROM.update(address + 1, one);
}


int EEPROMReadInt(int address)
{
  long two = EEPROM.read(address);
  long one = EEPROM.read(address + 1);
  //Serial.print(((two << 0) & 0xFFFFFF) + ((one << 8) & 0xFFFFFFFF)));
 
  return ((two << 0) & 0xFFFFFF) + ((one << 8) & 0xFFFFFFFF);
}
 
