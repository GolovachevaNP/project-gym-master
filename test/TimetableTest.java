import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondayTrainingSessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesdayTrainingSessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, mondayTrainingSessions.size());

        //Проверить, что за вторник не вернулось занятий
        assertTrue(tuesdayTrainingSessions.isEmpty());

        //Проверить, что вернулось именно то занятие, которое было добавлено
        assertEquals(singleTrainingSession, mondayTrainingSessions.get(0));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondayTrainingSessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> thursdayTrainingSessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        List<TrainingSession> tuesdayTrainingSessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        // Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, mondayTrainingSessions.size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        assertEquals(2, thursdayTrainingSessions.size());
        assertEquals(thursdayChildTrainingSession, thursdayTrainingSessions.get(0));
        assertEquals(thursdayAdultTrainingSession, thursdayTrainingSessions.get(1));
        // Проверить, что за вторник не вернулось занятий
        assertTrue(tuesdayTrainingSessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> trainingSessionAt13h =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        List<TrainingSession> trainingSessionAt14 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1, trainingSessionAt13h.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertTrue(trainingSessionAt14.isEmpty());

        //Проверить, что вернулось именно то занятие, которое было добавлено
        assertEquals(singleTrainingSession, trainingSessionAt13h.get(0));
    }

    @Test
    void testGetCountByCoachesIsEmptyTimetable() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> trainingsCountByCoach = timetable.getCountByCoaches();

        //Проверить, что список пустой
        assertTrue(trainingsCountByCoach.isEmpty());
    }

    @Test
    void testGetCountByCoachesAndSortingCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Николаев", "Сергей", "Васильевич");

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0)));

        List<CounterOfTrainings> trainingsCountByCoach = timetable.getCountByCoaches();

        //Проверить, что в списке два тренера
        assertEquals(2, trainingsCountByCoach.size());

        //Проверить, что у первого тренера две тренировки
        assertEquals(2, trainingsCountByCoach.get(0).getCountTrainingSessions());

        //Проверить, что у второго тренера одна тренировка
        assertEquals(1, trainingsCountByCoach.get(1).getCountTrainingSessions());

        //Проверить, что первый тренер в списке идёт первым (т.к. у него больше тренировок, чем у второго)
        assertEquals(coach1, trainingsCountByCoach.get(0).getCoach());

        //Проверить, что второй тренер в списке идёт вторым (т.к. у него меньше тренировок, чем у первого)
        assertEquals(coach2, trainingsCountByCoach.get(1).getCoach());
    }

    @Test
    void testGetCountByCoachesSameFioCountsAsOneCoach() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Васильев", "Николай", "Сергеевич");

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> trainingsCountByCoach = timetable.getCountByCoaches();

        //Проверить, что тренер в списке один
        assertEquals(1, trainingsCountByCoach.size());

        //Проверить, что количество тренировок суммируется
        assertEquals(2, trainingsCountByCoach.get(0).getCountTrainingSessions());
    }

    @Test
    void testGetCountByCoachesForSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> trainingsCountByCoach = timetable.getCountByCoaches();

        //Проверить, что в списке один тренер
        assertEquals(1, trainingsCountByCoach.size());

        //Проверить, что вернулся именно тот тренер, который в списке
        assertEquals(coach, trainingsCountByCoach.get(0).getCoach());

        //Проверить, что у тренера три тренировки
        assertEquals(3, trainingsCountByCoach.get(0).getCountTrainingSessions());
    }

    @Test
    void testGetCountByCoachesForCountsIsEquals() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Николаев", "Сергей", "Васильевич");

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> trainingsCountByCoach = timetable.getCountByCoaches();

        //Проверить, что в списке два тренера
        assertEquals(2, trainingsCountByCoach.size());

        //Проверить, что у тренера, идущего первым в списке, одна тренировка
        assertEquals(1, trainingsCountByCoach.get(0).getCountTrainingSessions());

        //Проверить, что у тренера, идущего вторым в списке, одна тренировка
        assertEquals(1, trainingsCountByCoach.get(1).getCountTrainingSessions());

        //Проверить, что в списке порядок тренеров с одинаковым количеством тренировок не имеет значения
        assertTrue(
                (trainingsCountByCoach.get(0).getCoach().equals(coach1)
                        && trainingsCountByCoach.get(1).getCoach().equals(coach2))
                        || (trainingsCountByCoach.get(0).getCoach().equals(coach2)
                        && trainingsCountByCoach.get(1).getCoach().equals(coach1))
        );
    }
}
