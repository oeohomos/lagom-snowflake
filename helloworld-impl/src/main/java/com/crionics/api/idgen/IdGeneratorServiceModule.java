/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.crionics.api.idgen;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the service so that it can be served.
 */
public class IdGeneratorServiceModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {

        bindServices(serviceBinding(IdGeneratorService.class, IdGeneratorServiceImpl.class));
    }
}
