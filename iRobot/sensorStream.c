//
//  sensorStream.c
//  
//
//  Created by Dillon Johnson on 10/15/13.
//
//  Sensor stream discussed in manual on page 14: http://www.irobot.com/filelibrary/pdfs/hrd/create/Create%20Open%20Interface_v2.pdf
//
//

#include <stdio.h>
#include <time.h>

#include "uartCom.h"
#include "sensorstream.h"
#include "firstMove.h"

static int fd;
static int bat_volt = 0;
static int bat_temp = 0;
static int bat_charge = 0;
static int bat_cap = 0;

int main (void)
{
    printf("Initializing port...");
    initPort();
    printf("complete.\n");
    printf("Initializing sensor stream...");
    initSensors();
    printf("complete.\n");
    
    while (1){
        readSensors();
        system("clear");
        printf("BATTERY DATA\n");
        printf("============\n");
        printf("Voltage:\t%d\n",bat_volt);
        printf("Temperature:\t%d\n",bat_volt);
        printf("Charge:\t\t%d\n",bat_volt);
        printf("Capacity:\t%d\n",bat_volt);
        
    }
    
    return 0;
}

const char* port = "/dev/cu.usbserial";
int initPort (void)
{
    fd = initializePort(port);
    setSerialBaudRate(fd, 57600);
    
    return 0;
}

/*
 What sensors packets do we want?
 
 23. Battery Voltage in mV(2, unsigned)
 24. Battery Temp in degrees Celsius (1, signed)
 25. Battery Charge in mAh (2, unsigned)
 26. Battery Capacity in mAh (2, unsigned)
 
*/
#define NUM_PACKETS 4
#define DATA_SIZE 7
int initSensors (void)
{
    int i;
    struct timespec t;
    t.tv_sec = 0;
    t.tv_nsec = 0.05e9;
    unsigned char Command [NUM_PACKETS + 2] = {148,NUM_PACKETS,23,24,25,26};
    
    writeByteToSer(fd,start, 2);
    for (i = 0; i < NUM_PACKETS + 2; i++){
        writeByteToSer(fd, &Command[i], 1);
        nanosleep(&t, NULL);
    }
    
    return 0;
}

int readSensors (void)
{
    unsigned char byte = 0;
    unsigned char raw_data [6 + DATA_SIZE];
    int i;
    
    do {
        readByteFromSer(fd, &byte, 1);
    } while (byte != 19);
    
    readByteFromSer(fd, raw_data, 6 + DATA_SIZE);
    
    unpackData(raw_data);
    
    return 0;
}

int unpackData (unsigned char* raw_data)
{
    unsigned char uchar = 0;
    char schar = 0;
    unsigned short ushort = 0;
    short sshort= 0;

    ushort = (raw_data[2] << 8) + raw_data[3];
    bat_volt = ushort;
    
    schar = raw_data[5];
    bat_temp = schar;
    
    ushort = (raw_data[7] << 8) + raw_data[8];
    bat_charge = ushort;
    
    ushort = (raw_data[10] << 8) + raw_data[11];
    bat_cap = ushort;
    
    return 0;
}
