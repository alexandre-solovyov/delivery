import requests;
from requests.auth import *

addr = "http://localhost:8080/delivery";

def cart():
    s = requests.Session() 
    s.post(addr + '/signin', auth=HTTPBasicAuth('alex', 'alex'))
    r = s.post(addr + '/order/add', data={'code': '12345', 'quantity': '1'})

    print(r, r.text);
    for item in r.json():
      print(item);

def showcart():
    s = requests.Session() 
    s.post(addr + '/signin', auth=HTTPBasicAuth('alex', 'alex'))
    r = s.get(addr + '/order/cart')

    print(r, r.text);
    for item in r.json():
      print(item);

#cart()
showcart()
