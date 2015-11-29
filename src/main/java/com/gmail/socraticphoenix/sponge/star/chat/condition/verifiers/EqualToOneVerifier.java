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
package com.gmail.socraticphoenix.sponge.star.chat.condition.verifiers;

import com.gmail.socraticphoenix.sponge.star.Star;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifier;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class EqualToOneVerifier implements Verifier {
    private Object[] possibleElements;

    public EqualToOneVerifier(long... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(short... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(byte... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(int... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(boolean... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(double... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(float... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(char... values) {
        this.possibleElements = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            this.possibleElements[i] = values[i];
        }
    }

    public EqualToOneVerifier(Object... values) {
        this.possibleElements = values;
    }


    @Override
    public VerificationResult verify(StarArgumentKeyValue argument) {
        return this.equalToElement(argument.getValue().getValue().orElse(null)) ? VerificationResult.success() : VerificationResult.failure(Texts.builder("Value '".concat(StarArgumentValue.write(argument.getValue().getValue().orElse(null))).concat("' did not match one of the following: ").concat(this.toString(this.possibleElements))).color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
    }

    private boolean equalToElement(Object object) {
        for (Object t : this.possibleElements) {
            if(t != null && t.equals(object)) {
                return true;
            }
        }

        return false;
    }

    private String toString(Object[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < array.length; i++) {
            builder.append(StarArgumentValue.write(array[i]));
            if(i < array.length - 1) {
                builder.append(", ");
            }
        }
        return builder.append("]").toString();
    }
}
