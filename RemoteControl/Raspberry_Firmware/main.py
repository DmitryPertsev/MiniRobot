import socket

test_stop_counter = 0

server_socket = socket.socket()
server_socket.bind(('0.0.0.0', 65432))

while True:
    server_socket.listen(1)
    connection, address = server_socket.accept()
    print('Connected:', address)

    try:
        while True:
            test_stop_counter += 1

            if(test_stop_counter > 2000):
                connection.send('s'.encode('utf-8'))
            else:
                connection.send('o'.encode('utf-8'))

            if(test_stop_counter == 3000):
                test_stop_counter = 0

            connection.settimeout(3)
            data = connection.recv(1024)
            if len(data) != 0:
                print(data)

    except:
        print('Closing connection')
        connection.close()