import requests;
from requests.auth import *

addr = "http://localhost:8080/delivery-0.1.0";

def user1():
    r = requests.post(addr + '/signup', data={
        "firstName": "Alexander",
        "lastName": "Solovyov",
        "parentName": "Gennadyevich",
        "date": "31/08/2019"
    }, auth=HTTPBasicAuth('alex', 'alex'));
    print(r.json());

def users():
    r = requests.get(addr + '/users', data={}, auth=('user', 'pass'));
    for item in r.json():
        print(item);

user1();
#users();
