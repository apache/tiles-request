/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.tiles.request.locale;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

/**
 * Test implementation of {@link URLApplicationResource}
 */
public class TestApplicationResource extends URLApplicationResource {

    public TestApplicationResource(String localePath, URL url) throws URISyntaxException {
        super(localePath, url);
    }

    public TestApplicationResource(String path, Locale locale, URL url) throws MalformedURLException, URISyntaxException {
        super(path, locale, url);
    }

    @Override
    protected URL getURL() {
        return super.getURL();
    }

    @Override
    protected File getFile() {
        return super.getFile();
    }
}
