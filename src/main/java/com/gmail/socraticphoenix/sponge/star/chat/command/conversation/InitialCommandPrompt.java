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
import java.util.*;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class InitialCommandPrompt extends Prompt {
    private CommandHandler handler;
    private boolean hasSingleLength;
    private Deque<Prompt> prompts;

    public InitialCommandPrompt(CommandHandler handler) {
        this.handler = handler;
        this.hasSingleLength = this.handler.getLengths().length == 1;
        if (this.hasSingleLength) {
            this.prompts = this.assemblePrompts(this.handler.getLengths()[0]);
        }
    }

    private Deque<Prompt> assemblePrompts(int len) {
        Optional<StarKeyConsumer> consumerOptional = this.handler.getDefaultsForLength(len);
        if (!consumerOptional.isPresent()) {
            // should be impossible due to verifiers
            return new ArrayDeque<>();
        }

        StarKeyConsumer consumer = consumerOptional.get();
        ConditionSet conditions = this.handler.getConditions();
        Deque<Prompt> prompts = new ArrayDeque<>();

        while (consumer.hasNext()) {
            String key = consumer.consume();
            Optional<Condition> conditionOptional = conditions.get(key);
            Verifier verifier;
            if (conditionOptional.isPresent()) {
                verifier = conditionOptional.get().getVerifier();
            } else {
                verifier = Verifiers.alwaysSuccessful(); //No conditions means any value is fine!
            }

            Prompt prompt = new CommandPrompt(prompts, key, verifier);
            prompts.add(prompt);
        }

        return prompts;
    }

    @Override
    public Verifier getVerifier() {
        if (!this.hasSingleLength) {
            return Verifiers.equalTo(this.handler.getLengths());
        } else {
            Prompt prompt = this.prompts.peek();
            if (prompt != null) {
                return prompt.getVerifier();
            } else {
                return Verifiers.alwaysSuccessful(); //Shouldn't really occur...
            }
        }
    }

    @Override
    public Text getMessage() {
        if (!this.hasSingleLength) {
            return Texts.builder("Please enter the amount of the arguments you are going to enter. Acceptable amounts are: ".concat(Arrays.toString(this.handler.getLengths()))).color(TextColors.GREEN).build();
        } else {
            Prompt prompt = this.prompts.peek();
            if (prompt != null) {
                return prompt.getMessage();
            } else {
                return Texts.builder("Executing command...").color(TextColors.GOLD).build();
            }
        }
    }

    @Override
    public Prompt process(StarArgumentValue value, Conversation conversation) {
        if (!this.hasSingleLength) {
            if (value.getAsInteger().isPresent()) {
                int len = value.getAsInteger().get();
                Deque<Prompt> deque = this.assemblePrompts(len);
                if (!deque.isEmpty()) {
                    return deque.poll();
                } else {
                    //Only occurs if length is 0
                    return Prompt.END;
                }
            } else {
                // should be impossible due to verifiers
                return Prompt.END;
            }
        } else {
            if (!this.prompts.isEmpty()) {
                Prompt next = this.prompts.poll();
                return next.process(value, conversation);
            } else {
                return Prompt.END;
            }
        }
    }

}
