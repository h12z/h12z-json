Implementation:

```gradle

repositorys {
  mavenCentral()
  maven { url = "https://jitpack.io/" }
}

dependencies {

  implementation "com.github.h12z:h12z-json:v1.0.1"

}

```

Usage:

```java

public static void main(String[] args) {

  JSONObject json = Json.parseFile("File.json");

}

```
