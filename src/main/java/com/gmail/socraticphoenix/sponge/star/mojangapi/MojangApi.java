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

import com.gmail.socraticphoenix.plasma.file.cif.CIFTagCompound;
import com.gmail.socraticphoenix.plasma.file.json.JSONException;
import com.gmail.socraticphoenix.plasma.file.json.JSONObject;
import com.gmail.socraticphoenix.plasma.file.json.JSONParser;
import com.gmail.socraticphoenix.sponge.star.Star;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

public class MojangApi {

    public static final String STATS_URL = "https://status.mojang.com/check";
    public static final String NAME_HISTORY_QUERY_URL = "https://api.mojang.com/user/profiles/%var%/names";
    public static final String UUID_QUERY_URL = "https://api.mojang.com/users/profiles/minecraft/%var%";
    public static final String PROFILE_QUERY_URL = "https://sessionserver.mojang.com/sesssion/minecraft/profile/%var%";

    public static SiteStatusQueryResult getWebsiteStatus() throws IOException, JSONException {
        return new SiteStatusQueryResult(MojangApi.fromUrl(MojangApi.STATS_URL));
    }

    public static NameHistoryQueryResult getNameHistory(UUID uuid) throws IOException, JSONException {
        Optional<NameHistoryQueryResult> resultOptional = MojangApi.getCachedNameHistory(uuid);
        if (resultOptional.isPresent()) {
            return resultOptional.get();
        } else {
            NameHistoryQueryResult historyQueryResult = new NameHistoryQueryResult(MojangApi.fromUrl(MojangApi.NAME_HISTORY_QUERY_URL.replaceAll("%var%", uuid.toString().replaceAll("-", ""))));
            MojangApi.cache(historyQueryResult);
            return historyQueryResult;
        }
    }

    public static UUID fromUnchecked(String string) {
        return UUID.fromString(string.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }

    public static UuidQueryResult getUuid(String name) throws IOException, JSONException {
        Optional<UuidQueryResult> resultOptional = MojangApi.getCachedUuid(name);
        if (resultOptional.isPresent()) {
            return resultOptional.get();
        } else {
            UuidQueryResult uuidQueryResult = new UuidQueryResult(MojangApi.fromUrl(MojangApi.UUID_QUERY_URL.replaceAll("%var%", name)));
            MojangApi.cache(uuidQueryResult);
            return uuidQueryResult;
        }
    }


    private static void cache(UuidQueryResult result) {
        CIFTagCompound cache = MojangApi.getCache();

    }

    private static void cache(NameHistoryQueryResult result) {
        CIFTagCompound cache = MojangApi.getCache();

    }

    private static void cache(ProfileQueryResult result) {

    }

    private static Optional<UuidQueryResult> getCachedUuid(String name) {
        return Optional.empty();
    }

    private static Optional<NameHistoryQueryResult> getCachedNameHistory(UUID uuid) {
        return Optional.empty();
    }

    private static Optional<ApiQueryResult> getCachedProfile(UUID uuid) {
        return Optional.empty();
    }

    private static ApiQueryResult fromUrl(String string) throws IOException, JSONException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        int code = connection.getResponseCode();
        if (code == 204) {
            return new ApiQueryResult(null, code, false);
        } else {
            String content = MojangApi.readAll(reader);
            if (content.startsWith("[")) {
                content = "{\"array\":".concat(content).concat("}");
            }
            return new ApiQueryResult(JSONParser.parse(content), code, true);
        }
    }

    private static String readAll(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            builder.append((char) cp);
        }
        reader.close();
        return builder.toString();
    }

    private static CIFTagCompound getCache() {
        CIFTagCompound data = Star.getStarMain().getData();
        CIFTagCompound cache;
        if (data.getCompound("cache").isPresent()) {
            cache = data.getCompound("cache").get().getValue();
        } else {
            cache = new CIFTagCompound();
            data.put("cache", cache);
        }

        return cache;
    }


}
