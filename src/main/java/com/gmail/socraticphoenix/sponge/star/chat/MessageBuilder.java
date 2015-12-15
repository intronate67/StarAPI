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
package com.gmail.socraticphoenix.sponge.star.chat;

import com.gmail.socraticphoenix.plasma.math.PlasmaRandomUtil;
import com.gmail.socraticphoenix.sponge.star.Star;
import com.gmail.socraticphoenix.sponge.star.plugin.StarPlugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageBuilder {
    private TextBuilder builder;
    private MessageBuilder parent;

    public MessageBuilder() {
        this.builder = Texts.builder();
    }

    public MessageBuilder(String content) {
        this.builder = Texts.builder(content);
    }

    public MessageBuilder(Text text, String content) {
        this.builder = Texts.builder(text, content);
    }

    public MessageBuilder(Text text) {
        this.builder = text.builder();
    }

    public static MessageBuilder of() {
        return new MessageBuilder();
    }

    public static MessageBuilder of(String content) {
        return new MessageBuilder(content);
    }

    public static MessageBuilder of(Text text, String content) {
        return new MessageBuilder(text, content);
    }

    public static MessageBuilder of(Text text) {
        return new MessageBuilder(text);
    }

    public MessageBuilder sub() {
        MessageBuilder builder = new MessageBuilder();
        builder.parent = this;
        return builder;
    }

    public MessageBuilder parent() {
        if(this.parent == null) {
            return this;
        } else {
            this.parent.append(this.build());
            return this.parent;
        }
    }

    public MessageBuilder randomColor() {
        this.color(this.getRandomColor());
        return this;
    }

    public MessageBuilder randomColor(TextColor... exlcudes) {
        this.color(this.getRandomColor(exlcudes));
        return this;
    }

    public MessageBuilder appendLegacy(String... strings) {
        Stream.of(strings).forEach(string -> this.append(Texts.legacy('&').fromUnchecked(string)));
        return this;
    }

    public MessageBuilder color(StarPlugin plugin, String name, TextColor def) {
        this.color(plugin.getLanguageMapping().query(name, def));
        return this;
    }

    public MessageBuilder highlight(String text, TextColor highlight) {
        return this.sub().append(text).color(highlight).parent();
    }

    public MessageBuilder highlight(String text) {
        return this.highlight(text, this.getRandomColor());
    }

    private TextColor getRandomColor() {
        List<TextColor> textColors = Star.getGameRegistry().getAllOf(TextColor.class).stream().collect(Collectors.toList());
        return PlasmaRandomUtil.randomValue(textColors);
    }

    private TextColor getRandomColor(TextColor... excludes) {
        List<TextColor> textColors = Star.getGameRegistry().getAllOf(TextColor.class).stream().filter(color -> !Stream.of(excludes).collect(Collectors.toList()).contains(color)).collect(Collectors.toList());
        return PlasmaRandomUtil.randomValue(textColors);
    }

    public MessageBuilder append(String... strings) {
        Stream.of(strings).forEach(string -> this.append(Texts.of(string)));
        return this;
    }

    public String getContent() {
        return Texts.toPlain(this.build());
    }

    public MessageBuilder append(Text... children) {
        builder.append(children);
        return this;
    }

    public MessageBuilder insert(int pos, Iterable<? extends Text> children) {
        builder.insert(pos, children);
        return this;
    }

    public Optional<HoverAction<?>> getHoverAction() {
        return builder.getHoverAction();
    }

    public TextFormat getFormat() {
        return builder.getFormat();
    }

    public MessageBuilder format(TextFormat format) {
        builder.format(format);
        return this;
    }

    public MessageBuilder onShiftClick(ShiftClickAction<?> shiftClickAction) {
        builder.onShiftClick(shiftClickAction);
        return this;
    }

    public MessageBuilder toText() {
        builder.toText();
        return this;
    }

    public MessageBuilder style(TextStyle... styles) {
        builder.style(styles);
        return this;
    }

    public MessageBuilder remove(Iterable<? extends Text> children) {
        builder.remove(children);
        return this;
    }

    public MessageBuilder color(TextColor color) {
        builder.color(color);
        return this;
    }

    public MessageBuilder append(Iterable<? extends Text> children) {
        builder.append(children);
        return this;
    }

    public TextColor getColor() {
        return builder.getColor();
    }

    public MessageBuilder remove(Text... children) {
        builder.remove(children);
        return this;
    }

    public TextStyle getStyle() {
        return builder.getStyle();
    }

    public List<Text> getChildren() {
        return builder.getChildren();
    }

    public MessageBuilder insert(int pos, Text... children) {
        builder.insert(pos, children);
        return this;
    }

    public MessageBuilder onHover(HoverAction<?> hoverAction) {
        builder.onHover(hoverAction);
        return this;
    }

    public Text buildParent() {
        return this.parent().build();
    }

    public Text build() {
        return builder.build();
    }

    public Optional<ClickAction<?>> getClickAction() {
        return builder.getClickAction();
    }

    public MessageBuilder removeAll() {
        builder.removeAll();
        return this;
    }


    public Optional<ShiftClickAction<?>> getShiftClickAction() {
        return builder.getShiftClickAction();
    }

    public MessageBuilder onClick(ClickAction<?> clickAction) {
        builder.onClick(clickAction);
        return this;
    }
}
