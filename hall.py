import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BCM)

hall = 23 
GPIO.setup(hall,GPIO.IN)
toggle = 1  
state = 1
counter = 0
circ = 7 #circumference of tire


# this loop runs forever
while True:
     
    if GPIO.input(hall) == 0:
        toggle = 0
    else:
        toggle = 1

    if state == 1 and toggle == 0: #did it this way so it only counts 1 for each path instead of 2 
        counter += 1 
        state = toggle
        distance = counter * circ
        print(f'distance traveled: {distance}ft')
    if state != toggle:
        state = toggle
    

