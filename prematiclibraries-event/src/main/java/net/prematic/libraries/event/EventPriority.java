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

package net.prematic.libraries.event;

/**
 * If more the one event is registered, the priority decides which event is executed first and last.
 *
 * <p>Execution order: Low -> Normal -> Height</p>
 */
public class EventPriority {

    public final static byte EXTREM_HEIGHT = 127;

    public final static byte HEIGHT = 100;

    public final static byte NORMAL = 50;

    public final static byte LOW = 0;

    public final static byte EXTREM_LOW = -127;

}
