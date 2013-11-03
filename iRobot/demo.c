//
//  demo.c
//  
//
//  Created by Dillon Johnson on 10/16/13.
//
//

#include <stdio.h>
#include <time.h>
#include <pthread.h>

#include "createoi.h"

void* isensor_t (void* ptr);
int updateSensorData (void);

static int bat_volt = 0;
static int bat_cur = 0;
static int bat_temp = 0;
static int bat_charge = 0;
static int bat_cap = 0;
static int dist = 0;
static int ang = 0;
static int vel = 0;

int main (void)
{
    pthread_t sens_thread;
    
    startOI_MT ("/dev/cu.usbserial");
    
    pthread_create(&sens_thread, NULL, isensor_t, NULL);
    pthread_detach(sens_thread);
    
    while (1)
    {
        driveDistance (300, 0, 50, 1);
        turn (200, 1, 30, 0);
    }
    
    stopOI_MT();
}

void* isensor_t (void* ptr)
{
    struct timespec t;
    t.tv_sec = 0;
    t.tv_nsec = 0.01e9;
    
    while (1){
        updateSensorData();
        system("clear");
        printf("Sensor DATA\n");
        printf("===========\n");
        //printf("Bat Charge:\t%d mAh\n",bat_volt);
        printf("Distance:\t%d mm\n", dist);
        printf("Angle:\t\t%d degrees\n", ang);
        printf("Velocity:\t%d mm/s\n", vel);
        //printf("Current:\t%d\n",bat_cur);
        //printf("Temperature:\t%d\n",bat_volt);
        //printf("Charge:\t\t%d\n",bat_volt);
        //printf("Capacity:\t%d\n",bat_volt);
        
        nanosleep(&t, NULL);
    }
    
    return NULL;
}

int updateSensorData(void)
{
    int* raw_data;
    
    //raw_data = getAllSensors();
    
    /* Battery Data */
    /*
    bat_volt = raw_data[15];
    bat_cur = raw_data[16];
    bat_temp = raw_data[17];
    bat_charge = raw_data[18];
    bat_cap = raw_data[19];
     */
    //bat_charge = getCharge();
    dist = getDistance();
    ang = getAngle();
    vel = getVelocity();
    
    free (raw_data);
    
    return 0;
}