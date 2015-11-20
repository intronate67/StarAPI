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
package com.gmail.socraticphoenix.sponge.star.chat.arguments.parse;

import java.util.Optional;

public class StarCharacterStream {
    private char[] stream;
    private int index;

    public StarCharacterStream(String... toStream) {
        String stream = "";
        for(String str : toStream) {
            stream = stream.concat(str);
        }
        this.stream = stream.toCharArray();
        this.index = 0;
    }

    public StarCharacterStream(char[] toStream) {
        this.stream = toStream;
        this.index = 0;
    }

    public Optional<Character> peek() {
        Optional<Character> character = this.next();
        this.back();
        return character;
    }

    public void back() {
        this.index--;
    }

    public boolean hasNext() {
        return this.index < this.stream.length;
    }

    public Optional<Character> next() {
        if(this.hasNext()) {
            char val = this.stream[this.index];
            this.index++;
            return Optional.of(val);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Character> nextExcludeWhitespace() {
        if(this.hasNext()) {
            char next = this.next().get();
            if(this.isWhitespace(next)) {
                return this.nextExcludeWhitespace();
            } else {
                return Optional.of(next);
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<Character> nextSkipEscaped() {
        if(this.hasNext()) {
            char next = this.next().get();
            if(next == '\\') {
                this.next();
                return this.next();
            } else {
                return Optional.of(next);
            }
        } else {
            return Optional.empty();
        }
    }

    public int index() {
        return this.index;
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\0';
    }
}
