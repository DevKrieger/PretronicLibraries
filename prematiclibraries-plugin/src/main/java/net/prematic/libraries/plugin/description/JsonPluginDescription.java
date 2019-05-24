package net.prematic.libraries.plugin.description;

import com.google.gson.JsonObject;
import net.prematic.libraries.utility.document.JsonDocument;

import java.io.File;
import java.util.UUID;

public class JsonPluginDescription extends JsonDocument implements PluginDescription{

    private final UUID id;
    private final File location;
    private final PluginMainClass mainClass;

    public JsonPluginDescription(File location) {
        this.location = location;
        this.id = getObject("id",UUID.class);
        this.mainClass = PluginMainClass.readFromDocumentEntry(getEntry("mainClass"));
    }

    public JsonPluginDescription(String content, File location) {
        super(content);
        this.location = location;
        this.id = getObject("id",UUID.class);
        this.mainClass = PluginMainClass.readFromDocumentEntry(getEntry("mainClass"));
    }

    public JsonPluginDescription(JsonObject content, File location) {
        super(content);
        this.location = location;
        this.id = getObject("id",UUID.class);
        this.mainClass = PluginMainClass.readFromDocumentEntry(getEntry("mainClass"));
    }

    @Override
    public File getPluginLocation() {
        return this.location;
    }

    @Override
    public String getName() {
        return getString("name");
    }

    @Override
    public String getDescription() {
        return getString("description");
    }

    @Override
    public String getAuthor() {
        return getString("author");
    }

    @Override
    public String getWebsite() {
        return getString("website");
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public PluginVersion getVersion() {
        return null;
    }

    @Override
    public PluginMainClass getMainClass() {
        return this.mainClass;
    }

    public static void load(File jarFile){

    }
}
