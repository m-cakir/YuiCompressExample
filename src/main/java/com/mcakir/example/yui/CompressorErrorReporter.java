package com.mcakir.example.yui;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class CompressorErrorReporter implements ErrorReporter {

    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        String log = line < 0 ? message : (line + ':' + lineOffset + ':' + message);

        System.out.println("CompressorErrorReporter.warning: " + log);
    }

    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        String log = line < 0 ? message : (line + ':' + lineOffset + ':' + message);

        System.out.println("CompressorErrorReporter.error: " + log);
    }

    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
        error(message, sourceName, line, lineSource, lineOffset);
        return new EvaluatorException(message);
    }
}
