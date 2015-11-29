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
package com.gmail.socraticphoenix.sponge.star.chat.conversation;

import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifier;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifiers;
import java.util.concurrent.TimeUnit;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

public abstract class Prompt implements Promptcessor {
    public static final Prompt END = new End();

    public abstract Verifier getVerifier();

    public abstract Text getMessage();

    public Promptcessor getProcessor() {
        return this;
    }

    private static class End extends Prompt {

        @Override
        public Verifier getVerifier() {
            return null;
        }

        @Override
        public Text getMessage() {
            return null;
        }

        @Override
        public Prompt process(StarArgumentValue keyValue, Conversation conversation) {
            return null;
        }

    }

    public static class Delay extends Prompt {
        private Prompt next;
        private long time;
        private TimeUnit unit;
        private boolean isTicks;

        public Delay(Prompt next, long time, TimeUnit timeUnit) {
            this.next = next;
            this.time = time;
            this.unit = timeUnit;
            this.isTicks = false;
        }

        public Delay(Prompt next, long ticks) {
            this.next = next;
            this.time = ticks;
            this.isTicks = true;
        }

        @Override
        public Verifier getVerifier() {
            return Verifiers.alwaysSuccessful();
        }

        @Override
        public Text getMessage() {
            return Texts.of();
        }

        @Override
        public Prompt process(StarArgumentValue value, Conversation conversation) {
            return this.next;
        }

        public Prompt getNext() {
            return next;
        }

        public long getTime() {
            return time;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public boolean isTicks() {
            return isTicks;
        }
    }

    public static class Message extends Prompt {
        private Text message;
        private Prompt next;

        public Message(Text message, Prompt next) {
            this.message = message;
            this.next = next;
        }

        @Override
        public Text getMessage() {
            return this.message;
        }

        @Override
        public Prompt process(StarArgumentValue keyValue, Conversation conversation) {
            return this.next;
        }

        @Override
        public Verifier getVerifier() {
            return Verifiers.alwaysSuccessful();
        }
    }



}
