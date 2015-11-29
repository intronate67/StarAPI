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
package com.gmail.socraticphoenix.sponge.star.minigame.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.spongepowered.api.util.Identifiable;

public class Cooldown implements Identifiable {
    private Map<UUID, Long> cooling;
    private UUID id;

    Cooldown(UUID id) {
        this.cooling = new HashMap<>();
        this.id = id;
    }

    public boolean startCooldown(Identifiable identifiable, long time, TimeUnit timeUnit) {
        boolean result = this.cooling.containsKey(identifiable.getUniqueId());
        this.cooling.putIfAbsent(identifiable.getUniqueId(), System.currentTimeMillis() + (TimeUnit.MILLISECONDS.convert(time, timeUnit)));
        return result;
    }

    public boolean startCooldown(Identifiable identifiable, long tickDuration) {
        boolean result = this.cooling.containsKey(identifiable.getUniqueId());
        this.cooling.putIfAbsent(identifiable.getUniqueId(), System.currentTimeMillis() + this.ticksToMilliseconds(tickDuration));
        return result;
    }

    private long ticksToMilliseconds(long ticks) {
        return TimeUnit.MILLISECONDS.convert(ticks * 20, TimeUnit.SECONDS);
    }

    public boolean isCooling(Identifiable identifiable) {
        this.refactor(identifiable.getUniqueId());
        return this.cooling.containsKey(identifiable.getUniqueId());
    }

    private void refactor(UUID id) {
        if (this.cooling.containsKey(id)) {
            long time = this.cooling.get(id);
            if (time < System.currentTimeMillis()) {
                this.cooling.remove(id);
            }
        }
    }

    public boolean remove(Identifiable identifiable) {
        boolean result = this.cooling.containsKey(identifiable.getUniqueId());
        this.cooling.remove(identifiable.getUniqueId());
        return result;
    }

    public long getRemainingTime(Identifiable identifiable, TimeUnit target) {
        return target.convert(this.getRemainingTime(identifiable), TimeUnit.MILLISECONDS);
    }

    public long getRemainingTime(Identifiable identifiable) {
        Long time = this.cooling.get(identifiable.getUniqueId());
        if (time != null) {
            return time - System.currentTimeMillis();
        } else {
            return -1;
        }
    }

    public String getFormattedTime(Identifiable identifiable) {
        long time = this.getRemainingTime(identifiable);
        if (time < 0) {
            return "0 minutes";
        } else {
            return String.format("%0d.%0d minutes", TimeUnit.MILLISECONDS.toMinutes(time), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        }
    }

    @Override
    public UUID getUniqueId() {
        return this.id;
    }
}
