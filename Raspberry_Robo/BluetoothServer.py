#
# includes parts of rfcomm-server.py
#
import signal
import os
import time
import subprocess
import json
from PIL import Image
from bluetooth import *
import numpy as np
from io import StringIO, BytesIO
import select
import socket
import ParseSettings as settings
from wifi import Cell, Scheme

socket.setdefaulttimeout(60)
wpa_supplicant_conf = "/etc/wpa_supplicant/wpa_supplicant.conf"
sudo_mode = "sudo "
FIFO_Sound = 'tosound'
FIFO_Interaction = 'tointeraction'

def send_command_to_sound(command):
    
    setting = settings.get_setting("settings.ini","PIDS","Sound")
    if setting != "0" :
        os.kill(int(setting), signal.SIGUSR1)
        with open(FIFO_Sound, "w" ) as fifo:
            print("FIFO opened")
            fifo.write(command)
                 
def send_command_to_interaction(command):
    setting = settings.get_setting("settings.ini","PIDS","Interaction")
    if setting != "0":
        os.kill(int(setting), signal.SIGUSR1)
        with open(FIFO_Interaction, "w" ) as fifo:
            print("FIFO opened")
            fifo.write(command)

def wifi_connect(ssid, psk):
    cmd = 'wpa_passphrase {ssid} {psk} | sudo tee -a {conf} > /dev/null'.format(
            ssid=str(ssid).replace('!', '\!'),
            psk=str(psk).replace('!', '\!'),
            conf=wpa_supplicant_conf
        )
    str_return = ""
    str_return = os.system(cmd)

    cmd = sudo_mode + 'wpa_cli -i wlan0 reconfigure'
    str_return = os.system(cmd)

    time.sleep(10)

    cmd = 'iwconfig wlan0'
    str_return = os.system(cmd)

    cmd = 'ifconfig wlan0'
    str_return = os.system(cmd)

    p = subprocess.Popen(['hostname', '-I'], stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE)
    out, err = p.communicate()

    if out:
        ip_address = out
    else:
        ip_address = "<Not Set>"

    return ip_address



def ssid_discovered():
    Cells = Cell.all('wlan0')

# for test purpose only
#     print(Cells)
#     for current in Cells:
#         print('\n key ')
#         print(current)
#         wifi_info +=  current.ssid + "\n"


    wait_ssid ="/wait_ssid/"
    return wait_ssid


def handle_client(client_sock) :
    # get ssid
    client_sock.send(ssid_discovered())

    ssid = client_sock.recv(1024)
    if ssid == '' :
        return
    
    # get psk
    client_sock.send("/wait_psk/")
    psk = client_sock.recv(1024)
    if psk == '' :
        return

    ip_address = wifi_connect(ssid, psk)
    print ('ip address: ' + str(ip_address))
    client_sock.send('status:' + str(ip_address))

    return

def root():
    return optionsStatus()

def optionsStatus():
    status = {}
    status['sound'] = settings.get_setting("settings.ini","Settings","Sound")
    status['mic'] = settings.get_setting("settings.ini","Settings","Sound")
    status['facerec'] = settings.get_setting("settings.ini","Settings","Sound")
    status['submon'] = settings.get_setting("settings.ini","Settings","Sound")
    status['opmode'] = settings.get_setting("settings.ini","Settings","Sound")
    status['battery'] = settings.get_setting("settings.ini","Settings","Battery")
    # JSON encode and transmit response
    response = json.dumps(status)
    client_sock.send(response)

def count_subjects():
    path = './data'
    imagePaths = [os.path.join(path,f) for f in os.listdir(path)]
    ids = []
    for imagePath in imagePaths:
        id = int(os.path.split(imagePath)[-1].split(".")[1])
        ids.append(id)

    print (ids)
    responce = {}
    responce['subjects'] = ids
    response = json.dumps(status)
    client_sock.send(response)
    

# add serial port service
subprocess.call(['sudo', 'sdptool', 'add', 'SP'])

# simple-agent.py script is a part of pybluez modules examples
# check out by:
#    sudo apt-get install python-module-pybluez
#    wget -O simple-agent http://git.kernel.org/?p=bluetooth/bluez.git;a=blob_plain;f=test/simple-agent;hb=HEAD
#    ./simple-agent hci0 <remote address>

subprocess.call(['python3','simple-agent.py'])
# signal main loop start
signalhandler = False
try:
    while True:
        server_sock = BluetoothSocket(RFCOMM)
        server_sock.bind(("", PORT_ANY))
        server_sock.listen(1)
        port = server_sock.getsockname()[1]
        uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

        advertise_service( server_sock, "roboserver-bt",
                           service_id = uuid,
                           service_classes = [uuid, SERIAL_PORT_CLASS],
                           profiles = [SERIAL_PORT_PROFILE] )
                           
        # signal server startup
        print ("Server socket created")
        # wait for connection
        client_sock, client_info = server_sock.accept()

        #for test purpose only
        #client_sock.settimeout(None)

        print ("Client connected")        

        # receiver loop
        try:
            partial = ''
            
            while True:
            
                try:
                    dat = client_sock.recv(1024)
                except socket.timeout as e:
                    err = e.args[0]
                    # timeout exception 
                    if err == 'timed out':
                        sleep(1)
                        print ('recv timed out, retry later')
                        continue
                    else:
                        print (e)
                        sys.exit(1)
                        
                print("Recieve some data")
                
                data = dat.decode("utf-8") 
                if len(data) == 0:
                    break
                elif len(data) == 1024:
                    isBufferFull = True
                else:
                    isBufferFull = False

                # save partial received commands
                commands = (partial + data).split('$$')
                number = len(commands)

                if isBufferFull:
                    partial = commands[number-1]
                    number -= 1
                else:
                    partial = ''

                # loop through commands
                for i in range(number):

                    # parse the command
                    command = commands[i]
                    print(" Command = " + command)
                    parts = command.split('/')
                    count = len(parts)

                    def parseCommand(command, parts, count):
                        
                        if command == '':
                            continue
                        
                        elif command == '/':
                            root()
                        elif command == '/status/':
                            optionsStatus()
                         
                        elif command == '/subjects/':
                            count_subjects()
                            
                        elif command == '/add/':
                            send_command_to_interaction("add")
                            
                        elif command[0:8] == '/remove/' and count == 3:
                            send_command_to_interaction("remove " + parts[2])
                            
                        elif command == '/restart/':
                            os.system('reboot')
                            
                        elif command == '/find/':                       
                            send_command_to_sound("find")
                            
                        elif command == '/wifi_connect/':
                            handle_client(client_sock)
                                                
                        elif command[0:8] == '/opmode/' and count == 3:
                            enable = parts[2]
                            settings.update_setting("settings.ini","Settings","Operation mode", enable)
                            soundEn = settings.get_setting("settings.ini","Settings","Operation mode")
                            send_command_to_interaction("opmode " + soundEn)

                        elif command[0:8] == '/submon/' and count == 3:
                            enable = parts[2]
                            settings.update_setting("settings.ini","Settings","Subject monitoring", enable)
                            soundEn = settings.get_setting("settings.ini","Settings","Subject monitoring")
                            send_command_to_interaction("submon " + soundEn)
                            
                        elif command[0:9] == '/facerec/' and count == 3:
                            enable = parts[2]
                            settings.update_setting("settings.ini","Settings","Face recognition", enable)
                            soundEn = settings.get_setting("settings.ini","Settings","Face recognition")
                            send_command_to_interaction("facerec " + soundEn)
                            
                        elif command[0:5] == '/mic/' and count == 3:
                            enable = parts[2]
                            settings.update_setting("settings.ini","Settings","Mic", enable)
                            soundEn = settings.get_setting("settings.ini","Settings","Mic")
                            send_command_to_interaction("mic " + soundEn)                        
                                            
                        elif command[0:7] == '/sound/' and count == 3:
                            enable = parts[2]
                            settings.update_setting("settings.ini","Settings","Sound", enable)
                            soundEn = settings.get_setting("settings.ini","Settings","Sound")
                            send_command_to_interaction("sound " + soundEn)
                            
                        elif command[0:7] == '/photo/':
                            number_ = 1
                            if count == 3:
                                number_ = parts[2]
                                
                            print("Count = " + str(count) + " part[2] = " + str(parts[2]) + " number = " + str(number))
                            imagePath = '/home/raspberry/data/subject.' + str(number_) + '.1.jpg'
                            with open(imagePath, mode='rb') as file: 
                                fileContent = file.read()
                                size = len(fileContent)
                                print("{" + str(size) + "}")
                                client_sock.settimeout(5.0)
                                client_sock.sendall(struct.pack(">L", size) + fileContent)
                                client_sock.settimeout(None)
                                print("Sent photo done")
                        else:
                            print('unrecognized command')

                    parseCommand(command, parts, count)
                
        # receiver loop try
        except IOError as err:
            print("IOError: {0}".format(err))
            pass

        # connection lost
        client_sock.close()
        server_sock.close()

        # signal disconnect

# main loop try
except KeyboardInterrupt:
    client_sock.close()
    server_sock.close()
