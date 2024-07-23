/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bo.ManageStudent;
import bo.StudentInputer;
import entity.Course;
import entity.Student;
import java.util.ArrayList;
import utils.Validator;

/**
 *
 * @author win
 */
public class Controller {

    private ManageStudent studentManager;
    private StudentInputer inputer;

    public Controller() {
        studentManager = new ManageStudent();
    }

    public void createStudent() throws Exception {
        while (true) {
            inputer = new StudentInputer();
            inputer.inputID();
            Student student = inputer.getStudent();
            if (studentManager.getListStudentById(student.getId()).isEmpty()) {
                inputer.inputStudentName();
            } else {
                String name = studentManager.getListStudentById(student.getId()).get(0).getStudentName();
                student.setStudentName(name);
                System.out.println("Name: " + name);
            }
            inputer.inputSemester();
            inputer.inputCourseName();
            if (!studentManager.add(student)) {
                throw new Exception("Can not create Student!");
            }
            if (studentManager.getList().size() > 3) {
                String choice = Validator.getString("Do you continue ( Y or N)? ", "Just Y or N", "[YNyn]");
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }
        }
    }

    public void findAndSort() throws Exception {
        String name = Validator.getString("Enter name student: ", "Invalid!", "[A-Za-z\\s]+").trim();
        ArrayList<Student> list = studentManager.getListStudentByName(name);
        if (list.isEmpty()) {
            throw new Exception("Can not found name!");
        }
        ManageStudent result = new ManageStudent();
        result.setList(list);
        result.sortStudentsByName();
        System.out.println(result.toString());
    }

    public void updateOrDelete() throws Exception {
        inputer = new StudentInputer();
        String id = Validator.getString("Enter id: ", "Invalid!", "^(SE|se)\\d+$");
        ArrayList<Student> list = studentManager.getListStudentById(id);
        if (list.isEmpty()) {
            throw new Exception("Can not found id");
        }
        ManageStudent result = new ManageStudent();
        result.setList(list);
        System.out.println(result.toString());
        int choice = Validator.getInt("Enter record your choice: ", "Just be 1-> " + list.size(),
                "Invalid!", 1, list.size());
        Student selectedStudent = list.get(choice - 1);
        System.out.println(selectedStudent);
        String choose = Validator.getString("Do you want Update(U) or Delete(D): ",
                "Just U or D", "[UDud]");
        if (choose.equalsIgnoreCase("U")) {
            System.out.println("Updating student with ID: " + selectedStudent.getId());
            String newName = Validator.getString("Enter new name: ", "Invalid!", "[A-Za-z\\s]+");
            for (Student student : studentManager.getListStudentById(selectedStudent.getId())) {
                student.setStudentName(newName);
            }
            inputer.inputSemester();
            String newSemester = inputer.getStudent().getSemester();
            inputer.inputCourseName();
            Course newCourse = inputer.getStudent().getCourseName();

            // Check if both new semester and new course name already exist for this student ID
            boolean duplicateFound = studentManager.getList().stream().anyMatch(s ->
                    s.getId().equals(selectedStudent.getId()) &&
                            s.getSemester().equals(newSemester) &&
                            s.getCourseName().equals(newCourse)
            );

            // Check if course name exists for this student ID
            boolean courseNameExists = studentManager.getList().stream().anyMatch(s ->
                    s.getId().equals(selectedStudent.getId()) &&
                            s.getCourseName().equals(newCourse)
            );

            if (duplicateFound) {
                throw new Exception("Both semester and course name already exist for this student ID!");
            } else if (courseNameExists && !selectedStudent.getSemester().equals(newSemester)) {
                selectedStudent.setCourseName(newCourse);
            }

            selectedStudent.setSemester(newSemester);

        } else {
            studentManager.delete(selectedStudent);
        }
    }



    public void report() throws Exception {
        String result = studentManager.report();
        if (result == null) {
            throw new Exception("List is empty!");
        }
        System.out.println(result);
    }

    public void generateStudent() throws Exception {
        studentManager.add(new Student("se1", "Nguyen Quan", "Fall2023", Course.JAVA));
        studentManager.add(new Student("se2", "Nguyen Quan", "Fall2024", Course.JAVA));
        studentManager.add(new Student("se1", "Nguyen Quan", "Fall2023", Course.C_CPP));
        studentManager.add(new Student("se1", "Nguyen Quan", "Fall2023", Course.DOT_NET));
        studentManager.add(new Student("se2", "Tran Linh", "Sum2024", Course.JAVA));
        studentManager.add(new Student("se2", "Tran Linh", "Sum2024", Course.DOT_NET));
        studentManager.add(new Student("se3", "Le Thu Thao", "Sum2024", Course.JAVA));
        studentManager.add(new Student("se4", "Le Phuong Minh", "Sum2024", Course.DOT_NET));
        studentManager.add(new Student("se5", "Minh Vu", "Spring2023", Course.JAVA));
        studentManager.add(new Student("se6", "Tuan Minh", "Spring2023", Course.C_CPP));
    }

}
