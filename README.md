# people-microservice
A microservice to play around with some new frameworks and features

## authentication
The http basic auth credential is username "user" with password "prod"

api.html can be accessed as anonymous, everything else needs a successful basic auth.

## documentation
The current documentation can be found under /api.html if the microservice is running

Example:
http://${hostname}:8080/api.html

## configuration
Mandatory configurations to run the project

### settings.xml
The maven plugin needs the docker repository server configured inside the settings.xml in your local .m2 folder.

See https://dmp.fabric8.io/#registry and https://dmp.fabric8.io/#authentication 

The entry looks like this:

```xml
<server>
	<id>dockerhost:8082</id>
	<username>USERNAME</username>
	<password>PASSWORD</password>
</server>
```

Username and password for the registry can be found inside the wiki on the 'dockerhost' site

### hosts file
To be able to push to the registry with hostname 'dockerhost' you have to modify the hosts file from your os otherwise you will receive an error because the fabric8 plugin in combination with windows docker is not able to resolve the hostname via dns. 

The entry looks like this:

192.168.1.43 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; dockerhost