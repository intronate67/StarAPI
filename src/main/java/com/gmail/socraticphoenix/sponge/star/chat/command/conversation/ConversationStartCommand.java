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

import com.gmail.socraticphoenix.plasma.string.PlasmaStringUtil;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.chat.ChatFormat;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArguments;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.parse.StarKeyConsumer;
import com.gmail.socraticphoenix.sponge.star.chat.command.Command;
import com.gmail.socraticphoenix.sponge.star.chat.command.CommandHandler;
import com.gmail.socraticphoenix.sponge.star.chat.command.MainCommand;
import com.gmail.socraticphoenix.sponge.star.chat.command.SpongeCommand;
import com.gmail.socraticphoenix.sponge.star.chat.condition.*;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.Conversation;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationResult;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationResult.Reason;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationTemplate;
import java.util.Optional;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

@Command (usage = "<command>", longHelp = "Starts a command-converted conversation with you for the specified command.", shortDescription = "Starts a command conversation.")
public class ConversationStartCommand extends CommandHandler {

    @Override
    public CommandResult execute(CommandSource source, StarArguments arguments, String argString) {
        String command = arguments.get("command").get().getAsString().get();
        CommandService service = StarMain.getOperatingInstance().getGame().getCommandDispatcher();
        CommandMapping mapping = service.get(command).get();
        if (mapping.getCallable() instanceof SpongeCommand) {
            SpongeCommand spongeCommand = (SpongeCommand) mapping.getCallable();
            ConversationTemplate template = spongeCommand.getHandler().toConversation(ChatFormat.builder().literal(Texts.builder("Indigo> ").color(TextColors.LIGHT_PURPLE).build()).literal(Texts.builder().color(TextColors.YELLOW).build()).variable(Conversation.PROMPT_KEY).build());
            ConversationResult result = template.startWith(source);
            if (!result.wasSuccessful()) {
                ConversationResult.Reason reason = result.getReason();
                if (Reason.TARGET_IN_CONVERSATION == reason) {
                    source.sendMessage(Texts.builder("Cannot start a conversation with you because you are already in a conversation.").color(TextColors.RED).build());
                } else if (Reason.UNKNOWN_COMMAND_SOURCE == reason) {
                    source.sendMessage(Texts.of("I don't know what you are... Conversations only work for the console or players"));
                }
            } else {
                result.getConversation().get().start();
            }
        } else if (mapping.getCallable() instanceof MainCommand) {
            MainCommand mainCommand = (MainCommand) mapping.getCallable();
            String[] pieces = PlasmaStringUtil.removeTrailingSpaces(PlasmaStringUtil.minimizeSpaces(argString)).split(" ");
            if (pieces.length <= 1) {
                source.sendMessage(Texts.builder("Found Main command under '".concat(pieces[0]).concat("' but no sub-commands in request")).color(TextColors.RED).build());
            } else if (!mainCommand.getSubCommand(pieces[1]).isPresent()) {
                source.sendMessage(Texts.builder("Found Main command under '".concat(pieces[0].concat("' but did not recognize sub-command '").concat(pieces[1]).concat("'"))).color(TextColors.RED).build());
            } else {
                CommandCallable current = mainCommand.getSubCommand(pieces[1]).get();
                for (int i = 2; i < pieces.length; i++) {
                    if(current instanceof SpongeCommand) {
                        if(i != pieces.length - 1) {
                            source.sendMessage(Texts.builder("Found sub command under '".concat(pieces[i - 1]).concat("' but more commands were specified")).color(TextColors.RED).build());
                        } else {
                            SpongeCommand spongeCommand = (SpongeCommand) current;
                            ConversationTemplate template = spongeCommand.getHandler().toConversation(ChatFormat.builder().literal(Texts.builder("Indigo> ").color(TextColors.LIGHT_PURPLE).build()).literal(Texts.builder().color(TextColors.YELLOW).build()).variable(Conversation.PROMPT_KEY).build());
                            ConversationResult result = template.startWith(source);
                            if (!result.wasSuccessful()) {
                                ConversationResult.Reason reason = result.getReason();
                                if (Reason.TARGET_IN_CONVERSATION == reason) {
                                    source.sendMessage(Texts.builder("Cannot start a conversation with you because you are already in a conversation.").color(TextColors.RED).build());
                                } else if (Reason.UNKNOWN_COMMAND_SOURCE == reason) {
                                    source.sendMessage(Texts.of("I don't know what you are... Conversations only work for the console or players"));
                                }
                            } else {
                                result.getConversation().get().start();
                            }
                        }
                        break;
                    } else if (current instanceof MainCommand) {
                        if(i == pieces.length - 1) {
                            source.sendMessage(Texts.builder("Found main command under '".concat(pieces[i - 1]).concat("' but there were no more sub commands left")).color(TextColors.RED).build());
                            break;
                        } else {
                            Optional<CommandCallable> optionalCommand = ((MainCommand) current).getSubCommand(pieces[i - 1]);
                            if(!optionalCommand.isPresent()) {
                                source.sendMessage(Texts.builder("Sub-command '".concat(pieces[i - 1]).concat("' could not be found")).color(TextColors.RED).build());
                                break;
                            } else {
                                current = optionalCommand.get();
                            }
                        }
                    } else {
                        source.sendMessage(Texts.builder("Command '".concat(pieces[i-1]).concat("' does not support conversation conversion.")).color(TextColors.RED).build());
                        break;
                    }
                }
            }
        }

        return CommandResult.success();
    }

    @Override
    public ConditionSet getConditions() {
        return new ConditionSet(
                new Condition("command", Verifiers.and(
                        Verifiers.type(String.class),
                        argument -> {
                            if (argument.getValue().getAsString().isPresent()) {
                                CommandService service = StarMain.getOperatingInstance().getGame().getCommandDispatcher();
                                String command = argument.getValue().getAsString().get();
                                if (service.get(command).isPresent()) {
                                    if (service.get(command).get().getCallable() instanceof SpongeCommand || service.get(command).get().getCallable() instanceof MainCommand) {
                                        return VerificationResult.success();
                                    } else {
                                        return VerificationResult.failure(Texts.builder("Command '".concat(command).concat("' does not support conversation conversion")).color(TextColors.RED).build());
                                    }
                                } else {
                                    return VerificationResult.failure(Texts.builder("Could not find command '".concat(command).concat("'")).color(TextColors.RED).build());
                                }
                            } else {
                                return VerificationResult.success();
                            }
                        }
                ))
        );
    }

    @Override
    public int[] getLengths() {
        return new int[] {1};
    }

    @Override
    public Optional<StarKeyConsumer> getDefaultsForLength(int length) {
        return Optional.of(new StarKeyConsumer("command"));
    }

    @Override
    public String[] getPermissions() {
        return new String[] {};
    }

}
