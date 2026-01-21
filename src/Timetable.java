import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    // добавление новой тренировки в расписание
    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (!timetable.containsKey(trainingSession.getDayOfWeek())) {
            TreeMap<TimeOfDay, ArrayList<TrainingSession>> dayTimetable = new TreeMap<>();
            timetable.put(trainingSession.getDayOfWeek(), dayTimetable);
            if (!dayTimetable.containsKey(trainingSession.getTimeOfDay())) {
                ArrayList<TrainingSession> timeTimetable = new ArrayList<>();
                timeTimetable.add(trainingSession);
                dayTimetable.put(trainingSession.getTimeOfDay(), timeTimetable);
            } else {
                dayTimetable.get(trainingSession.getTimeOfDay()).add(trainingSession);
            }
        } else {
            TreeMap<TimeOfDay, ArrayList<TrainingSession>> dayTimetable = timetable.get(trainingSession.getDayOfWeek());
            if (!dayTimetable.containsKey(trainingSession.getTimeOfDay())) {
                ArrayList<TrainingSession> timeTimetable = new ArrayList<>();
                timeTimetable.add(trainingSession);
                dayTimetable.put(trainingSession.getTimeOfDay(), timeTimetable);
            } else {
                dayTimetable.get(trainingSession.getTimeOfDay()).add(trainingSession);
            }

        }
    }

    // получение всех тренировок, упорядоченных по времени начала, за конкретный день недели
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            TreeMap<TimeOfDay, ArrayList<TrainingSession>> timetableForDay = timetable.get(dayOfWeek);
            List<TrainingSession> resultTimetableForDay = new ArrayList<>();
            for (ArrayList<TrainingSession> i : timetableForDay.values()) {
                resultTimetableForDay.addAll(i);
            }
            return resultTimetableForDay;
        } else {
            return Collections.emptyList();
        }
    }


    // получение всех тренировок, начинающихся в конкретное время, за конкретный день недели
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.containsKey(dayOfWeek)) {
            TreeMap<TimeOfDay, ArrayList<TrainingSession>> timetableForDay = timetable.get(dayOfWeek);
            if (timetableForDay.containsKey(timeOfDay)) {
                return timetableForDay.get(timeOfDay);
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }


    // подсчет количества занятий в неделю у каждого из тренеров
    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countTrainingSessionsForCoach = new HashMap<>();
        for (DayOfWeek day : timetable.keySet()) {
            for (ArrayList<TrainingSession> time : timetable.get(day).values()) {
                for (TrainingSession trainingSession : time) {
                    countTrainingSessionsForCoach.put(trainingSession.getCoach(), countTrainingSessionsForCoach.getOrDefault(trainingSession.getCoach(), 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> counterOfTrainings = new ArrayList<>();

        for (Map.Entry<Coach, Integer> entry : countTrainingSessionsForCoach.entrySet()) {
            counterOfTrainings.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(counterOfTrainings, new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings count1, CounterOfTrainings count2) {
                return Integer.compare(count2.getCountTrainingSessions(), count1.getCountTrainingSessions());
            }
        });

        return counterOfTrainings;
    }

}