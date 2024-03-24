package com.cristiancourse.greeting.server;

import com.cristiancourse.proto.avg.AvgReq;
import com.cristiancourse.proto.avg.AvgRes;
import com.cristiancourse.proto.avg.AvgServiceGrpc;
import io.grpc.stub.StreamObserver;

public class AvgService extends AvgServiceGrpc.AvgServiceImplBase {
    @Override
    public StreamObserver<AvgReq> avg(StreamObserver<AvgRes> responseObserver) {
        return new StreamObserver<>() {
            int numerator = 0;
            int counter = 0;
            @Override
            public void onNext(AvgReq req) {
                counter++;
                numerator += req.getNum();
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(AvgRes.newBuilder().setResult((double) numerator / counter).build());
                responseObserver.onCompleted();
            }
        };
    }
}
