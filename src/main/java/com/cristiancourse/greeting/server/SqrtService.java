package com.cristiancourse.greeting.server;


import com.cristiancourse.proto.sqrt.SqrtReq;
import com.cristiancourse.proto.sqrt.SqrtRes;
import com.cristiancourse.proto.sqrt.SqrtServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class SqrtService extends SqrtServiceGrpc.SqrtServiceImplBase {
    @Override
    public void sqrt(SqrtReq request, StreamObserver<SqrtRes> responseObserver) {
        var number = request.getNum();

        if (number < 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("The number being sent cannot be negative")
                    .augmentDescription("Number: " + number)
                    .asRuntimeException());
            return;
        }

        responseObserver.onNext(SqrtRes.newBuilder()
                .setResult(Math.sqrt(number))
                .build());
        responseObserver.onCompleted();
    }
}
