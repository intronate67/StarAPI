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

import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.ChatFormat;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationResult.Reason;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;

public class ConversationTemplate {
    private Prompt prompt;
    private ChatFormat format;
    private List<Text> initialMessages;
    private Conversation.Handler handler;

    public ConversationTemplate() {
        this.initialMessages = new ArrayList<>();
        this.handler = new Conversation.Handler() {
            @Override
            public void normalEnd(Conversation conversation) {

            }

            @Override
            public void targetQuit(Conversation conversation) {

            }

            @Override
            public void started(Conversation conversation) {

            }
        };
    }

    public ConversationTemplate setInitialPrompt(Prompt prompt) {
        this.prompt = prompt;
        return this;
    }

    public ConversationTemplate setChatFormat(ChatFormat format) {
        this.format = format;
        return this;
    }

    public ConversationTemplate addInitialMessage(Text... texts) {
        for (Text text : texts) {
            this.initialMessages.add(text);
        }
        return this;
    }

    public ConversationTemplate setHandler(Conversation.Handler handler) {
        this.handler = handler;
        return this;
    }

    public ConversationResult startWith(CommandSource target) {
        ConversationManager manager = StarMain.getOperatingInstance().getConversationManager();
        UUID id = manager.getFrom(target);
        if (id == null) {
            return new ConversationResult(Reason.UNKNOWN_COMMAND_SOURCE);
        } else if (manager.containsKey(id)) {
            return new ConversationResult(Reason.TARGET_IN_CONVERSATION);
        } else {
            Conversation conversation = new Conversation(this.prompt, target, id, this.format, this.handler, this.initialMessages.toArray(new Text[this.initialMessages.size()]));
            manager.put(id, conversation);
            return new ConversationResult(conversation);
        }
    }

}
