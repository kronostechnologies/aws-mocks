import com.github.jengelman.gradle.plugins.shadow.relocation.RelocateClassContext
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowCopyAction
import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.util.PatternSet
import shadow.org.apache.tools.zip.ZipEntry
import shadow.org.apache.tools.zip.ZipOutputStream

/**
 * This is inspired from ServiceFileTransformer
 * @see com.github.jengelman.gradle.plugins.shadow.transformers.ServiceFileTransformer
 *
 * AWS defines it's request handlers in files similar to services descriptor and we need to also relocate those paths
 * when we relocate AWS jars' content
 */
class RewriteServiceHandlers : Transformer {
    private val patterns: PatternSet = PatternSet().include("com/amazonaws/services/**/*.handler*")
    private val linesToWrite: MutableMap<String, String> = mutableMapOf()

    override fun getName(): String = "RewriteServiceHandlersForAws"

    override fun canTransformResource(element: FileTreeElement?): Boolean = if (element != null) {
        val target: FileTreeElement = if (element is ShadowCopyAction.ArchiveFileTreeElement) {
            element.asFileTreeElement()
        } else element

        patterns.asSpec.isSatisfiedBy(target)
    } else false

    override fun hasTransformedResource(): Boolean = linesToWrite.isNotEmpty()

    override fun transform(context: TransformerContext?) {
        context?.`is`?.bufferedReader()?.useLines { lines ->
            context.relocators.forEach { relocator ->
                val updatedContent = lines.map { line ->
                    val lineContext = RelocateClassContext.builder()
                        .className(line)
                        .stats(context.stats)
                        .build()
                    if (relocator.canRelocateClass(lineContext.className)) {
                        relocator.relocateClass(lineContext)
                    } else {
                        line
                    }
                }.joinToString("\n")

                linesToWrite[context.path] = updatedContent
            }
        }
    }

    override fun modifyOutputStream(outputStream: ZipOutputStream?, preserveFileTimestamps: Boolean) {
        if (outputStream != null) {
            linesToWrite.forEach { (path, content) ->
                val entry = ZipEntry(path)
                entry.time = TransformerContext.getEntryTimestamp(preserveFileTimestamps, entry.time)
                outputStream.putNextEntry(entry)
                outputStream.write(content.toByteArray())
                outputStream.closeEntry()
            }
        }
    }
}
