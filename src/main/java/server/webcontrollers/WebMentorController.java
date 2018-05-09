package server.webcontrollers;


import system.dao.*;
import system.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WebMentorController implements IMentorController {

    private final IDaoWallet daoWallet;
    private final IDaoStudent daoStudent;
    private final IDaoArtifact daoArtifact;
    private final IDaoLevel daoLevel;
    private final IDaoTeam daoTeam;
    private final IDaoClass daoClass;
    private final IDaoQuest daoQuest;
    private final IDaoMentor daoMentor;

    public static IMentorController create(IDaoWallet daoWallet, IDaoStudent daoStudent,
                                            IDaoArtifact daoArtifact, IDaoLevel daoLevel,
                                            IDaoTeam daoTeam, IDaoClass daoClass,
                                            IDaoQuest daoQuest, IDaoMentor daoMentor) {
        return new WebMentorController(daoWallet, daoStudent, daoArtifact,
                daoLevel, daoTeam, daoClass, daoQuest, daoMentor);
    }

    private WebMentorController(
                                 IDaoWallet daoWallet, IDaoStudent daoStudent,
                                 IDaoArtifact daoArtifact, IDaoLevel daoLevel,
                                 IDaoTeam daoTeam, IDaoClass daoClass,
                                 IDaoQuest daoQuest, IDaoMentor daoMentor) {

        this.daoWallet = daoWallet;
        this.daoStudent = daoStudent;
        this.daoArtifact = daoArtifact;
        this.daoLevel = daoLevel;
        this.daoTeam = daoTeam;
        this.daoClass = daoClass;
        this.daoQuest = daoQuest;
        this.daoMentor = daoMentor;
    }

    @Override
    public String getMentorName(int mentorId) {
        Mentor mentor = getMentorById(mentorId);
        if(mentor.getUserId() == 0) {
            return "";
        }
        return mentor.getName();
    }

    @Override
    public String getMentorEmail(int mentorId) {
        Mentor mentor = getMentorById(mentorId);
        if(mentor.getUserId() == 0) {
            return "";
        }
        return mentor.getEmail();
    }

    @Override
    public String getMentorClassWithStudents(int mentorId) {
        CodecoolClass codecoolClass = daoClass.getMentorsClass(mentorId);
        if(codecoolClass.getGroupId() == 0) {
            return "no class assigned yet..";
        }
        String[] classData = codecoolClass.toString().split("ID:");
        int counter = 0;
        StringBuilder sb = new StringBuilder(codecoolClass.getBasicInfo());
        sb.append(", students:<br>");
        for(String element : classData) {
            if(counter > 0) {
                sb.append("<br>###   id: ");
                sb.append(element);
            }
            counter++;
        }
        return sb.toString();
    }

    @Override
    public String getAllTeams() {
        List<Team> teams = daoTeam.getAllTeams();
        StringBuilder teamsAsText = new StringBuilder();
        int counter = 1;
        int maxNumTeamsInLine = 7;
        for(Team team : teams) {
            int teamId = team.getGroupId();
            if(teamId != 0) {
                if(counter % maxNumTeamsInLine == 1) {
                    teamsAsText.append("<br>");
                }
                String teamAsText = String.format(" ' %s', ", team.getName());
                teamsAsText.append(teamAsText);
                counter++;
            }
        }
        return teamsAsText.toString();
    }

    @Override
    public boolean createStudent(String name, String password, String email, int classId) {
        int studentId = daoStudent.createStudent(name, password, email).getUserId();


        daoClass.assignStudentToClass(studentId, classId);
        return studentId != 0;
    }

    public List<String> getQuests() {
        List<String> quests = new ArrayList<>();
        for(Quest quest : daoQuest.getAllQuests()) {
            quests.add(quest.getFullInfo());
        }
        return quests;
    }

    @Override
    public boolean editQuest(Map<String, String> questData) {
        Quest quest = daoQuest.importQuest(Integer.parseInt(questData.get("quest-id")));
        if(quest.getItemId() != 0) {
            if(questData.containsKey("questname")) {
                quest.setName(questData.get("questname"));
            }
            if(questData.containsKey("description")) {
                quest.setDescription(questData.get("description"));
            }
            if(questData.containsKey("value")) {
                quest.setValue(Integer.parseInt(questData.get("value")));
            }
            if(questData.containsKey("type")) {
                quest.setType(questData.get("type"));
            }
            if(questData.containsKey("category")) {
                quest.setCategory(questData.get("category"));
            }

            return daoQuest.updateQuest(quest);
        } else{
            return false;
        }
    }

    @Override
    public boolean createTeam(String teamName) {

        return daoTeam.createTeam(teamName).getGroupId() != 0;
    }

    private Mentor getMentorById(int mentorId) {
        return daoMentor.importMentor(mentorId);
    }

    @Override
    public boolean addQuest(String name, int value, String description, String type, String category) {

        return daoQuest.createQuest(name, value, description, type, category).getItemId() != 0;
    }

    @Override
    public boolean addArtifact(String name, int value, String type, String category) {

        return daoArtifact.createArtifact(name, value, type, category).getItemId() != 0;
    }

    @Override
    public Map<String, String> getAllWallets() {
        List<Student> students = daoStudent.getAllStudents();
        Map<String, String> dataFromWallets = new HashMap<>();

        for (Student student : students) {
            dataFromWallets.put(student.getName(), daoWallet.importWallet(student.getUserId()).toString().replaceAll("\n", "<br/>"));
        }

        return dataFromWallets;
    }

    public List<String> getArtifacts() {
        List<String> artifacts = new ArrayList<>();
        for(Artifact artifact : daoArtifact.getAllArtifacts()) {
            artifacts.add(artifact.toString());
        }
        return artifacts;
    }

    @Override
    public boolean editArtifact(Map<String, String> artifactData) {
        Artifact artifact = daoArtifact.importArtifact(Integer.parseInt(artifactData.get("artifact_id")));
        if(artifact.getItemId() != 0) {
            if(artifactData.containsKey("name")) {
                artifact.setName(artifactData.get("name"));
            }
            if(artifactData.containsKey("description")) {
                artifact.setDescription(artifactData.get("description"));
            }
            if(artifactData.containsKey("value")) {
                artifact.setValue(Integer.parseInt(artifactData.get("value")));
            }
            if(artifactData.containsKey("type")) {
                artifact.setType(artifactData.get("type"));
            }

            return daoArtifact.updateArtifact(artifact);
        } else{
            return false;
        }
    }
}





