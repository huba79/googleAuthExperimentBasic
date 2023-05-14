package com.example.googleauthexperimentbasic.network

import android.content.Context
import com.example.googleauthexperimentbasic.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object RetrofitAdapter {

    val baseUrl = "https://192.168.50.7:8443/dental/api/v1/"
    val keyStoreType = "PKCS12"
    fun getInstance(context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient(context))
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        //TODO solve it with some fileManager fos starters
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        val certFileInputStream = context.resources.openRawResource(R.raw.chillandfish)
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(certFileInputStream, context.getString(R.string.pk_dev_pass).toCharArray())

        val keyManagerFactory =
            KeyManagerFactory.getInstance(context.getString(R.string.key_manager_factory_type))
        keyManagerFactory.init(keyStore, context.getString(R.string.pk_dev_pass).toCharArray())

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init((keyStore))
        val trustManagers = trustManagerFactory.getTrustManagers()
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:$trustManagers")
        }
        val trustManager =  trustManagers[0] as TrustManager //X509TrustManager

        val sslContext = SSLContext.getInstance(context.getString(R.string.ssl_protocol_type))

        sslContext.init(keyManagerFactory.keyManagers, trustManagers, SecureRandom())

//        val sslSocketFactory = sslContext.socketFactory

        //DANGER - not to leave inside  a prod code, it is just for dev purposes
        //because of the self-signed certificate
        val hostnameVerifier = HostnameVerifier { hostname, session -> true }

        return OkHttpClient
            .Builder()
            .sslSocketFactory(sslContext.socketFactory,trustManager as X509TrustManager)
            .hostnameVerifier(hostnameVerifier)
            .addInterceptor(interceptor)
            .build()
    }
}