#!/usr/bin/env python
import socket

BUFFER_SIZE = 1024

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((socket.gethostname(), 5005))
server.listen(1)

data, addr = server.accept()

while 1:
	data = server.recv(BUFFER_SIZE)
        print "Received connection from IP address " + addr[0]
        server.send('Hello World!')
