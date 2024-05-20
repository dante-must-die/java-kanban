package moduls;

public class SubTask extends Task { // модульный класс наследует
    private int epicId;

    public SubTask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    // методы работы с Id
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Moduls.SubTask{" + "epicId=" + epicId + " " + getStatus() + '}';
    }

}

