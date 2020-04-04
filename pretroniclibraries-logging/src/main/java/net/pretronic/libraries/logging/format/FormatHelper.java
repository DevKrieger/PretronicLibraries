/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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

package net.pretronic.libraries.logging.format;

import net.pretronic.libraries.logging.InfoAbleException;

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
     * <p>{} - Object</p>
     *
     * @param message The message which should be formatted.
     * @param objects The objects for the formation.
     * @return The formatted message.
     */
    public static String format(String message, Object... objects){
        if(message == null) return "null";
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
        buildStackTrace(builder, thread, throwable, prefix,System.lineSeparator());
    }

    public static void buildStackTrace(StringBuilder builder,Thread thread, Throwable throwable, String prefix, String lineSeparator){
        builder.append(prefix).append("Exception ");

        if(throwable instanceof InfoAbleException && ((InfoAbleException) throwable).getInfo() != null
                && ((InfoAbleException) throwable).getInfo().getService() != null)
            builder.append("from ").append(((InfoAbleException) throwable).getInfo().getService().getName()).append(' ');

        if(thread != null) builder.append("in ").append(thread.getName()).append('/').append(thread.getId()).append(' ');
        builder.append("-> ");

        buildSubStackTrace(builder,throwable,null,DEFAULT_CAPTION,prefix,lineSeparator, ConcurrentHashMap.newKeySet());
    }

    private static void buildSubStackTrace(StringBuilder builder,Throwable throwable, StackTraceElement[] previousTrace, String caption, String prefix, String lineSeparator, Set<Throwable> used) {
        if(!used.contains(throwable)){
            used.add(throwable);
            if(previousTrace != null) builder.append(prefix);
            builder.append(caption);

            buildServiceInfo(builder, throwable);

            builder.append(throwable).append(lineSeparator);

            buildStackTrace(builder, throwable, previousTrace, prefix, lineSeparator);

            for(Throwable subThrowable : throwable.getSuppressed()){
                buildSubStackTrace(builder, subThrowable,throwable.getStackTrace(),SUPPRESSED_CAPTION, prefix,lineSeparator, used);
            }

            Throwable cause = throwable.getCause();
            if(cause != null) buildSubStackTrace(builder, cause, throwable.getStackTrace(),CAUSE_CAPTION, prefix,lineSeparator, used);
        }
    }

    private static void buildStackTrace(StringBuilder builder, Throwable throwable, StackTraceElement[] previousTrace, String prefix, String lineSeparator) {
        StackTraceElement[] trace = throwable.getStackTrace();

        int traceLength = trace.length-1;
        int previousLength = previousTrace!=null?previousTrace.length-1:-1;

        while (previousLength >=0 && traceLength >= 0 && trace[traceLength].equals(previousTrace[previousLength])) {
            traceLength--; previousLength--;
        }

        for(int i = 0;i<=traceLength;i++) builder.append(prefix).append("\tat ").append(trace[i]).append(lineSeparator);

        if(previousTrace != null){
            int more = trace.length-1 - traceLength;
            if(more > 0)  builder.append(prefix).append("\t... ").append(more).append(" more").append(lineSeparator);
        }
    }

    private static void buildServiceInfo(StringBuilder builder, Throwable throwable) {
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
    }
}
