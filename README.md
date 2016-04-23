HttpLogging
========
HTTP logging interceptor for OkHttp2 and OkHttp3

For more information please see [the website][1].


Download
--------
__Using OkHttp2:__
Download [the latest JAR][2] or grab via Maven:
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jleth.util/okhttp2-logging/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jleth.util/okhttp2-logging)
[ ![Bintray](https://api.bintray.com/packages/jeppeleth/maven/okhttp2-logging/images/download.svg) ](https://bintray.com/jeppeleth/maven/okhttp2-logging/_latestVersion)
```xml
<dependency>
  <groupId>com.jleth.util</groupId>
  <artifactId>okhttp2-logging</artifactId>
  <version>1.0.1</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.jleth.util:okhttp2-logging:1.0.1'
```

__Using OkHttp3:__
Download [the latest JAR][3] or grab via Maven:
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jleth.util/okhttp2-logging/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jleth.util/okhttp2-logging)
[ ![Bintray](https://api.bintray.com/packages/jeppeleth/maven/okhttp3-logging/images/download.svg) ](https://bintray.com/jeppeleth/maven/okhttp3-logging/_latestVersion)
```xml
<dependency>
  <groupId>com.jleth.util</groupId>
  <artifactId>okhttp3-logging</artifactId>
  <version>1.0.2</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.jleth.util:okhttp3-logging:1.0.2'
```

## Usage

To use the logging interceptor to the OkHttpClient in your project, do the following:
```java
OkHttpClient.Builder builder = new OkHttpClient.Builder();
Interceptor loggingInterceptor = new LoggingInterceptorOkHttp3(new LoggingInterceptorOkHttp3.Logger() {
            @Override
            public void info(String log) {
                System.out.println(log);
            }
        });
clientBuilder.addInterceptor(loggingInterceptor);
OkHttpClient okClient = builder.build();
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].



License
=======

    Copyright 2015 Jeppe Leth Nielsen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: https://github.com/JeppeLeth/HttpLogging
 [2]: https://search.maven.org/remote_content?g=com.jleth.util&a=okhttp2-logging&v=LATEST
 [3]: https://search.maven.org/remote_content?g=com.jleth.util&a=okhttp3-logging&v=LATEST
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
