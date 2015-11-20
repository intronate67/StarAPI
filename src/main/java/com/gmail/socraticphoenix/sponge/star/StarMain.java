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

import com.gmail.socraticphoenix.plasma.file.asac.ASACList;
import com.gmail.socraticphoenix.plasma.file.asac.ASACNode;
import com.gmail.socraticphoenix.plasma.file.asac.annotation.ASACAnnotation;
import com.gmail.socraticphoenix.plasma.file.asac.values.ASACKeyValue;
import com.gmail.socraticphoenix.plasma.file.asac.values.ASACValue;
import com.gmail.socraticphoenix.plasma.file.asac.values.ASACValueList;
import com.gmail.socraticphoenix.sponge.star.chat.arguments.conversion.StarArgumentValueConverter;
import com.gmail.socraticphoenix.sponge.star.chat.command.conversation.ConversationStartCommand;
import com.gmail.socraticphoenix.sponge.star.chat.command.conversation.ConversationEndCommand;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationListener;
import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationManager;
import com.gmail.socraticphoenix.sponge.star.map.lobby.Lobby;
import com.gmail.socraticphoenix.sponge.star.plugin.PluginInformation;
import com.gmail.socraticphoenix.sponge.star.serialization.asac.ASACSerializers;
import com.gmail.socraticphoenix.sponge.star.serialization.cif.CIFSerializers;
import com.google.inject.Inject;
import java.io.IOException;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;
import com.gmail.socraticphoenix.sponge.star.plugin.StarPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.ServiceManager;

@Plugin (id = "starApi", name = "StarAPI", version = "0.1")
@PluginInformation(authors = {"Socratic_Phoenix"}, description = "StarAPI is a Sponge Plugin framework that aims to make plugin interactions with the player smooth, and enable them to do things such as control NPCs and create minigame Lobbies.")
public class StarMain extends StarPlugin {
    private static StarMain starMain;

    @Inject
    private Game game;
    private List<Lobby> lobbies;
    private ConversationManager conversationManager;

    @Override
    public void onConstruction() {
        this.lobbies = new ArrayList<>();
        this.conversationManager = new ConversationManager();
        StarMain.starMain = this;
        ASACSerializers.init();
        CIFSerializers.init();
        StarArgumentValueConverter.init();
    }

    @Override
    @Listener
    public void onPreInitialization(GamePreInitializationEvent ev) {
        try {
            ServiceManager manager = this.game.getServiceManager();
            manager.setProvider(this, ConversationManager.class, this.getConversationManager());
        } catch (ProviderExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Listener
    public void onInitialization(GameInitializationEvent ev) {

    }

    @Listener
    public void onServerStartingEvent(GameStartedServerEvent ev) {
        this.game.getEventManager().registerListeners(this, new ConversationListener());
        new ConversationEndCommand().registerAsSpongeCommand("end");
        new ConversationStartCommand().registerAsSpongeCommand("conversation");

        Optional<ASACNode> asacNodeOptional = this.getConfig().getNode("Lobbies");
        if(asacNodeOptional.isPresent()) {
            ASACNode lobbies = asacNodeOptional.get();
            if(lobbies.getBoolean("Enabled").isPresent() && lobbies.getBoolean("Enabled").get()) {
                Optional<ASACList> listOptional = lobbies.getList("LobbyWorlds");
                if(listOptional.isPresent()) {
                    ASACList lobbyWorlds = listOptional.get();
                    for(int i = 0; i < lobbyWorlds.size(); i++) {
                        Optional<String> stringOptional = lobbyWorlds.getString(i);
                        if(stringOptional.isPresent() && this.getGame().getServer().getWorld(stringOptional.get()).isPresent()) {
                            this.lobbies.add(new Lobby(this.getGame().getServer().getWorld(stringOptional.get()).get()));
                        }
                    }
                }
            }
        }
    }

    @Override
    @Listener
    public void onServerStopping(GameStoppingServerEvent ev) {
        this.lobbies.forEach(com.gmail.socraticphoenix.sponge.star.map.lobby.Lobby:: killNpcs);
    }

    @Override
    @Listener
    public void onServerStopped(GameStoppedServerEvent ev) {
        try {
            this.saveConfig();
            this.saveData();
            this.saveLanguageMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putDefaultValues(ASACNode node) {
        ASACNode lobbies = new ASACNode("Lobbies");
        lobbies.put("Enabled", false);
        ASACValue value = new ASACValue("world", null);
        value.addAnnotation(ASACAnnotation.IN_LIST);
        ASACKeyValue keyValue = new ASACKeyValue("LobbyWorlds", ASACValueList.buildList(lobbies, value));
        lobbies.putKeyValue(keyValue);
        node.putNode(lobbies);
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    public Game getGame() {
        return this.game;
    }

    public static StarMain getOperatingInstance() {
        return StarMain.starMain;
    }
}
