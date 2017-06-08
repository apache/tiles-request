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
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Tests URLApplicationResource.
 *
 * @version $Rev$ $Date$
 */
public class URLApplicationResourceTest {

    private static class TestUrlConnection extends URLConnection {
        private static boolean alreadyCalled;

        public TestUrlConnection(final URL url) {
            super(url);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (alreadyCalled) {
                fail("Connect has already been called!");
            }
            alreadyCalled = true;
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public void connect() throws IOException {
            // noop
        }
    }

    private static class TestUrlStreamHandler extends URLStreamHandler {

        @Override
        protected URLConnection openConnection(final URL u) throws IOException {
            return new TestUrlConnection(u);
        }
    }

    private static class TestURLStreamHandlerFactory implements URLStreamHandlerFactory {

        @Override
        public URLStreamHandler createURLStreamHandler(final String protocol) {
            if ("test".equals(protocol)) {
                return new TestUrlStreamHandler();
            }
            return null;
        }
    }

    /**
     * Test getLocalePath(String path, Locale locale).
     * @throws URISyntaxException 
     */
    @Test
    public void testGetLocalePath() throws MalformedURLException, URISyntaxException {
        TestApplicationResource resource = new TestApplicationResource("/my test/path_fr.html", new URL("file:///"));
        assertEquals("/my test/path.html", resource.getLocalePath(null));
        assertEquals("/my test/path.html", resource.getLocalePath(Locale.ROOT));
        assertEquals("/my test/path_it.html", resource.getLocalePath(Locale.ITALIAN));
        assertEquals("/my test/path_it_IT.html", resource.getLocalePath(Locale.ITALY));
        assertEquals("/my test/path_en_GB_scotland.html", resource.getLocalePath(new Locale("en", "GB", "scotland")));
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
    }

    @Test
    public void testBuildFromString() throws MalformedURLException, URISyntaxException {
        TestApplicationResource resource = new TestApplicationResource("/my test/path_en_GB_scotland.html", new URL("file:///"));
        assertEquals("/my test/path_en_GB_scotland.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(new Locale("en", "GB", "scotland"), resource.getLocale());
        resource = new TestApplicationResource("/my test/path_it_IT.html", new URL("file:///"));
        assertEquals("/my test/path_it_IT.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(Locale.ITALY, resource.getLocale());
        resource = new TestApplicationResource("/my test/path_it.html", new URL("file:///"));
        assertEquals("/my test/path_it.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(Locale.ITALIAN, resource.getLocale());
        resource = new TestApplicationResource("/my test/path.html", new URL("file:///"));
        assertEquals("/my test/path.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(Locale.ROOT, resource.getLocale());
        resource = new TestApplicationResource("/my test/path_zz.html", new URL("file:///"));
        assertEquals("/my test/path_zz.html", resource.getLocalePath());
        assertEquals("/my test/path_zz.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(Locale.ROOT, resource.getLocale());
        resource = new TestApplicationResource("/my test/path_en_ZZ.html", new URL("file:///"));
        assertEquals("/my test/path_en.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(new Locale("en"), resource.getLocale());
        resource = new TestApplicationResource("/my test/path_tiles.html", new URL("file:///"));
        assertEquals("/my test/path_tiles.html", resource.getLocalePath());
        assertEquals("/my test/path_tiles.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(Locale.ROOT, resource.getLocale());
        resource = new TestApplicationResource("/my test/path_longwordthatbreaksISO639.html", new URL("file:///"));
        assertEquals("/my test/path_longwordthatbreaksISO639.html", resource.getLocalePath());
        assertEquals("/my test/path_longwordthatbreaksISO639.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(Locale.ROOT, resource.getLocale());
        resource = new TestApplicationResource("/my test/path_en_tiles.html", new URL("file:///"));
        assertEquals("/my test/path_en.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(new Locale("en"), resource.getLocale());
        resource = new TestApplicationResource("/my test/path_en_longwordthatbreaksISO3166.html", new URL("file:///"));
        assertEquals("/my test/path_en.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        assertEquals(new Locale("en"), resource.getLocale());
    }

    @Test
    public void testBuildFromStringAndLocale() throws MalformedURLException, URISyntaxException {
        TestApplicationResource resource = new TestApplicationResource("/my test/path.html", new Locale("en", "GB", "scotland"), new URL("file:///"));
        assertEquals("/my test/path_en_GB_scotland.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals(new Locale("en", "GB", "scotland"), resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        resource = new TestApplicationResource("/my test/path.html", Locale.ITALY, new URL("file:///"));
        assertEquals("/my test/path_it_IT.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals(Locale.ITALY, resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        resource = new TestApplicationResource("/my test/path.html", Locale.ITALIAN, new URL("file:///"));
        assertEquals("/my test/path_it.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals(Locale.ITALIAN, resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
        resource = new TestApplicationResource("/my test/path.html", Locale.ROOT, new URL("file:///"));
        assertEquals("/my test/path.html", resource.getLocalePath());
        assertEquals("/my test/path.html", resource.getPath());
        assertEquals(Locale.ROOT, resource.getLocale());
        assertEquals("file:/", resource.getURL().toString());
        assertEquals(File.separator, resource.getFile().toString());
    }
    
    @Test
    public void testGetLastModified() throws IOException {
    	URL url = getClass().getClassLoader().getResource("org/apache/tiles/request/test/locale/resource.txt");
    	URLApplicationResource resource = new URLApplicationResource("org/apache/tiles/request/test/locale/resource.txt", url);
    	assertTrue(resource.getLastModified() > 0);
    }
    
    @Test
    public void testGetLastModifiedWithSpace() throws IOException {
    	URL url = getClass().getClassLoader().getResource("org/apache/tiles/request/test/locale/resource with space.txt");
    	URLApplicationResource resource = new URLApplicationResource("org/apache/tiles/request/test/locale/resource with space.txt", url);
    	assertTrue(resource.getLastModified() > 0);
    }
    
    @Test
    public void testGetInputStream() throws IOException {
    	URL url = getClass().getClassLoader().getResource("org/apache/tiles/request/test/locale/resource.txt");
    	URLApplicationResource resource = new URLApplicationResource("org/apache/tiles/request/test/locale/resource.txt", url);
    	InputStream is = resource.getInputStream();
    	assertNotNull(is);
    	is.close();
    }
    
    @Test
    public void testGetInputStreamWithSpace() throws IOException {
    	URL url = getClass().getClassLoader().getResource("org/apache/tiles/request/test/locale/resource with space.txt");
    	URLApplicationResource resource = new URLApplicationResource("org/apache/tiles/request/test/locale/resource with space.txt", url);
    	InputStream is = resource.getInputStream();
    	assertNotNull(is);
    	is.close();
    }

    @Test
    public void testUseCachedBundleCheckResult() throws IOException {
        URL.setURLStreamHandlerFactory(new TestURLStreamHandlerFactory());
        URL url = new URL("test://foo/bar.txt");
        new URLApplicationResource("org/apache/tiles/request/test/locale/resource.txt", url);

        // This would cause an AssertionError if the protocol had not been cached
        new URLApplicationResource("org/apache/tiles/request/test/locale/resource.txt", url);
    }

    @Test
    public void testProtocolNotFileAndNoManifestFound() throws IOException {
        URL url = new URL("http://--$_foo.org/bar.txt");
        URLApplicationResource resource = new URLApplicationResource("org/apache/tiles/request/test/locale/resource.txt", url);
        try {
            resource.getInputStream();
        } catch (IOException e) {
            assertFalse(e instanceof FileNotFoundException);
        }
    }
}
