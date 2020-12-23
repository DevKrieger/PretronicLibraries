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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
.put(ChronoUnit.YEARS, "y(?:ear)?s?")
            .put(ChronoUnit.MONTHS, "mo(?:nth)?s?")
            .put(ChronoUnit.WEEKS, "w(?:eek)?s?")
            .put(ChronoUnit.DAYS, "d(?:ay)?s?")
            .put(ChronoUnit.HOURS, "h(?:our|r)?s?")
            .put(ChronoUnit.MINUTES, "m(?:inute|in)?s?")
            .put(ChronoUnit.SECONDS, "(?:s(?:econd|ec)?s?)?")
 */
public class DurationProcessor {

    private static final DurationProcessor STANDARD = newBuilder()
            .addUnit(ChronoUnit.YEARS,"Year","Years","y","y(?:ear)?s?")
            .addUnit(ChronoUnit.MONTHS,"Month","Months","mo","mo(?:nth)?s?")
            .addUnit(ChronoUnit.WEEKS,"Week","Weeks","w","w(?:eek)?s?")
            .addUnit(ChronoUnit.DAYS,"Day","Days","d","d(?:ay)?s?")
            .addUnit(ChronoUnit.HOURS,"Hour","Hours","h","h(?:our|r)?s?")
            .addUnit(ChronoUnit.MINUTES,"Minute","Minutes","m","m(?:inute|in)?s?")
            .addUnit(ChronoUnit.SECONDS,"Second","Seconds","s","(?:s(?:econd|ec)?s?)?")
            .build();

    private final List<DurationUnit> units;
    private final Pattern pattern;

    public DurationProcessor(List<DurationUnit> units) {
        Validate.notNull(units);
        this.units = Collections.unmodifiableList(units);

        StringBuilder toCompile = new StringBuilder();
        for (DurationUnit unit : units) {
            toCompile.append("(?:(\\d+)\\s*").append(unit.getPattern()).append("[,\\s]*)?");
        }
        pattern = Pattern.compile(toCompile.toString(), Pattern.CASE_INSENSITIVE);
    }

    public List<DurationUnit> getUnits() {
        return units;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String format(Duration duration){
        return format(duration,false);
    }

    public String format(long time){
        return format(time,false);
    }

    public String formatShort(Duration duration){
        return format(duration,true);
    }

    public String formatShort(long time){
        return format(time,true);
    }

    public String format(Duration duration, boolean short0){
        return format(duration.getSeconds(),short0);
    }

    public String format(long seconds, boolean short0){
        StringBuilder builder = new StringBuilder();

        for (DurationUnit unit : this.units) {
            long time = seconds / unit.getUnit().getDuration().getSeconds();
            if (time > 0) {
                seconds -= unit.getUnit().getDuration().getSeconds() * time;
                builder.append(time).append(unit.toString(time,short0)).append(' ');
            }
            if (seconds <= 0) break;
        }
        if (builder.length() == 0){
            return "0" + this.units.get(this.units.size()-1).getPlural();
        }
        builder.setLength(builder.length()-1);
        return builder.toString();
    }

    public Duration parse(String input){
        if(!input.isEmpty()){
            Matcher matcher = this.pattern.matcher(input);
            if(matcher.matches()){
                matcher.reset();
                while (matcher.find()) {
                    if (matcher.group() == null || matcher.group().isEmpty()) {
                        continue;
                    }
                    Duration duration = Duration.ZERO;
                    int index = 1;
                    for (DurationUnit unit : units) {
                        if (matcher.group(index) != null && !matcher.group(index).isEmpty()) {
                            int time = Integer.parseInt(matcher.group(index));
                            if (time > 0) {
                                duration = duration.plus(unit.getUnit().getDuration().multipliedBy(time));
                            }
                        }
                        index++;
                    }
                    return duration;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Input (%s) cannot be parsed to a Duration", input));
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static DurationProcessor getStandard(){
        return STANDARD;
    }

    private static class Builder {

        private final List<DurationUnit> units;

        protected Builder() {
            this.units = new ArrayList<>();
        }

        public Builder addUnit(DurationUnit unit){
            Validate.notNull(unit);
            this.units.add(unit);
            return this;
        }

        public Builder addUnit(ChronoUnit unit,String singular,String plural,String short0, String pattern){
            return addUnit(new DurationUnit(unit,plural,singular,short0,pattern));
        }

        public DurationProcessor build(){
            return new DurationProcessor(units);
        }

    }

}
