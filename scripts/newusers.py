import requests;

addr = "http://localhost:8080/delivery-0.1.0";

def user1():
    r = requests.post(addr + '/newuser', data={
        "login": "alex", 
        "password": "alex",
        "firstName": "Alexander",
        "lastName": "Solovyov",
        "parentName": "Gennadyevich",
        "date": "31/08/2019",
        "role": "admin"
    }, auth=('user', 'pass'));
    print(r.json());

def user2():
    r = requests.post(addr + '/newuser', data={
        "login": "dns", 
        "password": "dns",
        "firstName": "DNS",
        "lastName": "company",
        "parentName": " ",
        "date": "25/08/2019",
        "role": "producer"
    }, auth=('user', 'pass'));
    print(r.json());

def user3():
    r = requests.post(addr + '/newuser', data={
        "login": "test", 
        "password": "test",
        "firstName": " ",
        "lastName": " ",
        "parentName": " ",
        "date": "20/08/2019",
        "role": " "
    }, auth=('user', 'pass'));
    print(r.json());


def users():
    r = requests.get(addr + '/users', data={}, auth=('user', 'pass'));
    for item in r.json():
        print(item);


#user1();
#user2();
#user3();
users();


