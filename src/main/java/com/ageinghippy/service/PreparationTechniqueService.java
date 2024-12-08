package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.PreparationTechnique;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class PreparationTechniqueService {
    private final GutHealthDAO gutHealthDAO;

    public PreparationTechniqueService(GutHealthDAO gutHealthDAO) {
        this.gutHealthDAO = gutHealthDAO;
    }











    public PreparationTechnique getPreparationTechnique(String code) {
        return gutHealthDAO.getPreparationTechnique(code);
    }

    public ArrayList<PreparationTechnique> getPreparationTechniques() {
        return gutHealthDAO.getPreparationTechniques();
    }

    public PreparationTechnique savePreparationTechnique(PreparationTechnique preparationTechnique) {
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

    public void deletePreparationTechnique(PreparationTechnique preparationTechnique) {
        gutHealthDAO.deletePreparationTechnique(preparationTechnique);
    }
}
