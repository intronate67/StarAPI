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
package com.gmail.socraticphoenix.sponge.star.chat.command;

import com.gmail.socraticphoenix.plasma.source.util.SourceNamespace;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.ChatFormat;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArguments;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarArgumentTokenizer;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarKeyConsumer;
import com.gmail.socraticphoenix.sponge.star.chat.command.conversation.CommandConversationHandler;
import com.gmail.socraticphoenix.sponge.star.chat.command.conversation.InitialCommandPrompt;
import com.gmail.socraticphoenix.sponge.star.chat.condition.ConditionSet;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.Conversation;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tristate;

public abstract class CommandHandler {

    public abstract CommandResult execute(CommandSource source, StarArguments arguments, String argString);

    public abstract ConditionSet getConditions();

    public StarArgumentTokenizer.Handler getTokenizingHandler(CommandSource source) {
        return new CommandTokenizerHandler(source);
    }

    public void displayHelpTo(CommandSource source) {
        source.sendMessage(this.getHelp());
    }

    public Text getHelp() {
        return Texts.builder("    - ").append(this.getInfo().getShortDescription()).append(Texts.of(": ")).append(this.getInfo().getUsage()).append(Texts.builder("\n    - ").color(TextColors.WHITE).build()).append(this.getInfo().getLongHelp()).build();
    }

    public boolean modularizedPermissions() {
        Command command = this.getClass().getAnnotation(Command.class);
        if (command != null) {
            return command.modularizePerms();
        } else {
            return true;
        }
    }

    public CommandInfo getInfo() {
        Command command = this.getClass().getAnnotation(Command.class);
        if (command != null) {
            return new CommandInfo(Texts.legacy('&').fromUnchecked(command.shortDescription()), Texts.legacy('&').fromUnchecked(command.longHelp()), Texts.legacy('&').fromUnchecked(command.usage()));
        }
        return new CommandInfo(Texts.of(), Texts.of(), Texts.of());
    }

    public boolean isValid() {
        return this.containsAcceptableDefaults() && this.containsAcceptableLengths();
    }

    public boolean containsAcceptableDefaults() {
        for (int i : this.getLengths()) {
            Optional<StarKeyConsumer> consumer = this.getDefaultsForLength(i);
            if (!consumer.isPresent()) {
                return false;
            } else if (consumer.get().size() != i) {
                return false;
            }
        }

        return true;
    }

    public boolean containsAcceptableLengths() {
        for (int i : this.getLengths()) {
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

    public abstract int[] getLengths();

    public abstract Optional<StarKeyConsumer> getDefaultsForLength(int length);

    public boolean isApplicableLength(int i) {
        for (int j : this.getLengths()) {
            if (i == j) {
                return true;
            }
        }

        return false;
    }

    public List<String> getSuggestions(CommandSource source, String argString) {
        return new ArrayList<>();
    }

    public boolean testPermission(CommandSource source) {
        return this.hasPermissions(source);
    }

    public abstract String[] getPermissions();

    public void registerAsSpongeCommand(String... names) {
        StarMain.getOperatingInstance().getGame().getCommandManager().register(StarMain.getOperatingInstance(), this.toSponge(), names);
    }

    public boolean hasPermissions(CommandSource source) {
        if (this.getPermissions().length == 0) {
            return true;
        } else if (source instanceof CommandBlockSource || source instanceof ConsoleSource) {
            return true;
        } else {
            if (!this.modularizedPermissions()) {
                for (String perm : this.getPermissions()) {
                    if (source.hasPermission(perm) && source.getPermissionValue(source.getActiveContexts(), perm) == Tristate.TRUE) {
                        return true;
                    }
                }
                return false;
            } else {
                for (String perm : this.getPermissions()) {
                    SourceNamespace namespace = new SourceNamespace(perm);
                    if (source.hasPermission(perm) && source.getPermissionValue(source.getActiveContexts(), perm) == Tristate.TRUE) {
                        return true;
                    } else {
                        StringBuilder builder = new StringBuilder();
                        String[] parents = namespace.getParentNames();
                        for (int i = 0; i < parents.length; i++) {
                            String parent = parents[i];
                            builder.append(parent);
                            if (source.hasPermission(builder.toString()) && source.getPermissionValue(source.getActiveContexts(), builder.toString()) == Tristate.TRUE) {
                                return true;
                            }
                            if(i < parents.length - 1) {
                                builder.append(".");
                            }
                        }
                    }
                }
                return false;
            }
        }
    }

    public SpongeCommand toSponge() {
        return new SpongeCommand(this);
    }

    public ConversationTemplate toConversation(ChatFormat format, Text... initialMessages) {
        return Conversation.template().setChatFormat(format).setHandler(new CommandConversationHandler(this)).addInitialMessage(initialMessages).setInitialPrompt(new InitialCommandPrompt(this));
    }

}
