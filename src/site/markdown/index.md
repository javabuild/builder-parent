## A Maven plugin to power up your Maven builds with custom java code

Maven offers a lot of standard plugins that let you write fully automated and reliable builds. You can even develop your own plugins if you have some specific needs. But it can be complicated to make a separate project for your custom plugin with a separate release lifecycle. Wouldn't it be easier if you could just put your build code directly inside the project?

**builder-maven-plugin** adds a **src/build/java** folder on your project

![Source structure](images/ScreenShot001.png "Source structure") 

Then you just write java classes with a few **annotations** in order to bind them to standard maven lifecycle.

![Source example](images/ScreenShot002.png "Source example") 

<a href="https://github.com/javabuild/builder-parent"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://camo.githubusercontent.com/e7bbb0521b397edbd5fe43e7f760759336b5e05f/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f677265656e5f3030373230302e706e67" alt="Fork me on GitHub" data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_green_007200.png" /></a>

In the end, you will still have a **pom.xml** file with the essential informations of the project (dependencies, scm...) but for all the more custom tasks, no more need to use a complex and poorly documented maven plugin, no more need for maven-antrun-plugin, you just code java in the project. This is only some examples of What you can do :

* generate java classes
* scan the project for some annotations and do some stuff (at build time, not at runtime like with Spring!)
* generate documentation pages
* start an embedded database before launching your unit tests
* use any of the many libraries that can make cool stuff but don't have a maven plugin
* anything else you cannot do with an existing maven plugin
* ...

### Where to start?

* see the <a href="builder-maven-plugin/usage.html">usage page of the maven plugin</a> for how to use the plugin
* see the <a href="https://github.com/javabuild/builder-parent/tree/master/builder-maven-plugin/src/it/builder-maven-plugin-test">example project on Github</a> for examples on how to use it

