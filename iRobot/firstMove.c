//
// firstMove.c - first attempt at getting the iRobot Create to move
//

#include <stdio.h>
#include <time.h>

#include "uartCom.h"
#include "firstMove.h"

const char* port = "/dev/cu.usbserial";

int main (void)
{
    int fd;
    int i;
    struct timespec t;
    t.tv_sec = 0;
    t.tv_nsec = 0.05e9;
    
    printf("Initializing port...");
    fd = initializePort(port);
    printf("done.\n");
    printf("Setting serial baud rate to 57600...");
    setSerialBaudRate(fd, 57600);
    printf("done.\n");
    
    printf("Sending command.\n");
    
    /* Square */
    writeByteToSer(fd,start, 2);
    for (i = 0; i < DRIVE_SQUARE_LENGTH; i++){
        writeByteToSer(fd, &drive_square[i], 1);
        nanosleep(&t, NULL);
    }
    writeByteToSer(fd, start_script, 1);
    
    
    /* Drive Forward */
    //writeByteToSer(fd,drive_forward1, DRIVE_FORWARD1_LENGTH);
    //writeByteToSer(fd,drive_forward2, DRIVE_FORWARD2_LENGTH);
    
    /* Wimp */
    /*
    writeByteToSer(fd, start, START_LENGTH);
    nanosleep(&t,NULL);
    writeByteToSer(fd, wimp, WIMP_LENGTH);
    nanosleep(&t,NULL);
     */
    
    /* Drive 40cm and stop */
    /*
    writeByteToSer(fd, drive_40_1, 1);
    nanosleep(&t,NULL);
    writeByteToSer(fd, drive_40_2, 1);
    nanosleep(&t,NULL);
    writeByteToSer(fd, drive_40_3, 5);
    nanosleep(&t,NULL);
    writeByteToSer(fd, drive_40_4, 3);
    nanosleep(&t,NULL);
    writeByteToSer(fd, drive_40_5, 5);
     */
    
    /* DO SOMETHING!!! */
    /*
    writeByteToSer(fd, &drive_forward1[0], 1);
    nanosleep(&t,NULL);
    writeByteToSer(fd, &drive_forward1[1], 1);
    nanosleep(&t,NULL);
    writeByteToSer(fd, &drive_forward2[0], 1);
    //nanosleep(&t,NULL);
    writeByteToSer(fd, &drive_forward2[1], 1);
    //nanosleep(&t,NULL);
    writeByteToSer(fd, &drive_forward2[2], 1);
    //nanosleep(&t,NULL);
    writeByteToSer(fd, &drive_forward2[3], 1);
    //nanosleep(&t,NULL);
    writeByteToSer(fd, &drive_forward2[4], 1);
    */
    
    printf("Command sent.\n");
    
    return 0;
}
