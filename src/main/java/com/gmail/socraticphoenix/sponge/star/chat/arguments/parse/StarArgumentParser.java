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

import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArguments;
import java.util.Optional;

public class StarArgumentParser {

    public static StarArguments parse(String args, StarArgumentTokenizer.Handler handler, StarKeyConsumer consumer) {
        StarArgumentTokenizer tokenizer = new StarArgumentTokenizer(args, handler, consumer);
        StarArguments arguments = new StarArguments();
        while (tokenizer.hasNext()) {
            Optional<StarArgumentKeyValue> keyValueOptional = tokenizer.nextKeyValue();
            if(keyValueOptional.isPresent()) {
                arguments.add(keyValueOptional.get());
            }
        }

        return arguments;
    }

    public static int lengthOf(String args, StarArgumentTokenizer.Handler handler) {
        StarArgumentTokenizer tokenizer = new StarArgumentTokenizer(args, handler, null);
        tokenizer.consumeBreaks();
        int length = 0;
        while (tokenizer.hasNext()) {
            Optional<String> arg = tokenizer.nextUntilBreak();
            if(arg.isPresent()) {
                length = length + 1;
                tokenizer.consumeBreaks();
            }
        }

        return length;
    }

}
