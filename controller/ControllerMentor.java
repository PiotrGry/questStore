package controller;

import view.ViewMentor;
import dao.*;
import model.*;

import java.util.ArrayList;

public class ControllerMentor implements IUserController{
    private ViewMentor viewMentor;
    private Mentor mentor;

    public ControllerMentor(Mentor mentor){
        this.viewMentor = new ViewMentor();
        this.mentor = mentor;
    }

    public void createStudent() {
        DaoStudent daoStudent = new DaoStudent();

        String nameRequest = "Enter name of new student: ";
        String studentName = viewMentor.getInputFromUser(nameRequest);

        String passwordRequest = "Enter password of new student: ";
        String studentPassword = viewMentor.getInputFromUser(passwordRequest);

        String emailRequest = "Enter email of new student: ";
        String studentEmail = viewMentor.getInputFromUser(emailRequest);

        String classIdRequest = "Enter ID of class which student will attend: ";
        int classId = viewMentor.getIntInputFromUser(classIdRequest);

        daoStudent.createStudent(studentName, studentPassword, studentEmail, classId);

    }

    public void createTeam() {
        DaoTeam daoTeam = new DaoTeam();

        String nameRequest = "Enter name of new team: ";
        String teamName = viewMentor.getInputFromUser(nameRequest);

        daoTeam.createTeam(); //tu musi być przekazany argment teamName
    }

    public void addQuest(){
        DaoQuest daoQuest = new DaoQuest();

        String nameRequest = "Enter name of new quest: ";
        String questName = viewMentor.getIntInputFromUser(nameRequest);

        String valueRequest = "Enter value of new quest: ";
        int questValue = viewMentor.getIntInputFromUser(valueRequest);

        String descriptionRequest = "Enter description of quest";
        String questDescription = viewMentor.getInputFromUser(descriptionRequest);

        String questStatus = chooseStatus();
        daoQuest.createQuest(); //tu muszą być przekazane argmenty
    }

    public void addArtifact() {
        DaoArtifact daoArtifact = new DaoArtifact();

        String nameRequest = "Enter name of new artifact: ";
        String artifactName = viewMentor.getIntInputFromUser(nameRequest);

        String valueRequest = "Enter value of new artifact: ";
        int artifactValue = viewMentor.getIntInputFromUser(valueRequest);

        String descriptionRequest = "Enter description of new artifact";
        String artifactDescription = viewMentor.getInputFromUser(descriptionRequest);

        String artifactStatus = chooseStatus();
        daoArtifact.createArtifact();
    }

    private String chooseStatus() {
        String statusRequest = "Choose status:\n1. Individual\n2. Team\nOption: ";
        String status = null;
        boolean choosingStatus = true;
        int option = 0;
        while(choosingStatus) {
            option = viewMentor.getIntInputFromUser(statusRequest);
            switch(option) {
                case 1:
                    status = "individual";
                    choosingStatus = false;
                    break;
                case 2:
                    status = "team";
                    choosingStatus = false;
                    break;
                default:
                    viewMentor.displayText("Wrong option number!")
            }
        }
        return status;
    }

    public void updateQuest() {

    }

    public void updateArtifact() {
        seeArtifacts();
    }

    public void markStudentAchivedQuest() {

    }

    public void markTeamAchivedQuest() {

    }

    public void markStudentBoughtArtifact() {

    }

    public void markTeamBoughtArtifact() {

    }

    public void seeAllWallets() {
        int idOfMentorClass = mentor.getClassId();
        viewMentor.displayText("Wallets of students of class: ");
        ArrayList<Student> allStudentsOfClass = new DaoStudent().getStudentsByClassId();
        ArrayList<Wallet> walletsOfStudents = getWalletsOfStudents(allStudentsOfClass);
        viewMentor.displayList(walletsOfStudents);
    }
    private ArrayList<Wallet> getWalletsOfStudents(ArrayList<Student> students) {
        ArrayList<Wallet> walletsOfStudents = new ArrayList<>();
        for (Student student : students) {
            walletsOfStudents.add(student.getWallet());
        }
        return walletsOfStudents;
    }

    public void seeQuests() {
        viewMentor.displayText("Available quests: ");
        viewMentor.displayList(new DaoArtifact().importData());
    }

    public void seeArtifacts() {
        viewMentor.displayText("Available artifacts: ");
        viewMentor.displayList(new DaoArtifact().importData());
    }

    public void runMenu() {
        String mentorOption = "";
        while (!mentorOption.equals("0")) {

            viewMentor.displayText("\nWhat would like to do?");
            viewMentor.displayList(viewMentor.getMentorOptions());

    mentorOption = viewMentor.getInputFromUser("Option: ");
    switch (mentorOption) {
        case "1": createStudent();
                break;
        case "2": createTeam();
                break;
        case "3": addQuest();
                break;
        case "4": addArtifact();
                break;
        case "5": updateQuests();
                break;
        case "6": updateArtifact();
                break;
        case "7": markStudentAchivedQuest();
                break;
        case "8": markStudentAchivedQuest();
                break;
        case "9": markStudentBoughtArtifact();
                break;
        case "10": markTeamBoughtArtifact();
                break;
        case "11": seeAllWallets();
                break;
        case "12":  seeQuests();
                break;
        case "13":  seeArtifacts();
                break;

        case "0": break;

        default: viewMentor.displayText("Wrong option. Try again!");
                 break;
    }
        }

    }

}
