package jslc;

import org.junit.runners.Parameterized.Parameters;

import java.io.*;


public class Handler {
    public final String path;

    public Handler(String path) {
        this.path = path;
    }

    @Parameters
    public static File[] data(String path) {
        return new File(path).listFiles();
    }

    public char [] readFile(File file) {
        FileReader stream;
        int numChRead;

        if ( ! file.exists() || ! file.canRead() ) {
          throw new RuntimeException();
        }
        try { 
          stream = new FileReader(file);  
         } catch ( FileNotFoundException e ) {
             throw new RuntimeException();
         }
             // one more character for '\0' at the end that will be added by the
             // compiler
         char []input = new char[ (int ) file.length() + 1 ];
         
         try {
           numChRead = stream.read( input, 0, (int ) file.length() );
         } catch ( IOException e ) {
             throw new RuntimeException();
         }
             
         if ( numChRead != file.length() ) {
             throw new RuntimeException();
         }
         try {
           stream.close();
         } catch ( IOException e ) {
             throw new RuntimeException();
         }

         return input;
    }
}