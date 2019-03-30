/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 24.03.19 19:42
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

package net.prematic.libraries.logging.format;

import net.prematic.libraries.logging.InfoAbleException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class helps formatting messages and throwable classes.
 */
public final class FormatHelper {

    //From Java Throwable
    private static final String CAUSE_CAPTION = "Caused by: ";

    private static final String SUPPRESSED_CAPTION = "Suppressed: ";

    private static final String DEFAULT_CAPTION = "";

    /**
     * Append the objects to the message. Adds sequentially the objects to the message by every {}.
     *
     * <p>{} -> Object</p>
     *
     * @param message The message which should be formatted.
     * @param objects The objects for the formation.
     * @return The formatted message.
     */
    public static String format(String message, Object... objects){
        final StringBuilder builder = new StringBuilder();
        char last = ' ';
        int position = 0;
        for(char c : message.toCharArray()){
            if(c == '}' && last == '{'){
                builder.setLength(builder.length()-1);
                builder.append(objects.length>position?objects[position]:"Unknown");
                position++;
            }else{
                builder.append(c);
                last = c;
            }
        }

        return builder.toString();
    }

    /**
     * This method builds a stack trace of a throwable object.
     *
     * @param builder The string builder, where the messages should be added.
     * @param thread The thrown execution thread.
     * @param throwable The throwable for witch the stack trace should be built.
     * @param prefix The prefix before the stack trace.
     */
    public static void buildStackTrace(StringBuilder builder,Thread thread, Throwable throwable, String prefix){
        builder.append(prefix).append("Exception ");

        if(throwable instanceof InfoAbleException && ((InfoAbleException) throwable).getInfo() != null
                && ((InfoAbleException) throwable).getInfo().getService() != null)
            builder.append("from ").append(((InfoAbleException) throwable).getInfo().getService().getName()).append(' ');

        if(thread != null) builder.append("in ").append(thread.getName()).append('/').append(thread.getId()).append(' ');
        builder.append("-> ");

        buildSubStackTrace(builder,throwable,null,DEFAULT_CAPTION,prefix, ConcurrentHashMap.newKeySet());
    }

    private static void buildSubStackTrace(StringBuilder builder,Throwable throwable, StackTraceElement[] previousTrace, String caption, String prefix, Set<Throwable> used) {
        if(!used.contains(throwable)){
            used.add(throwable);
            if(previousTrace != null) builder.append(prefix);
            builder.append(caption);
            if(throwable instanceof InfoAbleException && ((InfoAbleException) throwable).getInfo() != null){
                boolean has = false;
                if(((InfoAbleException) throwable).getInfo().getService() != null){
                    has = true;
                    builder.append('(').append(((InfoAbleException) throwable).getInfo().getService().getName());
                }
                if(((InfoAbleException) throwable).getInfo().getId() > 0){
                    builder.append(has?'/':'(');
                    builder.append(((InfoAbleException) throwable).getInfo().getId());
                    builder.append(") ");
                }else if(has) builder.append(") ");
            }
            builder.append(throwable).append(System.lineSeparator());

            StackTraceElement[] trace = throwable.getStackTrace();
            
            int traceLength = trace.length-1;
            int previousLength = previousTrace!=null?previousTrace.length-1:-1;

            while (previousLength >=0 && traceLength >= 0 && trace[traceLength].equals(previousTrace[previousLength])) {
                traceLength--; previousLength--;
            }

            for(int i = 0;i<=traceLength;i++) builder.append(prefix).append("\tat ").append(trace[i]).append(System.lineSeparator());

            if(previousTrace != null){
                int more = trace.length-1 - traceLength;
                if(more > 0)  builder.append(prefix).append("\t... ").append(more).append(" more").append(System.lineSeparator());
            }

            for(Throwable subThrowable : throwable.getSuppressed()) buildSubStackTrace(builder, subThrowable,throwable.getStackTrace(),SUPPRESSED_CAPTION, prefix, used);

            Throwable cause = throwable.getCause();
            if(cause != null) buildSubStackTrace(builder, cause, throwable.getStackTrace(),CAUSE_CAPTION, prefix, used);
        }
    }
}
