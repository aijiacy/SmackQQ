/**
 *
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
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
package com.silkroad.core.socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

/**
 * An SSL socket factory that will let any certifacte past, even if it's expired
 * or not singed by a root CA.
 */
public class DummySSLSocketFactory extends SSLSocketFactory {

    private SSLSocketFactory factory;

    public DummySSLSocketFactory() {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(
                    null, // KeyManager not required
                    new TrustManager[]{new DummyTrustManager()},
                    new java.security.SecureRandom());
            factory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static SocketFactory getDefault() {
        return new DummySSLSocketFactory();
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException {
        return factory.createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket(InetAddress srcInAddr, int srcPort, InetAddress tarInAddr,
                               int tarPort) throws IOException {
        return factory.createSocket(srcInAddr, srcPort, tarInAddr, tarPort);
    }

    public Socket createSocket(InetAddress inaddr, int port) throws IOException {
        return factory.createSocket(inaddr, port);
    }

    public Socket createSocket(String host, int srcPort, InetAddress inaddr, int tarPort)
            throws IOException {
        return factory.createSocket(host, srcPort, inaddr, tarPort);
    }

    public Socket createSocket(String host, int port) throws IOException {
        return factory.createSocket(host, port);
    }

    public String[] getDefaultCipherSuites() {
        return factory.getSupportedCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
    }
}

/**
 * Trust manager which accepts certificates without any validation except date
 * validation.
 */
class DummyTrustManager implements X509TrustManager {

    public boolean isClientTrusted(X509Certificate[] cert) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] cert) {
        try {
            cert[0].checkValidity();
            return true;
        } catch (CertificateExpiredException e) {
            return false;
        } catch (CertificateNotYetValidException e) {
            return false;
        }
    }

    public void checkClientTrusted(X509Certificate[] x509Certificates,
                                   String authType) throws CertificateException {
        // Do nothing for now.
    }

    public void checkServerTrusted(X509Certificate[] x509Certificates,
                                   String authType) throws CertificateException {
        // Do nothing for now.
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
