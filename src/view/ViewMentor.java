package view;

import java.util.ArrayList;
import java.util.Arrays;


public class ViewMentor extends View{

    private static ArrayList <String> mentorOptions = new ArrayList <String>(Arrays.asList("1. Create student",
        "2. Create team", "3. Create quest", "4. Add artifact to store", "5. Edit quest",
        "6. Edit artifact", "7. Mark quest", "8. Mark artifact", "9. See student's wallet",
        "0. Log out"));

    private static ArrayList <String> artifactEditOptions = new ArrayList<>(Arrays.asList("1. Change name",
            "2. Change value", "3. Change description", "4. Change type", "5. Change category"));

    private static ArrayList <String> updateArtifactsOptions = new ArrayList <String>(Arrays.asList("1. Update artifacts name",
            "2. Update artifacts description", "3. Update artifacts value", "4. Update artifacts type",
            "0. Log out"));

    private static ArrayList <String> updateArtifactTypeOptions = new ArrayList <String>(Arrays.asList("1. Type individual",
            "2. Type team", "0. Log out"));

    public ArrayList<String> getMentorOptions() {
        return mentorOptions;
    }

    public static ArrayList<String> getArtifactEditOptions() {
        return artifactEditOptions;
    }

    public ArrayList<String> getUpdateArtifactsOptions() {
        return updateArtifactsOptions;
    }

    public ArrayList<String> getUpdateArtifactTypeOptions() {
        return updateArtifactTypeOptions;
    }


}
