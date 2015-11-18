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

import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.map.lobby.event.LobbyEvent;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.entity.*;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.ChangeWorldWeatherEvent;
import org.spongepowered.api.world.weather.Weathers;

public class LobbyListener {
    private Lobby lobby;
    private Game game;

    public LobbyListener(Lobby lobby) {
        this.lobby = lobby;
        this.game = StarMain.getOperatingInstance().getGame();
    }

    @Listener
    public void onBlockChangeEvent(ChangeBlockEvent ev) {
        if (this.lobby.is(ev.getTargetWorld())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onBlockCollideEvent(CollideBlockEvent ev) {
        if (this.lobby.is(ev.getTargetLocation().getExtent())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onBlockInteractEvent(InteractBlockEvent ev) {
        if (ev.getTargetBlock().getLocation().isPresent() && this.lobby.is(ev.getTargetBlock().getLocation().get().getExtent())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onBlockGrowEvent(GrowBlockEvent ev) {
        if (this.lobby.is(ev.getTargetWorld())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onBlockMoveEvent(MoveBlockEvent ev) {
        if (this.lobby.is(ev.getTargetWorld())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onBlockTickEvent(TickBlockEvent ev) {
        if (ev.getTargetBlock().getLocation().isPresent() && this.lobby.is(ev.getTargetBlock().getLocation().get().getExtent())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onSpawn(SpawnEntityEvent ev) {
        if(this.lobby.is(ev.getTargetWorld()) && !ev.getCause().any(StarMain.class)) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onDamage(InteractEntityEvent.Primary ev){
        if(this.lobby.is(ev.getTargetEntity().getLocation().getExtent())) {
            ev.setCancelled(true);
        }
    }

    @Listener
    public void onWeatherChange(ChangeWorldWeatherEvent ev){
        if(this.lobby.is(ev.getTargetWorld())) {
            ev.setWeather(Weathers.CLEAR);
        }
    }

    @Listener
    public void onLogIn(ClientConnectionEvent.Join ev){
        if(this.lobby.is(ev.getTargetEntity().getLocation().getExtent())) {
            ev.getTargetEntity().setLocation(this.lobby.getSpawn());
        }
    }

    @Listener
    public void onChangeWorld(DisplaceEntityEvent.Teleport.TargetPlayer ev) {
        if (! this.lobby.is(ev.getFromTransform().getExtent()) || ! this.lobby.is(ev.getToTransform().getExtent())) {
            if (this.lobby.is(ev.getFromTransform().getExtent())) {
                LobbyEvent toCall = new LobbyEvent.PlayerConnect.Quit(this.lobby, ev.getTargetEntity());
                this.game.getEventManager().post(toCall);
            } else if (this.lobby.is(ev.getToTransform().getExtent())) {
                ev.getToTransform().setLocation(this.lobby.getSpawn());
                LobbyEvent toCall = new LobbyEvent.PlayerConnect.Join(this.lobby, ev.getTargetEntity());
                this.game.getEventManager().post(toCall);
            }
        }
    }

    @Listener
    public void onInteractEntity(InteractEntityEvent ev) {
        if (this.lobby.is(ev.getTargetEntity().getLocation().getExtent())) {
            if (ev.getCause().root().isPresent() && ev.getCause().root().get() instanceof Player) {
                Player player = (Player) ev.getCause().root().get();
                Entity entity = ev.getTargetEntity();
                LobbyEvent toCall;
                if (this.lobby.getKitNpc().isPresent() && this.lobby.getKitNpc().get().getEntity().getUniqueId().equals(entity.getUniqueId())) {
                    toCall = new LobbyEvent.PlayerInteract.Kit(this.lobby, player);
                } else if (this.lobby.getTeamNpc().isPresent() && this.lobby.getTeamNpc().get().getEntity().getUniqueId().equals(entity.getUniqueId())) {
                    toCall = new LobbyEvent.PlayerInteract.Team(this.lobby, player);
                } else {
                    toCall = new LobbyEvent.PlayerInteract(this.lobby, player);
                }
                this.game.getEventManager().post(toCall);
            }
            ev.setCancelled(true);
        }
    }

}
