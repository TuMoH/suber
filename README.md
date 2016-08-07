# suber
A Java serialization/deserialization library that can convert Java Objects into Subtitle formats (for example: *.srt, *.ass) and back.

## Using Suber
1) To start using Suber, add the jar to your project or use gradle dependencies: 
```java
repositories {
  ...
  maven { url "https://jitpack.io" }
}
dependencies {
  compile 'com.github.TuMoH:suber:1.2.1'
}
```
2) Create a static import to Suber in your code like this:
```java
import static com.timursoft.suber.Suber.suber;
```
3) Now you can access Suber in your code like this:
```java
SubFileObject sfo = suber().parse(subFile);
...
suber().serialize(sfo, newSubFile);
```
Easy!

##License
-------

    Copyright (C) 2016 Gasymov Timur

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
