package cs682.server.utils;

import cs682.server.Server;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by RyanZhu on 10/14/15.
 */
public class LoadUtil {

    private static List<Class> allClasses = null;

    public synchronized static List<Class> getAllClasses(Class serverClass) throws Exception {
        if (allClasses == null) {
            allClasses = loadApplicationContext(serverClass);
        }
        return allClasses;
    }

    private LoadUtil() {
    }

    private static List<Class> loadApplicationContext(Class serverClass) throws Exception {
        List<Class> classes = new LinkedList<>();
        ClassLoader classLoader = serverClass.getClassLoader();
        String packageName = serverClass.getPackage().getName();
        String packageNameSlash = packageName.replace(".", "/");
        URL packageUrl = classLoader.getResource(packageNameSlash);
        if (packageUrl.getProtocol().equals("jar")) {
            CodeSource src = Server.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry zipEntry = zip.getNextEntry();
                    if (zipEntry == null) {
                        break;
                    } else {
                        if (zipEntry.getName().startsWith(packageNameSlash) && zipEntry.getName().endsWith(".class")) {
                            System.out.println(zipEntry.getName());
                            classes.add(Class.forName(zipEntry.getName().replace(".class", "").replace("/", ".")));
                        }
                    }
                }
            }
        } else {
            Enumeration<URL> resources = classLoader.getResources(packageNameSlash);
            while (resources.hasMoreElements()) {
                File file = new File(resources.nextElement().getFile());
                FileUtil.recursiveScan(packageName, file, classes);
            }
        }
        return classes;
    }
}
