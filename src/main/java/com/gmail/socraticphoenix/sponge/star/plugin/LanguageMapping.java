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

    public static LanguageMapping fromCif(CIFTagCompound compound) {
        LanguageMapping mapping = new LanguageMapping();
        Optional<CIFTag<CIFTagCompound>> colorOptional = compound.getCompound("Colors");
        Optional<CIFTag<CIFTagCompound>> messageOptional = compound.getCompound("Messages");
        if(colorOptional.isPresent()) {
            for(CIFTag tag : colorOptional.get().getValue()) {
                if(tag.getTagType() == CIFContentType.STRING && Star.getGameRegistry().getType(TextColor.class, String.valueOf(tag.getValue())).isPresent()) {
                    TextColor value = Star.getGameRegistry().getType(TextColor.class, String.valueOf(tag.getValue())).get();
                    mapping.query(tag.getName(), value);
                }
            }
        }

        if(messageOptional.isPresent()) {
            for(CIFTag tag : messageOptional.get().getValue()) {
                if(tag.getTagType() == CIFContentType.STRING) {
                    mapping.query(tag.getName(), String.valueOf(tag.getValue()));
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

    public CIFCConfiguration toCif() {
        CIFCConfiguration parent = new CIFCConfiguration();

        CIFTagCompound color = new CIFTagCompound();
        CIFTagCompound message = new CIFTagCompound();

        for(String key : this.messages.keySet()) {
            message.add(CIFTag.of(key, this.messages.get(key)));
        }

        parent.add(CIFTag.of("Colors", color));

        for(String key : this.colors.keySet()) {
            color.add(CIFTag.of(key, this.colors.get(key).getId()));
        }

        parent.add(CIFTag.of("Messages", message));

        return parent;
    }

}
