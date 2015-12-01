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
import com.gmail.socraticphoenix.plasma.file.cif.CIFTagCompound;
import com.gmail.socraticphoenix.plasma.file.cif.cifc.CIFCConfiguration;
import com.gmail.socraticphoenix.plasma.file.cif.tags.CIFContentType;
import com.gmail.socraticphoenix.plasma.file.cif.tags.CIFTag;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSCCompound;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSConfiguration;
import com.gmail.socraticphoenix.plasma.file.jlsc.value.JLSCPair;
import com.gmail.socraticphoenix.sponge.star.Star;
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

    public JLSCCompound toJLSC() {
        JLSCCompound root = new JLSCCompound();

        JLSCCompound color = new JLSCCompound();
        JLSCCompound message = new JLSCCompound();

        this.messages.keySet().stream().forEach(key -> message.put(key, this.messages.get(key)));
        this.colors.keySet().stream().forEach(key -> message.put(key, this.colors.get(key).getId()));

        root.put("Colors", color);
        root.put("Messages", message);

        return root;
    }

    public static LanguageMapping fromJLSC(JLSConfiguration compound) {
        LanguageMapping mapping = new LanguageMapping();
        Optional<JLSCPair> colorOptional = compound.get("Colors");
        Optional<JLSCPair> messageOptional = compound.get("Messages");
        if(colorOptional.isPresent() && colorOptional.get().getAsCompound().isPresent()) {
            Optional<JLSCCompound> compoundOptional = colorOptional.get().getAsCompound();
            compoundOptional.get().stream().forEach(pair -> mapping.query(pair.getKey(), Star.getGameRegistry().getType(TextColor.class, String.valueOf(pair.getRawValue().orElse("NONE"))).get()));
        }
        if(messageOptional.isPresent() && messageOptional.get().getAsCompound().isPresent()) {
            Optional<JLSCCompound> compoundOptional = messageOptional.get().getAsCompound();
            compoundOptional.get().stream().forEach(pair -> mapping.query(pair.getKey(), String.valueOf(pair.getRawValue().orElse("NULL"))));
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



}
