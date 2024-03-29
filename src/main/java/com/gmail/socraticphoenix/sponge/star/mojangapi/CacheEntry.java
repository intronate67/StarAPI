/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 socraticphoenix@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Socratic_Phoenix (socraticphoenix@gmail.com)
 */
package com.gmail.socraticphoenix.sponge.star.mojangapi;

import com.gmail.socraticphoenix.plasma.file.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.plasma.file.jlsc.serialization.annotation.Serialize;

import java.util.concurrent.TimeUnit;

@Serializable
public class CacheEntry<T> {
    public static long EXPIRED_THRESHOLD = TimeUnit.HOURS.toMillis(6);

    @Serialize(name = "value")
    private T value;
    @Serialize(name = "timestamp")
    private long timestamp;

    public CacheEntry(T value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public CacheEntry(T value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public T getValue() {
        return this.value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() - this.getTimestamp() >= CacheEntry.EXPIRED_THRESHOLD);
    }

}
