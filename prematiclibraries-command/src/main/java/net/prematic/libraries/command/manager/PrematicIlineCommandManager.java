package net.prematic.libraries.command.manager;

import jline.console.ConsoleReader;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.command.sender.ConsoleCommandSender;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

public class PrematicIlineCommandManager extends PrematicCommandManager{

    public void startConsoleInputReader(final ConsoleReader reader){
        startConsoleInputReader(reader,new ConsoleCommandSender());
    }

    public void startConsoleInputReader(final ConsoleReader reader, final CommandSender sender){
        this.running = true;
        new Thread(()->{
            while(this.running){
                try{
                    String command = reader.readLine();
                    if(command != null && (!command.equalsIgnoreCase(""))) dispatchCommand(sender,command);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}
