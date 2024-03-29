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

import com.gmail.socraticphoenix.plasma.file.jlsc.serialization.annotation.Mapping;
import com.gmail.socraticphoenix.plasma.file.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.plasma.file.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.plasma.file.jlsc.serialization.annotation.Serialize;
import com.gmail.socraticphoenix.plasma.file.json.JSONObject;

import java.util.UUID;

@Serializable
public class UuidQueryResult {
    @Serialize(name = "responseCode")
    private int code;
    @Serialize(name = "successful")
    private boolean successful;
    @Serialize(name = "response")
    private JSONObject response;
    private UUID id;
    private String currentName;

    public UuidQueryResult(ApiQueryResult result) {
        this(result.getResponse(), result.getCode(), result.isSuccessful());
    }

    @SerializationConstructor(mappings = {@Mapping(fieldName = "responseCode", parameterIndex = 1), @Mapping(fieldName = "successful", parameterIndex = 2), @Mapping(fieldName = "response", parameterIndex = 0)})
    public UuidQueryResult(JSONObject response, int code, boolean successful) {
        ApiQueryResult wrapping = new ApiQueryResult(response, code, successful);
        this.code = wrapping.getCode();
        this.successful = wrapping.isSuccessful();
        this.response = wrapping.getResponse();
        this.id = MojangApi.fromUnchecked(this.response.getString("id").get());
        this.currentName = this.response.getString("name").get();
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

    public UUID getUuid() {
        return this.id;
    }

    public String getCurrentName() {
        return this.currentName;
    }

}
