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
package com.gmail.socraticphoenix.sponge.star.chat.conversation;

import com.gmail.socraticphoenix.plasma.collection.KeyValue;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.ChatFormat;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArguments;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult.Type;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Verifier;
import java.util.UUID;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;

public class Conversation {
    public static final String PROMPT_KEY = "$prompt";
    public static final UUID CONSOLE_ID = UUID.randomUUID();

    private Prompt current;
    private CommandSource target;
    private StarArguments arguments;
    private ChatFormat format;
    private Text[] initialMessages;
    private Handler handler;
    private UUID id;

    public Conversation(Prompt first, CommandSource target, UUID id, ChatFormat format, Handler handler, Text... initialMessages) {
        this.current = first;
        this.target = target;
        this.handler = handler;
        this.arguments = new StarArguments();
        this.initialMessages = initialMessages;
        this.id = id;
    }

    public StarArguments getArguments() {
        return this.arguments;
    }

    public void start() {
        this.target.sendMessage(this.initialMessages);
        this.handler.started(this);
        this.target.sendMessage(this.format.fill(new KeyValue<>(Conversation.PROMPT_KEY, this.current.getMessage())));
        while (this.current instanceof Prompt.Message) {
            this.current = this.current.process(StarArgumentValue.of(null), this);
            this.target.sendMessage(this.format.fill(new KeyValue<>(Conversation.PROMPT_KEY, this.current.getMessage())));
        }
    }

    public void end() {

    }

    public void step(String message) {
        Verifier verifier = this.current.getVerifier();
        StarArgumentKeyValue keyValue = StarArgumentKeyValue.parse("value", message);
        VerificationResult result = verifier.verify(keyValue);
        if (result.getType() == Type.SUCCESS) {
            this.current = this.current.process(keyValue, this);
            if (this.current == Prompt.END) {
                this.end();
            }
            this.target.sendMessage(this.current.getMessage());
        } else if (result.getType() == Type.FAILURE) {
            this.target.sendMessage(result.getMessage());
        }
    }

    public interface Handler {

        void normalEnd(Conversation conversation);

        void targetQuit(Conversation conversation);

        void nullPrompt(Conversation conversation);

        void started(Conversation conversation);

    }

}
