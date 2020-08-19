package ch1.server2;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by crownus on 30/10/2016.
 */
public class Main {
    private static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Handler());
        server.createContext("/count", new Counter());
        server.createContext("/lissajous", new Lissajous());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            count.incrementAndGet();
            String response = String.format("getRequestURI().getPath() = %s\n", exchange.getRequestURI().getPath());
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class Counter implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = String.format("Count %d\n", count.intValue());
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class Lissajous implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, 0);
            try {
                ch1.lissajous.Main.lissajous(os);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                os.close();
            }
        }
    }
}
