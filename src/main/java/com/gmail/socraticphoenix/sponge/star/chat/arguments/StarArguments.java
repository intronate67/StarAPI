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
package com.gmail.socraticphoenix.sponge.star.chat.arguments;

import com.gmail.socraticphoenix.plasma.collection.PlasmaListUtil;
import java.util.*;
import java.util.function.Consumer;

public class StarArguments implements Iterable<StarArgumentKeyValue> {
    private List<StarArgumentKeyValue> keyValues;

    public StarArguments(StarArgumentKeyValue... values) {
        this(PlasmaListUtil.buildList(values));
    }

    public StarArguments(List<StarArgumentKeyValue> values) {
        this.keyValues = values;
    }

    public StarArguments() {
        this(new ArrayList<>());
    }

    public int size() {
        return this.keyValues.size();
    }

    public void add(StarArgumentKeyValue keyValue) {
        this.put(keyValue.getKey(), keyValue.getValue().getValue().orElse(null));
    }

    public void put(String key, Object value) {
        for (StarArgumentKeyValue keyValue : this.keyValues) {
            if (keyValue.getKey().equals(key)) {
                keyValue.setValue(value);
                break;
            }
        }

        this.keyValues.add(new StarArgumentKeyValue(key, value));
    }

    public void remove(String key) {
        for (int i = 0; i < this.keyValues.size(); i++) {
            if (key.equals(this.keyValues.get(i).getKey())) {
                this.keyValues.remove(i);
                break;
            }
        }
    }

    public boolean containsKey(String key) {
        for (int i = 0; i < this.keyValues.size(); i++) {
            if (key.equals(this.keyValues.get(i).getKey())) {
                this.keyValues.remove(i);
                return true;
            }
        }
        return false;
    }

    public Optional<StarArgumentValue> get(String key) {
        for (StarArgumentKeyValue keyValue : this.keyValues) {
            if(keyValue.getKey().equals(key)) {
                return Optional.of(keyValue.getValue());
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < this.keyValues.size(); i++) {
            StarArgumentKeyValue keyValue = this.keyValues.get(i);
            string.append(keyValue.getKey()).append(":").append(StarArgumentValue.write(keyValue.getValue().getValue().orElse("null")));
            if(i < this.keyValues.size() - 1) {
                string.append(", ");
            }
        }
        return string.toString();
    }

    @Override
    public Iterator<StarArgumentKeyValue> iterator() {
        return this.keyValues.iterator();
    }

    @Override
    public void forEach(Consumer<? super StarArgumentKeyValue> action) {
        this.keyValues.forEach(action);
    }

    @Override
    public Spliterator<StarArgumentKeyValue> spliterator() {
        return this.keyValues.spliterator();
    }
}
