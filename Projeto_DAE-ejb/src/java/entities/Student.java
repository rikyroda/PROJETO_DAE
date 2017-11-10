package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name = "getAllStudents",
    query = "SELECT s FROM Student s ORDER BY s.name"),
    @NamedQuery(name = "getAllCourseStudents",
    query = "SELECT s FROM Student s WHERE s.course.code = :courseCode ORDER BY s.name")})
public class Student extends User implements Serializable {

    @ManyToOne
    @JoinColumn(name="COURSE_CODE")
    @NotNull (message="A student must have a course")
    private Course course;
    //@ManyToMany(mappedBy = "students")
    //private List<Subject> subjects;

    protected Student() {
        //subjects = new LinkedList<>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        //subjects = new LinkedList<>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
    }*/

    @Override
    public String toString() {
        return "Student{" + "username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + '}';
    }
}
