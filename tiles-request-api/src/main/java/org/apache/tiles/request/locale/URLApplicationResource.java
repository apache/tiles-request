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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.jar.Manifest;

/**
 * A {@link PostfixedApplicationResource} that can be accessed through a URL.
 *
 * @version $Rev$ $Date$
 */

public class URLApplicationResource extends PostfixedApplicationResource {
    private static final Logger LOG = LoggerFactory.getLogger(URLApplicationResource.class);

    /** the URL where the contents can be found. */
    private URL url;
    /** if the URL matches a file, this is the file. */
    private File file;
    /** if the URL points into an OSGi bundle, this is true */
    private boolean bundle;

    /**
     * Creates a URLApplicationResource for the specified path that can be accessed through the specified URL.
     *
     * @param localePath the path including localization.
     * @param url the URL where the contents can be found.
     */
    public URLApplicationResource(String localePath, URL url) {
        super(localePath);
        this.url = url;
        if ("file".equals(url.getProtocol())) {
            file = getFile(url);
        } else {
            bundle = checkBundle(url);
        }
    }

    /**
     * Creates a URLApplicationResource for the specified path that can be accessed through the specified URL.
     *
     * @param path the path excluding localization.
     * @param locale the Locale.
     * @param url the URL where the contents can be found.
     */
    public URLApplicationResource(String path, Locale locale, URL url) {
        super(path, locale);
        this.url = url;
        if ("file".equals(url.getProtocol())) {
            file = getFile(url);
        } else {
            bundle = checkBundle(url);
        }
    }

    private static boolean checkBundle(URL url) {
        try {
            URL manifestUrl = new URL(url.toExternalForm().replace(url.getFile(), "/META-INF/MANIFEST.MF"));
            InputStream in = manifestUrl.openStream();
            try {
                return new Manifest(in).getMainAttributes().getValue("Bundle-SymbolicName") != null;
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.debug("Manifest could not be closed properly", e);
                }
            }
        } catch (IOException e) {
            LOG.debug("No manifest found", e);
            return false;
        }
    }

    private static File getFile(URL url) {
    	try {
			return new File(new URI(url.toExternalForm()).getSchemeSpecificPart());
		} catch (URISyntaxException e) {
			LOG.debug("Cannot translate URL to file name, expect a performance impact", e);
			return null;
		}
    }

    private URLConnection openConnection() throws IOException {
        try {
            return url.openConnection();
        } catch (IOException e) {
            // If the url points into a bundle but the resource cannot be
            // opened means, that the resource actually does not exist. In this
            // case throw a FileNotFoundException, see
            if (bundle) {
                throw new FileNotFoundException(url.toString());
            }
            throw e;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream() throws IOException {
        if (file != null) {
            return new FileInputStream(file);
        } else {
            return openConnection().getInputStream();
        }
    }

    /** {@inheritDoc} */
    @Override
    public long getLastModified() throws IOException {
        if (file != null) {
            return file.lastModified();
        } else {
            URLConnection connection = openConnection();
            if (connection instanceof JarURLConnection) {
                return ((JarURLConnection) connection).getJarEntry().getTime();
            } else {
                long result = connection.getLastModified();
                return result;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Resource " + getLocalePath() + " at " + url.toString();
    }

    protected URL getURL(){
        return url;
    }

    protected File getFile(){
        return file;
    }
}
