/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 27.10.19, 13:36
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

package net.prematic.libraries.utility.io;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public interface IOConsumer<T> {

    void accept(T t) throws IOException;

    default IOConsumer<T> andThen(Consumer<? super T> after){
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }

}
