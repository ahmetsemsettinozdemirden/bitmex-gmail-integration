/*
 * MIT License
 *
 * Copyright (c) 2017 Franz Granlund
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package business.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import play.libs.Json;

import java.util.Date;

public class VerifiedJwtImpl implements VerifiedJwt {

    private String header;
    private String payload;
    private String issuer;
    private String username;
    private Date expiresAt;

    public VerifiedJwtImpl(DecodedJWT decodedJWT) {
        this(decodedJWT.getHeader(), decodedJWT.getPayload(), decodedJWT.getIssuer(),
                decodedJWT.getClaim("username").asString(), decodedJWT.getExpiresAt());
    }

    public VerifiedJwtImpl(String header, String payload, String issuer, String username, Date expiresAt) {
        this.header = header;
        this.payload = payload;
        this.issuer = issuer;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public Date getExpiresAt() {
        return expiresAt;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return Json.toJson(this).toString();
    }
}
