/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.console.terminal;

public class TerminalFactory {

    private static Terminal TERMINAL;

    public static Terminal get(){
        if(TERMINAL == null){
            try{
                if(WindowsTerminal.AVAILABLE) TERMINAL = new WindowsTerminal();
                else if(OSVTerminal.AVAILABLE) TERMINAL = new OSVTerminal();
                else TERMINAL = new UnixTerminal();
            }catch (Exception ignored){}
            if(TERMINAL == null){
                TERMINAL = new UnknownTerminal();
                System.out.println("No supported terminal found.");
            }
            Runtime.getRuntime().addShutdownHook(new Thread(()-> TERMINAL.reset()));
        }
        return TERMINAL;
    }
}
