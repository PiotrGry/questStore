package terminal.controller;


import system.dao.*;
import system.model.Admin;
import system.model.Mentor;
import system.model.Student;
import system.model.User;
import terminal.controller.view.ViewAdmin;
import terminal.controller.view.ViewLogin;
import terminal.controller.view.ViewMentor;
import terminal.controller.view.ViewStudent;

public class ControllerLogin{

    private ViewLogin viewLogin;
    private final IDaoFactory daoFactory;

    public ControllerLogin(ViewLogin viewLogin, IDaoFactory daoFactory){
        this.viewLogin = viewLogin;
        this.daoFactory = daoFactory;
    }

    public void runMenu(){
        String userOption = "";
        while (!userOption.equals("0")) {

            System.out.println("\nWhat would like to do?");
            viewLogin.displayList(viewLogin.getLoginOptions());

            userOption = viewLogin.getInputFromUser("Option: ");
            switch (userOption) {
                case "1": login();
                break;
                case "0": break;

                default: System.out.println("Wrong option. Try again!");
                break;
            }
        }
    }

    private void login(){
        String userEmail = viewLogin.getInputFromUser("email: ");
        String userPassword = viewLogin.getInputFromUser("password: ");
        IDaoLogin daoLogin = daoFactory.create(DaoLogin.class);

        User user = daoLogin.getUser(userEmail, userPassword);

        if(user != null){
            IUserController userController = getUserController(user);
            if(userController != null){
                userController.runMenu();
            }
        }else{
            viewLogin.displayText("Incorrect data");
        }  
    }

    private IUserController getUserController(User user){
        IUserController controller = null;

        if(user instanceof Admin){
            controller = new ControllerAdmin((Admin)user, new ViewAdmin(),
                    daoFactory.create(DaoMentor.class),
                    daoFactory.create(DaoClass.class),
                    daoFactory.create(DaoLevel.class));
        }else if(user instanceof Mentor){
            controller = new ControllerMentor((Mentor)user, new ViewMentor(),
                    daoFactory.create(DaoStudent.class), daoFactory.create(DaoClass.class),
                    daoFactory.create(DaoArtifact.class), daoFactory.create(DaoQuest.class),
                    daoFactory.create(DaoTeam.class), daoFactory.create(DaoWallet.class));
        }else if(user instanceof Student){
            controller = new ControllerStudent((Student)user, new ViewStudent(),
                    daoFactory.create(DaoWallet.class), daoFactory.create(DaoStudent.class),
                    daoFactory.create(DaoArtifact.class), daoFactory.create(DaoLevel.class),
                    daoFactory.create(DaoTeam.class));
        }

        return controller;
    }

}
