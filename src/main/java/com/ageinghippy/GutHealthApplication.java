package com.ageinghippy;

import com.ageinghippy.controller.CLIMenu;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GutHealthApplication {
    public static void main(String[] args) {
        CLIMenu cliMenu = new CLIMenu();
        cliMenu.showMainMenu();
    }
}