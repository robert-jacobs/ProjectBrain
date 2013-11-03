//
//  sensorStream.h
//  
//
//  Created by Dillon Johnson on 10/15/13.
//
//

#ifndef _sensorStream_h
#define _sensorStream_h

int initPort (void);
int initSensors (void);
int readSensors (void);
int unpackData (unsigned char* raw_data);

#endif
