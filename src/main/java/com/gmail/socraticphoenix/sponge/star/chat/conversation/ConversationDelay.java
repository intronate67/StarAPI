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

import com.gmail.socraticphoenix.sponge.star.Star;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentValue;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.Prompt.Delay;
import com.gmail.socraticphoenix.sponge.star.scheduler.SpongeRunnable;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class ConversationDelay extends SpongeRunnable {
    private Queue<String> messages = new LinkedBlockingQueue<>();
    private UUID conversationId;

    public ConversationDelay(UUID id) {
        this.conversationId = id;
    }

    @Override
    public void run() {
        ConversationManager manager = Star.getConversationManager();

        if(manager.isDelayed(this.conversationId)) {
            manager.removeDelay(this.conversationId);
            Conversation conversation = manager.get(this.conversationId);
            if(conversation.getCurrent() instanceof Prompt.Delay) {
                Prompt.Delay delay = (Delay) conversation.getCurrent();
                ConversationDelay conversationDelay = new ConversationDelay(conversation.getId());
                manager.putDelay(conversation.getId(), conversationDelay);
                if (delay.isTicks()) {
                    conversationDelay.runTaskLater(StarMain.getOperatingInstance(), delay.getTime());
                } else {
                    conversationDelay.runTaskLater(StarMain.getOperatingInstance(), delay.getTime(), delay.getUnit());
                }
                conversation.setCurrent(delay.process(StarArgumentValue.parse(""), conversation));
            } else {
                while (!this.messages.isEmpty()) {
                    Star.getConversationManager().get(this.conversationId).step(this.messages.poll());
                }
            }
        }
    }

    public void queueMessage(String message) {
        this.messages.add(message);
    }

}
