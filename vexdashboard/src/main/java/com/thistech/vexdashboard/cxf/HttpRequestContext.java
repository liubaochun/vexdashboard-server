package com.thistech.vexdashboard.cxf;

/**
 * Implemented by objects that contain information about an HTTP request. These context objects exist to be injected
 * into service objects in order to supply the services with contextual information.
 */
public interface HttpRequestContext {

    /**
     * Returns an HTTP request's URL.
     *
     * @return A string or null.
     */
    String getRequestUrl();
}

