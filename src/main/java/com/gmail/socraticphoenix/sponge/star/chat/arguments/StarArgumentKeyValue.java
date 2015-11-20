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

public class StarArgumentKeyValue {
    private String key;
    private StarArgumentValue value;

    public StarArgumentKeyValue(String key, Object value) {
        this(key, StarArgumentValue.of(value));
    }

    public StarArgumentKeyValue(String key, StarArgumentValue value) {
        this.key = key;
        this.value = value;
    }

    public static StarArgumentKeyValue parse(String key, String value) {
        return new StarArgumentKeyValue(key, StarArgumentValue.parse(value));
    }

    public void setValue(Object object) {
        this.value = StarArgumentValue.of(object);
    }

    public String getKey() {
        return this.key;
    }

    public StarArgumentValue getValue() {
        return this.value;
    }

    public void setValue(StarArgumentValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getKey()).append(":").append(StarArgumentValue.write(this.getValue().getValue().orElse(null))).toString();
    }

}
