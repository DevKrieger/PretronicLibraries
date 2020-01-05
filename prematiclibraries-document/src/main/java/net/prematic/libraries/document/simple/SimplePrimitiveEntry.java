/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 14:39
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

package net.prematic.libraries.document.simple;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.entry.ArrayEntry;
import net.prematic.libraries.document.entry.DocumentAttributes;
import net.prematic.libraries.document.entry.DocumentNode;
import net.prematic.libraries.document.entry.PrimitiveEntry;
import net.prematic.libraries.utility.GeneralUtil;

public class SimplePrimitiveEntry implements PrimitiveEntry {

    private transient String key;
    private transient Object value;
    private transient DocumentAttributes attributes;

    public SimplePrimitiveEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object getAsObject() {
        return value;
    }

    @Override
    public String getAsString() {
        return value !=null?value.toString():null;
    }

    @Override
    public char getAsCharacter() {
        if(value == null) return ' ';
        else if(value instanceof Character) return (char) value;
        else return value.toString().charAt(0);
    }

    @Override
    public boolean getAsBoolean() {
        if(value == null) return false;
        else if(value instanceof Boolean) return (boolean) value;
        else if(value instanceof String && ((String) value).equalsIgnoreCase("true")) return true;
        else return value instanceof Number && ((Number) value).doubleValue() == 1;
    }

    @Override
    public Number getAsNumber() {
        if(value == null) return 0;
        else if(value instanceof Number) return (Number) value;
        return getAsDouble();
    }

    @Override
    public byte getAsByte() {
        if(value == null) return 0;
        else if(value instanceof Byte) return (byte) value;
        else if(value instanceof Number) return ((Number) value).byteValue();
        else if(value instanceof String && GeneralUtil.isNumber((String) value)){
            return Byte.parseByte((String)value);
        }
        return 0;
    }

    @Override
    public int getAsInt() {
        if(value == null) return 0;
        else if(value instanceof Integer) return (int) value;
        else if(value instanceof Number) return ((Number) value).intValue();
        else if(value instanceof String && GeneralUtil.isNumber((String) value)){
            return Integer.parseInt((String)value);
        }
        return 0;
    }

    @Override
    public long getAsLong() {
        if(value == null) return 0;
        else if(value instanceof Long) return (long) value;
        else if(value instanceof Number) return ((Number) value).longValue();
        else if(value instanceof String && GeneralUtil.isNumber((String) value)){
            return Long.parseLong((String)value);
        }
        return 0;
    }

    @Override
    public float getAsFloat() {
        if(value == null) return 0;
        else if(value instanceof Float) return (float) value;
        else if(value instanceof Number) return ((Number) value).floatValue();
        else if(value instanceof String && GeneralUtil.isNumber((String) value)){
            return Float.parseFloat((String)value);
        }
        return 0;
    }

    @Override
    public short getAsShort() {
        if(value == null) return 0;
        else if(value instanceof Short) return (short) value;
        else if(value instanceof Number) return ((Number) value).shortValue();
        else if(value instanceof String && GeneralUtil.isNumber((String) value)){
            return Short.parseShort((String)value);
        }
        return 0;
    }

    @Override
    public double getAsDouble() {
        if(value == null) return 0;
        else if(value instanceof Double) return (double) value;
        else if(value instanceof Number) return ((Number) value).doubleValue();
        else if(value instanceof String && GeneralUtil.isNumber((String) value)){
            return Double.parseDouble((String)value);
        }
        return 0;
    }

    @Override
    public void setValue(Object object) {
        this.value = object;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public DocumentAttributes getAttributes() {
        if(attributes == null) attributes = Document.factory().newAttributes();
        return attributes;
    }

    @Override
    public void setAttributes(DocumentAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean hasAttributes() {
        return attributes != null && !attributes.isEmpty();
    }

    @Override
    public PrimitiveEntry toPrimitive() {
        return this;
    }

    @Override
    public ArrayEntry toArray() {
        throw new UnsupportedOperationException("This entry is not an array.");
    }

    @Override
    public Document toDocument() {
        throw new UnsupportedOperationException("This entry is not a document.");
    }

    @Override
    public DocumentAttributes toAttributes() {
        throw new UnsupportedOperationException("This entry is not a attribute.");
    }

    @Override
    public DocumentNode toNode() {
        throw new UnsupportedOperationException("This entry is not a node.");
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isAttributes() {
        return false;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }


    @Override
    public PrimitiveEntry copy(String key) {
        return new SimplePrimitiveEntry(key,this.value);
    }
}
