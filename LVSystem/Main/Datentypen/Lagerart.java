package LVSystem.Main.Datentypen;

public enum Lagerart {
    FEST, FLUESSIG;

    public String getRegalart(){
        switch(this){
            case FEST:
                return "Fach";
            case FLUESSIG:
                return "Tank";
            default:
                return "";
        }
    }
}
