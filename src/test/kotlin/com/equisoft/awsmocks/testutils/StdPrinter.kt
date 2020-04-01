package com.equisoft.awsmocks.testutils

import org.testcontainers.containers.output.OutputFrame
import java.util.function.Consumer

class StdPrinter : Consumer<OutputFrame> {
    override fun accept(frame: OutputFrame) {
        when (frame.type) {
            OutputFrame.OutputType.STDOUT -> System.out
            OutputFrame.OutputType.STDERR -> System.err
            OutputFrame.OutputType.END -> null
            null -> null
        }?.println(frame.utf8String)
    }
}
