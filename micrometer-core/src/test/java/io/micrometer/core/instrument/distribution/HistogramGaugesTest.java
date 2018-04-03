/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.instrument.distribution;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class HistogramGaugesTest {

    @Test
    void snapshotRollsOverAfterEveryPublish() {
        MeterRegistry registry = new SimpleMeterRegistry();

        Timer timer = Timer.builder("my.timer")
                .sla(Duration.ofMillis(1))
                .register(registry);

        HistogramGauges gauges = HistogramGauges.registerWithCommonFormat(timer, registry);

        timer.record(1, TimeUnit.MILLISECONDS);

        assertThat(registry.get("my.timer.histogram").gauge().value()).isEqualTo(1);
        assertThat(gauges.polledGaugesLatch.getCount()).isEqualTo(0);
    }
}