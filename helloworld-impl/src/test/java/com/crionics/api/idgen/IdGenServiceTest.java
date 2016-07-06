/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.crionics.api.idgen;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IdGenServiceTest {

  @Test
  public void shouldStorePersonalizedGreeting() throws Exception {
    withServer(defaultSetup(), server -> {
      IdGeneratorService service = server.client(IdGeneratorService.class);

      String msg1 = service.generateId().invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hello, Alice!", msg1); // default greeting

      String msg2 = service.generateId().invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hello, Bob!", msg2); // default greeting
    });
  }

}
