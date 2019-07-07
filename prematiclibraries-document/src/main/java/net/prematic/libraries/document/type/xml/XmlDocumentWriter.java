package net.prematic.libraries.document.type.xml;

import net.prematic.libraries.document.ArrayEntry;
import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.PrimitiveEntry;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class XmlDocumentWriter implements DocumentWriter {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"%s\"?>";

    @Override
    public byte[] write(Document document) {
        return new byte[0];
    }

    @Override
    public byte[] write(Document document, Charset charset) {
        return new byte[0];
    }

    @Override
    public void write(Writer output, Document document, boolean pretty) {
        try {
            output.write(String.format(HEADER, Charset.defaultCharset().name()));
            int indent = pretty ? 0 : -1;
            writeNewLine(output, indent);
            writeObjectValue(output, document, indent);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    private void writeObjectValue(Writer output, Document document, int indent) throws IOException {
        String key = document.getKey() == null ? "root" : document.getKey();
        output.write("<" + key + ">");
        if(indent != -1) indent+=2;
        writeNewLine(output, indent);
        boolean first = true;
        for (DocumentEntry entry : document) {
            if(first) first = false;
            else writeNewLine(output, indent);
            if(entry.isObject()) {
                writeObjectValue(output, entry.toDocument(), indent);
            } else if(entry.isPrimitive()) {
                writePrimitiveValue(output, document, entry.toPrimitive());
            } else if(entry.isArray()) {
                writeArrayValue(output, entry.toArray(), indent);
            }
        }
        if(indent != -1) indent-=2;
        writeNewLine(output, indent);
        output.write("</"+key+">");
    }

    private void writePrimitiveValue(Writer output, Document document, PrimitiveEntry primitiveEntry) throws IOException {
        String key;
        if(document instanceof ArrayEntry) {
            if(document.getKey() != null && document.getKey().endsWith("s")) {
                key = document.getKey().substring(0, document.getKey().length()-1);
            } else {
                key = "value";
            }
        } else {
            key = primitiveEntry.getKey();
        }
        output.write("<" + key + ">" + primitiveEntry.getAsString() + "</" + key + ">");
    }

    private void writeArrayValue(Writer output, ArrayEntry arrayEntry, int indent) throws IOException {
        writeObjectValue(output, arrayEntry, indent);
    }

    private void writeNewLine(Writer output, int indent)  throws IOException{
        if(indent >= 0){
            output.write("\n");
            for(int i = 0;i<indent;i++) output.write("  ");
        }
    }
}
