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
package com.gmail.socraticphoenix.sponge.star.map.lobby.event;

import com.gmail.socraticphoenix.sponge.star.StarMain;
import com.gmail.socraticphoenix.sponge.star.map.lobby.Lobby;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.event.world.TargetWorldEvent;
import org.spongepowered.api.world.World;

public abstract class LobbyEvent extends AbstractEvent implements TargetWorldEvent {
    private Lobby lobby;

    public LobbyEvent(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return this.lobby;
    }


    @Override
    public World getTargetWorld() {
        return this.lobby.getWorld();
    }

    public Game getGame() {
        return StarMain.getOperatingInstance().getGame();
    }

    public static class PlayerInteract extends LobbyEvent {
        private Player player;

        public PlayerInteract(Lobby lobby, Player player) {
            super(lobby);
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public Cause getCause() {
            return Cause.of(this.player);
        }

        public static class Team extends PlayerInteract {

            public Team(Lobby lobby, Player player) {
                super(lobby, player);
            }
        }

        public static class Kit extends PlayerInteract {

            public Kit(Lobby lobby, Player player) {
                super(lobby, player);
            }
        }
    }

    public static class PlayerConnect extends LobbyEvent {
        private Player player;

        public PlayerConnect(Lobby lobby, Player player) {
            super(lobby);
            this.player = player;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public Cause getCause() {
            return Cause.of(this.player);
        }

        public static class Join extends PlayerConnect {
            public Join(Lobby lobby, Player player) {
                super(lobby, player);
            }
        }

        public static class Quit extends PlayerConnect {
            public Quit(Lobby lobby, Player player) {
                super(lobby, player);
            }
        }

    }
}
