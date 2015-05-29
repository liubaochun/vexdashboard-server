package com.thistech.vexdashboard.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.thistech.common.util.DateUtil;

import java.text.DateFormat;

/**
 * Created by brent on 5/28/15.
 */
public class VexdashboardObjectMapper extends ObjectMapper {

    public VexdashboardObjectMapper() {
        setAnnotationIntrospector(new AnnotationIntrospectorPair(new JacksonAnnotationIntrospector(), new JaxbAnnotationIntrospector(getTypeFactory())));
        setDateFormat(DateUtil.toDateFormat(DateUtil.DATE_TIME_MILLIS_TIMEZONE)); // use Z instead of +0000

        //configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        configure(SerializationFeature.INDENT_OUTPUT, true);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        DateFormat dateFormat = new ISO8601DateFormat();
        this.setDateFormat(dateFormat);
    }
}
