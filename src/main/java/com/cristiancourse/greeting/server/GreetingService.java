package com.cristiancourse.greeting.server;

import com.cristiancourse.proto.greeting.GreetingRequest;
import com.cristiancourse.proto.greeting.GreetingResponse;
import com.cristiancourse.proto.greeting.GreetingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class GreetingService extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        responseObserver.onNext(GreetingResponse.newBuilder().setResult("Hello World " + request.getFirstName()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        GreetingResponse response = GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build();

        for (int i = 0; i < 10; i++) {
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreetingRequest> longGreet(StreamObserver<GreetingResponse> responseObserver) {
        StringBuilder sb = new StringBuilder();

        return new StreamObserver<>() {
            @Override
            public void onNext(GreetingRequest request) {
                sb.append("Hello ");
                sb.append(request.getFirstName());
                sb.append("!\n");
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(GreetingResponse.newBuilder().setResult(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GreetingRequest> greetEveryone(StreamObserver<GreetingResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(GreetingRequest request) {
                responseObserver.onNext(GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build());
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
