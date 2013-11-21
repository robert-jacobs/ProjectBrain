package com.test.bluetooth;

public final class IRobot {

	//getting started commands
	//
	public static final int start = 128;
	//0 data bytes
	public static final int baud = 129;
	//1 data byte [Baud code](0 - 11)
	public static final int safe = 131;
	//0 data bytes
	public static final int full = 132;
	//0 data bytes
	
	
	//demo commands
	//
	public static final int demo = 136;
	//1 data byte [Demo number](-1 - 9)
	public static final int cover = 135;
	//0 data bytes
	public static final int coverNdock = 143;
	//0 data bytes
	public static final int spot = 134;
	//0 data bytes
	
	
	//actuator commands
	public static final int drive = 137;
	//4 data bytes [Velocity high byte][Velocity low byte](-500 - 500)[Radius high byte][Radius low byte](-2000 - 2000)
	public static final int driveDirect = 145;
	//4 data bytes [Right velocity high byte][Right velocity low byte](-500 - 500)[Left velocity high byte][Left velocity low byte](-500 - 500)
	public static final int leds = 139;
	//3 data bytes [LED bits](0 - 10)[Power color](0 - 255)[Power intensity](0 - 255)
	public static final int digitalOut = 147;
	//1 data byte [Output bits](0 - 7)
	public static final int pwmLowSideDrivers = 144;
	//3 data bytes [Low side driver 2 duty cycle](0 - 128)[Low side driver 1 duty cycle](0 - 128)[Low side driver 0 duty cycle](0 - 128)
	public static final int lowSideDrivers = 138;
	//1 data bytes [Driver bits](0 - 7)
	public static final int sendIR = 151;
	//1 data byte [Byte value](0 - 255)
	public static final int song = 140;
	//2N + 2 data bytes [Song number](0-15)[Song length](1-16)[Note Number](31 - 127)[Note Duration](0 - 255)[repeat the last two, if needed]
	public static final int playSong = 141;
	//1 data byte [Song number](0 - 15)
	public static final int sensors = 142;
	//1 data byte [Packet ID](0 - 42)
	public static final int queryList = 149;
	//N + 1 [Number of Packets](0 - 255)[Packet ID Num](0 - 42)[repeat last as needed]
	public static final int stream = 148;
	//N + 1 [Number of Packets](0 - 255)[Packet ID Num](0 - 42)[repeat last as needed]
	public static final int pauseORresumeStream = 150;
	//1 data byte [Stream state](0 - 1)
	
	
	//script commands
	//
	public static final int script = 152;
	//N + 1 [Script length](0 - 100)[Opcode 1](use opcodes above, repeat as many as needed with data bytes)
	public static final int playScript = 153;
	//0 data bytes
	public static final int showScript = 154;
	//0 data bytes
	
	//wait commands
	//
	public static final int waitTime = 155;
	//1 data byte [time](0 - 255)
	public static final int waitDistance = 156;
	//2 data bytes [Distance high byte][Distance low byte](-32767 - 32768)
	public static final int waitAngle = 157;
	//2 data bytes [Angle high byte][Angle low byte](-32767 - 32768)
	public static final int waitEvent = 158;
	//1 data byte [Event number] (-1 - -20 AND 1 - 20)
	
	
	//sensor packets
	//
	public static final int bump = 7;
	//1 data byte (0 - 1)
	public static final int wall = 8;
	//1 data byte (0 - 1)
	public static final int cliffLeft = 9;
	//1 data byte (0 - 1)
	public static final int cliffFrontLeft = 10;
	//1 data byte (0 - 1)
	public static final int cliffFrontRight = 11;
	//1 data byte (0 - 1)
	public static final int cliffRight = 12;
	//1 data byte (0 - 1)
	public static final int virtualWall = 13;
	//1 data byte (0 - 1)
	public static final int lowSideDrivAndWheelOverCurr = 14;
	//1 data byte (0 - 31)
	public static final int infrared = 17;
	//1 data byte (0 - 255)
	public static final int buttons = 18;
	//1 data byte (0 - 5)
	public static final int distance = 19;
	//2 data byte (-32768 - 32767)
	public static final int angle = 20;
	//2 data byte (-32768 - 32767)
	public static final int chargingState = 21;
	//1 data byte (0 - 5)
	public static final int voltage = 22;
	//2 data byte (0 - 65535)
	public static final int current = 23;
	//2 data byte (-32768 - 32767)
	public static final int batteryTemp = 24;
	//1 data byte (-128 - 127)
	public static final int batteryCharge = 25;
	//2 data byte (0 - 65535)
	public static final int batteryCap = 26;
	//2 data byte (0 - 65535)
	public static final int wallSignal = 27;
	//2 data byte (0 - 4095)
	public static final int cliffLeftSignal = 28;
	//2 data byte (0 - 4095)
	public static final int cliffFrontLeftSignal = 29;
	//2 data byte (0 - 4095)
	public static final int cliffFrontRightSignal = 30;
	//2 data byte (0 - 4095)
	public static final int cliffRightSignal = 31;
	//2 data byte (0 - 4095)
	public static final int cargoBayDigitalIn = 32;
	//1 data byte (0 - 31)
	public static final int cargoBayAnalogSignal = 33;
	//2 data byte (0 - 1023)
	public static final int chargingSourcesAvailable = 34;
	//1 data byte (0 - 3)
	public static final int oiMode = 35;
	//1 data byte (0 - 3)
	public static final int songNum = 36;
	//1 data byte (0 - 15)
	public static final int songPlaying = 37;
	//1 data byte (0 - 1)
	public static final int numStreamPackets = 38;
	//1 data byte (0 - 43)
	public static final int requestedVelocity = 39;
	//2 data byte (-500 - 500)
	public static final int requestedRadius = 40;
	//2 data byte (-32768 - 32767)
	public static final int requestedRightVelocity = 41;
	//2 data byte (-500 - 500)
	public static final int requestedLeftVelocity = 42;
	//2 data byte (-500 - 500)
	
}
