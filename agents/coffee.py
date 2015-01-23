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
	uid = randrange(1, 3)
        #t = int(time.time())
        #bro = ({'v':1,'uid':uid})
        #ret = {'bt':t, 'e':bro}
        
		ret = {'user_id': uid} 
		
        jsondump = json.dumps(ret) 
        out(jsondump)
        sys.stdout.flush()
        time.sleep(5)

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        log("Agent exit")
