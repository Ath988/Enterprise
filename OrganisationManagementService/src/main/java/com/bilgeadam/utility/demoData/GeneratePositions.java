package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Position;

import java.util.ArrayList;
import java.util.List;

public class GeneratePositions {
    
    public static List<Position> generatePositions() {
        Position position1 = Position.builder().title("CEO").companyId(1L).departmentId(1L).build();
        Position position2 = Position.builder().title("CTO").companyId(1L).departmentId(2L).parentPositionId(1L).build();
        Position position3 = Position.builder().title("CFO").companyId(1L).departmentId(3L).parentPositionId(1L).build();
        Position position4 = Position.builder().title("Developer").companyId(1L).departmentId(2L).parentPositionId(2L).build();
        Position position5 = Position.builder().title("Tester").companyId(1L).departmentId(2L).parentPositionId(2L).build();
        Position position6 = Position.builder().title("Accountant").companyId(1L).departmentId(3L).parentPositionId(3L).build();
        Position position7 = Position.builder().title("HR Manager").companyId(1L).departmentId(3L).parentPositionId(3L).build();
        Position position8 = Position.builder().title("Recruiter").companyId(1L).departmentId(3L).parentPositionId(7L).build();
        Position position9 = Position.builder().title("System Administrator").companyId(1L).departmentId(2L).parentPositionId(2L).build();
        Position position10 = Position.builder().title("DevOps Engineer").companyId(1L).departmentId(2L).parentPositionId(9L).build();
        Position position11 = Position.builder().title("Project Manager").companyId(1L).departmentId(1L).parentPositionId(1L).build();
        Position position12 = Position.builder().title("Business Analyst").companyId(1L).departmentId(1L).parentPositionId(11L).build();
        Position position13 = Position.builder().title("HR Specialist").companyId(1L).departmentId(3L).parentPositionId(7L).build();
        Position position14 = Position.builder().title("IT Support").companyId(1L).departmentId(2L).parentPositionId(2L).build();
        Position position15 = Position.builder().title("Finance Analyst").companyId(1L).departmentId(3L).parentPositionId(3L).build();
        Position position16 = Position.builder().title("Network Engineer").companyId(1L).departmentId(2L).parentPositionId(9L).build();
        Position position17 = Position.builder().title("Security Specialist").companyId(1L).departmentId(2L).parentPositionId(9L).build();
        Position position18 = Position.builder().title("QA Engineer").companyId(1L).departmentId(2L).parentPositionId(5L).build();
        Position position19 = Position.builder().title("Senior Developer").companyId(1L).departmentId(2L).parentPositionId(4L).build();
        Position position20 = Position.builder().title("Junior Developer").companyId(1L).departmentId(2L).parentPositionId(19L).build();
        
        return List.of(position1, position2, position3, position4, position5, position6, position7, position8, position9, position10,
                       position11, position12, position13, position14, position15, position16, position17, position18, position19, position20);
    }

}