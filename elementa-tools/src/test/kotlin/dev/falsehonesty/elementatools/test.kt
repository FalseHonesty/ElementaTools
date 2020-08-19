package dev.falsehonesty.elementatools

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import kotlin.test.assertEquals
import kotlin.test.fail

class CompilerTest {
    @Test
    fun simpleVarDeclaration() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import kotlin.test.assertEquals
            fun main() {
              val myVarName = UIBlock()
              assertEquals("myVarName", myVarName.componentName)
            }"""
        )
    }

    @Test
    fun complexVarDeclaration() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
              val myVarName = UIBlock()
              val myComplexVar = (UIBlock().constrain {  } childOf myVarName).apply {} as UIComponent
              assertEquals("myVarName", myVarName.componentName)
              assertEquals("myComplexVar", myComplexVar.componentName)
            }"""
        )
    }

    @Test
    fun simpleVarAssignment() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
              val myVarName: UIComponent
              myVarName = UIBlock()
              assertEquals("myVarName", myVarName.componentName)
            }"""
        )
    }

    @Test
    fun complexVarAssignment() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
              val myVarName: UIComponent
              myVarName = UIBlock()
              val myComplexVar: UIComponent
              myComplexVar = (UIBlock().constrain {  } childOf myVarName).apply {} as UIComponent
              assertEquals("myVarName", myVarName.componentName)
              assertEquals("myComplexVar", myComplexVar.componentName)
            }"""
        )
    }

    @Test
    fun simpleFieldDeclaration() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
                Test()
            }
            class Test {
                private val myFieldName = UIBlock()
                
                init {
                    println(this)
                    assertEquals("myFieldName", myFieldName.componentName)
                }
            }"""
        )
    }

    @Test
    fun complexFieldDeclaration() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
                Test()
            }
            class Test {
                private val myFieldName = UIBlock()
                private val myComplexField = (UIBlock().constrain {  } childOf myFieldName).apply {} as UIComponent
                
                init {
                    assertEquals("myFieldName", myFieldName.componentName)
                    assertEquals("myComplexField", myComplexField.componentName)
                }
            }
            """
        )
    }

    @Test
    fun simpleFieldAssignment() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
                Test()
            }
            class Test {
                private val myFieldName: UIComponent
                
                init {
                    myFieldName = UIBlock()
                    assertEquals("myFieldName", myFieldName.componentName)
                }
            }"""
        )
    }

    @Test
    fun complexFieldAssignment() {
        compiledTestHelper(
            """
            import club.sk1er.elementa.UIBlock
            import club.sk1er.elementa.UIComponent
            import kotlin.test.assertEquals
            fun main() {
                Test()
            }
            class Test {
                private val myFieldName: UIComponent
                private val myComplexField: UIComponent
                
                init {
                    myFieldName = UIBlock()
                    myComplexField = (UIBlock().constrain {  } childOf myFieldName).apply {} as UIComponent
                    
                    assertEquals("myFieldName", myFieldName.componentName)
                    assertEquals("myComplexField", myComplexField.componentName)
                }
            }
            """
        )
    }
}

fun compiledTestHelper(@Language("kotlin") source: String) {
    val result = KotlinCompilation().apply {
        sources = listOf(SourceFile.kotlin("main.kt", source))
        useIR = true
        messageOutputStream = System.out
        compilerPlugins = listOf(ElementaToolsComponentRegistrar())
        inheritClassPath = true
    }.compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    val kClazz = result.classLoader.loadClass("MainKt")
    val main = kClazz.declaredMethods.single { it.name == "main" && it.parameterCount == 0 }
    try {
        try {
            main.invoke(null)
        } catch (t: InvocationTargetException) {
            throw t.cause!!
        }
    } catch (t: Throwable) {
        fail("Shouldn't have thrown exception", t)
    }
}
