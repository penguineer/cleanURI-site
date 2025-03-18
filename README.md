# cleanURI - Site

> Common definitions and functions for the [cleanURI](https://github.com/penguineer/cleanURI) sites.


## How to use

### Including the library

The library can be included with [jitpack](https://jitpack.io/) with two additions to the project's POM file.

Adding the jitpack repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Adding the library:
```xml
<dependency>
    <groupId>com.github.penguineer</groupId>
    <artifactId>cleanURI-site</artifactId>
    <version>main-HEAD</version>
    <scope>compile</scope>
</dependency>
```
Replace the `version` tag with the desired release.

Please refer to the [jitpack documentation](https://jitpack.io/) on using alternative build systems.


### Creating site implementations

To create a site implementation, you can extend the `AbstractSiteBase` class and implement the `canProcessURI(URI uri)` method. This method should return `true` if the given URI can be processed by your site, and `false` otherwise.

You can also optionally override the `newCanonizer(URI uri)` and `newExtractor(URI uri)` methods to provide site-specific canonizers and extractors. If you don't override these methods, they will return an empty `Optional`.

Here's an example of a site implementation:

```java
public class MySite extends AbstractSiteBase {
    public MySite() {
        super(new SiteDescriptor.Builder("MySite")
            .description("My site description.")
            .site(URI.create("https://www.mysite.com/"))
            .author("Author Name")
            .license("License")
            .build());
    }

    @Override
    public boolean canProcessURI(URI uri) {
        // Implement site-specific URI processing logic here
    }

    @Override
    public Optional<Canonizer> newCanonizer(URI uri) {
        // Optionally provide a site-specific Canonizer here
    }

    @Override
    public Optional<Extractor> newExtractor(URI uri) {
        // Optionally provide a site-specific Extractor here
    }
}
```

In the constructor of your site implementation, you should create a `SiteDescriptor` that describes your site. The `SiteDescriptor` includes the name, description, URL, author, and license of your site.

For more information, refer to the `AbstractSiteBase` class documentation.


### Registering site implementations

To register a site implementation, you need to create a provider-configuration file in the `META-INF/services` directory of your JAR file. The name of this file should be the fully-qualified class name of the `Site` interface (`com.penguineering.cleanuri.site.Site`). The file should contain the fully-qualified class name of your implementation, one per line.

For example, if you have a site implementation called `com.example.MySite`, you would create a file named `META-INF/services/com.penguineering.cleanuri.site.Site` with the following content:

```
com.example.MySite
```

The `SiteLoader` class uses the Java `ServiceLoader` mechanism to discover and load all available `Site` implementations on the classpath. When you call `findSitesOnClasspath()`, it returns a list of all loaded `Site` implementations.

Here's an example of how to use `SiteLoader`:

```java
SiteLoader loader = new SiteLoader();
List<Site> sites = loader.findSitesOnClasspath();
```

For more information, refer to the `SiteLoader` class documentation.

### Development

Version numbers are determined with [jgitver](https://jgitver.github.io/).
Please check your [IDE settings](https://jgitver.github.io/#_ides_usage) to avoid problems, as there are still some unresolved issues.
If you encounter a project version `0` there is an issue with the jgitver generator.

If access to the GitHub packages repository is not available, please check *Including the library from Local installation* on how to make the artifact available locally. 


## Maintainers

* Stefan Haun ([@penguineer](https://github.com/penguineer))


## Contributing

PRs are welcome!

If possible, please stick to the following guidelines:

* Keep PRs reasonably small and their scope limited to a feature or module within the code.
* If a large change is planned, it is best to open a feature request issue first, then link subsequent PRs to this issue, so that the PRs move the code towards the intended feature.


## License

[MIT](LICENSE.txt) © 2022-2025 Stefan Haun and contributors
