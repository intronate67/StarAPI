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

import com.gmail.socraticphoenix.sponge.star.chat.conversation.ConversationManager;
import com.gmail.socraticphoenix.sponge.star.minigame.util.CooldownManager;
import com.gmail.socraticphoenix.sponge.star.scheduler.SpongeRunnable;
import com.gmail.socraticphoenix.sponge.star.scheduler.SpongeRunnableRunnable;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.network.ChannelRegistrar;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

public class Star {

    public static SpongeRunnable getRunnableFrom(Runnable runnable) {
        return new SpongeRunnableRunnable(runnable);
    }

    public static boolean isDefaultWorld(World world) {
        Optional<WorldProperties> defaultWorldOptional = Star.getGame().getServer().getDefaultWorld();
        return defaultWorldOptional.isPresent() && defaultWorldOptional.get().getUniqueId().equals(world.getUniqueId());
    }

    public static File getWorldFile(World world) {
        return Star.getWorldPath(world).toFile();
    }

    public static Path getWorldPath(World world) {
        if(Star.isDefaultWorld(world)) {
            return Star.getWorldsDirectory();
        } else {
            return new File(Star.getWorldsDirectory().toFile(), world.getName()).toPath();
        }
    }

    public static Path getWorldsDirectory() {
        return Star.getGame().getSavesDirectory().resolve(Star.getGame().getServer().getDefaultWorld().get().getWorldName());
    }

    public static Server getServer() {
        return Star.getGame().getServer();
    }

    public static ChannelRegistrar getChannelRegistrar() {
        return Star.getGame().getChannelRegistrar();
    }

    public static Platform getPlatform() {
        return Star.getGame().getPlatform();
    }

    public static PluginManager getPluginManager() {
        return Star.getGame().getPluginManager();
    }

    public static GameDictionary getGameDictionary() {
        return Star.getGame().getGameDictionary();
    }

    public static GameRegistry getGameRegistry() {
        return Star.getGame().getRegistry();
    }

    public static CommandManager getCommandManager() {
        return Star.getGame().getCommandManager();
    }

    public static EventManager getEventManager() {
        return Star.getGame().getEventManager();
    }

    public static ServiceManager getServiceManager() {
        return Star.getGame().getServiceManager();
    }

    public static CooldownManager getCooldownManager() {
        return Star.getStarMain().getCooldownManager();
    }

    public static ConversationManager getConversationManager() {
        return Star.getStarMain().getConversationManager();
    }

    public static Game getGame() {
        return Star.getStarMain().getGame();
    }

    public static StarMain getStarMain() {
        return StarMain.getOperatingInstance();
    }

}
