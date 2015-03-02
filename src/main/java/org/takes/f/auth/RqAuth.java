/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.f.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.takes.Request;
import org.takes.rq.RqHeaders;

/**
 * Request with auth information.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = { "origin", "header" })
public final class RqAuth implements Request {

    /**
     * Original request.
     */
    private final transient Request origin;

    /**
     * Header with authentication info.
     */
    private final transient String header;

    /**
     * Ctor.
     * @param request Original
     */
    public RqAuth(final Request request) {
        this(request, "X-Takes-Auth");
    }

    /**
     * Ctor.
     * @param request Original
     * @param hdr Header to read
     */
    public RqAuth(final Request request, final String hdr) {
        this.origin = request;
        this.header = hdr;
    }

    /**
     * Authenticated user.
     * @return User URN
     * @throws IOException If fails
     */
    public String identity() throws IOException {
        final List<String> headers =
            new RqHeaders(this.origin).header(this.header);
        final String user;
        if (headers.isEmpty()) {
            user = Pass.ANONYMOUS;
        } else {
            user = headers.get(0);
        }
        return user;
    }

    @Override
    public List<String> head() throws IOException {
        return this.origin.head();
    }

    @Override
    public InputStream body() throws IOException {
        return this.origin.body();
    }
}
