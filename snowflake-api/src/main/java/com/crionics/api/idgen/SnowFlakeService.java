/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.crionics.api.idgen;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

/**
 * The service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the HelloService.
 */
public interface SnowFlakeService extends Service {

  /**
   * curl http://localhost:9000/api/IdGenerator
   */
  ServiceCall<NotUsed, String> generateId();

  /**
   * curl http://localhost:9000/api/IdGenerator/5
   */
  ServiceCall<NotUsed, String[]> generateIds(int count);

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("snowflake").withCalls(
        pathCall("/api/IdGenerator",  this::generateId),
        pathCall("/api/IdGenerator/{count}",  this::generateIds)
      ).withAutoAcl(true);
    // @formatter:on
  }




}
