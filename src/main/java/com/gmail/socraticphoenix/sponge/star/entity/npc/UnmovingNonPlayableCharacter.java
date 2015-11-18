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

import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.entity.hologram.Hologram;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.entity.EyeLocationProperty;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class UnmovingNonPlayableCharacter implements NonPlayableCharacter {
    private Location<World> current;
    private Entity entity;
    private EntityType type;
    private Text name;
    private Hologram hologram;

    public UnmovingNonPlayableCharacter(Location<World> toSpawn, EntityType type, Text name) {
        this.type = type;
        this.current = toSpawn;
        this.name = name;
        Optional<Entity> entityOptional = toSpawn.getExtent().createEntity(type, toSpawn.getPosition());
        if(entityOptional.isPresent()) {
            this.entity = entityOptional.get();
            this.entity.offer(Keys.AI_ENABLED, false);
            StarMain.getOperatingInstance().getGame().getEventManager().registerListeners(StarMain.getOperatingInstance(), new NonPlayableCharacterDamageListener(this));
            this.current.getExtent().spawnEntity(entity, Cause.of(StarMain.getOperatingInstance()));
            Location<World> hologramLoc;
            if(this.entity.getProperty(EyeLocationProperty.class).isPresent()) {
                hologramLoc = new Location<>(this.entity.getWorld(), this.entity.getProperty(EyeLocationProperty.class).get().getValue());
            } else {
                hologramLoc = this.entity.getLocation();
            }
            this.hologram = new Hologram(hologramLoc, name);
            this.hologram.spawn();
        } else {
            throw new IllegalStateException("Entity could not be created!");
        }
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public Text getName() {
        return this.name;
    }

    @Override
    public void move(Location<World> to) {
        this.teleport(to);
    }

    @Override
    public void teleport(Location<World> to) {
        this.entity.setLocation(to);
        this.hologram.teleport(to);
    }

    @Override
    public void setName(Text text) {
        this.hologram.setName(text);
    }

    @Override
    public void setType(EntityType type) {
        this.type = type;
        this.respawn();
    }

    @Override
    public void kill() {
        if(this.entity != null) {
            this.entity.remove();
        }
    }

    @Override
    public void respawn() {
        this.kill();
        this.hologram.kill();
        Location<World> toSpawn = this.current;
        Optional<Entity> entityOptional = toSpawn.getExtent().createEntity(this.type, toSpawn.getPosition());
        if(entityOptional.isPresent()) {
            this.entity = entityOptional.get();
            this.entity.offer(Keys.AI_ENABLED, false);
            this.current.getExtent().spawnEntity(entity, Cause.of(StarMain.getOperatingInstance()));
            this.hologram = new Hologram(this.entity.getLocation().add(0, 0.25, 0), name);
            this.hologram.spawn();
        }
    }


}
