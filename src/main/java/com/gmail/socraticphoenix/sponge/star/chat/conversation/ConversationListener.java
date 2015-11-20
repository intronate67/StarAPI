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

import com.gmail.socraticphoenix.plasma.string.PlasmaStringUtil;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.command.MessageSinkEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

public class ConversationListener {

    @Listener(ignoreCancelled = false, beforeModifications = true, order = Order.LAST)
    public void onChat(MessageSinkEvent.Chat event) {
        ConversationManager manager = StarMain.getOperatingInstance().getConversationManager();
        Cause cause = event.getCause();
        Optional<Player> playerOptional = cause.first(Player.class);
        if(playerOptional.isPresent()) {
            UUID id = manager.getFrom(playerOptional.get());
            if(manager.containsKey(id)) {
                String message = Texts.toPlain(event.getRawMessage());
                manager.get(id).step(message);
                event.setCancelled(true);
            }
        }
    }

    @Listener(ignoreCancelled = false, beforeModifications = true, order = Order.LAST)
    public void onConsoleChat(SendCommandEvent event) {
        ConversationManager manager = StarMain.getOperatingInstance().getConversationManager();
        Cause cause = event.getCause();
        Optional<ConsoleSource> consoleSourceOptional = cause.first(ConsoleSource.class);
        if(consoleSourceOptional.isPresent()) {
            UUID id = manager.getFrom(consoleSourceOptional.get());
            if(manager.containsKey(id)) {
                String message = PlasmaStringUtil.removeTrailingSpaces(event.getCommand().concat(" ").concat(event.getArguments()));
                if(!message.equals("end")) {
                    manager.get(id).step(message);
                    event.setCancelled(true);
                }
            }
        }
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        ConversationManager manager = StarMain.getOperatingInstance().getConversationManager();
        UUID id = event.getTargetEntity().getUniqueId();
        if(manager.containsKey(id)) {
            manager.get(id).targetQuitEnd();
        }
    }


}
