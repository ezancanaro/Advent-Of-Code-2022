/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2022;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Eric
 */
public interface AdventPuzzle {

    static InputStream getInput(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        FileInputStream in = new FileInputStream(f);
        return in;
    }

    public InputStream example();

    public int solve(InputStream input) throws IOException, Exception;

}
