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

import com.gmail.socraticphoenix.plasma.file.asac.ASACException;
import com.gmail.socraticphoenix.plasma.file.asac.values.ASACValueConverter;


public class AsacArgumentValueConverter<T> extends StarArgumentValueConverter<T> {
    private ASACValueConverter<T> converter;

    public AsacArgumentValueConverter(ASACValueConverter<T> converter) {
        super(converter.getTarget());
        this.converter = converter;
    }

    @Override
    public T parse(String value) {
        try {
            return this.converter.convertFromString(value);
        } catch (ASACException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String write(T value) {
        try {
            return this.converter.convertToString(value);
        } catch (ASACException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean canParse(String value) {
        return this.converter.canConvertFromString(value);
    }

    @Override
    public boolean canWrite(Object object) {
        return this.converter.canConvertToString(object);
    }
}
