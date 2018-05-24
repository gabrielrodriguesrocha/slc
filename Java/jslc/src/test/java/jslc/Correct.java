package jslc;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;


@RunWith(Parameterized.class)
public class Correct {

    Handler handle = new Handler("test/ok");

    @Parameters
    public static File[] data() {
        return new File("test/ok").listFiles();
    }

    @Parameter
    public File file;

    @Test 
    public void lexTest() {
        Compiler compiler = new Compiler();

        Assert.assertNotNull(compiler.compile(handle.readFile(file), new PrintWriter(System.out)));
    }
}