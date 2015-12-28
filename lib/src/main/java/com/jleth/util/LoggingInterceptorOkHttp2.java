package com.jleth.util;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;

/**
 * Interceptor for OkHttpClient that prints request and response with content and headers
 *
 * @author Jeppe Leth Nielsen, https://github.com/JeppeLeth
 */
public class LoggingInterceptorOkHttp2 implements Interceptor {

    public static final Logger NO_LOG = new Logger() {
        @Override
        public void info(String log) {
            // No nothing
        }
    };

    /**
     * Controls the level of logging.
     */
    public enum LogLevel {
        /**
         * No logging.
         */
        NONE,
        /**
         * Log the headers, body, and metadata for both requests and responses.
         * <p>
         * Note: This requires that the entire request and response body be buffered in memory!
         */
        FULL;

        public boolean log() {
            return this != NONE;
        }
    }

    private Logger logger;
    private LogLevel logLevel;

    public LoggingInterceptorOkHttp2(Logger logger) {
        this(logger, LogLevel.FULL);
    }

    public LoggingInterceptorOkHttp2(Logger logger, LogLevel logLevel) {
        this.logger = logger;
        setLogLevel(logLevel);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!logLevel.log()) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request();

        long t1 = System.nanoTime();
        logger.info(String.format("---> HTTP %s %s on %s%n%s",
                request.method(),
                request.url(),
                chain.connection(),
                removeLastNewline(request.headers())));
        String requestContent = bodyToString(request);
        logger.info(String.format("%s", requestContent));
        logger.info(String.format("---> END HTTP (%d-byte body)", (request.body() != null ? request.body().contentLength() : 0)));
        Response response;
        try {
            response = chain.proceed(request);

        } catch (IOException e) {
            logger.info(String.format("<--> HTTP connection error (%s) %s", e.getClass().getCanonicalName(), e.getMessage()));
            throw e;
        }

        long t2 = System.nanoTime();
        logger.info(String.format("<--- HTTP %d %s (%.1fms)%n%s",
                response.code(),
                response.request().url(),
                (t2 - t1) / 1e6d,
                removeLastNewline(response.headers())));

        MediaType contentType = null;
        String responseContent = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            responseContent = response.body().string();
        }
        logger.info(String.format("%s", bodyToString(responseContent)));

        logger.info(String.format("<--- END HTTP (%d-byte body)", (response.body() != null ? response.body().contentLength() : 0)));

        if (response.body() != null) {
            ResponseBody wrappedBody = ResponseBody.create(contentType, responseContent);
            response = response.newBuilder().body(wrappedBody).build();
        }

        return response;
    }

    private static String removeLastNewline(Object object) {
        return object != null ? object.toString().replaceFirst("\\n$", "") : null;
    }

    private static String bodyToString(final Request request) {
        try {
            if (request.body() == null) {
                return "(no body)";
            }
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            String requestContent = buffer.readUtf8();
            if (requestContent.isEmpty()) {
                requestContent = "(empty body)";
            }
            return requestContent;
        } catch (final Throwable e) {
            return "(body not printable)";
        }
    }

    private static String bodyToString(String responseContent) {
        if (responseContent == null) {
            responseContent = "(no body)";
        } else if (responseContent.length() == 0) {
            responseContent = "(empty body)";
        }
        return responseContent;
    }

    /**
     * Change the level of logging.
     */
    public LoggingInterceptorOkHttp2 setLogLevel(LogLevel logLevel) {
        if (logLevel == null) {
            throw new NullPointerException("Log level may not be null.");
        }
        this.logLevel = logLevel;
        return this;
    }

    /**
     * The current logging level.
     */
    public LogLevel getLogLevel() {
        return logLevel;
    }

    public interface Logger {
        void info(String log);
    }
}
