package net.prematic.libraries.plugin.manager;

import net.prematic.libraries.plugin.PluginLocation;

import java.util.Collection;

public interface PluginContext {

    Collection<PluginLocation> getLocations();

}
