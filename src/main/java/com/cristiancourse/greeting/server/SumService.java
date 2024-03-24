package com.cristiancourse.greeting.server;

import com.cristiancourse.proto.greeting.SumReq;
import com.cristiancourse.proto.greeting.SumRes;
import com.cristiancourse.proto.greeting.SumServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SumService extends SumServiceGrpc.SumServiceImplBase {

    @Override
    public void sum(SumReq request, StreamObserver<SumRes> responseObserver) {
        var result = request.getNum1() + request.getNum2();
        responseObserver.onNext(SumRes.newBuilder().setResult(result).build());
        responseObserver.onCompleted();
    }
}
