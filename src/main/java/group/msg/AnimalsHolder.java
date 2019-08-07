package group.msg;

import group.msg.entities.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalsHolder {
    private static List<Animal> animals;
    static{
        animals = new ArrayList<>();
        animals.add(new Animal(1,"Caine","Bobby",5,"Florin"));
        animals.add(new Animal(2,"Caine","Lily",2,"Catalina"));
        animals.add(new Animal(3,"Pisica","Titi",7,"Iulia"));
        animals.add(new Animal(4,"Tigru","Roar",4,"George"));
        animals.add(new Animal(5,"Hamster","Mimi",8,"Vlad"));
        animals.add(new Animal(6,"Pinguin","Reci",4,"Arnold"));
    }
    public static List<Animal> getAnimals(){
        return animals;
    }
}
