package cs682.server.utils;

import java.io.File;
import java.util.List;

/**
 * Created by RyanZhu on 10/14/15.
 */
public class FileUtil {
    static void recursiveScan(String packageName, File file, List<Class> classes) throws ClassNotFoundException {
        if (file.isDirectory()) {
            System.out.println("scan directory " + file.getName());
            File[] files = file.listFiles();
            for (File f1 : files) {
                if (f1.isDirectory()) {
                    recursiveScan(packageName + "." + f1.getName(), f1, classes);
                } else {
                    recursiveScan(packageName, f1, classes);
                }
            }
        } else {
            System.out.println("scan file " + file.getName());
            if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().replace(".class", "")));
            }
        }
    }
}
