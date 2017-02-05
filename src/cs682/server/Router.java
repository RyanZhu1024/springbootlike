package cs682.server;

import cs682.server.annotations.RequestMapping;
import cs682.server.annotations.RestController;
import cs682.server.exceptions.NoPathMatchedException;
import cs682.server.http.HttpMethod;
import cs682.server.http.HttpReqKey;
import cs682.server.http.HttpRequest;
import cs682.server.http.HttpResponse;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class Router {
    private static final Router routerObj = null;
    private Map<HttpReqKey, Method> routers;
    private Map<String, Object> controllerMap;

    public HttpResponse run(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, NoPathMatchedException {
        String path = httpRequest.getPath();
        if (path == null || path.isEmpty() || path.equals("/")) {
            throw new NoPathMatchedException();
        }
        String controllerName = path.substring(path.indexOf("/") + 1);
        String methodName = "/";
        if (controllerName.indexOf("/") > 0) {
            methodName = controllerName.substring(controllerName.indexOf("/"));
            controllerName = controllerName.substring(0, controllerName.indexOf("/"));
        }
        if (controllerName.contains("?")) {
            controllerName = controllerName.substring(0, controllerName.indexOf("?"));
        }
        if (methodName.contains("?")) {
            methodName = methodName.substring(0, methodName.indexOf("?"));
        }
        Method method = routers.get(HttpReqKey.getHttpReqKeyInstance(controllerName + methodName, httpRequest.getHttpMethod()));
        Object controller = controllerMap.get(controllerName);
        if (method == null || controller == null) {
            throw new NoPathMatchedException();
        }
        return (HttpResponse) method.invoke(controller, httpRequest);

    }

    private Router(Map<HttpReqKey, Method> routers, Map<String, Object> controllerMap) {
        this.routers = routers;
        this.controllerMap = controllerMap;
    }

    public static Router getInstance() {
        return routerObj;
    }

    static void init(List<Class> classes) throws Exception {
        Map<String, Object> controllerMap = new HashMap<>();
        Map<HttpReqKey, Method> routers = new HashMap<>();
        for (Class aClass : classes) {
            if (aClass.isAnnotationPresent(RestController.class)) {
                Object controller = aClass.newInstance();
                String controllerName = controller.getClass().getAnnotation(RestController.class).value();
                System.out.println("loading controller " + controller.getClass().getName());
                controllerMap.put(controllerName, controller);

                for (Method method : controller.getClass().getMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        String path = method.getAnnotation(RequestMapping.class).value();
                        HttpMethod httpMethod = method.getAnnotation(RequestMapping.class).method();
                        HttpReqKey httpReqKey = HttpReqKey.getHttpReqKeyInstance(controllerName + path, httpMethod);
                        System.out.println("loading method " + method.getName());
                        routers.put(httpReqKey, method);
                    }
                }
            }
        }
        //init router
        Constructor constructor = Router.class.getDeclaredConstructor(Map.class, Map.class);
        constructor.setAccessible(true);
        Router router = (Router) constructor.newInstance(routers, controllerMap);

        Field routerObj = Router.class.getDeclaredField("routerObj");
        routerObj.setAccessible(true);
        Field modifiers = routerObj.getClass().getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(routerObj, routerObj.getModifiers() & ~Modifier.FINAL);
        routerObj.set(Router.class, router);
        System.out.println(Router.getInstance());
    }

}
