package server.webcontrollers;

public interface IStudentController {


    String getStudentName(int studentId);
    String getStudentEmail(int studentId);
    String getStudentWallet(int studentId);
    String getStudentGroup(int studentId);
    String getStudentExpLevel(int studentId);
    String getStudentClass(int studentId);
}