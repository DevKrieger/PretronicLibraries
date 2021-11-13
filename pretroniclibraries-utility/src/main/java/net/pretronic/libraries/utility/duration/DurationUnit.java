/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.05.20, 12:05
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

package net.pretronic.libraries.utility.duration;

import net.pretronic.libraries.utility.Validate;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class DurationUnit {


    private final TemporalUnit unit;
    private final String plural;
    private final String singular;
    private final String short0;
    private final String pattern;

    protected DurationUnit(TemporalUnit unit, String plural, String singular, String short0, String pattern) {
        Validate.notNull(unit,plural,short0,short0,pattern);
        this.unit = unit;
        this.plural = plural;
        this.singular = singular;
        this.short0 = short0;
        this.pattern = pattern;
    }

    public TemporalUnit getUnit() {
        return unit;
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }

    public String getShort0() {
        return short0;
    }

    public String getPattern() {
        return pattern;
    }

    public String toString(long input, boolean short0){
        if(short0) return this.short0;
        else return input == 1 ? singular : plural;
    }

    public static DurationUnit of(ChronoUnit unit,String singular,String plural,String short0, String pattern){
        return new DurationUnit(unit,plural,singular,short0,pattern);
    }
}
