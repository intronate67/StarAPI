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
package com.gmail.socraticphoenix.sponge.star.plugin;

import com.gmail.socraticphoenix.plasma.file.asac.ASACNode;
import com.gmail.socraticphoenix.plasma.file.asac.values.ASACKeyValue;
import com.gmail.socraticphoenix.sponge.star.StarMain;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColor;

public class LanguageMapping {
    private Map<String, TextColor> colors;
    private Map<String, String> messages;

    public LanguageMapping() {
        this.colors = new LinkedHashMap<>();
        this.messages = new LinkedHashMap<>();
    }

    public static LanguageMapping fromAsac(ASACNode node) {
        LanguageMapping mapping = new LanguageMapping();
        Optional<ASACNode> colorOptional = node.getNode("Colors");
        Optional<ASACNode> messageOptional = node.getNode("Messages");
        if (colorOptional.isPresent()) {
            for (ASACKeyValue keyValue : colorOptional.get().getAllValues()) {
                String key = keyValue.getKey();
                if (keyValue.getSingletonValue().getObjectValue().isPresent() && keyValue.getSingletonValue().getObjectValue().get() instanceof String && StarMain.getOperatingInstance().getGame().getRegistry().getType(TextColor.class, keyValue.getSingletonValue().getObjectValue().get().toString()).isPresent()) {
                    TextColor value = StarMain.getOperatingInstance().getGame().getRegistry().getType(TextColor.class, String.valueOf(keyValue.getSingletonValue().getObjectValue().get())).get();
                    mapping.query(key, value);
                }
            }
        }

        if(messageOptional.isPresent()) {
            for(ASACKeyValue keyValue : messageOptional.get().getAllValues()) {
                String key = keyValue.getKey();
                if(keyValue.getSingletonValue().getObjectValue().isPresent() && keyValue.getSingletonValue().getObjectValue().get() instanceof String) {
                    String value = String.valueOf(keyValue.getSingletonValue().getObjectValue().get());
                    mapping.query(key, value);
                }
            }
        }

        return mapping;
    }

    public TextColor query(String name, TextColor def) {
        if (this.colors.containsKey(name)) {
            return this.colors.get(name);
        } else {
            this.colors.put(name, def);
            return def;
        }
    }

    public Text query(String name, String def) {
        if (this.messages.containsKey(name)) {
            return Texts.legacy('&').fromUnchecked(this.messages.get(name));
        } else {
            this.messages.put(name, def);
            return this.query(name, def);
        }
    }

    public ASACNode toAsac() {
        ASACNode parent = new ASACNode("Language");
        ASACNode color = new ASACNode("Colors");
        ASACNode message = new ASACNode("Messages");

        for (String key : this.messages.keySet()) {
            String value = this.messages.get(key);
            message.put(key, value);
        }
        parent.putNode(message);

        for (String key : this.colors.keySet()) {
            TextColor value = this.colors.get(key);
            color.put(key, value.getId());
        }
        parent.putNode(color);

        return parent;
    }

}
