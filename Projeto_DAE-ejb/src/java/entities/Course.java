package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "COURSES",
uniqueConstraints =
@UniqueConstraint(columnNames = {"NAME"}))
@NamedQueries({
    @NamedQuery(name = "getAllCourses",
    query = "SELECT c FROM Course c ORDER BY c.name"),
    @NamedQuery(name = "getAllCoursesNames",
    query = "SELECT c.name FROM Course c ORDER BY c.name")})
public class Course implements Serializable {
    @Id
    private int code;
    @NotNull (message= "Course name must not be empty")
    private String name;
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Student> students;
    //@OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    //private List<Subject> subjects;
    
    public Course(){
        students = new LinkedList<>();
        //subjects = new LinkedList<>();
    }
    
    public Course(int code, String name){
        this.code = code;
        this.name = name;
        students = new LinkedList<>();
        //subjects = new LinkedList<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    /*public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }    */
}
