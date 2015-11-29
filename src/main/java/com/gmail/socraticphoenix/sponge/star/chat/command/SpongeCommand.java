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

import com.gmail.socraticphoenix.sponge.star.Star;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArguments;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarArgumentParser;
import com.gmail.socraticphoenix.sponge.star.chat.condition.ConditionSet;
import com.gmail.socraticphoenix.sponge.star.chat.condition.Conditions;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult;
import com.gmail.socraticphoenix.sponge.star.chat.condition.VerificationResult.Type;
import com.gmail.socraticphoenix.sponge.star.plugin.LanguageMapping;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

public class SpongeCommand implements CommandCallable {
    private CommandHandler handler;

    public SpongeCommand(CommandHandler handler) {
        this.handler = handler;
        if(!handler.isValid()) {
            throw new IllegalArgumentException("Invalid CommandHandler '".concat(handler.getClass().getName()).concat("' (invalidLengths=".concat(String.valueOf(handler.containsAcceptableLengths()))).concat(", invalidDefaults:".concat(String.valueOf(handler.containsAcceptableDefaults()))).concat(")"));
        }
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if(this.handler.hasPermissions(source)) {
            int length = StarArgumentParser.lengthOf(arguments, this.handler.getTokenizingHandler(source));
            if (this.handler.isApplicableLength(length)) {
                StarArguments args = StarArgumentParser.parse(arguments, this.handler.getTokenizingHandler(source), this.handler.getDefaultsForLength(length).get());
                ConditionSet set = this.handler.getConditions();
                VerificationResult result = Conditions.runConditions(set, args);
                if (result.getType() == Type.SUCCESS) {
                    return this.handler.execute(source, args, arguments);
                } else if (result.getType() == Type.FAILURE) {
                    source.sendMessage(result.getMessage());
                    this.handler.displayHelpTo(source);
                    return CommandResult.empty();
                }
            } else {
                source.sendMessage(Texts.builder().color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).append(Texts.of("Improper amount of arguments (".concat(String.valueOf(length)).concat(")! Acceptable lengths: ").concat(Arrays.toString(this.handler.getLengths())))).build());
                this.handler.displayHelpTo(source);
            }
        } else {
            source.sendMessage(Star.getStarMain().getLanguageMapping().query("perm-message", "You don't have permission to do that!").builder().color(Star.getStarMain().getLanguageMapping().query("command-error", TextColors.RED)).build());
        }
        return CommandResult.empty();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return this.handler.getSuggestions(source, arguments);
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return this.handler.testPermission(source);
    }

    @Override
    public Optional<? extends Text> getShortDescription(CommandSource source) {
        return Optional.of(this.handler.getInfo().getShortDescription());
    }

    @Override
    public Optional<? extends Text> getHelp(CommandSource source) {
        return Optional.of(this.handler.getInfo().getLongHelp());
    }

    @Override
    public Text getUsage(CommandSource source) {
        return this.handler.getInfo().getUsage();
    }

    public CommandHandler getHandler() {
        return this.handler;
    }
}
