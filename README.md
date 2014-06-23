DocsReporter-web-sample
=======================

DocsReporter project web application using Spring MVC

For development stage execute:
```xml
mvn install -Dmaven.test.skip=true -Pprod
```
and then run Jetty:
```xml
mvn jetty:run-exploded
```


For production:
```xml
mvn install -Dmaven.test.skip=true -Pprod
```
After all deploy .war on any web container such as Tomcat

