# Software required

### Java
1. Download here -> [Java](https://www.oracle.com/pl/java/technologies/javase/jdk11-archive-downloads.html) (JDK 11)
2. Setting Java variables in Windows [Tutorial](https://www.ibm.com/docs/en/b2b-integrator/5.2?topic=installation-setting-java-variables-in-windows)
3. Verify java installation.
```command
$ java -version
```


### Maven
1. Download here -> [Maven](https://maven.apache.org/install.html) Zip File and Extract
2. How to install Maven on Windows [tutorial](https://phoenixnap.com/kb/install-maven-windows)
3. Verify Maven installation.
```command
$ mvn -version
```
4. Reboot your system 



### Docker
1. Download here -> [Docker](https://docs.docker.com/get-docker/)
2. Open Terminal and run RabbitMQ server ( first '-p port' is for GUI plugin )
```docker
$ docker run -d --hostname rabbitmq-dpc --name rabbitmq-dpc -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```
3. Run MySQL server
```docker
$ docker run --name user-mysql-agent -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql
```


### MySQL Workbench
1. Download here -> [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

### ```optional``` IntelliJ IDEA
1. Download here -> [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) Community version (FREE)


# First configuration
### load database schema
1. Open MySQL Workbech and make connection with the SQL server
Hostname: ```127.0.0.1```
Username: ```root```
password: ```root```
Port: ```3306```

2. In ```top bar``` go to ```Server``` -> ```Data Import``` -> and check ```Import from Self-Contained File```. 
3. Press ```[...]``` button and select ```user-schema.sql``` file from repository
4. Click ```Start Import```


### Create queues for communication between applications
1. Go to RabbitMQ Management panel ```http://localhost:15672/```
2. Login with username: ```guest``` and password: ```guest``` 
3. Go to ```Gueue``` tab  
4. Go to ```Add a new queue``` section
5. in ```Name``` input text field , type "```agentPost```" and press ```Add queue```
6. then create another queue named "```backPost```"


### ```optional``` Changing the application properties 
If you have made changes to used ports, or login credentials, or you have another application that uses those ports. You need to update them in configuration files.
You will find these in ```<app name>\src\main\resources\application.properties```




# Run aplications
### Agent aplication
1. Open terminal and set used path to ```.\agent```
2. Type
```mvn
mvn spring-boot:run
```
Now agent aplication is running, dont close that window

### BackEnd application
1. Open new terminal and set used path to ```.\backDPC```
2. Type
```mvn
mvn spring-boot:run
```

# DONE
by default user generation is performed every 1 min,
you can change this in ```.\backDPC\src\main\resources\application.properties``` file
the value is in ms, so for exapmple if you want to create user for each 10 minuts, set the value for ```intervalUserCreation = 600000```

```info``` Sometimes jdbc freezes after a failed db insertion and takes about 5 minutes to reconnect (Shut down and turn on the server for testing), but if you execute an sql query on the server with MySQL Workbench it will work immediately