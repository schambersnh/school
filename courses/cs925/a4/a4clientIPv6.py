#!/usr/bin/env python

from socket import *

BUFFER_SIZE = 1024

addrinfo = getaddrinfo('agate.cs.unh.edu', 5005, AF_INET6, SOCK_STREAM)
(family, socktype, proto, canonname, sockaddr) = addrinfo[0]
client = socket(family, socktype, proto)
client.connect(sockaddr)
client.send('HI!')
data  = client.recv(BUFFER_SIZE)
print data
