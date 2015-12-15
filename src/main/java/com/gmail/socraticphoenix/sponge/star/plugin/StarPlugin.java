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
import com.gmail.socraticphoenix.plasma.file.cif.cifc.CIFCConfiguration;
import com.gmail.socraticphoenix.plasma.file.cif.cifc.io.CIFCReader;
import com.gmail.socraticphoenix.plasma.file.cif.cifc.io.CIFCWriter;
import com.gmail.socraticphoenix.plasma.file.cif.io.CIFException;
import com.gmail.socraticphoenix.plasma.file.cif.util.CIFUtil;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSCCompound;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSCException;
import com.gmail.socraticphoenix.plasma.file.jlsc.JLSConfiguration;
import com.gmail.socraticphoenix.plasma.file.jlsc.io.JLSCReader;
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
    private JLSConfiguration config;
    private JLSConfiguration data;
    private LanguageMapping languageMapping;

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
            if (i != 0 && i == this.authors.length - 2) {
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

    public JLSConfiguration getConfig() {
        return this.config;
    }

    public File getConfigDir() {
        return this.configDir;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void saveLanguageMapping() throws IOException, JLSCException {
        if (this.languageMapping != null) {
            new JLSConfiguration(this.languageMapping.toJLSC(), new File(this.configDir, StarLang.LANGUAGE_NAME.toString())).writeToTarget();
        }
    }

    public void saveConfig() throws IOException, JLSCException {
        this.config.writeToTarget();
    }

    public void reloadConfig() throws IOException, JLSCException {
        this.config.loadFromTarget();
    }

    public JLSConfiguration getData() {
        return this.data;
    }

    public LanguageMapping getLanguageMapping() {
        try {
            this.initializeLanguage(); //Lazy loading because Sponge TextColors aren't available on construction
        } catch (IOException | JLSCException e) {
            e.printStackTrace();
        }
        return this.languageMapping;
    }

    public void saveData() throws IOException, JLSCException {
        this.data.writeCompressedToTarget();
    }

    private void initializeLogger() {
        this.logger = LoggerFactory.getLogger(this.id);
    }

    protected void putDefaultConfigValues(JLSConfiguration node) {

    }

    protected void putDefaultDataValues(JLSConfiguration data) {

    }

    private void writeInformationFile() throws IOException {
        File info = new File(this.configDir, StarLang.INFORMATION_NAME.toString());
        if (!info.exists()) {
            FileChannelThread thread = new FileChannelThread(info);
            thread.start();

            String ls = System.lineSeparator();
            thread.write("== Information File For ".concat(this.name).concat(" =="), ls);
            thread.write("Name= ".concat(this.name), ls);
            thread.write("Id= ".concat(this.id), ls);
            thread.write("Authors= ".concat(this.getHumanReadableAuthorString()), ls, ls);
            thread.write("Description:", ls, this.description, ls, ls);
            thread.write("Config:", ls, "The config file uses the JLSC format, or JSON-Like Structured Configuration. The config file is human readable, and generally easy to set values in.", ls, ls);
            thread.write("Data:", ls, "The data file also uses the JLSC format, however it is its compressed form. Compressed JLSC is not human readable, and is meant for internal storage only.");

            thread.close();
        }
    }

    private void initializeLanguage() throws IOException, JLSCException {
        if(this.languageMapping == null) {
            File language = new File(this.configDir, StarLang.LANGUAGE_NAME.toString());
            if (language.exists()) {
                this.languageMapping = LanguageMapping.fromJLSC(JLSCReader.readFromFile(language));
            } else {
                this.languageMapping = new LanguageMapping();
            }

            this.saveLanguageMapping();
        }
    }

    private void initializeData() throws IOException, JLSCException {
        File data = new File(this.configDir, StarLang.DATA_NAME.toString());
        if (data.exists()) {
            this.data = JLSConfiguration.fromCompressedFile(data);
        } else {
            this.data = new JLSConfiguration(new JLSCCompound(), data);
            this.putDefaultDataValues(this.data);
        }
        this.saveData();
    }

    private void initializeConfig() throws IOException, JLSCException {
        File modDir = new File(StarLang.CONFIG_DIR.toString());
        File localDir = new File(modDir, this.name);
        this.configDir = localDir;
        this.configDir.mkdirs();
        File config = new File(localDir, StarLang.CONFIG_NAME.toString());
        if (config.exists()) {
            this.config = JLSConfiguration.fromFile(config);
        } else {
            this.config = new JLSConfiguration(new JLSCCompound(), config);
            this.putDefaultConfigValues(this.config);
        }
        this.saveConfig();
    }

    private void grabFromAnnotation() {
        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof Plugin) {
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