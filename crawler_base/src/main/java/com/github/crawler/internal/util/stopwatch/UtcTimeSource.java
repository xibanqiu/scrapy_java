/*
 * Copyright 2019 Peter Bencze.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.crawler.internal.util.stopwatch;

import java.time.Instant;

/**
 * A source providing access to the current UTC instant.
 */
public final class UtcTimeSource implements TimeSource {

    /**
     * Returns the current instant from the system UTC clock.
     *
     * @return the current instant from the system UTC clock
     */
    @Override
    public Instant getTime() {
        return Instant.now();
    }
}
