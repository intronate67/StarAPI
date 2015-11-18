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
package com.gmail.socraticphoenix.sponge.star.entity.hologram;

import com.gmail.socraticphoenix.sponge.star.StarMain;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.data.manipulator.mutable.entity.InvisibilityData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnCauseBuilder;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.function.Function;

public class Hologram {
    private Location<World> location;
    private ArmorStand entity;
    private Text name;

    public Hologram(Location<World> location, Text name) {
        this.location = location;
        this.name = name;
    }

    public boolean spawn() {
        Optional<Entity> entityOptional = this.location.getExtent().createEntity(EntityTypes.ARMOR_STAND, this.location.getPosition());
        if(entityOptional.isPresent()) {
            this.kill();
            this.entity = (ArmorStand) entityOptional.get();
            this.entity.offer(Keys.DISPLAY_NAME, this.name);
            this.entity.offer(Keys.SHOWS_DISPLAY_NAME, true);
            //this.entity.offer(Keys.INVISIBLE, true);
            this.entity.setGravity(false);
            this.entity.setSmall(true);
            StarMain.getOperatingInstance().getGame().getEventManager().registerListeners(StarMain.getOperatingInstance(), new HologramListener(this));
            this.location.getExtent().spawnEntity(entity, Cause.of(StarMain.getOperatingInstance()));
            return true;
        } else {
            return false;
        }
    }

    public void kill() {
        if(this.entity != null){
            this.entity.remove();
        }
    }

    public void teleport(Location<World> location){
        this.ensureNotNull();
        this.entity.setLocation(location);
    }

    public void setName(Text text) {
        this.name = text;
        this.reiterateKeys();
    }

    public ArmorStand getEntity() {
        return this.entity;
    }

    private void reiterateKeys() {
        if(this.entity != null) {
            this.entity.getOrCreate(DisplayNameData.class).get().set(Keys.DISPLAY_NAME, this.name).set(Keys.SHOWS_DISPLAY_NAME, true);
            //this.entity.offer(Keys.INVISIBLE, true);
            this.entity.setGravity(false);
            this.entity.setSmall(true);        }
    }

    private void ensureNotNull() {
        if(this.entity == null) {
            this.spawn();
        }
    }

}
