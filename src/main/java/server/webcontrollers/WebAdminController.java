package server.webcontrollers;

import dao.IDaoAdmin;
import dao.IDaoClass;
import dao.IDaoLevel;
import dao.IDaoMentor;
import model.Admin;
import model.CodecoolClass;
import model.Level;
import model.Mentor;

public class WebAdminController implements IAdminController {

    private IDaoAdmin daoAdmin;
    private IDaoMentor daoMentor;
    private IDaoClass daoClass;
    private IDaoLevel daoLevel;

    public static IAdminController create(IDaoAdmin daoAdmin, IDaoMentor daoMentor,
                                          IDaoClass daoClass, IDaoLevel daoLevel) {
        return new WebAdminController(daoAdmin, daoMentor, daoClass, daoLevel);
    }

    private WebAdminController(IDaoAdmin daoAdmin, IDaoMentor daoMentor,
                               IDaoClass daoClass, IDaoLevel daoLevel) {
        this.daoAdmin = daoAdmin;
        this.daoMentor = daoMentor;
        this.daoClass = daoClass;
        this.daoLevel = daoLevel;
    }


    @Override
    public String getAdmin(int adminId) {
        Admin admin = daoAdmin.importAdmin(adminId);
        if(admin != null) {
            return admin.getName();
        }
        return "";
    }

    public boolean createMentor(String name, String password, String email) {
        Mentor mentor = daoMentor.createMentor(name, password, email);
        return daoMentor.exportMentor(mentor);
    }


    public boolean createClass(String name) {
        CodecoolClass codecoolClass = daoClass.createClass(name);
        return daoClass.exportClass(codecoolClass);
    }


    public boolean editMentor(String id) {
        Mentor mentor = daoMentor.importMentor(Integer.parseInt(id));
        return daoMentor.updateMentor(mentor);
    }


    public String getCodecoolClass(String name) {
        for (CodecoolClass codecoolClass : daoClass.getAllClasses()) {
            if (codecoolClass.getName().equals(name)) {
                return codecoolClass.getName();
            }
        }
        return "";
    }


    public String seeMentorData(String name) {
        for (Mentor mentor : daoMentor.getAllMentors()) {
            if(mentor.getName().equals(name)) {
                return mentor.toString();
            }
        }
        return "";
    }


    public boolean createLevel(String name, String coinsLimit) {
        Level level = daoLevel.createLevel(name, Integer.parseInt(coinsLimit));
        return daoLevel.exportLevel(level);
    }
}
