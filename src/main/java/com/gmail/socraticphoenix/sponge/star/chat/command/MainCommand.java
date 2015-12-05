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

import com.gmail.socraticphoenix.plasma.collection.KeyValue;
import com.gmail.socraticphoenix.plasma.string.PlasmaStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gmail.socraticphoenix.sponge.star.Star;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

public class MainCommand implements CommandCallable {
    private Map<String, CommandCallable> subCommands;
    private Text description;
    private Text help;
    private Text usage;

    public MainCommand(Text description, Text help, Text usage, Map<String, CommandCallable> subCommands) {
        this.description = description;
        this.help = help;
        this.usage = usage;
        this.subCommands = subCommands;
    }

    public MainCommand(Text description, Text help, Text usage, KeyValue<String, CommandCallable>... subCommands) {
        this(description, help, usage, new HashMap<>());
        for(KeyValue<String, CommandCallable> keyValue : subCommands) {
            this.subCommands.put(keyValue.getKey(), keyValue.getValue());
        }
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        String[] pieces = arguments.split(" ");
        String command = pieces[0];
        List<String> argPieces = new ArrayList<>();
        for (int i = 1; i < pieces.length; i++) {
            argPieces.add(pieces[i]);
        }
        String argString = PlasmaStringUtil.join(" ", argPieces.toArray());

        Optional<CommandCallable> subCommand = this.getSubCommand(command);
        if(subCommand.isPresent()) {
            return subCommand.get().process(source, argString);
        } else {
            source.sendMessage(Texts.builder("Sub-command '".concat(command).concat("' was not found")).color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
            return CommandResult.empty();
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return this.subCommands.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public Optional<? extends Text> getShortDescription(CommandSource source) {
        return Optional.of(this.description);
    }

    @Override
    public Optional<? extends Text> getHelp(CommandSource source) {
        return Optional.of(this.help);
    }

    @Override
    public Text getUsage(CommandSource source) {
        return this.usage;
    }

    public Optional<CommandCallable> getSubCommand(String key) {
        return Optional.ofNullable(this.subCommands.get(key));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Text description;
        private Text help;
        private Text usage;
        private Map<String, CommandCallable> subs;

        Builder() {
            this.subs = new HashMap<>();
        }

        public Builder description(Text description) {
            this.description = description;
            return this;
        }

        public Builder help(Text help) {
            this.help = help;
            return this;
        }

        public Builder usage(Text usage) {
            this.usage = usage;
            return this;
        }

        public Builder addSubCommand(CommandCallable command, String... aliases) {
            for(String str : aliases) {
                this.subs.put(str, command);
            }
            return this;
        }

        public MainCommand build() {
            return new MainCommand(this.description, this.help, this.usage, this.subs);
        }
    }
}
