package net.prematic.libraries.plugin.test;

import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.lifecycle.Lifecycle;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;

public class TestPlugin extends Plugin {

    @Lifecycle(state= LifecycleState.CONSTRUCTION)
    public void onConstruction(LifecycleState state){
        System.out.println("Plugin wurde konstruiert");
    }

    @Lifecycle(state=LifecycleState.INITIALISATION)
    public void onInit(LifecycleState state){
        System.out.println("Plugin init");
    }

    @Lifecycle(state=LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        System.out.println("Plugin wurde fertig geladen");
    }

    @Lifecycle(state=LifecycleState.BOOTSTRAP)
    public void onTest(LifecycleState state){
        System.out.println("Plugin wurde gestarten ");
    }

    @Lifecycle(state=LifecycleState.SHUTDOWN)
    public void onShutdown(LifecycleState state){
        System.out.println("Plugin wurde heruntergefahren");
    }

    @Lifecycle(state=LifecycleState.UNLOAD)
    public void onUnload(LifecycleState state){
        System.out.println("Plugin wurde entladen");
    }

}
