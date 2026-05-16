package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public final class ComputerScienceSemesterPlanner extends AbstractSemesterPlanner{
    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) throws DisappointmentException {
        return super.calculateJarCount(subjects, maximumSlackTime, semesterDuration);
    }

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException, CryToStudentsDepartmentException {
        if(semesterPlan==null){
            throw new IllegalArgumentException("Semester plan is null!");
        }
        for (int i = 0; i <semesterPlan.subjectRequirements().length ; i++) {
            for (int j = 0; j <semesterPlan.subjectRequirements().length; j++) {
                if(j!=i &&semesterPlan.subjectRequirements()[i].category().equals(semesterPlan.subjectRequirements()[j].category())){
                    throw new InvalidSubjectRequirementsException("Duplicated categories!");
                }
            }
        }

        UniversitySubject[] subjectsSortedByRating=new UniversitySubject[semesterPlan.subjects().length];
        for (int i = 0; i <semesterPlan.subjects().length ; i++) {
            subjectsSortedByRating[i]=semesterPlan.subjects()[i];
        }
        for (int i = 0; i < subjectsSortedByRating.length-1 ; i++) {
            for (int j = 1; j <subjectsSortedByRating.length-i ; j++) {
                if(subjectsSortedByRating[j-1].rating()<subjectsSortedByRating[j].rating()){
                    UniversitySubject temp=subjectsSortedByRating[j];
                    subjectsSortedByRating[j]=subjectsSortedByRating[j-1];
                    subjectsSortedByRating[j-1]=temp;
                }

            }
        }
        UniversitySubject[] subjectsSelected=new UniversitySubject[subjectsSortedByRating.length];
        int size=0;
        int totalCredits=0;
        for (int i = 0; i <subjectsSelected.length ; i++) {
            subjectsSelected[i]=subjectsSortedByRating[i];
            size++;
            totalCredits+=subjectsSelected[i].credits();
            if(totalCredits>=semesterPlan.minimalAmountOfCredits()){
                break;
            }
        }
        if(totalCredits< semesterPlan.minimalAmountOfCredits()){
            throw new CryToStudentsDepartmentException("Let me pass,please!");
        }
        UniversitySubject[] result=new UniversitySubject[size];
        for (int i = 0; i <size ; i++) {
            result[i]=subjectsSelected[i];
        }
        return result;
    }
}
