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

import com.gmail.socraticphoenix.sponge.star.Star;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArguments;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarKeyConsumer;
import com.gmail.socraticphoenix.sponge.star.chat.command.Command;
import com.gmail.socraticphoenix.sponge.star.chat.command.CommandHandler;
import com.gmail.socraticphoenix.sponge.star.chat.condition.ConditionSet;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationManager;
import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

@Command(usage = "no arguments", longHelp = "Ends any conversation you are currently in.", shortDescription = "Ends your conversation.")
public class ConversationEndCommand extends CommandHandler {

    @Override
    public CommandResult execute(CommandSource source, StarArguments arguments, String argString) {
        ConversationManager manager = StarMain.getOperatingInstance().getConversationManager();
        UUID id = manager.getFrom(source);
        if(manager.containsKey(id)) {
            manager.get(id).targetQuitEnd();
        } else {
            source.sendMessage(Texts.builder("You aren't in a conversation!").color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
        }

        return CommandResult.success();
    }

    @Override
    public ConditionSet getConditions() {
        return new ConditionSet();
    }

    @Override
    public int[] getLengths() {
        return new int[]{0};
    }

    @Override
    public Optional<StarKeyConsumer> getDefaultsForLength(int length) {
        return Optional.of(new StarKeyConsumer());
    }

    @Override
    public String[] getPermissions() {
        return new String[]{};
    }

}
