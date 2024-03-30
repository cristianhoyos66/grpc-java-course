package com.cristiancourse.greeting.server;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;

        var server = ServerBuilder.forPort(port)
                .addService(new GreetingService())
                .addService(new SumService())
                .addService(new AvgService())
                .addService(new MaxService())
                .addService(new SqrtService())
                .build();

        server.start();

        System.out.println("Server Started!!!");
        System.out.println("Listening on port: " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Server Stopped");
        }));

        server.awaitTermination();
    }

}
