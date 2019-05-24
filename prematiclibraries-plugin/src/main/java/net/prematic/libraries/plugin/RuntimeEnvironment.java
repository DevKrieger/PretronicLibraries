package net.prematic.libraries.plugin;

public class RuntimeEnvironment<T> {

    private final String name;
    private final T instance;

    public RuntimeEnvironment(String name, T instance) {
        this.name = name;
        this.instance = instance;
    }

    public String getName(){
        return this.name;
    }

    public T getInstance(){
        return instance;
    }

}
