package compiler;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Created by sulvto on 16-12-3.
 */
public class CompilerTest {
    private String getResourcePath(String name) throws FileNotFoundException {
        URL resource = CompilerTest.class.getResource("/"+name);
        if (resource == null) {
            throw new FileNotFoundException();
        }
        return resource.getPath();
    }

    @Test
    public void main() throws Exception {

        String[] args = {"--dump-ast", getResourcePath("hello.c")};
        Compiler.main(args);
    }

    @Test
    public void commandMain() throws Exception {

    }

}