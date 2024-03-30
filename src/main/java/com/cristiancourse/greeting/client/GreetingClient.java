package com.cristiancourse.greeting.client;

import com.cristiancourse.proto.avg.AvgReq;
import com.cristiancourse.proto.avg.AvgRes;
import com.cristiancourse.proto.avg.AvgServiceGrpc;
import com.cristiancourse.proto.greeting.GreetingRequest;
import com.cristiancourse.proto.greeting.GreetingResponse;
import com.cristiancourse.proto.greeting.GreetingServiceGrpc;
import com.cristiancourse.proto.greeting.SumReq;
import com.cristiancourse.proto.greeting.SumRes;
import com.cristiancourse.proto.greeting.SumServiceGrpc;
import com.cristiancourse.proto.max.MaxReq;
import com.cristiancourse.proto.max.MaxRes;
import com.cristiancourse.proto.max.MaxServiceGrpc;
import com.cristiancourse.proto.sqrt.SqrtReq;
import com.cristiancourse.proto.sqrt.SqrtRes;
import com.cristiancourse.proto.sqrt.SqrtServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    private static void doGreet(ManagedChannel channel) {
        System.out.println("Enter doGreet");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse greetingResponse = stub.greet(GreetingRequest.newBuilder().setFirstName("Cristian").build());
        System.out.println("Greeting: " + greetingResponse.getResult());
    }

    private static void doGreetManyTimes(ManagedChannel channel) {
        System.out.println("Enter doGreetManyTimes");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        stub.greetManyTimes(GreetingRequest.newBuilder().setFirstName("Cristian").build())
                .forEachRemaining(greetingResponse -> System.out.println("Greeting: " + greetingResponse.getResult()));
    }

    private static void doLongGreet(ManagedChannel channel) throws InterruptedException {
        System.out.println("Enter doLongGreetTimes");
        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        var names = List.of("Pepito", "Perez", "Pepo", "Pancha");

        var stream = stub.longGreet(new StreamObserver<>() {
            @Override
            public void onNext(GreetingResponse response) {
                System.out.println(response.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });
        for (String name : names) {
            stream.onNext(GreetingRequest.newBuilder().setFirstName(name).build());
        }

        stream.onCompleted();
        latch.await(3, TimeUnit.SECONDS);
    }

    private static void doAvg(ManagedChannel channel) throws InterruptedException {
        System.out.println("Enter doAvg");
        AvgServiceGrpc.AvgServiceStub stub = AvgServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        var numbers = List.of(5, 10, 15, 20);

        var stream = stub.avg(new StreamObserver<>() {
            @Override
            public void onNext(AvgRes response) {
                System.out.println(response.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });
        for (Integer number : numbers) {
            stream.onNext(AvgReq.newBuilder().setNum(number).build());
        }

        stream.onCompleted();
        latch.await(3, TimeUnit.SECONDS);
    }

    private static void doGreetEveryone(ManagedChannel channel) throws InterruptedException {
        System.out.println("Enter doGreetEveryone");
        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        var names = List.of("Pepito", "Perez", "Pepo", "Pancha");

        var stream = stub.greetEveryone(new StreamObserver<>() {
            @Override
            public void onNext(GreetingResponse response) {
                System.out.println(response.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        for (String name : names) {
            stream.onNext(GreetingRequest.newBuilder().setFirstName(name).build());
        }

        stream.onCompleted();
        latch.await(3, TimeUnit.SECONDS);
    }

    private static void doMax(ManagedChannel channel) throws InterruptedException {
        System.out.println("Enter doMax");
        MaxServiceGrpc.MaxServiceStub stub = MaxServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        var nums = List.of(1, 5, 20, 2);

        var stream = stub.max(new StreamObserver<>() {
            @Override
            public void onNext(MaxRes response) {
                System.out.println(response.getMaxNumber());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        for (Integer num : nums) {
            stream.onNext(MaxReq.newBuilder().setNum(num).build());
        }

        stream.onCompleted();
        latch.await(3, TimeUnit.SECONDS);
    }

    private static void doSum(ManagedChannel channel) {
        System.out.println("Enter doSum");
        SumServiceGrpc.SumServiceBlockingStub stub = SumServiceGrpc.newBlockingStub(channel);
        SumRes sumRes = stub.sum(SumReq.newBuilder()
                .setNum1(13)
                .setNum2(5)
                .build());
        System.out.println("Summing: " + sumRes.getResult());
    }

    private static void doSqrt(ManagedChannel channel) {
        System.out.println("Enter doSqrt");
        SqrtServiceGrpc.SqrtServiceBlockingStub stub = SqrtServiceGrpc.newBlockingStub(channel);
        SqrtRes sqrtRes = stub.sqrt(SqrtReq.newBuilder().setNum(-5).build());
        System.out.println("Result: " + sqrtRes.getResult());
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Need one arg to work");
            return;
        }

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Shutting Down");

        switch (args[0]) {
            case "greet" -> doGreet(channel);
            case "sum" -> doSum(channel);
            case "greet_many_times" -> doGreetManyTimes(channel);
            case "long_greet" -> doLongGreet(channel);
            case "avg" -> doAvg(channel);
            case "greet_everyone" -> doGreetEveryone(channel);
            case "max" -> doMax(channel);
            case "sqrt" -> doSqrt(channel);
            default -> System.out.println("Keyword Invalid: " + args[0]);
        }

        channel.shutdown();

    }

}
