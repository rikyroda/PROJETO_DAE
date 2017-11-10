package web;

import dtos.CourseDTO;
import dtos.StudentDTO;
//import dtos.SubjectDTO;
import ejbs.CourseBean;
import ejbs.StudentBean;
//import ejbs.SubjectBean;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class AdministratorManager {

    @EJB
    private StudentBean studentBean;
    @EJB
    private CourseBean courseBean;
    @EJB
    //private SubjectBean subjectBean;
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    private StudentDTO newStudent;
    private StudentDTO currentStudent;
    private CourseDTO newCourse;
    private CourseDTO currentCourse;
    //private SubjectDTO newSubject;
    //private SubjectDTO currentSubject;
    private UIComponent component;

    public AdministratorManager() {
        newStudent = new StudentDTO();
        newCourse = new CourseDTO();
        //newSubject = new SubjectDTO();
    }

    /////////////// STUDENTS /////////////////
    public String createStudent() {
        try {
            studentBean.create(
                    newStudent.getUsername(),
                    newStudent.getPassword(),
                    newStudent.getName(),
                    newStudent.getEmail(),
                    newStudent.getCourseCode());
            newStudent.reset();
        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, logger);
            return null;
        }

        return "index?faces-redirect=true";
    }

    public List<StudentDTO> getAllStudents() {
        try {
            return studentBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public String updateStudent() {
        try {
            studentBean.update(
                    currentStudent.getUsername(),
                    currentStudent.getPassword(),
                    currentStudent.getName(),
                    currentStudent.getEmail(),
                    currentStudent.getCourseCode());

        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
        return "index?faces-redirect=true";
    }

    public void removeStudent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("studentUsername");
            String id = param.getValue().toString();
            studentBean.remove(id);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    /*public List<SubjectDTO> getCurrentStudentSubjects() {
        try {
            return subjectBean.getStudentSubjects(currentStudent.getUsername());
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }*/

    /////////////// COURSES /////////////////
    public String createCourse() {
        try {
            courseBean.create(
                    newCourse.getCode(),
                    newCourse.getName());
            newCourse.reset();
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, logger);
            return null;
        }
        return "index?faces-redirect=true";
    }

    public List<CourseDTO> getAllCourses() {
        try {
            return courseBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public void removeCourse(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("courseCode");
            int code = Integer.parseInt(param.getValue().toString());
            courseBean.remove(code);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    /*public List<SubjectDTO> getCurrentCourseSubjects() {
        try {
            return subjectBean.getCourseSubjects(currentCourse.getCode());
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    /////////////// SUBJECTS /////////////////
    public String createSubject() {
        try {
            subjectBean.create(
                    newSubject.getCode(),
                    newSubject.getName(),
                    newSubject.getCourseCode(),
                    newSubject.getCourseYear(),
                    newSubject.getScholarYear());
            newSubject.reset();
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
        return "index?faces-redirect=true";
    }

    public List<SubjectDTO> getAllSubjects() {
        try {
            return subjectBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public void removeSubject(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("subjectCode");
            int code = Integer.parseInt(param.getValue().toString());
            subjectBean.remove(code);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public List<StudentDTO> getEnrolledStudents() {
        try {
            return studentBean.getEnrolledStudents(currentSubject.getCode());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public List<StudentDTO> getUnrolledStudents() {
        try {
            return studentBean.getUnrolledStudents(currentSubject.getCode());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public void enrollStudent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("studentUsername");
            String username = param.getValue().toString();
            studentBean.enrollStudent(username, currentSubject.getCode());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public void unrollStudent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("studentUsername");
            String username = param.getValue().toString();
            studentBean.unrollStudent(username, currentSubject.getCode());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }*/

    /////////////// GETTERS & SETTERS /////////////////    
    public StudentDTO getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(StudentDTO newStudent) {
        this.newStudent = newStudent;
    }

    public StudentDTO getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(StudentDTO currentStudent) {
        this.currentStudent = currentStudent;
    }

    public CourseDTO getNewCourse() {
        return newCourse;
    }

    public void setNewCourse(CourseDTO newCourse) {
        this.newCourse = newCourse;
    }

    public CourseDTO getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(CourseDTO currentCourse) {
        this.currentCourse = currentCourse;
    }
/*
    public SubjectDTO getNewSubject() {
        return newSubject;
    }

    public void setNewSubject(SubjectDTO newSubject) {
        this.newSubject = newSubject;
    }

    public SubjectDTO getCurrentSubject() {
        return currentSubject;
    }

    public void setCurrentSubject(SubjectDTO currentSubject) {
        this.currentSubject = currentSubject;
    }
*/
    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    ///////////// VALIDATORS ////////////////////////
    public void validateUsername(FacesContext context, UIComponent toValidate, Object value) {
        try {
            //Your validation code goes here
            String username = (String) value;
            //If the validation fails
            if (username.startsWith("xpto")) {
                FacesMessage message = new FacesMessage("Error: invalid username.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(toValidate.getClientId(context), message);
                ((UIInput) toValidate).setValid(false);
            }
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unkown error.", logger);
        }
    }
}
