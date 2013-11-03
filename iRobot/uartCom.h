int writeByteToSer(int fd, char* mybyte_ptr,int numElem);
int readSlipFromSer(int fd,char* mybyte_ptr,int maxLen);
int readByteFromSer(int fd,char* mybyte_ptr,int numElem);
int writeDoubleToSer(int fd, double dataOut[], int numElem);
int readDoubleFromSer(int fd,double dataIn[],int numElem);
int getbaud(int fd);
int initializePort(const char *serialPort);
int setSerialBaudRate(int fd, int baudRate);
