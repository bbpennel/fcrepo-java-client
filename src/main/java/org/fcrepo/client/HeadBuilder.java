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

import java.net.URI;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Builds a HEAD request to retrieve resource headers.
 * 
 * @author bbpennel
 */
public class HeadBuilder extends
        RequestBuilder {

    /**
     * Instantiate builder
     * 
     * @param uri uri request will be issued to
     * @param client the client
     */
    public HeadBuilder(final URI uri, final FcrepoClient client) {
        super(uri, client);
        this.request = HttpMethods.HEAD.createRequest(targetUri);
    }

    @Override
    protected HttpRequestBase createRequest() {
        return HttpMethods.HEAD.createRequest(targetUri);
    }
}
