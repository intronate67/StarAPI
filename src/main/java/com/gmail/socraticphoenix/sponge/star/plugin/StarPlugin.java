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

import com.gmail.socraticphoenix.plasma.file.FileChannelThread;
import com.gmail.socraticphoenix.plasma.file.asac.ASACException;
import com.gmail.socraticphoenix.plasma.file.asac.ASACNode;
import com.gmail.socraticphoenix.plasma.file.asac.ASACParser;
import com.gmail.socraticphoenix.plasma.file.cif.CIFTagCompound;
import com.gmail.socraticphoenix.plasma.file.cif.io.CIFException;
import com.gmail.socraticphoenix.plasma.file.cif.util.CIFUtil;
import com.gmail.socraticphoenix.sponge.star.StarLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;

public abstract class StarPlugin {
    private ASACNode config;
    private CIFTagCompound data;

    private Logger logger;

    private File configDir;

    private String[] authors;
    private String name;
    private String version;
    private String id;
    private String description;

    public StarPlugin() {
        try {
            this.grabFromAnnotation();
            this.initializeLogger();
            this.initializeConfig();
            this.initializeData();
            this.writeInformationFile();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        this.logger.info("--- StarPlugin Constructed ---");
        this.logger.info("name= ".concat(this.name));
        this.logger.info("version= ".concat(this.version));
        this.logger.info("id= ".concat(this.id));
        this.logger.info("author(s)= ".concat(this.getHumanReadableAuthorString()));
        this.logger.info("description= ".concat(this.description));
        this.logger.info("------------------------------");


        this.onConstruction();
    }

    public String[] getAuthors() {
        return this.authors;
    }

    public String getHumanReadableAuthorString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.authors.length; i++) {
            String author = this.authors[i];
            if(i != 0 && i == this.authors.length - 2) {
                builder.append(author).append(" and ");
            } else if (i == this.authors.length - 1) {
                builder.append(author);
            } else {
                builder.append(author).append(", ");
            }
        }
        return builder.toString();
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getVersion() {
        return this.version;
    }

    public ASACNode getConfig() {
        return this.config;
    }

    public File getConfigDir() {
        return this.configDir;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void saveConfig() throws IOException {
        this.config.writeToFile(new File(this.configDir, StarLang.CONFIG_NAME.toString()));
    }

    public void reloadConfig() throws IOException, ASACException {
        this.initializeConfig();
    }

    public CIFTagCompound getData() {
        return this.data;
    }

    public void saveData() throws IOException {
        CIFUtil.writeToFile(new File(this.configDir, StarLang.DATA_NAME.toString()), this.data, true);
    }

    private void initializeLogger() {
        this.logger = LoggerFactory.getLogger(this.id);
    }

    protected void putDefaultValues(ASACNode node) {

    }

    protected void putDefaultValues(CIFTagCompound data) {

    }

    private void writeInformationFile() throws IOException {
        File info = new File(this.configDir, StarLang.INFORMATION_NAME.toString());
        if(!info.exists()) {
            FileChannelThread thread = new FileChannelThread(info);
            thread.start();

            String ls = System.lineSeparator();
            thread.write("== Information File For ".concat(this.name).concat(" =="), ls);
            thread.write("Name= ".concat(this.name), ls);
            thread.write("Id= ".concat(this.id), ls);
            thread.write("Authors= ".concat(this.getHumanReadableAuthorString()), ls, ls);
            thread.write("Description:", ls, this.description, ls, ls);
            thread.write("Config:", ls, "The config file uses the ASAC format, or At-Symbol-Annotated-Configuration. The config file is human readable, and generally easy to set values in.", ls, ls);
            thread.write("Data:", ls, "The data file uses the CIF format, or Compressed Information Format. It is not human readable, and is intended to store internal information only.");

            thread.close();
        }
    }

    private void initializeData() throws IOException, CIFException {
        File data = new File(this.configDir, StarLang.DATA_NAME.toString());
        if(data.exists()) {
            this.data = CIFUtil.parseFromFile(data, true);
        } else {
            this.data = new CIFTagCompound();
            this.putDefaultValues(this.data);
        }
        CIFUtil.writeToFile(data, this.data, true);
    }

    private void initializeConfig() throws IOException, ASACException {
        File modDir = new File(StarLang.CONFIG_DIR.toString());
        File localDir = new File(modDir, this.name);
        this.configDir = localDir;
        this.configDir.mkdirs();
        File config = new File(localDir, StarLang.CONFIG_NAME.toString());
        if(config.exists()) {
            this.config = ASACParser.parseFile(config, null);
        } else {
            this.config = new ASACNode("Config");
            this.putDefaultValues(this.config);
        }
        this.config.writeToFile(new File(this.configDir, StarLang.CONFIG_NAME.toString()));
    }

    private void grabFromAnnotation() {
        for(Annotation annotation : this.getClass().getAnnotations()){
            if(annotation instanceof Plugin){
                Plugin plugin = (Plugin) annotation;
                this.name = plugin.name();
                this.version = plugin.version();
                this.id = plugin.id();
            } else if (annotation instanceof PluginInformation) {
                PluginInformation info = (PluginInformation) annotation;
                this.description = info.description();
                this.authors = info.authors();
            }
        }
    }

    public abstract void onConstruction();
    public abstract void onPreInitialization(GamePreInitializationEvent ev);
    public abstract void onInitialization(GameInitializationEvent ev);
    public abstract void onServerStopping(GameStoppingServerEvent ev);
    public abstract void onServerStopped(GameStoppedServerEvent ev);
}