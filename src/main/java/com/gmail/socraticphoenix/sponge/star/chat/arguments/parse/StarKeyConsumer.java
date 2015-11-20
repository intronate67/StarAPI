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

import com.gmail.socraticphoenix.plasma.collection.PlasmaListUtil;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class StarKeyConsumer {
    private Queue<String> stringQueue;

    public StarKeyConsumer(List<String> keys) {
        this.stringQueue = new LinkedTransferQueue<>();
        this.stringQueue.addAll(keys);
    }

    public StarKeyConsumer(String... keys){
        this(PlasmaListUtil.buildList(keys));
    }

    public int size() {
        return this.stringQueue.size();
    }

    public String consume() {
        return this.stringQueue.poll();
    }

    public String peek() {
        return this.stringQueue.peek();
    }

    public boolean hasNext() {
        return !this.stringQueue.isEmpty();
    }

}