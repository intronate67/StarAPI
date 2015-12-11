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
package com.gmail.socraticphoenix.sponge.star.mojangapi;

import com.gmail.socraticphoenix.plasma.file.json.JSONObject;
import com.gmail.socraticphoenix.plasma.file.json.value.JSONKeyValue;

import java.util.HashMap;
import java.util.Map;

public class SiteStatusQueryResult {
    public static final String MINECRAFT_NET = "minecraft.net";
    public static final String SESSION_MINECRAFT_NET = "session.minecraft.net";
    public static final String ACCIOUNT_MOJANG_COM = "account.mojang.com";
    public static final String AUTH_MOJANG_COM = "auth.mojang.com";
    public static final String SKINS_MINECRAFT_NET = "skins.minecraft.net";
    public static final String AUTHSERVER_MOJANG_COM = "authserver.mojang.com";
    public static final String SESSIONSERVER_MOJANG_COM = "sessionserver.mojang.com";
    public static final String API_MOJANG_COM = "api.mojang.com";
    public static final String TEXTURES_MINECRAFT_NET = "textures.minecraft.net";
    private Map<String, MojangStatus> statusMap;
    private int code;
    private boolean successful;
    private JSONObject response;

    public SiteStatusQueryResult() {
        this.statusMap = new HashMap<>();
    }

    public SiteStatusQueryResult(ApiQueryResult wrapping) {
        this.code = wrapping.getCode();
        this.successful = wrapping.isSuccessful();
        this.response = wrapping.getResponse();
        this.statusMap = new HashMap<>();
        this.response.getArray("array").get().stream().filter(testValue -> testValue.getJsonObjectValue().isPresent()).forEach(value -> {
            JSONKeyValue valueOptional = value.getJsonObjectValue().get().getAllValues().stream().findFirst().get();
            this.statusMap.put(valueOptional.getKey(), MojangStatus.valueOf(valueOptional.getStringValue().get().toUpperCase()));
        });
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public JSONObject getResponse() {
        return response;
    }

    public MojangStatus getStatusFor(String identifier) {
        return this.statusMap.get(identifier);
    }

}
