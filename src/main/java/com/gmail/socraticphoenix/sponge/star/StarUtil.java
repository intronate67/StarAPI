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

import com.gmail.socraticphoenix.plasma.collection.PlasmaListUtil;
import com.gmail.socraticphoenix.plasma.file.cif.CIFArray;
import com.gmail.socraticphoenix.plasma.file.cif.CIFTagCompound;
import com.gmail.socraticphoenix.plasma.file.cif.CIFValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.service.user.UserStorageService;

public class StarUtil {

    public static Optional<User> getOfflinePlayer(UUID id) {
        GameProfileManager resolver = Star.getServer().getGameProfileManager();
        UserStorageService storage = StarMain.getOperatingInstance().getGame().getServiceManager().provideUnchecked(UserStorageService.class);
        try {
            return storage.get(resolver.get(id).get());
        } catch (InterruptedException | ExecutionException ignored) {

        }
        return Optional.empty();
    }

    public static Optional<User> getOfflinePlayer(String name) {
        GameProfileManager resolver = Star.getServer().getGameProfileManager();
        UserStorageService storage = StarMain.getOperatingInstance().getGame().getServiceManager().provideUnchecked(UserStorageService.class);
        try {
            return storage.get(resolver.get(name).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static String toJson(ConfigurationNode node) throws IOException {
        StringWriter writer = new StringWriter();
        GsonConfigurationLoader.builder().build().saveInternal(node, writer);
        return writer.toString();
    }

    public static String serializeToJson(DataContainer container) throws IOException {
        return StarUtil.toJson(ConfigurateTranslator.instance().translateData(container));
    }

    public static <T extends DataSerializable> Optional<T> deSerializeJson(String json, Class<T> type) throws IOException {
        DataView target = ConfigurateTranslator.instance().translateFrom(GsonConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(json))).build().load());
        DataManager manager = Star.getGame().getDataManager();
        return manager.deserialize(type, target);
    }

}
