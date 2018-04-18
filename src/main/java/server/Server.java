package server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.ControllerAdmin;
import dao.*;
import server.sessions.ISessionManager;
import server.sessions.SessionManager;
import view.ViewAdmin;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server implements IServer {


    private final int PORT;

    private final long MAX_SESSION_DURATION = 300000;  // in milliseconds
    private final ISessionManager sessionManager = SessionManager.create(MAX_SESSION_DURATION);

    private Server(int port){
        PORT = port;
    }

    public static IServer getInstance(int port){
        return new Server(port);
    }

    @Override
    public void run() throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/static", createStaticHandler());
        server.createContext("/", createLoginHandler());
        server.createContext("/admin", createAdminHandler());

        // set routes
        server.setExecutor(null); // creates a default executor
        // start listening
        server.start();
    }


    // initialize objects

    private HttpHandler createStaticHandler() {
        return Static.create();
    }

    private HttpHandler createLoginHandler() {
        return Login.create(new DaoLogin(), sessionManager);
    }

    private HttpHandler createAdminHandler() {
        ControllerAdmin controller = ControllerAdmin
                .createController(new ViewAdmin(), new DaoMentor(), new DaoClass(), new DaoLevel());
        return AdminHandler.create(sessionManager, controller);
    }
}