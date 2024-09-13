package org.example

import org.jetbrains.kotlin.wasm.ir.WasmElement
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.ScriptingHostConfiguration
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.host.getScriptingClass
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptHost {
    fun executeScript() {

        val host = BasicJvmScriptingHost()
        val modelFromOutside = Model("A Model from Outside")
        val result = host.eval(StringScriptSource("""
            printName()
            "Done"
        """.trimIndent()), ScriptWithMavenDepsConfiguration, ScriptEvaluationConfiguration {
            implicitReceivers(modelFromOutside)
        })


        if (result is ResultWithDiagnostics.Success) {
            val x = result.value.returnValue
            if (x is ResultValue.Value) {
                assert(x.value == "Done")
            }
        }

    }

    object ScriptWithMavenDepsConfiguration : ScriptCompilationConfiguration(
        {
            implicitReceivers(Model::class)
            //providedProperties("typedDocument" to TypedDocument::class)
            // Implicit imports for all scripts of this type
            //defaultImports(TypedDocument::class)
            jvm {
                // Extract the whole classpath from context classloader and use it as dependencies
                dependenciesFromCurrentContext(wholeClasspath = true)
                //dependenciesFromClassloader(classLoader = TypedDocument::class.java.classLoader, wholeClasspath = true)
                //dependenciesFromClassContext(SimpleMainKtsScriptDefinition::class, wholeClasspath = true)
                //dependencies(*classpathFromClassloader(TypedDocument::class.java.classLoader)!!)
                //dependencies(JvmDependency(File("target/base-starter-flow-quarkus-dev.jar")))
            }
            // Callbacks
            refineConfiguration {
                // Process specified annotations with the provided handler
                //onAnnotations(DependsOn::class, Repository::class, handler = ::configureMavenDepsOnAnnotations)
            }
        }
    )

}