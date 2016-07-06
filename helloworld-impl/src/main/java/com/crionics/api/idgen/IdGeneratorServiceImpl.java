/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.crionics.api.idgen;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class IdGeneratorServiceImpl implements IdGeneratorService {




    @Override
    public ServiceCall<NotUsed, String> generateId() {
        return (name) -> {



            return completedFuture("Hello "); };
    }

}