package com.smsjuegos.quiz.retrofit;

import static com.smsjuegos.quiz.retrofit.Constant.BASE_URL;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static final Retrofit retrofit;
    static {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit getClient() {
        return retrofit;
    }
}

/*public class ApiClient {

    private static final Retrofit retrofit;
   // private static final String BASE_URL = "https://your-api-url.com"; // Change to your API's base URL

    static {
        try {
            // Create SSLContext and initialize it with TLS protocols
            SSLContext sslContext = SSLContext.getInstance("TLS"); // TLSv1.2 or TLSv1.3
            sslContext.init(null, getTrustManagers(), new java.security.SecureRandom());

            // Create a custom SSLSocketFactory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Setup HttpLoggingInterceptor for debugging
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Build the OkHttpClient with custom SSL configuration
            final OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, getTrustManager()) // Set custom SSL socket factory
                    .connectionSpecs(Collections.singletonList(ConnectionSpec.COMPATIBLE_TLS)) // Enforce TLS compatibility
                    .addInterceptor(interceptor)
                    .build();

            // Build Retrofit with the custom OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Error setting up SSLContext", e);
        }
    }

    public static Retrofit getClient() {
        return retrofit;
    }

    // Returns a TrustManager that accepts all certificates (for testing only)
    private static X509TrustManager getTrustManager() {
        return new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null; // Accept all certificate authorities
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                // Custom logic to check client certificates (optional)
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                // Custom logic to check server certificates (optional)
            }
        };
    }

    // Returns the default TrustManager to validate SSL certificates
    private static javax.net.ssl.TrustManager[] getTrustManagers() throws NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        try {
            trustManagerFactory.init((java.security.KeyStore) null);  // Use default keystore
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trustManagerFactory.getTrustManagers();
    }
}*/
