package View;

public abstract class AbstractInput {
    public abstract int getInputFromUser(String message);

    public abstract String getStringInput(String format);

    public abstract long getLongFromUser(String message);
}
