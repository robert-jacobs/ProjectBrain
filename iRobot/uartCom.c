/*==================================================================================
	Henri Kjellberg

====================================================================================*/
#include <sys/types.h>
#include <sys/stat.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <termios.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

// These items can be modified in order to change the baudrate or the serial port
#define DEFAULTBAUD B9600
#define FALSE 0
#define TRUE 1
#define FEND 0xC0
#define FESC 0xDB
#define TFEND 0xDC
#define TFESC 0xDD

//Not sure if volatile is required in this context. Could be helpful if multiple threads.
volatile int STOP=FALSE;


//writeByteToSer writes an array of byte to the serial port using the write command.
//Inputs include the file descriptor the pointer of the byte array and the number of elements
//in the byte array.
int writeByteToSer(int fd, void* mybyte_ptr,int numElem)
{
	int res,total;
	res = 0;
	total = 0;

	while(STOP==FALSE) //This loop continues to write to the serial port until all bytes arwritten
	{

	  res = write(fd,mybyte_ptr,sizeof(char)*numElem);
	  mybyte_ptr += res;  //Move the pointer forward the number of bytes written
	  total += res; 	//Keep tally of total number of bytes written
	  if(total >= (sizeof(char))*numElem) //Once all bytes are written get out of loop
	  {
	    STOP=TRUE;
	  }
	}
	STOP = FALSE; //reset the loop
	return total;  //Returns number bytes written
}

/* For use with SLIP encapsulated messages. The pointer
 * that returns the data shall not have the SLIP encapsulation
 * on it anymore, the message will only contain the data
 * more details can be found in the Sinclair Interplanetary docs.
 */
int readSlipFromSer(int fd,char* mybyte_ptr,int maxLen)
{
	int res;
	int received = 0;
	while(1)
	{
		res = read(fd,mybyte_ptr,1);

		switch(*mybyte_ptr)
		{
			case FEND:
				if(received)
				{
				    *mybyte_ptr=0;
					return received-1;
				}
				else
				{
                    received++;
					break;
				}
			case FESC:
				res = read(fd,mybyte_ptr,1);
				switch(*mybyte_ptr)
				{
					case TFEND:
						*mybyte_ptr = FEND;
						received++;
						mybyte_ptr++;
						break;
					case TFESC:
						*mybyte_ptr = FESC;
						received++;
						mybyte_ptr++;
						break;
				}
			default:
                if(received!=0)
                {
                    if(received < maxLen)
                    {
                        received++;
                        mybyte_ptr++;
                    }
                }
		}
	}
}

int readByteFromSer(int fd,void* mybyte_ptr,int numElem)
{

      	int res=0;
	int total=0;
	while(STOP==FALSE)
	{
		res = read(fd,mybyte_ptr,sizeof(char)*numElem);   //Blocks until atleast 1 byte is read

		mybyte_ptr += res; //Move pointer forward by the number of bytes read
		total += res; //add up total number of bytes read

		if(total>=sizeof(char)*numElem) //Check if number of bytes read is number expected.
		{
			STOP=TRUE;
		}

	}

	STOP=FALSE; //Reset the while loop
	return total;
}



//writeDoubleToSer writes an array of doubles to the serial port using the write command.
//Inputs include the file descriptor the pointer of the double array and the number of elements
//in the double array.
int writeDoubleToSer(int fd, double dataOut[],int numElem)
{

	//For some reason this would not work until I put a [0] here.
	char *mydouble_ptr = (char *)dataOut;
	int c,i,res,total;
	res = 0;
	total = 0;
	while(STOP==FALSE) //This loop continues to write to the serial port until all bytes are written
	{

	  res = write(fd,mydouble_ptr,sizeof(double)*numElem);
	  mydouble_ptr += res;  //Move the pointer forward the number of bytes written
	  total += res; 	//Keep tally of total number of bytes written
	  if(total >= (sizeof(double))*numElem) //Once all bytes are written get out of loop
	  {
	    STOP=TRUE;
	  }
	}
	STOP = FALSE; //reset the loop
	return 1;  //TODO add error condition that returns 1 if failure is reported
}


//readDoubleFromSer reads an array of doubles to the serial port using the read command.
//Inputs include the file descriptor the pointer of the double array that you want to take the data
//and the number of elements in the double array that is expected to come in.
//The data is transmitted as bytes not as characters
int readDoubleFromSer(int fd,double dataIn[],int numElem)
{

      //Not sure why I need to throw a [0] here. I guess its the pointer to the first element...
      char *mydouble_ptr = (char *) &dataIn[0];
      int res,total;
      res = 0;
      total = 0;


      while(STOP==FALSE)
      {

	  res = read(fd,mydouble_ptr,sizeof(double)*numElem);   //Blocks until atleast 1 byte is read

	  mydouble_ptr += res; //Move pointer forward by the number of bytes read
	  total += res; //add up total number of bytes read

	  if(total>=sizeof(double)*numElem) //Check if number of bytes read is number expected.
	  {
	    STOP=TRUE;
	  }

      }

      STOP=FALSE; //Reset the while loop


      return 1;  //TODO add error condition that returns 1 if failure is reported
}

//Obtains the baudrate, a way to check that things are working...
int getbaud(int fd)
{
	struct termios settings;
	int ioSpeed = -1;
	speed_t baudRate;
	tcgetattr(fd, &settings);

	//Retrieve the baud settings
	baudRate = cfgetispeed(&settings);
	switch (baudRate)
	{
		case B0:      ioSpeed = 0; break;
		case B50:     ioSpeed = 50; break;
		case B110:    ioSpeed = 110; break;
		case B134:    ioSpeed = 134; break;
		case B150:    ioSpeed = 150; break;
		case B200:    ioSpeed = 200; break;
		case B300:    ioSpeed = 300; break;
		case B600:    ioSpeed = 600; break;
		case B1200:   ioSpeed = 1200; break;
		case B1800:   ioSpeed = 1800; break;
		case B2400:   ioSpeed = 2400; break;
		case B4800:   ioSpeed = 4800; break;
		case B9600:   ioSpeed = 9600; break;
		case B19200:  ioSpeed = 19200; break;
		case B38400:  ioSpeed = 38400; break;
		case B57600:  ioSpeed = 57600; break;
		case B115200: ioSpeed = 115200; break;
		case B230400: ioSpeed = 230400; break;
	}
	return ioSpeed; //returns -1 if not working.
}


//Initialize the port, returns the file descriptor
int initializePort(const char *serialPort)
{
	int fd; //file descriptor
	struct termios oldSettings,newSettings;

	fd = open(serialPort, O_RDWR | O_NOCTTY ); //Open read and write, not control terminal
	if (fd < 0)
	{
		perror(serialPort);
		exit(-1);
	}

	tcgetattr(fd,&oldSettings); // Save the old settings if this matters to you
	bzero(&newSettings, sizeof(newSettings)); //Cear the settings
	newSettings.c_cflag = DEFAULTBAUD | CS8 | CLOCAL | CREAD; //CRTSCTS
	newSettings.c_iflag = IGNPAR; //Ignore parity
	newSettings.c_oflag = 0; //Raw output
	newSettings.c_lflag = 0; //Non cannonical input mode.
	newSettings.c_cc[VTIME] = 0;  // inter-character timer is not used
	newSettings.c_cc[VMIN] = 1;   // The read blocks until atleast 1 byte is read.
	cfmakeraw(&newSettings);
	tcflush(fd, TCIOFLUSH); //Flush input and output buffers
	tcsetattr(fd,TCSANOW,&newSettings); //Set the setings

	return fd; //return the file descriptor
}
int setSerialBaudRate(int fd, int baudRate)
{
	struct termios config;
	int ret;
	speed_t br;

	tcgetattr(fd,&config); // Save the old settings if this matters to you
	switch(baudRate)
	{
		case 1200:
                cfsetispeed(&config, B1200);
				cfsetospeed(&config, B1200);
				break;
		case 9600:
                cfsetispeed(&config, B9600);
				cfsetospeed(&config, B9600);
				break;
		case 19200:
		        cfsetispeed(&config, B19200);
				cfsetospeed(&config, B19200);
				break;
        case 57600:
                cfsetispeed(&config, B57600);
				cfsetospeed(&config, B57600);
				break;
		case 115200:
                cfsetispeed(&config, B115200);
				cfsetospeed(&config, B115200);
				break;
		case 230400:
                cfsetispeed(&config, B230400);
				cfsetospeed(&config, B230400);
				break;
	}
	tcsetattr(fd,TCSANOW,&config); //Set the setings
	return ret;
}



