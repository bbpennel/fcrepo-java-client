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

import static org.fcrepo.client.FedoraHeaderConstants.CONTENT_DISPOSITION;
import static org.fcrepo.client.FedoraHeaderConstants.SLUG;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Builds a post request for interacting with the Fedora HTTP API in order to create a new resource within an LDP
 * container.
 * 
 * @author bbpennel
 */
public class PostBuilder extends BodyRequestBuilder {

    /**
     * Instantiate builder
     * 
     * @param uri uri of the resource this request is being made to
     * @param client the client
     */
    public PostBuilder(final URI uri, final FcrepoClient client) {
        super(uri, client);
    }

    @Override
    protected HttpRequestBase createRequest() {
        return HttpMethods.POST.createRequest(targetUri);
    }

    /**
     * Add a body to this request as a stream with the given content type
     *
     * @param stream InputStream of the content to be sent to the server
     * @param contentType the Content-Type of the body
     * @return this builder
     */
    public PostBuilder body(final InputStream stream, final String contentType) {
        return (PostBuilder) super.body(stream, contentType);
    }

    /**
     * Provide a SHA-1 checksum for the body of this request
     * 
     * @param digest sha-1 checksum to provide as the digest for the request body
     * @return this builder
     */
    public PostBuilder digest(final String digest) {
        addDigest(digest);
        return this;
    }

    /**
     * Provide a content disposition header which will be used as the filename
     * 
     * @param filename the name of the file being provided in the body of the request
     * @return this builder
     * @throws FcrepoOperationFailedException if unable to encode filename
     */
    public PostBuilder filename(final String filename) throws FcrepoOperationFailedException {
        if (filename != null) {
            try {
                final String encodedFilename = URLEncoder.encode(filename, "utf-8");
                final String disposition = "attachment; filename=\"" + encodedFilename + "\"";
                request.addHeader(CONTENT_DISPOSITION, disposition);
            } catch (UnsupportedEncodingException e) {
                throw new FcrepoOperationFailedException(request.getURI(), -1, e.getMessage());
            }

        }
        return this;
    }

    /**
     * Provide a suggested name for the new child resource, which the repository may ignore.
     * 
     * @param slug value to supply as the slug header
     * @return this builder
     */
    public PostBuilder slug(final String slug) {
        if (slug != null) {
            request.addHeader(SLUG, slug);
        }
        return this;
    }
}
