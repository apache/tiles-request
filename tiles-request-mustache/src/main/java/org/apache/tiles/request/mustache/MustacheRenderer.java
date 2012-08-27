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

package org.apache.tiles.request.mustache;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheException;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.CannotRenderException;
import org.apache.tiles.request.render.Renderer;

/**
 * The Mustache-specific renderer.
 *
 * Can be configured to render paths only according to the acceptPattern.
 *
 * @version $Rev: 1215006 $ $Date: 2011-12-16 01:30:41 +0100 (Fri, 16 Dec 2011) $
 */
public class MustacheRenderer implements Renderer {

    // hack. exposes the tiles Request for MustacheFactory implementations.
    private static final ThreadLocal<Request> REQUEST_HOLDER = new ThreadLocal<Request>();

    private final MustacheFactory factory;
    private Pattern acceptPattern;

    /** Uses the {@link DefaultMustacheFactory} */
    public MustacheRenderer(){
        this.factory = new DefaultMustacheFactory();
    }

    public MustacheRenderer(MustacheFactory factory) {
        this.factory = factory;
    }

    @Override
    public void render(String path, Request request) throws IOException {
        if (path == null) {
            throw new CannotRenderException("Cannot dispatch a null path");
        }

        try {
            REQUEST_HOLDER.set(request);
            factory
                    .compile(path)
                    .execute(request.getWriter(), buildScope(request));
            REQUEST_HOLDER.remove();

        } catch(MustacheException ex) {
            throw new IOException("failed to MustacheRenderer.render(" + path + ",request)", ex);
        }
    }

    public static Request getThreadLocalRequest() {
        return REQUEST_HOLDER.get();
    }

    protected Map<String,Object> buildScope(Request request) {
        Map<String,Object> scope = new HashMap<String,Object>();
        List<String> availableScopes = request.getAvailableScopes();
        for (int i = availableScopes.size() -1; i >= 0; --i) {
            scope.putAll(request.getContext(availableScopes.get(i)));
        }
        return scope;
    }

    @Override
    public boolean isRenderable(String path, Request request) {
        if (path == null) {
            return false;
        }
        if (acceptPattern != null) {
            final Matcher matcher = acceptPattern.matcher(path);
            return matcher.matches();
        }
        return true;
    }

    public final void setAcceptPattern(Pattern acceptPattern) {
        this.acceptPattern = acceptPattern;
    }
}
