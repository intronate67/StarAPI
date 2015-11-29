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
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifier;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class MinMaxVerifier implements Verifier {
    private double min;
    private double max;

    public MinMaxVerifier(double min, double max) {
        this.min = min;
        this.max = max;
    }


    @Override
    public VerificationResult verify(StarArgumentKeyValue argument) {
        if(argument.getValue().getValue().isPresent() && (argument.getValue().getValue().get() instanceof Double || argument.getValue().getValue().get() instanceof Integer)) {
            double value;
            Object arg = argument.getValue().getValue().get();
            if(arg instanceof Double) {
                value = (double) arg;
            } else if (arg instanceof Integer) {
                value = (int) arg;
            } else {
                return VerificationResult.failure("Argument was not a double or an integer. (Internal error)");
            }

            if(value >= min && value <= max) {
                return VerificationResult.success();
            } else {
                return VerificationResult.failure(Texts.builder("Argument '".concat(argument.getKey()).concat("' was not between ").concat(String.valueOf(this.min)).concat(" and ").concat(String.valueOf(this.max))).color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
            }

        }

        return VerificationResult.success();
    }
}
