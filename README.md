# Bluetooth-Bike-IOT

An IOT project I made that uses a Raspberry Pi, hall sensor, and an android smartphone to create a smart bike application that displays current speed and distance on your bike ride. The Raspberry Pi uses PiBluez to constantly search for incoming bluetooth connection. I wrote an android app for the smartphone, which uses a bluetooth service to send a connection request to the Raspberry Pi. Upon connection, the Raspberry Pi starts sending data from the hall sensor, which is received by the app, processed, and displayed.

The hall sensor is placed on the fork of the bike, and a magnet is placed on a tire spoke. As the magnet passes over the hall sensor, the hall sensor senses the magnetic field and counts one revolution of the tire.
