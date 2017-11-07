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

package org.apache.tiles.request.osgi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.tiles.request.locale.URLApplicationResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackage;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;


/**
 * Tests OSGi behaviour of URLApplicationResource.
 *
 * @version $Rev$ $Date$
 */
@RunWith(PaxExam.class)
public class URLBundleApplicationResourceTest {

    @Configuration
    public Option[] configuration() throws IOException {
        return new Option[]{
                junitBundles(),
                frameworkProperty("org.osgi.framework.bundle.parent").value("app"),
                bootDelegationPackage("org.apache.tiles.request.locale"),
        };
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetLastModified() throws Exception {
        URL url = new URL(getClass().getResource("/org/apache/tiles/request/test/locale/resource.txt"), "notExisting");
        URLApplicationResource resource = new URLApplicationResource("/my test/path_fr.html", url);
        resource.getLastModified();
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetInputStream() throws IOException {
        URL url = new URL(getClass().getResource("/org/apache/tiles/request/test/locale/resource.txt"), "notExisting");
        URLApplicationResource resource = new URLApplicationResource("org/apache/tiles/request/test/locale/resource.txt", url);
        resource.getInputStream();
    }
}
