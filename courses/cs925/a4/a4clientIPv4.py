#!/usr/bin/env python
import socket

BUFFER_SIZE = 1024

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('agate.cs.unh.edu', 5005))
client.send('HI!')
data  = client.recv(BUFFER_SIZE)
print data
