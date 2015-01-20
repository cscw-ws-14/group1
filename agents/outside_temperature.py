#!/usr/bin/python
import time
import sys
import json
try:
    import urllib.request as urllib2
except ImportError:
    import urllib2

def log(msg):
    sys.stderr.write("[%s]: %s\n" % (__file__, msg))

def out(msg): 
    sys.stdout.write("%s\n" % json.dumps(msg))
  
def main(): 
    if len(sys.argv) < 3:
        #raise Exception("please put city and the country")
        city = "bonn"
        country = "germany"
    else:
        city = sys.argv[1]
        country = sys.argv[2]
    url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&units=metric"
    cache_temperature = 0
    
    while(True):   
        t = int(time.time())
        try: 
            resp = urllib2.urlopen(url)
            str_result = resp.read()
            str_result = str_result.decode("utf-8")
            decoded = json.loads(str_result) 
            cache_temperature = decoded['main']['temp']
             
            bro = {"v":cache_temperature,"u":"degC"}
            ret = {"bt":t, "e":bro} 
        except ValueError:
            ret = {"bt":t, "e":"error"}
            
        out(ret)
        sys.stdout.flush()
        time.sleep(0.1)

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        log("Agent exit")