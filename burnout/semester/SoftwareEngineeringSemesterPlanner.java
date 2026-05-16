package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.Collections;

public final class SoftwareEngineeringSemesterPlanner extends AbstractSemesterPlanner {
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
        UniversitySubject[] subjectsSort = new UniversitySubject[semesterPlan.subjects().length];
        for (int i = 0; i < subjectsSort.length; i++) {
            subjectsSort[i] = semesterPlan.subjects()[i];
        }
        for (int i = 0; i < subjectsSort.length - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < subjectsSort.length; j++) {
                if (subjectsSort[j].credits() > subjectsSort[maxIndex].credits()) {
                    maxIndex = j;
                }
            }
            UniversitySubject temp = subjectsSort[maxIndex];
            subjectsSort[maxIndex] = subjectsSort[i];
            subjectsSort[i] = temp;
        }

        UniversitySubject[] calculated = new UniversitySubject[subjectsSort.length];
        int totalCredits = 0;
        int index = 0;
        for (int i = 0; i < semesterPlan.subjectRequirements().length; i++) {
            int currentAmount = 0;
            for (int j = 0; j < subjectsSort.length; j++) {
                if (subjectsSort[j].category().equals(semesterPlan.subjectRequirements()[i].category())) {
                    calculated[index] = subjectsSort[j];
                    totalCredits += subjectsSort[j].credits();
                    index++;
                    currentAmount++;
                    if (currentAmount == semesterPlan.subjectRequirements()[i].minAmountEnrolled()) {
                        break;
                    }
                }
            }
        }

        boolean reachedMinCredits = false;
        if (totalCredits < semesterPlan.minimalAmountOfCredits()) {
            for (int i = 0; i < subjectsSort.length; i++) {
                boolean isAlreadyAdded = false;
                for (int j = 0; j < index; j++) {
                    if (subjectsSort[i].name().equals(calculated[j].name())) {
                        isAlreadyAdded = true;
                        break;
                    }
                }
                if (!isAlreadyAdded) {
                    calculated[index] = subjectsSort[i];
                    totalCredits += subjectsSort[i].credits();
                    index++;
                }
                if (totalCredits >= semesterPlan.minimalAmountOfCredits()) {
                    reachedMinCredits = true;
                    break;
                }
            }
        } else {
            reachedMinCredits = true;
        }

        if (!reachedMinCredits && totalCredits < semesterPlan.minimalAmountOfCredits()) {
            throw new CryToStudentsDepartmentException("Let me pass, please!");
        }

        UniversitySubject[] result = new UniversitySubject[index];
        for (int i = 0; i < index; i++) {
            result[i] = calculated[i];
        }
        return result;
    }

    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) throws DisappointmentException {
        return super.calculateJarCount(subjects, maximumSlackTime, semesterDuration);
    }
}