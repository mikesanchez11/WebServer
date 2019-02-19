package com.michaelsanchez.utils;

import java.io.*;

public class PrintWriterWrapper {
    private final PrintWriter pw;
    private StringBuilder buffer = new StringBuilder();

    public PrintWriterWrapper(PrintWriter pw) {
        this.pw = pw;
    }

    public void println(Object x) {
        if (pw != null) {
            this.pw.println(x);
        }
        buffer.append(x);
    }

    public String getBufferContent() {
        return buffer.toString();
    }
}
