package terminal.controller;

import system.dao.*;
import system.model.*;
import terminal.controller.view.ViewTeam;

import java.util.HashMap;
import java.util.List;

public class ControllerTeam implements IUserController {

    private ViewTeam viewTeam;
    private Team team;
    private IDaoArtifact daoArtifact;
    private IDaoTeam daoTeam;
    private IDaoWallet daoWallet;

    ControllerTeam(Team team, ViewTeam viewTeam, IDaoArtifact daoArtifact, IDaoTeam daoTeam, IDaoWallet daoWallet) {
        this.viewTeam = viewTeam;
        this.team = team;
        this.daoArtifact = daoArtifact;
        this.daoTeam = daoTeam;
        this.daoWallet = daoWallet;
    }

    private Artifact getArtifact(String type) {

        viewTeam.displayText("Available artifacts:\n");
        List<Artifact> artifacts = daoArtifact.getArtifacts(type);
        viewTeam.displayList(artifacts);

        Artifact artifact = null;

        if(artifacts.size() != 0) {
            int artifactId = viewTeam.getIntInputFromUser("\nEnter id of artifact: ");
            for(Artifact choosenArtifact: artifacts) {
                if(choosenArtifact.getItemId() == artifactId) {
                    return choosenArtifact;
                }
            }
        }
        else {
            viewTeam.displayText("\nNo artifacts");
        }
        return artifact;
    }

    private void buyArtifact() {
        Artifact artifact = getArtifact("team");
        if(artifact != null) {
            int price = artifact.getValue();
            viewTeam.displayText("This artifact costs " + price + " coins");


        List<Student> students = team.getStudents();
        int teamSize = team.getSize();

            HashMap<Student, Integer> studentsToPrices = new HashMap<>();
            for (Student student : students) {
                studentsToPrices.put(student, price / teamSize);
            }

            int remainderCoins = (price % teamSize);
            for (Student student : students) {
                int amountToAdd = studentsToPrices.get(student) + 1;
                studentsToPrices.put(student, amountToAdd);
                remainderCoins--;
                if (remainderCoins == 0) {
                    break;
                }
            }

            for (Student student : studentsToPrices.keySet()) {
                int coins_to_pay = studentsToPrices.get(student);
                if (!student.hasEnoughCoins(coins_to_pay)) {
                    viewTeam.displayText("Students do not have enough money to buy this artifact");
                    return;
                }
            }

            for (Student student : studentsToPrices.keySet()) {
                int coinsToPay = studentsToPrices.get(student);
                student.subtractCoins(coinsToPay);
                student.addNewArtifact(artifact);

                daoWallet.updateWallet(student);
                int artifactId = artifact.getItemId();
                int studentId = student.getUserId();
                daoWallet.exportStudentArtifact(artifactId, studentId);
            }

            viewTeam.displayText("Purchase of team artifact was successful");
        }
        else {
            viewTeam.displayText("\nWrong id of artifact");
        }
    }

    private void splitTeamMoney() {
        List<Student> students = team.getStudents();
        int remainderCoins = team.getAvailableCoins();

        if (remainderCoins == 0) {
            viewTeam.displayText("Team has no coins to split");
            return;
        }

        for (Student student: students) {
            String name = student.getName();
            int coins = viewTeam.getIntInputFromUser("How much should student " + name + " get?");
            if (coins <= remainderCoins) {
                student.addCoins(coins);
                team.subtractCoins(coins);
                daoWallet.updateWallet(student);
                daoTeam.updateTeamData(team);
                remainderCoins -= coins;
            } else {
                viewTeam.displayText("You do not have enough money");
            }
        }

    }

    public void runMenu() {

        String teamOption = "";
        while (!teamOption.equals("0")) {

            viewTeam.displayText(this.team.getBasicTeamInfo());
            viewTeam.displayText("\nWhat would like to do?");
            viewTeam.displayList(viewTeam.getStudentOptions());

            teamOption = viewTeam.getInputFromUser("Option: ");
            switch (teamOption) {
                case "1":
                    buyArtifact();
                    break;
                case "2":
                    splitTeamMoney();
                    break;
                case "0":
                    break;
                default: viewTeam.displayText("Wrong option. Try again!");
                    break;
            }
        }
    }
}
