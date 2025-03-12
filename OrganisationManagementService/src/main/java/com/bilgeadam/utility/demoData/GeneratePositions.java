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
        
        return List.of(position1, position2, position3, position4, position5, position6);
    }

}