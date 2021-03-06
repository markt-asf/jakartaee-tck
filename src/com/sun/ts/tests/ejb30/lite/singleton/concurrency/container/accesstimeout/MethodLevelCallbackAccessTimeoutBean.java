/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout;

import com.sun.ts.tests.ejb30.common.helper.Helper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.AccessTimeout;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

/**
 * The purpose of this class is to verify the wait time in post-construct method
 * does not count towards AccessTimeout. This bean is not annotated
 * with @Startup.
 * 
 * @AccessTimeout at method-level also applies to the PostConstruct,
 *                AroundInvoke and PreDestroy methods that are exposed as
 *                business methods.
 */
@Singleton
public class MethodLevelCallbackAccessTimeoutBean
    extends CallbackAccessTimeoutBeanBase {
  @Override
  @AccessTimeout(value = 1000)
  @Lock(LockType.READ)
  public int postConstructWait(int resultVal) {
    return super.postConstructWait(resultVal);
  }

  @Override
  @PostConstruct
  @AccessTimeout(value = 1000)
  @Lock(LockType.READ)
  public void postConstruct() {
    super.postConstruct();
  }

  @Override
  @PreDestroy
  @Lock(LockType.READ)
  @AccessTimeout(value = 1000)
  public void preDestroy() {
    super.preDestroy();
  }

  @Override
  @AroundInvoke
  @Lock(LockType.READ)
  @AccessTimeout(value = 1000)
  public Object intercept(InvocationContext inv) throws Exception {
    return super.intercept(inv);
  }

  protected void busyWait(long waitMillis, String methodName) {
    Helper.getLogger().fine("Waiting in " + methodName
        + ", but it should not affect AccessTimeout:" + waitMillis);
    Helper.busyWait(waitMillis);
  }

  protected static final long ACCESS_TIMEOUT_MILLIS = 1000;

  protected static final long AROUND_INVOKE_WAIT_MILLIS = 600;
}
