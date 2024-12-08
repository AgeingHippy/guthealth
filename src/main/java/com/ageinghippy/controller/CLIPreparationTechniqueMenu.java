package com.ageinghippy.controller;

import com.ageinghippy.data.model.PreparationTechnique;
import com.ageinghippy.service.PreparationTechniqueService;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class CLIPreparationTechniqueMenu {
    private final PreparationTechniqueService preparationTechniqueService;

    public CLIPreparationTechniqueMenu(PreparationTechniqueService preparationTechniqueService) {
        this.preparationTechniqueService = preparationTechniqueService;
    }

    public void preparationTechniqueDataManipulationMenu() {
        int choice;
        String title = "=== PREPARATION TECHNIQUE DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add new preparation technique";
        options[2] = "to update an existing preparation technique";
        options[3] = "to delete an existing preparation technique";
        options[4] = "to view existing preparation techniques";

        do {
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    createPreparationTechniqueMenuOption();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    updatePreparationTechniqueMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    deletePreparationTechniqueMenuOption();
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    printPreparationTechniquesMenuOption();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    public void createPreparationTechniqueMenuOption() {
        //get preparation technique data
        PreparationTechnique preparationTechnique = new PreparationTechnique();
        preparationTechnique.setCode(Util.getStringFromUser("Please enter the new preparation technique code"));
        preparationTechnique.setDescription(Util.getStringFromUser("Please enter the new preparation technique description"));
        //todo - validate data
        //insert into database
        preparationTechnique = preparationTechniqueService.savePreparationTechnique(preparationTechnique);
        //return result
        System.out.println(preparationTechnique);
    }

    public void updatePreparationTechniqueMenuOption() {
        //get the record that needs to be updated
        String code = Util.getStringFromUser("Please enter the preparation technique code");
        PreparationTechnique preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);

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
                        preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);
                        System.out.println("CHANGES DISCARDED : " + preparationTechnique);
                        break;
                    case 1:
                        preparationTechnique = preparationTechniqueService.savePreparationTechnique(preparationTechnique);
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

    public void deletePreparationTechniqueMenuOption() {
        //get the record that needs to be deleted
        String code = Util.getStringFromUser("Please enter the preparation technique code");
        PreparationTechnique preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);
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
                    preparationTechniqueService.deletePreparationTechnique(preparationTechnique);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("PreparationTechnique with primary key '" + code + "' not found");
        }
    }

    public void printPreparationTechniquesMenuOption() {
        ArrayList<PreparationTechnique> preparationTechniques = preparationTechniqueService.getPreparationTechniques();
        System.out.println("=== " + preparationTechniques.size() + " PreparationTechnique records returned ===");
        preparationTechniques.forEach(System.out::println);
        System.out.println("=== ========= ===");
    }

    public PreparationTechnique selectPreparationTechniqueMenuOption() {
        String[] options;
        int choice;
        PreparationTechnique preparationTechnique = null;
        ArrayList<PreparationTechnique> preparationTechniques = preparationTechniqueService.getPreparationTechniques();
        if (!preparationTechniques.isEmpty()) {
            //build an array containing food category items
            options = new String[preparationTechniques.size()];
            for (int i = 0; i < preparationTechniques.size(); i++) {
                options[i] = preparationTechniques.get(i).getCode() + "( " + preparationTechniques.get(i).getDescription() + ")";
            }
            choice = CLIMenu.getChoice("Please select the preparation technique", options);
            preparationTechnique = preparationTechniques.get(choice);
        }
        return preparationTechnique;
    }

}
