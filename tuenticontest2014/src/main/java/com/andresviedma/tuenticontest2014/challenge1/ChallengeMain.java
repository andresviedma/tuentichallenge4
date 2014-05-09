package com.andresviedma.tuenticontest2014.challenge1;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import com.andresviedma.tuenticontest2014.challenge1.Student.StudentProfile;
import com.google.common.base.Joiner;


public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        // Load students register
        URL studentsUrl = ChallengeMain.class.getResource("students");
        InputStream inStudents0 = studentsUrl.openStream();
        LineNumberReader inStudents = new LineNumberReader (new InputStreamReader (inStudents0, "UTF-8"));

        StudentRegister register = new StudentRegister();
        register.loadData(inStudents);
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));

        // first line: number of cases
        String linea = in.readLine();
        
        // Remaining lines: test cases
        linea = in.readLine();
        int i = 1;
        while ((linea != null) && (linea.length() > 0)) {
            StudentProfile profile = StudentProfile.parse(linea);
            List<String> resList = register.findSortedStudentNamesByProfile(profile);

            String res = "NONE";
            if (!resList.isEmpty()) {
                Joiner joiner = Joiner.on(',');
                res = joiner.join(resList);
            }
            out.println ("Case #" + i + ": " + res);
            
            linea = in.readLine();
            i++;
        }
        out.close();
    }

}
