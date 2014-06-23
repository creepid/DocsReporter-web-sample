DocsReporter-web-sample
=======================

DocsReporter project web application using Spring MVC

<b>For development</b> stage execute:
```xml
mvn install -Dmaven.test.skip=true -Pprod
```
and then run Jetty:
```xml
mvn jetty:run-exploded
```


<b>For production</b>:
```xml
mvn install -Dmaven.test.skip=true -Pprod
```
After all deploy .war on any web container such as Tomcat

