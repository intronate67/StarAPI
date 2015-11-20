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
package com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.converters;

import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentList;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.StarArgumentValueConverter;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarArgumentTokenizer;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarKeyConsumer;
import java.util.Optional;

public class ListConverter extends StarArgumentValueConverter<StarArgumentList> {

    public ListConverter() {
        super(StarArgumentList.class);
    }

    @Override
    public StarArgumentList parse(String value) {
        StarArgumentTokenizer tokenizer = new StarArgumentTokenizer(value, new StarArgumentTokenizer.DefaultHandler(), new StarKeyConsumer());
        StarArgumentList list = new StarArgumentList();
        while (tokenizer.hasNext()) {
            Optional<StarArgumentValue> next = tokenizer.nextValue();
            if(next.isPresent()) {
                list.add(next.get());
            }
        }
        return list;
    }

    @Override
    public String write(StarArgumentList value) {
        return value.toString();
    }

    @Override
    public boolean canParse(String value) {
        return value.startsWith("[") && value.endsWith("]");
    }

}
