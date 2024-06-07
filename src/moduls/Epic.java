package moduls;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW, Duration.ZERO, null);
    }

    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public Duration calculateDuration(Map<Integer, SubTask> subTasks) {
        return subTaskIds.stream()
                .map(subTasks::get)
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime calculateStartTime(Map<Integer, SubTask> subTasks) {
        return subTaskIds.stream()
                .map(subTasks::get)
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public LocalDateTime calculateEndTime(Map<Integer, SubTask> subTasks) {
        return subTaskIds.stream()
                .map(subTasks::get)
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                '}';
    }
}
