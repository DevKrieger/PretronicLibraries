package net.prematic.libraries.plugin.lifecycle;

import net.prematic.libraries.plugin.RuntimeEnvironment;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.driver.Driver;
import net.prematic.libraries.plugin.loader.PluginLoader;

public class LifecycleState<R> {


    public static final String CONSTRUCTION = "CONSTRUCTION";

    public static final String INITIALISATION = "INITIALISATION";

    public static final String LOAD = "LOAD";

    public static final String INSTALLATION = "INSTALLATION";

    public static final String BOOTSTRAP = "BOOTSTRAP";

    public static final String RELOAD = "RELOAD";

    public static final String SHUTDOWN = "SHUTDOWN";

    public static final String UNINSTALLATION = "UNINSTALLATION";

    public static final String UNLOAD = "UNLOAD";



    private final PluginDescription description;
    private final PluginLoader<R> loader;
    private final RuntimeEnvironment<R> environment;

    public LifecycleState(PluginDescription description, PluginLoader<R> loader, RuntimeEnvironment<R> environment) {
        this.description = description;
        this.loader = loader;
        this.environment = environment;
    }

    public PluginDescription getDescription() {
        return description;
    }

    public PluginLoader<R> getLoader() {
        return loader;
    }

    public RuntimeEnvironment<R> getEnvironment() {
        return environment;
    }


    public R getRuntime(){
        return this.environment.getInstance();
    }

    public <D extends Driver> D getDriver(Class<D> driverClass){
        return (D) this.loader.getPluginManager().getDriver(driverClass);
    }


}
