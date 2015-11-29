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
import com.gmail.socraticphoenix.sponge.star.StarUtil;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifier;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class PlayerVerifier implements Verifier {
    private boolean online;

    public PlayerVerifier(boolean mustBeOnline) {
        this.online = mustBeOnline;
    }

    @Override
    public VerificationResult verify(StarArgumentKeyValue argument) {
        if(argument.getValue().getAsString().isPresent()) {
            String player = argument.getValue().getAsString().get();
            if(this.online && !Star.getServer().getPlayer(player).isPresent()) {
                return VerificationResult.failure(Texts.builder("Player '".concat(player).concat("' is not online")).color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
            } else if (!this.online && !(Star.getServer().getPlayer(player).isPresent() || StarUtil.getOfflinePlayer(player).isPresent())) {
                return VerificationResult.failure(Texts.builder("Player '".concat(player).concat("' could not be found offline or online")).color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
            }

            return VerificationResult.success();
        } else {
            return VerificationResult.success();
        }
    }

}
