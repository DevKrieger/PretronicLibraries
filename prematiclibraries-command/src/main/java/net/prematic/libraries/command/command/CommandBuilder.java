/*
 * (C) Copyright 2020 The DKPerms Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.01.20, 22:54
 * @website %web%
 *
 * %license%
 */

package net.prematic.libraries.command.command;

import java.util.function.Consumer;
import java.util.function.Function;

public interface CommandBuilder {

    CommandBuilder name(String name);

    CommandBuilder permission(String permission);

    CommandBuilder description(String description);

    CommandBuilder alias(String... alias);

    CommandBuilder sub(String name, Consumer<CommandBuilder> builder);

    CommandBuilder other(CommandExecutor executor);

    CommandBuilder object(Function<String, Object> getter, Consumer<CommandBuilder> builder);

    CommandBuilder executor(CommandExecutor executor);

    Command build();

}
