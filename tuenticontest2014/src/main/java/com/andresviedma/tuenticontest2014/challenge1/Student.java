package com.andresviedma.tuenticontest2014.challenge1;

import java.util.Objects;
import java.util.StringTokenizer;


public class Student {
    private String name;
    private StudentProfile profile;

    
    public static Student parse(String l) {
        Student res = new Student();
        int firstComma = l.indexOf(',');
        res.name = l.substring(0, firstComma);
        String profileStr = l.substring(firstComma + 1);
        res.profile = StudentProfile.parse(profileStr);
        
        return res;
    }
    
    
    public Student() {
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public StudentProfile getProfile() {
        return profile;
    }
    public void setProfile(StudentProfile profile) {
        this.profile = profile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.profile);
    }
    @Override
    public boolean equals(Object obj) {
        if((obj == null) || (obj.getClass() != this.getClass()))  return false;
        final Student s = (Student) obj;
        return Objects.equals(s.name, name) && Objects.equals(s.profile, profile);
    }
    
    

    public static enum Gender { M, F };
    
    public static class StudentProfile {
        
        private Gender gender;
        private int age;
        private String studies;
        private int academicYear;
        
        public static StudentProfile parse(String s) {
            StudentProfile res = new StudentProfile();
            StringTokenizer tok = new StringTokenizer(s, ",");
            res.gender = Gender.valueOf(tok.nextToken());
            res.age = Integer.parseInt(tok.nextToken());
            res.studies = tok.nextToken();
            res.academicYear = Integer.parseInt(tok.nextToken());
            return res;
        }
        
        public Gender getGender() {
            return gender;
        }
        public void setGender(Gender gender) {
            this.gender = gender;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public String getStudies() {
            return studies;
        }
        public void setStudies(String studies) {
            this.studies = studies;
        }
        public int getAcademicYear() {
            return academicYear;
        }
        public void setAcademicYear(int academicYear) {
            this.academicYear = academicYear;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.academicYear, this.age, this.gender, this.studies);
        }
        @Override
        public boolean equals(Object obj) {
            if((obj == null) || (obj.getClass() != this.getClass()))  return false;
            final StudentProfile s = (StudentProfile) obj;
            return    Objects.equals(s.academicYear, academicYear)
                   && Objects.equals(s.age, age)
                   && Objects.equals(s.gender, gender)
                   && Objects.equals(s.studies, studies);
        }
        
    }
}
