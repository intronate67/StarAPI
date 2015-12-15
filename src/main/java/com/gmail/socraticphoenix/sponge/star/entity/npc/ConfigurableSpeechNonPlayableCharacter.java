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
package com.gmail.socraticphoenix.sponge.star.entity.npc;

import com.gmail.socraticphoenix.plasma.file.asac.ASACNode;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSCCompound;
import com.gmail.socraticphoenix.plasma.math.PlasmaRandomUtil;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;

public class ConfigurableSpeechNonPlayableCharacter implements NonPlayableCharacter {
    private UnmovingNonPlayableCharacter wrapped;
    private String speech;

    public ConfigurableSpeechNonPlayableCharacter(JLSCCompound node, World world) {
        if (! (node.getString("name").isPresent() && node.getString("location").isPresent() && node.getString("type").isPresent() && node.getString("speech").isPresent())) {
            throw new IllegalArgumentException("Cannot create a ConfigurableSpeechNonPlayableCharacter from an ASACNode lacking one of the following keys: name, location, type, speech");
        }

        String nameS = node.getString("name").get();
        Text name = Texts.builder(nameS).color(this.randomColor()).build();
        Location<World> worldLocation = this.generateLocation(node.getString("location").get(), world);
        EntityType type = this.getType(node.getString("type").get());
        this.wrapped = new UnmovingNonPlayableCharacter(worldLocation, type, name);
        this.speech = node.getString("speech").get();

        ConfigurableSpeechNonPlayableCharacterListener listener = new ConfigurableSpeechNonPlayableCharacterListener(this);
        StarMain.getOperatingInstance().getGame().getEventManager().registerListeners(StarMain.getOperatingInstance(), listener);
    }

    @Override
    public Entity getEntity() {
        return this.wrapped.getEntity();
    }

    @Override
    public Text getName() {
        return this.wrapped.getName();
    }

    @Override
    public void move(Location<World> to) {
        this.wrapped.move(to);
    }

    @Override
    public void teleport(Location<World> to) {
        this.wrapped.teleport(to);
    }

    @Override
    public void setName(Text text) {
        this.wrapped.setName(text);
    }

    @Override
    public void setType(EntityType type) {
        this.wrapped.setType(type);
    }

    @Override
    public void kill() {
        this.wrapped.kill();
    }

    @Override
    public void respawn() {
        this.wrapped.respawn();
    }

    public Text getSpeech() {
        return this.getName().builder().append(Texts.builder("> ").color(TextColors.GRAY).append(Texts.builder(this.speech).color(TextColors.GRAY).build()).build()).build();
    }

    private TextColor randomColor() {
        Collection<TextColor> textColor = StarMain.getOperatingInstance().getGame().getRegistry().getAllOf(TextColor.class);
        return PlasmaRandomUtil.randomValue(textColor.toArray(new TextColor[textColor.size()]));
    }

    private EntityType getType(String id) {
        return StarMain.getOperatingInstance().getGame().getRegistry().getType(EntityType.class, id.toUpperCase()).get();
    }

    private Location<World> generateLocation(String string, World world) {
        String[] pieces = string.replaceAll(" ", "").split(",");
        double x = Double.parseDouble(pieces[0]);
        double y = Double.parseDouble(pieces[1]);
        double z = Double.parseDouble(pieces[2]);
        return new Location<>(world, x, y, z);
    }

}
