import requests;

addr = "http://localhost:8080/delivery-0.1.0";

def products():
    r = requests.get(addr + '/products', auth=('user', 'pass'));
    for item in r.json():
        print(item);

products();
