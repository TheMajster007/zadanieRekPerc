# Software required

### Java
[Java](https://www.java.com) is a programming language and computing platform first released by Sun Microsystems in 1995
1. Instal [Java](https://www.oracle.com/pl/java/technologies/javase/jdk11-archive-downloads.html) (JDK 11)
2. Setting Java variables in Windows [Tutorial](https://www.ibm.com/docs/en/b2b-integrator/5.2?topic=installation-setting-java-variables-in-windows)
3. Verify java installation.
```command
$ java -version
```


### Maven
[Apache Maven](https://maven.apache.org/) is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information. 

1. Download [Maven](https://maven.apache.org/install.html) Zip File and Extract
2. How to install Maven on Windows [tutorial](https://phoenixnap.com/kb/install-maven-windows)
4. Verify Maven installation.
```command
$ mvn -version
```

5. Reboot your system 



### Docker

[Docker](https://docs.docker.com) is a platform designed to help developers build, share, and run modern applications.

1. Install [Docker](https://docs.docker.com/get-docker/)
2. Open Terminal and run RabbitMQ server ( first '-p port' is for GUI plugin )
```docker
$ docker run -d --hostname rabbitmq-dpc --name rabbitmq-dpc -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```
3. Run MySQL server
```docker
$ docker run --name user-mysql-agent -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql
```


### MySQL Workbench
[MySQL Workbench](https://www.mysql.com/products/workbench/) is a unified visual tool for database architects, developers, and DBAs. MySQL Workbench provides data modeling, SQL development, and comprehensive administration tools for server configuration, user administration, backup, and much more.
1. Install [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

### ```optional``` IntelliJ IDEA
[IntelliJ IDEA](https://www.jetbrains.com/idea/) is an integrated development environment (IDE) written in Java for developing computer software written in Java, Kotlin, Groovy, and other JVM-based languages

1. Instal [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) Community version (FREE)


# First configuration
### load database schema
1. Open MySQL Workbech and make connection to the SQL server
```Hostname:``` 127.0.0.1
```Username:``` root
```password:``` root
```Port:``` 3306

2. In ```top bar``` go to ```Server``` -> ```Data Import``` -> and check ```Import from Self-Contained File```. 
3. Press ```[...]``` button and select ```user-schema.sql``` file
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
Now agent aplication is running, dont close the window

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