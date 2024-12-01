package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.model.PreparationTechnique;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class PreparationTechniqueService {
    private final GutHealthDAO gutHealthDAO;

    public PreparationTechniqueService(GutHealthDAO gutHealthDAO) {
        this.gutHealthDAO = gutHealthDAO;
    }

    public void createPreparationTechnique() {
        //get preparation technique data
        PreparationTechnique preparationTechnique = new PreparationTechnique();
        preparationTechnique.setCode(Util.getStringFromUser("Please enter the new preparation technique code"));
        preparationTechnique.setDescription(Util.getStringFromUser("Please enter the new preparation technique description"));
        //todo - validate data
        //insert into database
        preparationTechnique = savePreparationTechnique(preparationTechnique);
        //return result
        System.out.println(preparationTechnique);
    }

    public void updatePreparationTechnique() {
        //get the record that needs to be updated
        String code = Util.getStringFromUser("Please enter the preparation technique code");
        PreparationTechnique preparationTechnique = gutHealthDAO.getPreparationTechnique(code);

        int choice = -1;
        String title = "=== UPDATE PREPARATION TECHNIQUE RECORD ===";
        String[] options = new String[3];
        options[0] = "to discard all changes and exit";
        options[1] = "to save the changes and exit";
        options[2] = "to change the description";

        if (preparationTechnique != null) {
            System.out.println("preparationTechnique = " + preparationTechnique);
            do {
                choice = CLIMenu.getChoice(title, options);

                switch (choice) {
                    case 0:
                        preparationTechnique = gutHealthDAO.getPreparationTechnique(code);
                        System.out.println("CHANGES DISCARDED : " + preparationTechnique);
                        break;
                    case 1:
                        preparationTechnique = savePreparationTechnique(preparationTechnique);
                        System.out.println("SAVED : " + preparationTechnique);
                        break;
                    case 2:
                        preparationTechnique.setDescription(Util.getStringFromUser("Please enter the updated preparation technique description"));
                        System.out.println("UNSAVED : " + preparationTechnique);
                        break;
                }

            } while (choice < 0 || choice > 1);
        }
    }

    public void deletePreparationTechnique() {
        //get the record that needs to be deleted
        String code = Util.getStringFromUser("Please enter the preparation technique code");
        PreparationTechnique preparationTechnique = gutHealthDAO.getPreparationTechnique(code);
        if (preparationTechnique != null) {
            String title = "=== DELETE " + preparationTechnique + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the preparation technique";

            int choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    gutHealthDAO.deletePreparationTechnique(preparationTechnique);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("PreparationTechnique with primary key '" + code + "' not found");
        }
    }

    public void printPreparationTechniques() {
        ArrayList<PreparationTechnique> preparationTechniques = gutHealthDAO.getPreparationTechniques();
        System.out.println("=== " + preparationTechniques.size() + " PreparationTechnique records returned ===");
        preparationTechniques.forEach(System.out::println);
        System.out.println("=== ========= ===");
    }

    private PreparationTechnique savePreparationTechnique(PreparationTechnique preparationTechnique) {
        String code = null;
        if (gutHealthDAO.getPreparationTechnique(preparationTechnique.getCode()) == null) {
            //insert
            code = gutHealthDAO.insertPreparationTechnique(preparationTechnique);
        }
        else {
            //update
            if (gutHealthDAO.updatePreparationTechnique(preparationTechnique)) {
                code = preparationTechnique.getCode();
            }
        }
        return gutHealthDAO.getPreparationTechnique(code);
    }

}
