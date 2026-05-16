# burnout-prevention-system

A Java simulation of a university semester planner built as a university assignment.
Implements OOP principles including sealed interfaces, abstract classes, records, enums, and custom exceptions.

## Structure

- `UniversitySubject` — record representing a university subject with credits, rating, category and study time
- `SubjectRequirement` — record representing a minimum enrollment requirement per category
- `SemesterPlan` — record representing the full semester configuration
- `Category` — enum with four academic categories: MATH, PROGRAMMING, THEORY, PRACTICAL
- `AbstractSemesterPlanner` — abstract class implementing shared jar count logic
  - `SoftwareEngineeringSemesterPlanner` — minimizes the number of subjects while covering credit and category requirements
  - `ComputerScienceSemesterPlanner` — selects subjects with the highest student ratings regardless of category requirements

## How jar count works

Each subject requires a certain number of study days. After studying, a rest period is calculated
based on the subject's category. The total study days determine how many food supply jars are needed
for survival — one jar per every 5 days of studying. If the semester is too short to cover both
studying and rest, the jar count doubles. If rest time exceeds the allowed limit, the system throws
a `DisappointmentException`.

## Notes

- Only arrays are used as data structures — no Lists, Maps, or other collections
- Custom exceptions: `CryToStudentsDepartmentException`, `DisappointmentException`, `InvalidSubjectRequirementsException`
