#!/usr/bin/python
import time
from random import randrange
import sys
from array import array
import json

def log(msg):
    sys.stderr.write("[%s]: %s\n" % (__file__, msg))

def out(msg): 
    sys.stdout.write("%s\n" % msg)
  
def main(): 
    while(True):  
        randV = randrange(1, 100)
        t = int(time.time())
        bro = ({'v':randV,'u':"degC",'n': "http://cscw-bplus-04/bmp180/temperature"})
        ret = {'bt':t, 'e':bro}
         
        jsondump = json.dumps(ret) 
        out(jsondump)
        sys.stdout.flush()
        time.sleep(1)

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        log("Agent exit")