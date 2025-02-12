package com.inventoryservice.util;

import com.inventoryservice.dto.request.WareHouseSaveRequestDTO;
import com.inventoryservice.services.WareHouseService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoDataGenerator
{
    private final WareHouseService wareHouseService;

    @PostConstruct
    public void generator(){
        addWarehouses();
    }
    public void addWarehouses(){
        String[] warehouseNames = {
                "Depo Atlas", "Depo Nova", "Depo Vega", "Depo Omega", "Depo Delta",
                "Depo Horizon", "Depo Zen", "Depo Prime", "Depo Nebula", "Depo Vortex"
        };


        String[] cities = {
                "Istanbul", "Ankara", "Izmir", "Bursa", "Antalya",
                "Adana", "Konya", "Gaziantep", "Kayseri", "Eskisehir"
        };

        for (int i = 0; i < 10; i++) {
            wareHouseService.save(new WareHouseSaveRequestDTO(warehouseNames[i], cities[i]));
        }
    }
}
