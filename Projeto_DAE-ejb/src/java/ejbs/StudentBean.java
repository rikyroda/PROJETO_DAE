package ejbs;

import dtos.StudentDTO;
import entities.Course;
import entities.Student;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.StudentEnrolledException;
import exceptions.SubjectNotInCourseException;
import exceptions.Utils;
import exceptions.StudentNotEnrolledException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class StudentBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email, int courseCode)
         throws EntityAlreadyExistsException, EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            if (em.find(Student.class, username) != null) {
                throw new EntityAlreadyExistsException("A user with that username already exists.");
            }
            Course course = em.find(Course.class, courseCode);
            if (course == null) {
                throw new EntityDoesNotExistsException("There is no course with that code.");
            }
            Student student = new Student(username, password, name, email, course);
            course.addStudent(student);
            em.persist(student);
        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<StudentDTO> getAll() {
        try {
            List<Student> students = (List<Student>) em.createNamedQuery("getAllStudents").getResultList();
            return studentsToDTOs(students);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public Student getStudent(String username) {
        try {
            Student student = em.find(Student.class, username);
            return student;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(String username, String password, String name, String email, int courseCode) 
        throws EntityDoesNotExistsException, MyConstraintViolationException{
        try {
            Student student = em.find(Student.class, username);
            if (student == null) {
                throw new EntityDoesNotExistsException("There is no student with that username.");
            }

            Course course = em.find(Course.class, courseCode);
            if (course == null) {
                throw new EntityDoesNotExistsException("There is no course with that code.");
            }

            student.setPassword(password);
            student.setName(name);
            student.setEmail(email);
            student.getCourse().removeStudent(student);
            student.setCourse(course);
            course.addStudent(student);
            em.merge(student);
            
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void remove(String username) throws EntityDoesNotExistsException {
        try {
            Student student = em.find(Student.class, username);
            if (student == null) {
                throw new EntityDoesNotExistsException("There is no student with that username.");
            }
            student.getCourse().removeStudent(student);
            
            /*for (Subject subject : student.getSubjects()) {
                subject.removeStudent(student);
            }*/
            
            em.remove(student);
        
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    /*public void enrollStudent(String username, int subjectCode) 
            throws EntityDoesNotExistsException, SubjectNotInCourseException, StudentEnrolledException{
        try {

            Student student = em.find(Student.class, username);
            if (student == null) {
                throw new EntityDoesNotExistsException("There is no student with that username.");
            }

            Subject subject = em.find(Subject.class, subjectCode);
            if (subject == null) {
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }

            if (!student.getCourse().getSubjects().contains(subject)) {
                throw new SubjectNotInCourseException("Student's course has no such subject.");
            }

            if (subject.getStudents().contains(student)) {
                throw new StudentEnrolledException("Student is already enrolled in that subject.");
            }

            subject.addStudent(student);
            student.addSubject(subject);

        } catch (EntityDoesNotExistsException | SubjectNotInCourseException | StudentEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }*/
    
    /*public void unrollStudent(String username, int subjectCode) 
            throws EntityDoesNotExistsException, StudentNotEnrolledException {
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if(subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            
            Student student = em.find(Student.class, username);
            if(student == null){
                throw new EntityDoesNotExistsException("There is no student with that username.");
            }
            
            if(!subject.getStudents().contains(student)){
                throw new StudentNotEnrolledException();
            }            
            
            subject.removeStudent(student);
            student.removeSubject(subject);

        } catch (EntityDoesNotExistsException | StudentNotEnrolledException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }*/
    
    /*public List<StudentDTO> getEnrolledStudents(int subjectCode) throws EntityDoesNotExistsException{
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if( subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            List<Student> students = (List<Student>) subject.getStudents();
            return studentsToDTOs(students);
        } catch (EntityDoesNotExistsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }*/

    /*public List<StudentDTO> getUnrolledStudents(int subjectCode) throws EntityDoesNotExistsException{
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if( subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            List<Student> students = (List<Student>) em.createNamedQuery("getAllCourseStudents")
                    .setParameter("courseCode", subject.getCourse().getCode())
                    .getResultList();
            List<Student> enrolled = em.find(Subject.class, subjectCode).getStudents();
            students.removeAll(enrolled);
            return studentsToDTOs(students);
        } catch (EntityDoesNotExistsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }*/

    StudentDTO studentToDTO(Student student) {
        return new StudentDTO(
                student.getUsername(),
                null,
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName());
    }

    List<StudentDTO> studentsToDTOs(List<Student> students) {
        List<StudentDTO> dtos = new ArrayList<>();
        for (Student s : students) {
            dtos.add(studentToDTO(s));
        }
        return dtos;
    }
}
