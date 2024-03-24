package com.cristiancourse.greeting.server;

import com.cristiancourse.proto.max.MaxReq;
import com.cristiancourse.proto.max.MaxRes;
import com.cristiancourse.proto.max.MaxServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class MaxService extends MaxServiceGrpc.MaxServiceImplBase {

    @Override
    public StreamObserver<MaxReq> max(StreamObserver<MaxRes> responseObserver) {
        return new StreamObserver<>() {
            List<Integer> numList = new ArrayList<>();

            @Override
            public void onNext(MaxReq req) {
                numList.add(req.getNum());
                responseObserver.onNext(MaxRes.newBuilder()
                        .setMaxNumber(numList.stream().max(Integer::compareTo).get())
                        .build());
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
