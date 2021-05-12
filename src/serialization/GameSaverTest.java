package serialization;

import java.io.*;

public class GameSaverTest {
    public static void main(String[] args) {
        GameCharacter one = new GameCharacter();
        GameCharacter two = new GameCharacter();
        one.setPower(55);
        two.setPower(80);
        one.setWeapon("Longsword");
        two.setWeapon("Katana");
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("Game.txt"));
            os.writeObject(one);
            os.writeObject(two);
            os.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        one = null; // Set them null so we can't access them on heap.
        two = null;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("Game.txt"));
            GameCharacter oneRestore = (GameCharacter) is.readObject();
            GameCharacter twoRestore = (GameCharacter) is.readObject();
            System.out.println(oneRestore.getWeapon());
            System.out.println(twoRestore.getPower());
        } catch (ClassNotFoundException | IOException exception) {
            exception.printStackTrace();
        }
    }



}
