package com.upwork.healthchecker.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.healthchecker.domain.ServiceRegistry;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Created by rajaniy on 4/24/18.
 */
public class HTTPHealthChecker implements Callable<String> {

    private static final Logger _logger = LoggerFactory.getLogger(HTTPHealthChecker.class);

    private ServiceRegistry serviceRegistry;

    public HTTPHealthChecker(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public String call() throws Exception {
        if (StringUtils.hasText(serviceRegistry.getUri())) {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();
            HttpGet httpGet = new HttpGet(serviceRegistry.getUri());
            HttpClientContext httpClientContext = HttpClientContext.create();
            CredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
            basicCredentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), new UsernamePasswordCredentials(serviceRegistry.getUsername(), serviceRegistry.getPassword()));
            httpClientContext.setCredentialsProvider(basicCredentialsProvider);
            CloseableHttpResponse response = httpClient.execute(httpGet, httpClientContext);
            if (response != null && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception("Error response received - " + response.getStatusLine().getStatusCode() + "--" + response.getStatusLine().toString());
            }
            return streamToString(response.getEntity().getContent());
        }
        return null;
    }

    private static String streamToString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
