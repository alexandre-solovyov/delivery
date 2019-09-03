import requests;
from requests.auth import *

addr = "http://localhost:8080/delivery";

def user1():
    r = requests.post(addr + '/signup', data={
        "firstName": "Alexander",
        "lastName": "Solovyov",
        "parentName": "Gennadyevich",
        "date": "31/08/2019"
    });  # without authentification it does not work!
    print(r);
    q = r.json();
    print(q);

def user2():
    r = requests.post(addr + '/signup', data={
        "firstName": "Alexander",
        "lastName": "Solovyov",
        "parentName": "Gennadyevich",
        "date": "31/08/2019"
    }, auth=HTTPBasicAuth('alex', 'alex'));
    print(r);
    q = r.json();
    print(q);

def user3():
    r = requests.post(addr + '/signup', data={
        "firstName": "DNS",
        "lastName": " ",
        "parentName": " ",
        "date": "31/08/2019"
    }, auth=HTTPBasicAuth('dns', 'dns'));
    print(r);
    q = r.json();
    print(q);

def set_producer(login):
    s = requests.Session() 
    s.post(addr + '/signin', auth=HTTPBasicAuth('alex', 'alex'))
    r = s.post(addr + '/user/role', data={"userLogin": login, "newRole": "PRODUCER"})
    print(r, r.text);
    for item in r.json():
      print(item);    

def users():
    s = requests.Session() 
    s.post(addr + '/signin', auth=HTTPBasicAuth('alex', 'alex'))
    r = s.get(addr + '/users')
    print(r);
    for item in r.json():
      print(item);

#user2();
#user3();
#set_producer("dns")
users();
