import csv
import sys
import random
import time
import datetime
import socket

HOST, PORT = "sandbox.hortonworks.com", 9999

# Create a socket (SOCK_STREAM means a TCP socket)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))

try:
    # writer = csv.writer(f)
    i = 0
    while True:
    	txnID = i
    	customerID = random.randint(100, 999)
    	countryCode = random.choice(["US", "UK", "IN", "FR"])
    	ipAddress = '.'.join('%s'%random.randint(0, 255) for i in range(4))
    	isp = random.choice(["AT&T", "Verizon", "Comcast", "Dish"])
    	txnType = random.choice(["CREDIT", "DEBIT", "WIRE", "CHECK"])
    	st = time.time()
    	ts = datetime.datetime.fromtimestamp(st).strftime('%Y-%m-%d %H:%M:%S')
    	beneficiaryAcctNum = random.randint(100000, 999999)
    	data = ",".join([str(txnID), str(customerID), countryCode, ipAddress, isp, txnType, str(ts), str(beneficiaryAcctNum)])
        # writer.writerow( (txnID, customerID, countryCode, ipAddress, isp, txnType, st, beneficiaryAcctNum) )
        # need to append newline character for message demarcation
        sock.sendall(data + "\n")
        print data
        i = i + 1
        time.sleep(1)
finally:
    #f.close()
    sock.close()

#print open(sys.argv[1], 'rt').read()
