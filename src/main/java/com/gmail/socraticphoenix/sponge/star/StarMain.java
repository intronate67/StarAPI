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
package com.gmail.socraticphoenix.sponge.star;

import com.gmail.socraticphoenix.plasma.file.FileChannelThread;
import com.gmail.socraticphoenix.plasma.file.cif.io.CIFException;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSCException;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.StarArgumentValueConverter;
import com.gmail.socraticphoenix.sponge.star.chat.command.conversation.ConversationStartCommand;
import com.gmail.socraticphoenix.sponge.star.chat.command.conversation.ConversationEndCommand;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationManager;
import com.gmail.socraticphoenix.sponge.star.minigame.util.CooldownManager;
import com.gmail.socraticphoenix.sponge.star.plugin.InformationConstants;
import com.gmail.socraticphoenix.sponge.star.plugin.PluginInformation;
import com.gmail.socraticphoenix.sponge.star.serialization.asac.ASACSerializers;
import com.gmail.socraticphoenix.sponge.star.serialization.cif.CIFSerializers;
import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import com.gmail.socraticphoenix.sponge.star.plugin.StarPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.ServiceManager;

@Plugin (id = "starApi", name = "StarAPI", version = "0.1")
@PluginInformation (authors = {"Socratic_Phoenix"}, description = "StarAPI is a Sponge Plugin framework that aims to make plugin interactions with the player smooth, and enable them to do things such as control NPCs and create minigame Lobbies.")
public class StarMain extends StarPlugin {
    private static StarMain starMain;

    @Inject
    private Game game;
    private ConversationManager conversationManager;
    private CooldownManager cooldownManager;
    private boolean initialized;

    public static StarMain getOperatingInstance() {
        return StarMain.starMain;
    }

    @Override
    public void onConstruction() {
        this.conversationManager = new ConversationManager();
        this.cooldownManager = new CooldownManager();
        StarMain.starMain = this;
        ASACSerializers.init();
        CIFSerializers.init();
        StarArgumentValueConverter.init();
        File infoDir = new File(this.getConfigDir(), "Additional Information");
        infoDir.mkdirs();
        File cifSpec = new File(infoDir, "CIF Specification.txt");
        File jlscSpec = new File(infoDir, "JLSC Specification.txt");
        File argsSpec = new File(infoDir, "StarArguments Specification.txt");
        try {
            if(!cifSpec.exists()) {
                FileChannelThread thread = new FileChannelThread(cifSpec);
                thread.start();
                thread.write(InformationConstants.CIF_SPEC);
                thread.close();
            }
            if(!jlscSpec.exists()) {
                FileChannelThread thread = new FileChannelThread(jlscSpec);
                thread.start();
                thread.write(InformationConstants.JLSC_SPEC);
                thread.close();
            }
            if(!argsSpec.exists()) {
                FileChannelThread thread = new FileChannelThread(argsSpec);
                thread.start();
                thread.write(InformationConstants.ARGUMENTS_SPEC);
                thread.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Listener
    public void onPreInitialization(GamePreInitializationEvent ev) {
        try {
            ServiceManager manager = this.game.getServiceManager();
            manager.setProvider(this, ConversationManager.class, this.conversationManager);
            manager.setProvider(this, CooldownManager.class, this.cooldownManager);
        } catch (ProviderExistsException e) {
            e.printStackTrace();
        }
        this.initialized = true;
    }

    @Override
    @Listener
    public void onInitialization(GameInitializationEvent ev) {

        new ConversationStartCommand().registerAsSpongeCommand("star.conversation.start");
        new ConversationEndCommand().registerAsSpongeCommand("star.conversation.end");
    }

    @Override
    @Listener
    public void onServerStopping(GameStoppingServerEvent ev) {
    }

    @Override
    @Listener
    public void onServerStopped(GameStoppedServerEvent ev) {
        try {
            this.saveConfig();
            this.saveData();
            this.saveLanguageMapping();
        } catch (IOException | JLSCException e) {
            e.printStackTrace();
        }
    }

    public ConversationManager getConversationManager() {
        this.checkAccess();
        return conversationManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    private void checkAccess() {
        if (!this.initialized) {
            throw new StarAccessException("StarAPI cannot be accessed until after GamePreInitializationEvent.");
        }
    }

    public Game getGame() {
        this.checkAccess();
        return this.game;
    }
}
