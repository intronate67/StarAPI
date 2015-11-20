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

import com.gmail.socraticphoenix.plasma.string.PlasmaStringUtil;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import java.util.Optional;

public class StarArgumentTokenizer {
    private StarCharacterStream args;
    private StarArgumentTokenizer.Handler handler;
    private StarKeyConsumer consumer;
    private boolean first = true;
    private boolean usingMapping = false;

    public StarArgumentTokenizer(String arguments, StarArgumentTokenizer.Handler handler, StarKeyConsumer consumer) {
        this.args = new StarCharacterStream(arguments);
        this.handler = handler;
        this.consumer = consumer;
    }

    public Optional<StarArgumentValue> nextValue() {
        Optional<String> valueString = this.nextUntilBreak();
        this.consumeBreaks();
        if (valueString.isPresent()) {
            String value = PlasmaStringUtil.removeTrailingSpaces(valueString.get());
            return Optional.of(StarArgumentValue.parse(value));
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> nextUntilBreak() {
        if (this.hasNext()) {
            StringBuilder builder = new StringBuilder();
            Optional<Character> next = this.next();
            int brackets = 0;
            int brackets2 = 0;
            int brackets3 = 0;
            boolean quotes = false;
            boolean escape = false;
            while (true) {
                if (!next.isPresent()) {
                    if (brackets != 0 || brackets2 != 0 || brackets3 != 0) {
                        this.handler.handleError(this, "Unbalanced brackets");
                    }
                    break;
                }
                char c = next.get();
                switch (c) {
                    case '"': {
                        quotes = escape ? quotes : !quotes;
                        break;
                    }
                    case '\\': {
                        escape = !escape;
                        break;
                    }
                    case '(': {
                        brackets = quotes ? brackets : brackets + 1;
                        break;
                    }
                    case ')': {
                        brackets = quotes ? brackets : brackets - 1;
                        break;
                    }
                    case '{': {
                        brackets2 = quotes ? brackets2 : brackets2 + 1;
                        break;
                    }
                    case '}': {


                        brackets2 = quotes ? brackets2 : brackets2 - 1;
                        break;
                    }
                    case '[': {
                        brackets3 = quotes ? brackets3 : brackets3 + 1;
                        break;
                    }
                    case ']': {
                        brackets3 = quotes ? brackets3 : brackets3 - 1;
                        break;
                    }
                }

                if (c != '\\') {
                    escape = false;
                }

                if (brackets == 0 && brackets2 == 0 && brackets3 == 0 && (next.get() == ' ' || next.get() == ',') && !quotes) {
                    break;
                } else {
                    builder.append(c);
                }

                next = this.next();
            }

            return Optional.of(builder.toString());
        }

        return Optional.empty();
    }

    public void consumeBreaks() {
        if(this.hasNext()) {
            Optional<Character> character = this.next();
            while (character.isPresent() && (character.get() == ' ' || character.get() == ',')) {
                character = this.next();
            }
            this.args.back();
        }
    }

    public boolean hasNext() {
        return this.args.hasNext();
    }

    public Optional<Character> next() {
        return this.args.next();
    }

    public Optional<StarArgumentKeyValue> nextKeyValue() {
        Optional<String> keyValueString = this.nextUntilBreak();
        this.consumeBreaks();
        if (keyValueString.isPresent()) {
            String keyValue = PlasmaStringUtil.removeTrailingSpaces(keyValueString.get());
            if (this.containsNotLiteral(keyValue, ':')) {
                if (this.first) {
                    this.usingMapping = true;
                    this.first = false;
                } else if (!this.usingMapping) {
                    this.handler.handleError(this, "Found values applicable for mapping pattern mixed with listing pattern.");
                    return Optional.empty();
                }

                String[] split = keyValue.split(":", 2);
                String name = split[0];
                String value = split[1];
                return Optional.of(StarArgumentKeyValue.parse(name, value));
            } else {
                if (this.first) {
                    this.usingMapping = false;
                    this.first = true;
                } else if (this.usingMapping) {
                    this.handler.handleError(this, "Found values applicable for listing pattern mixed with mapping pattern.");
                }

                if (!this.consumer.hasNext()) {
                    this.handler.handleError(this, "Too few default names in StarKeyConsumer! (internal error)");
                    return Optional.empty();
                } else {
                    String name = this.consumer.consume();
                    return Optional.of(StarArgumentKeyValue.parse(name, keyValue));
                }

            }

        } else {
            return Optional.empty();
        }
    }

    private boolean containsNotLiteral(String string, char condition) {
        StarCharacterStream stream = new StarCharacterStream(string);
        Optional<Character> next = stream.next();
        int brackets = 0;
        int brackets2 = 0;
        int brackets3 = 0;
        boolean quotes = false;
        boolean escape = false;
        while (true) {
            if (!next.isPresent()) {
                if (brackets != 0 || brackets2 != 0 || brackets3 != 0) {
                    this.handler.handleError(this, "Unbalanced brackets");
                }
                break;
            }
            char c = next.get();
            if (c == condition) {
                return true;
            }
            switch (c) {
                case '"': {
                    quotes = escape ? quotes : !quotes;
                    break;
                }
                case '\\': {
                    escape = !escape;
                    break;
                }
                case '(': {
                    brackets = quotes ? brackets : brackets + 1;
                    break;
                }
                case ')': {
                    brackets = quotes ? brackets : brackets - 1;
                    break;
                }
                case '{': {
                    brackets2 = quotes ? brackets2 : brackets2 + 1;
                    break;
                }
                case '}': {
                    brackets2 = quotes ? brackets2 : brackets2 - 1;
                    break;
                }
                case '[': {
                    brackets3 = quotes ? brackets3 : brackets3 + 1;
                    break;
                }
                case ']': {
                    brackets3 = quotes ? brackets3 : brackets3 - 1;
                    break;
                }
            }

            if (c != '\\') {
                escape = false;
            }

            if (brackets == 0 && brackets2 == 0 && brackets3 == 0 && (next.get() == ' ' || next.get() == ',') && !quotes) {
                break;
            }

            next = stream.next();


        }

        return false;
    }

    public interface Handler {
        void handleError(StarArgumentTokenizer tokenizer, String message);
    }

    public static class DefaultHandler implements Handler {

        @Override
        public void handleError(StarArgumentTokenizer tokenizer, String message) {

        }

    }
}
