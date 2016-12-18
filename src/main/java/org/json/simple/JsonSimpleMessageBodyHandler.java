/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flrv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.json.simple.DeserializationException;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

/**
 *
 * @author wlt
 */
public class JsonSimpleMessageBodyHandler implements MessageBodyWriter<Object>,
                                                     MessageBodyReader<Object> {

  private static final String UTF_8 = "UTF-8";

    @Override
    public boolean isReadable(Class<?> type,
                            Type genericType,
                            Annotation[] annotations,
                            MediaType mediaType) {
        return true;
    }
 
    @Override
    public Object readFrom(Class<Object> type,
                           Type genericType,
                           Annotation[] annotations,
                           MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException {
        Object json = new JsonObject();
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(entityStream, UTF_8);
            json = Jsoner.deserialize(streamReader);
        } catch (UnsupportedEncodingException | DeserializationException ex) {
            Logger.getLogger(JsonSimpleMessageBodyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(streamReader!=null)
                streamReader.close();
        }
        return(json);
    }
 
    @Override
    public boolean isWriteable(Class<?> type,
                             Type genericType,
                             Annotation[] annotations,
                             MediaType mediaType) {
        return true;
    }
 
    @Override
    public long getSize(Object object,
                      Class<?> type,
                      Type genericType,
                      Annotation[] annotations,
                      MediaType mediaType) {
        return -1;
    }
 
    @Override
    public void writeTo(Object object,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException,
                                                          WebApplicationException {
        OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
        try {
            writer.write(((JsonObject)object).toJson());
        } finally {
            writer.close();
        }
    }
}
