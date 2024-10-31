Implementation:

```gradle

repositorys {
  mavenCentral()
  maven { url = "https://h12z.me/releases/" }
}

dependencies {

  implementation "me.h12z:h12z-json:1.0.1"

}

```

Usage:

```java

public static void main(String[] args) {

  JSONObject json = Json.parseFile("File.json");

}

```
