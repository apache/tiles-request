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
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.junit.Test;

/**
 * Tests URLApplicationResource.
 *
 * @version $Rev$ $Date$
 */
public class URLApplicationResourceTest {

    private class TestApplicationResource extends URLApplicationResource {
        public TestApplicationResource(String localePath, URL url) {
            super(localePath, url);
        }

        public TestApplicationResource(String path, Locale locale, URL url) throws MalformedURLException {
            super(path, locale, url);
        }

        @Override
        protected URL getURL(){
            return super.getURL();
        }

        @Override
        protected File getFile(){
            return super.getFile();
        }
    };

    /**
     * Test getLocalePath(String path, Locale locale).
     */
    @Test
    public void testGetLocalePath() throws MalformedURLException {
        TestApplicationResource resource = new TestApplicationResource("/my/path_fr.html", new URL("file:///"));
        assertEquals("/my/path.html", resource.getLocalePath(null));
        assertEquals("/my/path.html", resource.getLocalePath(Locale.ROOT));
        assertEquals("/my/path_it.html", resource.getLocalePath(Locale.ITALIAN));
        assertEquals("/my/path_it_IT.html", resource.getLocalePath(Locale.ITALY));
        assertEquals("/my/path_en_GB_scotland.html", resource.getLocalePath(new Locale("en", "GB", "scotland")));
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
    }

    @Test
    public void testBuildFromString() throws MalformedURLException {
        TestApplicationResource resource = new TestApplicationResource("/my/path_en_GB_scotland.html", new URL("file:///"));
        assertEquals("/my/path_en_GB_scotland.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        assertEquals(new Locale("en", "GB", "scotland"), resource.getLocale());
        resource = new TestApplicationResource("/my/path_it_IT.html", new URL("file:///"));
        assertEquals("/my/path_it_IT.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        assertEquals(Locale.ITALY, resource.getLocale());
        resource = new TestApplicationResource("/my/path_it.html", new URL("file:///"));
        assertEquals("/my/path_it.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        assertEquals(Locale.ITALIAN, resource.getLocale());
        resource = new TestApplicationResource("/my/path.html", new URL("file:///"));
        assertEquals("/my/path.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        assertEquals(Locale.ROOT, resource.getLocale());
    }

    @Test
    public void testBuildFromStringAndLocale() throws MalformedURLException {
        TestApplicationResource resource = new TestApplicationResource("/my/path.html", new Locale("en", "GB", "scotland"), new URL("file:///"));
        assertEquals("/my/path_en_GB_scotland.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals(new Locale("en", "GB", "scotland"), resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        resource = new TestApplicationResource("/my/path.html", Locale.ITALY, new URL("file:///"));
        assertEquals("/my/path_it_IT.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals(Locale.ITALY, resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        resource = new TestApplicationResource("/my/path.html", Locale.ITALIAN, new URL("file:///"));
        assertEquals("/my/path_it.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals(Locale.ITALIAN, resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
        resource = new TestApplicationResource("/my/path.html", Locale.ROOT, new URL("file:///"));
        assertEquals("/my/path.html", resource.getLocalePath());
        assertEquals("/my/path.html", resource.getPath());
        assertEquals(Locale.ROOT, resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals("/", resource.getFile().toString());
    }
}
