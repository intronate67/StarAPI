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

import com.gmail.socraticphoenix.plasma.collection.HashString;
import com.gmail.socraticphoenix.plasma.constant.Constant;
import com.gmail.socraticphoenix.plasma.file.json.JSONObject;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.StarArgumentValueConverter;
import java.util.List;
import java.util.Optional;

public class StarArgumentValue {
    public static final Unconverted UNCONVERTED_VALUE = new Unconverted();

    private Object value;


    public Optional<Object> getValue() {
        return Optional.ofNullable(this.value);
    }

    public Optional<Integer> getAsInteger() {
        return this.getAs(Integer.class);
    }

    public Optional<String> getAsString() {
        return this.getAs(String.class);
    }

    public Optional<Boolean> getAsBoolean() {
        return this.getAs(Boolean.class);
    }

    public Optional<StarArgumentList> getAsList() {
        return this.getAs(StarArgumentList.class);
    }

    public Optional<Double> getAsDouble() {
        return this.getAs(Double.class);
    }

    public Optional<JSONObject> getAsJson() {
        return this.getAs(JSONObject.class);
    }

    public Optional<HashString> getAsHashString() {
        return this.getAs(HashString.class);
    }

    public boolean isUnconverted() {
        return this == StarArgumentValue.UNCONVERTED_VALUE;
    }

    public <T> Optional<T> getAs(Class<T> type ){
        if(type.isInstance(this.value)) {
            return Optional.of(type.cast(this.value));
        } else {
            return Optional.empty();
        }
    }

    public static StarArgumentValue of(Object object) {
        StarArgumentValue value = new StarArgumentValue();
        value.value = object;
        return value;
    }

    public static String write(Object object) {
        Optional<List<Constant>> constantList = Constant.values(StarArgumentValueConverter.class);
        if(constantList.isPresent()) {
            for(Constant constant : constantList.get()) {
                StarArgumentValueConverter converter = (StarArgumentValueConverter) constant;
                if(converter.canWrite(object)) {
                    return converter.write(object);
                }
            }
        }

        return String.valueOf(object);
    }

    public static StarArgumentValue parse(String string) {
        Optional<List<Constant>> constantList = Constant.values(StarArgumentValueConverter.class);
        if(constantList.isPresent()) {
            for(Constant constant : constantList.get()) {
                StarArgumentValueConverter converter = (StarArgumentValueConverter) constant;
                if(converter.canParse(string)) {
                    return StarArgumentValue.of(converter.parse(string));
                }
            }
        }

        return StarArgumentValue.of(string);
    }

    public static class Unconverted extends StarArgumentValue {

    }
}
