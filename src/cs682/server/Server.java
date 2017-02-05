package cs682.server;

import cs682.concurrent.WorkQueue;
import cs682.server.annotations.Configuration;
import cs682.server.annotations.ConfigurationMethod;
import cs682.server.http.HttpJob;
import cs682.server.utils.LoadUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.List;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class Server extends Thread {
    private final int port;
    private final WorkQueue workQueue;

    public Server(Class serverClass, int port, int numberThreads, String... args) throws Exception {
        this.workQueue = new WorkQueue(numberThreads);
        this.port = port;
        Router.init(LoadUtil.getAllClasses(serverClass));
        configure(serverClass, args);
    }

    public Server(Class serverClass, int port, String... args) throws Exception {
        this.port = port;
        this.workQueue = new WorkQueue(2);
        Router.init(LoadUtil.getAllClasses(serverClass));
        configure(serverClass, args);
    }

    private void configure(Class serverClass, String[] args) throws Exception {
        List<Class> allClasses = LoadUtil.getAllClasses(serverClass);
        for (Class clazz : allClasses) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Object obj = clazz.newInstance();
                for (Method method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(ConfigurationMethod.class)) {
                        if (method.getParameterCount() > 0) {
                            method.invoke(obj, args[0]);
                        } else {
                            method.invoke(obj);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
            while (true) {
                this.workQueue.execute(new HttpJob(serverSocket.accept()));
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + this.port);
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
                this.workQueue.shutdown();
                this.workQueue.awaitTermination();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
