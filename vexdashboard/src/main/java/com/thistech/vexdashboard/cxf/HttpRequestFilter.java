package com.thistech.vexdashboard.cxf;

import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;

/**
 * Creates objects that contain information about an HTTP request.
 */
public class HttpRequestFilter implements ContextProvider<HttpRequestContext> {

    @Override
    public HttpRequestContext createContext(final Message message) {
        final Object value = message.get(Message.REQUEST_URL);
        return new HttpRequestContextImpl((value == null) ? "" : value.toString());
    }

    private class HttpRequestContextImpl implements HttpRequestContext {

        private final String requestUrl;

        /**
         * Constructs contextual information about an HTTP request.
         *
         * @param url An HTTP request's url or null.
         */
        HttpRequestContextImpl(final String url) {
            requestUrl = url;
        }

        @Override
        public String getRequestUrl() {
            return requestUrl;
        }
    }
}
