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

public class ListVerifier implements Verifier {
    private Verifier verifier;

    public ListVerifier(Verifier valueVerifier) {
        this.verifier = valueVerifier;
    }

    @Override
    public VerificationResult verify(StarArgumentKeyValue argument) {
        if(argument.getValue().getAsList().isPresent()) {
            for (int i = 0; i < argument.getValue().getAsList().get().size(); i++) {
                StarArgumentValue value = argument.getValue().getAsList().get().get(i);
                VerificationResult result = this.verifier.verify(new StarArgumentKeyValue(String.valueOf(i), value));
                if(result.getType() == VerificationResult.Type.FAILURE) {
                    return VerificationResult.failure(Texts.builder("List at key '".concat(argument.getKey()).concat("' contained illegal value at index ".concat(String.valueOf(i)).concat(":"))).color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).append(result.getMessage()).build());
                }
            }
            return VerificationResult.success();
        } else {
            return VerificationResult.success();
        }
    }

}
