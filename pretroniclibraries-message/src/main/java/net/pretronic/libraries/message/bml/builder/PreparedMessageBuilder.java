/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 28.03.20, 11:08
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

public interface PreparedMessageBuilder extends MessageBuilder{

    boolean isUnformattedResultRequired();

    Object build(BuildContext context, boolean requiresString, String name, Object leftOperator
            , String operation, Object rightOperator, Object[] parameters, Object extension, Object next);

    @Override
    default Object build(BuildContext context, boolean requiresString, String name, Module leftOperator0
            , String operation, Module rightOperator0, Module[] parameters0, Module extension0, Module next0) {

        boolean unformatted = isUnformattedResultRequired();

        Object[] parameters = new Object[parameters0.length];
        int index = 0;
        for (Module parameter : parameters0) {
            parameters[index] = parameter.build(context,unformatted);
            index++;
        }

        Object next = null;
        if(next0 != null) next = next0.build(context,requiresString);

        Object leftOperator = buildModule(leftOperator0,context,unformatted);
        Object rightOperator = buildModule(rightOperator0,context, unformatted);
        Object extension = buildModule(extension0,context, unformatted);

        return build(context,requiresString,name
                ,leftOperator,operation,rightOperator
                ,parameters,extension,next);
    }
}
