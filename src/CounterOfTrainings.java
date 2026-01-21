import java.util.Objects;

public class CounterOfTrainings {
    private Coach coach;
    private int countTrainingSessions;

    public CounterOfTrainings(Coach coach, int countTrainingSessions) {
        this.coach = coach;
        this.countTrainingSessions = countTrainingSessions;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCountTrainingSessions() {
        return countTrainingSessions;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CounterOfTrainings that = (CounterOfTrainings) o;
        return countTrainingSessions == that.countTrainingSessions && Objects.equals(coach, that.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, countTrainingSessions);
    }
}


