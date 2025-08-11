package com.adarsh.foodiesapi.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Configuration
public class ProxyConfig {

    @Value("${proxy.enabled:false}")
    private boolean proxyEnabled;

    @Value("${proxy.host:}")
    private String proxyHost;

    @Value("${proxy.port:0}")
    private int proxyPort;

    @Value("${proxy.username:}")
    private String proxyUser;

    @Value("${proxy.password:}")
    private String proxyPassword;

    @PostConstruct
    public void configureProxy() {
        if (proxyEnabled) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", String.valueOf(proxyPort));

            if (!proxyUser.isBlank() && !proxyPassword.isBlank()) {
                // Set a default authenticator to provide credentials when proxy asks for authentication
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType() == RequestorType.PROXY) {
                            return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                        }
                        return null;
                    }
                });
            }

            System.out.println("✅ Proxy configured with authentication");
        } else {
            System.out.println("⚡ Running without proxy");
        }
    }
}
