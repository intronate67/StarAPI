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
package com.gmail.socraticphoenix.sponge.star.chat.command.conversation;

import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarKeyConsumer;
import com.gmail.socraticphoenix.sponge.star.chat.command.CommandHandler;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Condition;
import com.gmail.socraticphoenix.sponge.star.chat.condition.ConditionSet;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifier;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifiers;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.Conversation;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.Prompt;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class InitialCommandPrompt extends Prompt {
    private CommandHandler handler;

    public InitialCommandPrompt(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public Verifier getVerifier() {
        return Verifiers.equalTo(this.handler.getLengths());
    }

    @Override
    public Text getMessage() {
        return Texts.builder("Please enter the amount of the arguments you are going to enter. Acceptable amounts are: ".concat(Arrays.toString(this.handler.getLengths()))).color(TextColors.GREEN).build();
    }

    @Override
    public Prompt process(StarArgumentValue value, Conversation conversation) {
        if(value.getAsInteger().isPresent()) {
            int len = value.getAsInteger().get();
            Optional<StarKeyConsumer> consumerOptional = this.handler.getDefaultsForLength(len);
            if(!consumerOptional.isPresent()) {
                // should be impossible due to verifiers
                return Prompt.END;
            }

            StarKeyConsumer consumer = consumerOptional.get();
            ConditionSet conditions = this.handler.getConditions();
            Deque<Prompt> prompts = new ArrayDeque<>();

            while (consumer.hasNext()) {
                String key = consumer.consume();
                Optional<Condition> conditionOptional = conditions.get(key);
                Verifier verifier;
                if(conditionOptional.isPresent()) {
                    verifier = conditionOptional.get().getVerifier();
                } else {
                    verifier = Verifiers.alwaysSuccessful();
                }

                Prompt prompt = new CommandPrompt(prompts, key, verifier);
                prompts.add(prompt);
            }

            if(prompts.isEmpty()) {
                //Only occurs if length is 0
                return Prompt.END;
            } else {
                return prompts.poll();
            }

        } else {
            // should be impossible due to verifiers
            return Prompt.END;
        }
    }

}
