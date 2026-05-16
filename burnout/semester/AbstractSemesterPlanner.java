package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public abstract sealed class AbstractSemesterPlanner implements SemesterPlannerAPI permits ComputerScienceSemesterPlanner,SoftwareEngineeringSemesterPlanner{
    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) throws DisappointmentException {
            if(subjects==null){
                throw new IllegalArgumentException("Subjects array is null!");
            }
            if(maximumSlackTime<=0 ||semesterDuration<=0){
                throw new IllegalArgumentException("Value must be positive!");
            }
            int totalDaysStudy = 0;
            int totalRestDays = 0;
            int totalDays=0;
            for (UniversitySubject subject : subjects) {
                double koef = 0.0;
                switch (subject.category()) {
                    case MATH -> koef = 0.2;
                    case THEORY -> koef = 0.15;
                    case PRACTICAL -> koef = 0.05;
                    case PROGRAMMING -> koef = 0.1;
                }
                int restNeeded = (int) Math.ceil(koef * subject.neededStudyTime());
                totalDaysStudy +=subject.neededStudyTime();
                totalRestDays += restNeeded;
                totalDays+=restNeeded+subject.neededStudyTime();
            }
            if(totalRestDays>maximumSlackTime){
                throw new DisappointmentException("Grandma is disappointed!");
            }
            int jars=totalDaysStudy/5;
            if(semesterDuration<totalDays){
                jars*=2;
            }
            return jars;
        }

    }

