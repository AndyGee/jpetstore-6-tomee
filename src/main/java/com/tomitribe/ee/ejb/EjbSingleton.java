/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tomitribe.ee.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Singleton
@Startup
public class EjbSingleton {

    private Timer timer;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void postConstruct() {
        createTimer();
    }

    @PreDestroy
    public void preDestroy() {
        if (null != timer) {
            try {
                timer.cancel();
            } catch (final Exception e) {
                //Ignore
            }
        }
    }

    private void createTimer() {
        try {
            timer = this.timerService.createSingleActionTimer(5000, new TimerConfig("EjbSingleton.Timer", false));
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    @Timeout
    @Asynchronous
    public void programmaticTimeout(final Timer timer) {
        System.out.println("EjbSingleton doing some critical singleton work...");
        try {
            Thread.sleep(5000);
        } catch (final InterruptedException e) {
            return;
        }
        createTimer();
    }
}
