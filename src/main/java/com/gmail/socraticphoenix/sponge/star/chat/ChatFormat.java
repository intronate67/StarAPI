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
package com.gmail.socraticphoenix.sponge.star.chat;

import com.gmail.socraticphoenix.plasma.collection.KeyValue;
import java.util.*;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;

public class ChatFormat {
    private List<Element> elements;

    public ChatFormat(List<Element> elements) {
        this.elements = elements;
    }

    public Text fill(KeyValue<String, Text>... keyValues) {
        TextBuilder builder = Texts.builder();
        Map<String, Text> variables = this.toMap(keyValues);
        for(Element element : this.elements) {
            if(element.getLiteral().isPresent()) {
                builder.append(element.getLiteral().get());
            } else if (element.getKey().isPresent()) {
                if(variables.containsKey(element.getKey().get())) {
                    builder.append(variables.get(element.getKey().get()));
                } else {
                    builder.append(Texts.of("<".concat(element.getKey().get()).concat(">")));
                }
            }
        }
        return builder.build();
    }

    private Map<String, Text> toMap(KeyValue<String, Text>... keyValues) {
        Map<String, Text> map = new HashMap<>();
        for(KeyValue<String, Text> kv : keyValues) {
            map.put(kv.getKey(), kv.getValue());
        }

        return map;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        List<Element> elements;

        public Builder() {
            this.elements = new ArrayList<>();
        }

        public Builder variable(String... vars) {
            for (String v : vars) {
                this.elements.add(new Element(v));
            }

            return this;
        }

        public Builder literal(String... text) {
            for(String t : text) {
                this.elements.add(new Element(Texts.of(t)));
            }

            return this;
        }

        public Builder literal(Text... text) {
            for (Text t : text) {
                this.elements.add(new Element(t));
            }

            return this;
        }

        public ChatFormat build() {
            return new ChatFormat(this.elements);
        }
    }

    private static class Element {
        String key;
        Text literal;

        public Element(String key) {
            this.key = key;
        }

        public Element(Text literal) {
            this.literal = literal;
        }

        public Optional<String> getKey() {
            return Optional.ofNullable(this.key);
        }

        public Optional<Text> getLiteral() {
            return Optional.ofNullable(this.literal);
        }

    }

}
