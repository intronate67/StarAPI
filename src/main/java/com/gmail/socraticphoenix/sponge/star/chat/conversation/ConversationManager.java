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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;

public class ConversationManager {
    private Map<UUID, Conversation> conversations;
    private Map<UUID, ConversationDelay> delayedConversations;

    public ConversationManager() {
        this.conversations = new HashMap<>();
        this.delayedConversations = new HashMap<>();
    }

    public boolean isDelayed(UUID player) {
        return this.delayedConversations.containsKey(player);
    }

    public void removeDelay(UUID id) {
        this.delayedConversations.remove(id);
    }

    public void putDelay(UUID id, ConversationDelay delay) {
        this.delayedConversations.put(id, delay);
    }

    public Optional<ConversationDelay> getDelay(UUID player) {
        return Optional.ofNullable(this.delayedConversations.get(player));
    }

    public Conversation get(UUID player) {
        return this.conversations.get(player);
    }

    public boolean containsKey(UUID player) {
        return this.conversations.containsKey(player);
    }

    public void put(UUID player, Conversation conversation) {
        this.conversations.put(player, conversation);
    }

    public void remove(UUID player) {
        this.conversations.remove(player);
    }

    public UUID getFrom(CommandSource source) {
        if(source instanceof Player) {
            return ((Player) source).getUniqueId();
        } else if (source instanceof ConsoleSource) {
            return Conversation.CONSOLE_ID;
        } else {
            return null;
        }
    }
}
