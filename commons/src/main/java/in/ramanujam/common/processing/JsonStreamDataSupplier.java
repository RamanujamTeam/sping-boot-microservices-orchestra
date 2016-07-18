package in.ramanujam.common.processing;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonStreamDataSupplier<T> implements Iterator<T> {
    final Class<T> mappingClass;
    final JsonParser parser;

    boolean maybeHasNext;

    public JsonStreamDataSupplier(Class<T> mappingClass, JsonParser parser) {
        this.maybeHasNext = true;
        this.mappingClass = mappingClass;
        this.parser = parser;
    }

    public static <T> JsonStreamDataSupplierBuilder<T> mapping(Class<T> mappingClass) {
        return new JsonStreamDataSupplierBuilder<>(mappingClass);
    }

    /*
    This method returns the stream, and is the only method other
    than the constructor that should be used.
    */
    public Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, 0), false);
    }

    /* The remaining methods are what enables this to be passed to the spliterator generator,
       since they make it Iterable.
    */
    @Override
    public boolean hasNext() {
        if (!maybeHasNext) {
            return false; // didn't get started
        }
        try {
            return (parser.nextToken() == JsonToken.START_OBJECT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public T next() {
        try {
            return parser.readValueAs(mappingClass);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class JsonStreamDataSupplierBuilder<T> {
        InputStream inputStream;
        Class<T> aClass;


        public JsonStreamDataSupplierBuilder(Class<T> aClass) {
            this.aClass = aClass;
        }

        public JsonStreamDataSupplierBuilder<T> forStream(InputStream stream) {
            this.inputStream = stream;
            return this;
        }

        public JsonStreamDataSupplierBuilder<T> forFile(File file) {
            throw new UnsupportedOperationException();
        }

        public JsonStreamDataSupplierBuilder<T> forPath(Path path) {
            throw new UnsupportedOperationException();
        }

        public JsonStreamDataSupplier<T> build() {
            try {
                JsonFactory factory = new JsonFactory();
                JsonParser parser = factory.createParser(inputStream);
                ObjectMapper objectMapper = new ObjectMapper(factory);
                parser.setCodec(objectMapper);

                // Setup and get into a state to start iterating
                JsonToken token = parser.nextToken();
                if (token == null) {
                    throw new RuntimeException("Can't get any JSON Token");
                }

                // the first token is supposed to be the start of array '['
                if (!JsonToken.START_ARRAY.equals(token)) {
                    // return or throw exception
                    throw new RuntimeException("Can't get any JSON Token fro array start");
                }

                return new JsonStreamDataSupplier<>(aClass, parser);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}