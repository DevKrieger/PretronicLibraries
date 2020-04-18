/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.03.20, 17:04
 * @web %web%
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

package net.pretronic.libraries.message.bml;

import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;

public class Message {

    private final Module root;

    public Message(Module root) {
        this.root = root;
    }

    public Object build(BuildContext context){
        return root.build(context);
    }

    public String buildToString(BuildContext context) {
        return build(context).toString();
    }

    public static Message ofStaticText(String text){
        return new Message(new Module(null,new StaticTextMessageBuilder(text)));
    }
}
