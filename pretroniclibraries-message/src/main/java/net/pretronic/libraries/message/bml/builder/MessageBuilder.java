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

package net.pretronic.libraries.message.bml.builder;

import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.utility.annonations.Nullable;

public interface MessageBuilder {

    Object build(BuildContext context, boolean requiresString, @Nullable String name
            , Module leftOperator, String operation, Module rightOperator
            , Module[] parameters, @Nullable Module extension, Module next);

    default Object buildModule(Module module,BuildContext context,boolean formatted){
        return Module.build(module, context, formatted);
    }
}
