#!/usr/bin/python
import time
from random import randrange
import sys

def log(msg):
    sys.stderr.write("[%s]: %s\n" % (__file__, msg))

def out(msg): 
    sys.stdout.write("%s\n" % msg)
  
def main():
    while(True):
        v = randrange(1,2500)
        t = int(time.time())
        ret = "{\"e\":[{\"n\":\"IAQ\",\"v\":%d,\"u\":\"VOC\",\"t\":%d}]}" % (v,t)

        out(ret)
        sys.stdout.flush()
        time.sleep(5)

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        log("Agent exit")
