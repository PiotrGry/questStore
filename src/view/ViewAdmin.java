package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ViewAdmin extends View{

    private static ArrayList <String> adminOptions = new ArrayList <String>(Arrays.asList("1. Create mentor",
    "2. Create class", "3. Edit mentor", "4. See mentor's profile","5. Assign mentor to class",
    "6. Create levels of experience", "0. Log out"));

    public ArrayList<String> getAdminOptions() {
        return adminOptions;
    }

}