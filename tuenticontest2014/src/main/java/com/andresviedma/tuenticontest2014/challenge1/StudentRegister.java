package com.andresviedma.tuenticontest2014.challenge1;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.andresviedma.tuenticontest2014.challenge1.Student.StudentProfile;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;


public class StudentRegister {
    private Multimap<StudentProfile, Student> studentsByProfile = Multimaps.synchronizedSetMultimap(HashMultimap.<StudentProfile, Student>create());
    
    public void loadData (LineNumberReader in) throws IOException {
        String linea = in.readLine();
        while ((linea != null) && (linea.length() > 0)) {
            Student student = Student.parse(linea);
            this.studentsByProfile.put(student.getProfile(), student);
            
            linea = in.readLine();
        }
    }

    
    public Collection<Student> findStudentsByProfile (StudentProfile profile) {
        return Collections.unmodifiableCollection(this.studentsByProfile.get(profile));
    }
    
    
    public List<String> findSortedStudentNamesByProfile (StudentProfile profile) {
        Collection<Student> studs = this.findStudentsByProfile(profile);
        if (studs.isEmpty())  return Collections.emptyList();
        
        List<String> res = new ArrayList<String>(studs.size());
        Iterator<Student> it = studs.iterator();
        while (it.hasNext()) {
            Student stud = it.next();
            res.add (stud.getName());
        }
        Collections.sort(res);
        return res;
    }
    
}
