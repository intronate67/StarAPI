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
package com.gmail.socraticphoenix.sponge.star.chat.condition;

import com.gmail.socraticphoenix.plasma.collection.PlasmaListUtil;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult.Type;
import com.gmail.socraticphoenix.sponge.star.chat.condition.verifiers.*;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class Verifiers {
    private static final Verifier successful = new SuccessfulVerifier();

    public static Verifier alwaysSuccessful() {
        return Verifiers.successful;
    }

    public static Verifier listVerifier(Verifier valueVerifier) {
        return new ListVerifier(valueVerifier);
    }

    public static Verifier onlinePlayer() {
        return new PlayerVerifier(true);
    }

    public static Verifier offlinePlayer() {
        return new PlayerVerifier(false);
    }

    public static Verifier equalTo(Object... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(int... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(short... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(long... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(float... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(double... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(boolean... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(byte... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier equalTo(char... possibleValues) {
        return new EqualToOneVerifier(possibleValues);
    }

    public static Verifier enumValue(Class<? extends Enum<?>> target) {
        return new EnumVerifier(target);
    }

    public static Verifier type(Class<?> target) {
        return new TypeVerifier(target);
    }

    public static Verifier minMax(double min, double max) {
        return new MinMaxVerifier(min, max);
    }

    public static Verifier catalogType(Class<? extends CatalogType> target) {
        return new CatalogTypeVerifier(target);
    }

    public static Verifier uuid() {
        return new UuidVerifier();
    }

    public static Verifier or(Verifier... verifiers) {
        return new Or(verifiers);
    }

    public static Verifier and(Verifier... verifiers) {
        return new And(verifiers);
    }

    static class Or implements Verifier {
        private List<Verifier> verifiers;

        public Or(Verifier... verifiers) {
            this.verifiers = PlasmaListUtil.buildList(verifiers);
        }

        @Override
        public VerificationResult verify(StarArgumentKeyValue argument) {
            List<Text> messages = new ArrayList<>();
            for(Verifier verifier : this.verifiers) {
                VerificationResult result = verifier.verify(argument);
                if(result.getType() == Type.SUCCESS) {
                    return result;
                } else {
                    messages.add(result.getMessage());
                }
            }

            return VerificationResult.failure(this.compile(Texts.builder("No conditions were valid for argument '".concat(argument.getKey()).concat("':")).color(TextColors.RED).build(), messages));
        }

        private Text compile(Text message, List<Text> messages) {
            TextBuilder builder = message.builder();
            for(Text text : messages) {
                builder.append(Texts.of("    - ")).append(text).append(Texts.of("\n"));
            }

            return builder.build();
        }

    }

    static class And implements Verifier {
        private List<Verifier> verifiers;

        public And(Verifier... verifiers) {
            this.verifiers = PlasmaListUtil.buildList(verifiers);
        }

        @Override
        public VerificationResult verify(StarArgumentKeyValue argument) {
            for(Verifier verifier : this.verifiers) {
                VerificationResult result = verifier.verify(argument);
                if(result.getType() == Type.FAILURE) {
                    return result;
                }
            }

            return VerificationResult.success();
        }
    }

}
