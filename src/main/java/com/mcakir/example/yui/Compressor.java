package com.mcakir.example.yui;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class Compressor {

    public static final String RESOURCES = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    public static final String PATH_CSS = RESOURCES + "css\\";
    public static final String PATH_JS = RESOURCES + "js\\";
    public static final String PATH_COMPRESSED = RESOURCES + "min\\";

    public static final String MERGED_FILE_NAME = "merged";
    public static final String COMPRESSED_FILE_NAME = "compressed";

    public static void compress(){

        try {

            System.out.println("RESOURCES " + RESOURCES);

            compressFiles();

        } catch (Exception e) {
            System.out.println("Compressor.compress: error: " + e);
        }

    }

    private static void compressFiles() throws IOException {

        String[] cssFiles = new String[]{
                PATH_CSS + "a.css",
                PATH_CSS + "b.css",
        };

        String[] jsFiles = new String[]{
                PATH_JS + "a.js",
                PATH_JS + "b.js",
        };

        Options defaultOptions = new Options();

        String mergedCssFile = PATH_COMPRESSED + "\\" + MERGED_FILE_NAME + ".css";
        String mergedJsFile = PATH_COMPRESSED + "\\" + MERGED_FILE_NAME + ".js";

        String compressedCssFile = PATH_COMPRESSED + "\\" + COMPRESSED_FILE_NAME + ".css";
        String compressedJsFile = PATH_COMPRESSED + "\\" + COMPRESSED_FILE_NAME + ".js";

        mergeFiles(cssFiles, mergedCssFile);
        mergeFiles(jsFiles, mergedJsFile);

        System.out.println("... merging - Done");

        compressJS(mergedJsFile, compressedJsFile, defaultOptions);
        compressCss(mergedCssFile, compressedCssFile, defaultOptions);

        System.out.println("... compressing - Done");
    }

    private static void mergeFiles(String[] files, String outputFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            BufferedInputStream bis = null;
            byte[] buffer = new byte[1024];
            for(int i = 0; i < files.length; i++){
                bis = new BufferedInputStream(new FileInputStream(files[i]));
                int numbytes = 0;
                while( (numbytes = bis.read(buffer)) != -1){
                    writer.write(new String(buffer, 0, numbytes, "UTF-8"));
                }
                bis.close();
                writer.newLine();
            }
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void compressJS(String inputFilename, String outputFilename, Options o) throws IOException {
        Reader in = null;
        Writer out = null;
        try {
            in = new InputStreamReader(new FileInputStream(inputFilename), o.charset);

            JavaScriptCompressor compressor = new JavaScriptCompressor(in, new CompressorErrorReporter());
            in.close(); in = null;

            out = new OutputStreamWriter(new FileOutputStream(outputFilename), o.charset);
            compressor.compress(out, o.lineBreakPos, o.munge, o.verbose, o.preserveAllSemiColons, o.disableOptimizations);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    private static void compressCss(String inputFilename, String outputFilename, Options o) throws IOException {
        Reader in = null;
        Writer out = null;
        try {
            in = new InputStreamReader(new FileInputStream(inputFilename), o.charset);

            CssCompressor compressor = new CssCompressor(in);
            in.close(); in = null;

            out = new OutputStreamWriter(new FileOutputStream(outputFilename), o.charset);
            compressor.compress(out, o.lineBreakPos);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

}
