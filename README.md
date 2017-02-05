#Java Rest-api Framework from CS682
##Instructions
1. download the source code and build an artifact
2. include the jar into your project as a dependency
3. you are on your way to build a restful api

##Examples
###Main class
**Has to be in the root package**
```java
public class Main {
    public static void main(String[] args) throws Exception {
        new Server(Main.class, port-number, thread-number).start();
    }
}
```
###Controllers
```java
@RestController("test")
public class TestController {
    @RequestMapping(value = "/", method = HttpMethod.GET)
    public HttpResponse test(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setBody("123");
        return httpResponse;
    }
}
```
###Configurations
```java
@Configuration
public class Configuration {
    @ConfigurationMethod
    public void configure() {
        System.out.println("hello world");
    }
}
```