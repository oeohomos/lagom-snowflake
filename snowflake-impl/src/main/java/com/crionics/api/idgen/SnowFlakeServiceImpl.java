/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.crionics.api.idgen;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class SnowFlakeServiceImpl implements SnowFlakeService {

    public static DistributedIdGenerator idGen = DistributedIdGenerator.getInstance();

    @Override
    public ServiceCall<NotUsed, String> generateId() {
        return (x) -> completedFuture(idGen.next());
    }


    @Override
    public ServiceCall<NotUsed, String[]> generateIds(int count) {
        return (x) -> completedFuture(idGen.next(count));
    }

}