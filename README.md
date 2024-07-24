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
    int choice = Validator.getInt("Enter record your choice: ", "Just be 1-> " + list.size(), "Invalid!", 1, list.size());
    Student oldStudentRecord = list.get(choice - 1);
    System.out.println(oldStudentRecord);
    String choose = Validator.getString("Do you want Update(U) or Delete(D): ", "Just U or D", "[UDud]");
    if (choose.equalsIgnoreCase("U")) {
        inputer.inputStudentName();
        inputer.inputSemester();
        inputer.inputCourseName();
        Student newStudentRecord = inputer.getStudent();
        boolean nameChanged = !newStudentRecord.getStudentName().equals(oldStudentRecord.getStudentName());

        if (nameChanged) {
            for (Student s : studentManager.getListStudentById(oldStudentRecord.getId())) {
                s.setStudentName(newStudentRecord.getStudentName());
            }
        }

        if (oldStudentRecord.getSemester().equals(newStudentRecord.getSemester()) &&
            oldStudentRecord.getCourseName().equals(newStudentRecord.getCourseName())) {
            throw new Exception("New record be duplicate!!");
        } else {
            studentManager.update(oldStudentRecord, newStudentRecord);
        }
    } else {
        studentManager.delete(oldStudentRecord);
    }
}
