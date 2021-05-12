package serialization;

import java.io.Serializable;

public class GameCharacter implements Serializable {
    private int power;
    private String weapon;
    public void setPower(int power) {
        this.power = power;
    }
    public int getPower() {
        return power;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }
}
