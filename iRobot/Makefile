#
#
#

firstMove:
	gcc -c uartCom.c
	gcc -c firstMove.c
	gcc *.o -o firstMove
	rm *.o

sensorStream:
	gcc -c uartCom.c
	gcc -c sensorStream.c
	gcc *.o -o sensorStream
	rm *.o

demo::
	gcc -c createoi.c
	gcc -c demo.c
	gcc *.o -o demo
	rm *.o

clean:
	rm *.o
	rm *~