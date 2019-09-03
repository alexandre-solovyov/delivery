import requests;
from requests.auth import *

addr = "http://localhost:8080/delivery";

def upload():
    s = requests.Session() 
    s.post(addr + '/signin', auth=HTTPBasicAuth('alex', 'alex'))

    files={'files': open('../data/products1.xlsx','rb')}
    values={'upload_file': 'products1.xlsx', 'producerLogin': 'dns'}
    r = s.post(addr + '/product/upload', files=files, data=values)

    print(r, r.text);
    for item in r.json():
      print(item);

upload()
