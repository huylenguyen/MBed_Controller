# MBed_Controller

University project worked on during Feb-March 2016. This software uses an in-house library to implement functionality on an MBed FRDM-K64F and application shield with Java 
instead of C++.
The goal is to implement the MBed hardware as a one-handed videogame controller. The target group is gamers who can only reliably use one hand.

Currently implementation is in two parts:
  MBED_Publisher takes input from the hardware and publishes the data to an MQTT server.
  Robot_Receiver connects to MQTT server and subscribe for data. Data passed to an implementation of the Robot API to simulate keyboard
    and mouse functions. 

Software currently fully implemented and tested with Minecraft, with the exception of a hardware magnetic interference
  which makes the mouse data inaccurate. 
