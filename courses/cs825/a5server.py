#!/usr/bin/env python
import socket

BUFFER_SIZE = 1024

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server.bind((socket.gethostname(), 5005))

while 1:
   data, addr = server.recvfrom(BUFFER_SIZE)
   print "Received connection from IP address " + addr[0]
   server.sendto('Your IP address is ' + str(addr[0]), addr)
