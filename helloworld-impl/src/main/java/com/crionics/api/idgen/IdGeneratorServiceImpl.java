/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.crionics.api.idgen;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class IdGeneratorServiceImpl implements IdGeneratorService {

    public static DistributedIdGenerator idGen = DistributedIdGenerator.getInstance();

    @Override
    public ServiceCall<NotUsed, String> generateId() {
        return (x) -> completedFuture(idGen.next());
    }

}