package cs682;

import cs682.server.Server;

public class Main {

    public static void main(String[] args) throws Exception {
        new Server(Main.class, 8080, 10).start();
    }
}
