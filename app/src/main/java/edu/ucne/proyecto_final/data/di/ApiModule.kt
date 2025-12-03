package edu.ucne.proyecto_final.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.proyecto_final.data.remote.dto.*
import edu.ucne.proyecto_final.data.remote.usuario.UsuarioApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    private const val BASE_URL_HUACALES = "https://gestionhuacalesapi.azurewebsites.net/"
    private const val BASE_URL_VENTAS = "https://ventasever.azurewebsites.net/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private inline fun <reified T> createApi(
        baseUrl: String,
        moshi: Moshi,
        client: OkHttpClient
    ): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(T::class.java)


    @Provides @Singleton
    fun provideUsuarioApi(moshi: Moshi, client: OkHttpClient): UsuarioApi =
        createApi(BASE_URL_HUACALES, moshi, client)

    @Provides @Singleton
    fun provideCategoriaApi(moshi: Moshi, client: OkHttpClient): CategoriaApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideClienteApi(moshi: Moshi, client: OkHttpClient): ClienteApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideClienteDetalleApi(moshi: Moshi, client: OkHttpClient): ClienteDetalleApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideCompraApi(moshi: Moshi, client: OkHttpClient): CompraApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideCompraDetalleApi(moshi: Moshi, client: OkHttpClient): CompraDetalleApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideInsumoApi(moshi: Moshi, client: OkHttpClient): InsumoApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideInsumoDetalleApi(moshi: Moshi, client: OkHttpClient): InsumoDetalleApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideProveedorApi(moshi: Moshi, client: OkHttpClient): ProveedorApi =
        createApi(BASE_URL_VENTAS, moshi, client)

    @Provides @Singleton
    fun provideReclamoApi(moshi: Moshi, client: OkHttpClient): ReclamoApi =
        createApi(BASE_URL_VENTAS, moshi, client)

}
