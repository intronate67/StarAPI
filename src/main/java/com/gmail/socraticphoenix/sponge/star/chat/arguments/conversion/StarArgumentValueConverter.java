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
package com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion;

import com.gmail.socraticphoenix.plasma.collection.HashString;
import com.gmail.socraticphoenix.plasma.constant.Constant;
import com.gmail.socraticphoenix.plasma.file.asac.values.ASACValueConverter;
import com.gmail.socraticphoenix.plasma.file.json.JSONObject;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.converters.IntegerConverter;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.converters.ListConverter;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentList;

public abstract class StarArgumentValueConverter<T> extends Constant {
    public static final StarArgumentValueConverter<String> STRING_CONVERTER = new AsacArgumentValueConverter<>(ASACValueConverter.STRING_CONVERTER);
    public static final StarArgumentValueConverter<Object> NULL_CONVERTER = new AsacArgumentValueConverter<>(ASACValueConverter.NULL_CONVERTER);
    public static final StarArgumentValueConverter<Integer> INTEGER_CONVERTER = new IntegerConverter();
    public static final StarArgumentValueConverter<Boolean> BOOLEAN_CONVERTER = new AsacArgumentValueConverter<>(ASACValueConverter.BOOLEAN_CONVERTER);
    public static final StarArgumentValueConverter<Double> DOUBLE_CONVERTER = new AsacArgumentValueConverter<>(ASACValueConverter.DOUBLE_CONVERTER);
    public static final StarArgumentValueConverter<JSONObject> JSON_CONVERTER = new AsacArgumentValueConverter<>(ASACValueConverter.JSON_CONVERTER);
    public static final StarArgumentValueConverter<StarArgumentList> LIST_CONVERTER = new ListConverter();
    public static final StarArgumentValueConverter<HashString> HASH_STRING_CONVERTER = new AsacArgumentValueConverter<>(ASACValueConverter.HASH_STRING_CONVERTER);

    private Class<T> type;

    public StarArgumentValueConverter(Class<T> type) {
        super(type.getName());
        this.type = type;
    }

    public static void init(){} //Serves the purpose of initializing the class, and as such, all its public static final variables.

    public abstract T parse(String value);
    public abstract String write(T value);
    public abstract boolean canParse(String value);

    public boolean canWrite(Object value) {
        return this.type.isInstance(value);
    }

    public Class<T> getType() {
        return this.type;
    }
}
