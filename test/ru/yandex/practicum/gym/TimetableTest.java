package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, monday.size());
        Assertions.assertEquals(singleTrainingSession, monday.get(0));

        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
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

        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(new TimeOfDay(13, 0), thursday.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), thursday.get(1).getTimeOfDay());

        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> at13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, at13.size());
        Assertions.assertEquals(singleTrainingSession, at13.get(0));

        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0)).isEmpty());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");
        Group child = new Group("Акробатика для детей", Age.CHILD, 60);
        Group adult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TimeOfDay time = new TimeOfDay(13, 0);

        timetable.addNewTrainingSession(new TrainingSession(child, coach1, DayOfWeek.MONDAY, time));
        timetable.addNewTrainingSession(new TrainingSession(adult, coach2, DayOfWeek.MONDAY, time));

        Assertions.assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        Assertions.assertEquals(2, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time).size());
    }

    @Test
    void testGetCountByCoachesOrderedDescending() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");
        Group child = new Group("Акробатика для детей", Age.CHILD, 60);
        Group adult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(child, coach1, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(child, coach1, DayOfWeek.WEDNESDAY,
                new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(child, coach1, DayOfWeek.SATURDAY,
                new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adult, coach2, DayOfWeek.FRIDAY,
                new TimeOfDay(17, 0)));

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(2, counts.size());
        Assertions.assertEquals(coach1, counts.get(0).getCoach());
        Assertions.assertEquals(3, counts.get(0).getCount());
        Assertions.assertEquals(coach2, counts.get(1).getCoach());
        Assertions.assertEquals(1, counts.get(1).getCount());
    }
}
