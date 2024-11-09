import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;
public class SQL{
    public static void main(String[] args) {
        Path relativePath = Paths.get(".\\assets\\covers\\cover4.png");
        Path absolutePath = relativePath.toAbsolutePath();
        System.out.println(absolutePath);
    }
}