package com.bilgeadam.utility.demoData;

import com.bilgeadam.entity.Position;

import java.util.ArrayList;
import java.util.List;

public class GeneratePositions {

    public static List<Position> generatePositions(){
        Position position1 = Position.builder().title("BAŞKAN").departmentId(1L).companyId(1L).build();
        Position position2 = Position.builder().title("IT Specialist").departmentId(2L).companyId(1L).build();
        Position position3 = Position.builder().title("HR Specialist").departmentId(3L).companyId(1L).build();
        return List.of(position1, position2, position3);
    }

}
