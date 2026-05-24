package com.mycompany.projetpeinture2026;

public class Launcher {
   public static void main(String[] args) {
        // Appel de la méthode main de l'interface JavaFX
        MainApp.main(args);
    } 
}

// Il y avait un problème une perturbation du chargeur de la machine virutelle Java. La JVM n'a pas réussi à isoler la méthode main correctement.
//On ne peut pas lancer une classe qui hérite de javafx.application.Application via le plugin Maven Exec.
// On crée cette classe afin qu'elle n'hérite pas de JavaFX
