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
package com.gmail.socraticphoenix.sponge.star.map.lobby;

import com.gmail.socraticphoenix.plasma.file.asac.ASACNode;
import com.gmail.socraticphoenix.plasma.file.asac.ASACParser;
import com.gmail.socraticphoenix.plasma.math.PlasmaRandomUtil;
import com.gmail.socraticphoenix.sponge.star.Star;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.entity.npc.ConfigurableSpeechNonPlayableCharacter;
import com.gmail.socraticphoenix.sponge.star.entity.npc.NonPlayableCharacter;
import com.gmail.socraticphoenix.sponge.star.entity.npc.UnmovingNonPlayableCharacter;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lobby {
    private World world;
    private Location<World> spawn;
    private LobbyListener listener;
    private UnmovingNonPlayableCharacter kitNpc;
    private UnmovingNonPlayableCharacter teamNpc;
    private ASACNode config;
    private List<NonPlayableCharacter> characterList;

    public Lobby(World world) {
        this.world = world;
        this.listener = new LobbyListener(this);
        this.characterList = new ArrayList<>();
        this.spawn = world.getSpawnLocation();
        File worldFolder = Star.getWorldFile(world);
        File configFile = new File(worldFolder, "lobby.txt");
        if(configFile.exists()) {
            try {
                this.config = ASACParser.parseFile(configFile, null);
                this.config.writeToFile(configFile);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            if(this.config.getString("spawn").isPresent()) {
                this.spawn = this.generateLocation(this.config.getString("spawn").get(), this.world);
            }

        } else {
            this.config = new ASACNode("Lobby");
        }
        this.initializeFromConfig();
        StarMain.getOperatingInstance().getGame().getEventManager().registerListeners(StarMain.getOperatingInstance(), this.listener);
    }

    public World getWorld(){
        return this.world;
    }

    public Location<World> getSpawn() {
        return this.spawn;
    }

    public boolean is(World world) {
        return this.getWorld().getUniqueId().equals(world.getUniqueId());
    }

    public Optional<UnmovingNonPlayableCharacter> getTeamNpc() {
        return Optional.of(this.teamNpc);
    }

    public Optional<UnmovingNonPlayableCharacter> getKitNpc() {
        return Optional.of(this.kitNpc);
    }

    public void killNpcs() {
        this.characterList.forEach(com.gmail.socraticphoenix.sponge.star.entity.npc.NonPlayableCharacter:: kill);
        this.teamNpc.kill();
        this.kitNpc.kill();
    }

    public void respawnNpcs() {
        this.characterList.forEach(com.gmail.socraticphoenix.sponge.star.entity.npc.NonPlayableCharacter:: respawn);
        this.teamNpc.respawn();
        this.kitNpc.respawn();
    }

    private void initializeFromConfig() {
        if(this.config.getString("kitNPC").isPresent()) {
            this.kitNpc = new UnmovingNonPlayableCharacter(this.generateLocation(this.config.getString("kitNPC").get(), this.world), EntityTypes.VILLAGER, Texts.builder("Choose a Kit").color(this.randomColor()).build());
        }

        if(this.config.getString("teamNPC").isPresent()) {
            this.teamNpc = new UnmovingNonPlayableCharacter(this.generateLocation(this.config.getString("teamNPC").get(), this.world), EntityTypes.VILLAGER, Texts.builder("Choose a Team").color(this.randomColor()).build());
        }

        if(this.config.getNode("Animals").isPresent()) {
            this.characterList.addAll(this.config.getNode("Animals").get().getChildren().stream().map(node -> new ConfigurableSpeechNonPlayableCharacter(node, this.world)).collect(Collectors.toList()));
        }
    }

    private TextColor randomColor() {
        Collection<TextColor> textColor = StarMain.getOperatingInstance().getGame().getRegistry().getAllOf(TextColor.class);
        return PlasmaRandomUtil.randomValue(textColor.toArray(new TextColor[textColor.size()]));
    }

    private Location<World> generateLocation(String string, World world) {
        String[] pieces = string.replaceAll(" ", "").split(",");
        double x = Double.parseDouble(pieces[0]);
        double y = Double.parseDouble(pieces[1]);
        double z = Double.parseDouble(pieces[2]);
        return new Location<>(world, x, y, z);
    }

}
