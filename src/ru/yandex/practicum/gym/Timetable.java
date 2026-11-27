package ru.yandex.practicum.gym;


import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            timetable.put(day, daySchedule);
        }

        List<TrainingSession> sessionsAtTime = daySchedule.get(time);
        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            daySchedule.put(time, sessionsAtTime);
        }

        sessionsAtTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        if (sessions == null) {
            return Collections.emptyList();
        }
        return sessions;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counter = new HashMap<>();
        Iterator<TreeMap<TimeOfDay, List<TrainingSession>>> dayIterator = timetable.values().iterator();
        while (dayIterator.hasNext()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = dayIterator.next();

            Iterator<List<TrainingSession>> sessionsIterator = daySchedule.values().iterator();
            while (sessionsIterator.hasNext()) {
                List<TrainingSession> sessions = sessionsIterator.next();

                for (int i = 0; i < sessions.size(); i++) {
                    TrainingSession session = sessions.get(i);
                    Coach coach = session.getCoach();

                    Integer current = counter.get(coach);
                    if (current == null) {
                        counter.put(coach, 1);
                    } else {
                        counter.put(coach, current + 1);
                    }
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        Iterator<Map.Entry<Coach, Integer>> entryIterator = counter.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<Coach, Integer> entry = entryIterator.next();
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
                return Integer.compare(o2.getCount(), o1.getCount());
            }
        });

        return result;
    }
}
