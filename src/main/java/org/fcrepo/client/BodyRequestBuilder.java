/**
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fcrepo.client;

import static org.fcrepo.client.FedoraHeaderConstants.CONTENT_TYPE;
import static org.fcrepo.client.FedoraHeaderConstants.DIGEST;
import static org.fcrepo.client.FedoraHeaderConstants.IF_MATCH;
import static org.fcrepo.client.FedoraHeaderConstants.IF_UNMODIFIED_SINCE;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.slf4j.Logger;

/**
 * Request builder which includes a body component
 * 
 * @author bbpennel
 */
public abstract class BodyRequestBuilder<T extends BodyRequestBuilder<T>> extends
        RequestBuilder<BodyRequestBuilder<T>> {

    private static final Logger LOGGER = getLogger(PatchBuilder.class);

    protected InputStream bodyStream;

    protected String contentType;

    protected String digest;

    protected String etag;

    protected String unmodifiedSince;

    /**
     * Instantiate builder
     * 
     * @param uri uri request will be issued to
     * @param client the client
     */
    protected BodyRequestBuilder(final URI uri, final FcrepoClient client) {
        super(uri, client);
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    @Override
    protected void populateRequest(final HttpRequestBase request) throws FcrepoOperationFailedException {
        addBody((HttpEntityEnclosingRequestBase) request);

        addIfUnmodifiedSince(request);
        addIfMatch(request);

        addDigest(request);

        LOGGER.debug("Fcrepo {} request headers: {}", request.getMethod(), (Object[]) request.getAllHeaders());
    }

    /**
     * Add a body to this request from a stream, with application/octet-stream as its content type
     * 
     * @param stream InputStream of the content to be sent to the server
     * @return this builder
     */
    public T body(final InputStream stream) {
        return body(stream, null);
    }

    /**
     * Add a body to this request as a stream with the given content type
     * 
     * @param stream InputStream of the content to be sent to the server
     * @param contentType the Content-Type of the body
     * @return this builder
     */
    public T body(final InputStream stream, final String contentType) {
        this.bodyStream = stream;
        if (contentType == null) {
            this.contentType = "application/octet-stream";
        } else {
            this.contentType = contentType;
        }

        return self();
    }

    /**
     * Add the given file as the body for this request with the provided content type
     * 
     * @param file File containing the content to be sent to the server
     * @param contentType the Content-Type of the body
     * @return this builder
     * @throws IOException when unable to stream the body file
     */
    public T body(final File file, final String contentType) throws IOException {
        return body(new FileInputStream(file), contentType);
    }

    protected void addBody(final HttpEntityEnclosingRequestBase request) {
        if (bodyStream != null) {
            request.setEntity(new InputStreamEntity(bodyStream));
            request.addHeader(CONTENT_TYPE, contentType);
        }
    }

    protected void addDigest(final HttpRequestBase request) {
        if (digest != null) {
            request.addHeader(DIGEST, "sha1=" + digest);
        }
    }

    protected void addIfUnmodifiedSince(final HttpRequestBase request) {
        if (unmodifiedSince != null) {
            request.setHeader(IF_UNMODIFIED_SINCE, unmodifiedSince);
        }
    }

    protected void addIfMatch(final HttpRequestBase request) {
        if (etag != null) {
            request.setHeader(IF_MATCH, etag);
        }
    }
}
