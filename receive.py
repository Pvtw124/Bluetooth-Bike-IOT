import bluetooth
import logging
import threading
import time 
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
hall = 23  
GPIO.setup(hall,GPIO.IN)



def send_message():
    try: 
        hall, toggle, state, counter, circ, distance, speed, startTime = (23, 1, 1, 0, 7, 0, 0, 0)
        while True:
            if GPIO.input(hall) == 0:
                toggle = 0 
            else:
                toggle = 1

            if state == 1 and toggle == 0:
                counter += 1
                state = toggle
                distance = counter * circ
                newTime = time.time()
                timediff = newTime - startTime
                speed = round((7/timediff)*1.4666667) 
                client_sock.send(str(speed) + "|" + str(distance) + ",")
                startTime = time.time()
            elif state != toggle:
                state = toggle
            else:
                client_sock.getpeername() #if it can't get peer name, it means it's not connected so it throws error and loop breaks
    except OSError:
        print("unable to send message")
        pass

def receive_message():
    try:
        while True:
            data = client_sock.recv(1024)
            if not data:
                break
            print("Received", data)
    except OSError:
        pass


running = True
while(running):
    server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    server_sock.bind(("", bluetooth.PORT_ANY))
    server_sock.listen(1)
    port = server_sock.getsockname()[1]
    uuid = "00001101-0000-1000-8000-00805F9B34FB" #generic one from app
    bluetooth.advertise_service(server_sock, "SampleServer", service_id=uuid,
                                service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
                                profiles=[bluetooth.SERIAL_PORT_PROFILE],
                                )
    print("Waiting for connection on RFCOMM channel", port)
    client_sock, client_info = server_sock.accept()
    print("Accepted connection from", client_info)
    send_message()

#    stop_threads = False
#    t1 = threading.Thread(target=send_message)
#    t1.start()
#x.join()
client_sock.close()
server_sock.close()
print("All done.")
