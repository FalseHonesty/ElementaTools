# Elementa-Tools

Kotlin Compiler Plugin that adds some useful features for the [Elementa](https://github.com/Sk1erLLC/Elementa)
library.
This plugin uses the new IR backend for the Kotlin compiler.

## Example

Given following code:

```kotlin
val myVarName = UIBlock()
val myComplexVar = (UIBlock().constrain {  } childOf myVarName).apply {} as UIComponent
```

the following will succeed automatically using this plugin

```kotlin
assertEquals("myVarName", myVarName.componentName)
assertEquals("myComplexVar", myComplexVar.componentName)
```

While this may not seem useful instantly, it works very well in concert with Elementa's
Inspector widget, which will display the `componentName` property if available, making
debugging with the Inspector far more enjoyable.

## Gradle Plugin

Builds of the Gradle plugin are available through the
[Gradle Plugin Portal][elementa-tools-gradle].

```kotlin
plugins {
  kotlin("jvm") version "1.4.0"
  id("dev.falsehonesty.elementa-tools") version "0.1.0"
}
```

## Kotlin IR

Using this compiler plugin only works with code compiled with the Kotlin
1.4.0 IR compiler. Note: As of 1.4.0, Kotlin IR is in Alpha.

```kotlin
compileKotlin {
    kotlinOptions {
        useIR = true
    }
}
```

[elementa-tools-gradle]: https://plugins.gradle.org/plugin/dev.falsehonesty.elementa-tools
