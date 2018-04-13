package jslc;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;


@RunWith(Parameterized.class)
public class Syntax {

    Handler handle = new Handler("test/syntax");

    @Parameters
    public static File[] data() {
        return new File("test/syntax").listFiles();
    }

    @Parameter
    public File file;

    @Test(expected = Exception.class) 
    public void lexTest() {
        Compiler compiler = new Compiler();

        Assert.assertNull(compiler.compile(handle.readFile(file)));
    }
}