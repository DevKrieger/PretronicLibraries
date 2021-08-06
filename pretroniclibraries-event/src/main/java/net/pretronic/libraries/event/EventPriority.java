/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.event;

/**
 * If more the one event is registered, the priority decides which event is executed first and last.
 *
 * <p>Execution order: Low - Normal - Height</p>
 */
public class EventPriority {

    public static final byte MONITOR = 127;

    public static final byte HIGHEST = 126;

    public static final byte HIGH = 64;

    public final static byte NORMAL = 0;

    public final static byte LOW = -64;

    public static final byte LOWEST = -128;










}
