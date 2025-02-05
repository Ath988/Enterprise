package com.inventoryservice.services;


import com.inventoryservice.dto.request.PageRequestDTO;
import com.inventoryservice.dto.request.WareHouseSaveRequestDTO;
import com.inventoryservice.dto.request.WareHouseUpdateRequestDTO;
import com.inventoryservice.entities.WareHouse;
import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.exceptions.ErrorType;
import com.inventoryservice.exceptions.InventoryServiceException;
import com.inventoryservice.repositories.WareHouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WareHouseService
{
    private final WareHouseRepository wareHouseRepository;

    //1L OLAN BÜTÜN DEĞERLER DAHA SONRA İSTEK ATILAN HESABIN AUTH ID'SİNDEN ALINACAKTIR.
    public Boolean save(WareHouseSaveRequestDTO dto)
    {
        wareHouseRepository.save(WareHouse
                .builder()
                .name(dto.name())
                .authId(1L)
                .location(dto.location())
                .build());
        return true;
    }


    public Boolean delete(Long id)
    {
        WareHouse wareHouse = wareHouseRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.WAREHOUSE_NOT_FOUND));
        wareHouse.setStatus(EStatus.DELETED);
        wareHouseRepository.save(wareHouse);
        return true;
    }

    public Boolean update(WareHouseUpdateRequestDTO dto)
    {
        WareHouse wareHouse = wareHouseRepository.findByIdAndAuthId(dto.id(), 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.WAREHOUSE_NOT_FOUND));
        if (dto.location() != null)
        {
            wareHouse.setLocation(dto.location());
        }
        if (dto.name() != null)
        {
            wareHouse.setName(dto.name());
        }
        wareHouseRepository.save(wareHouse);
        return true;
    }


    public List<WareHouse> findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(PageRequestDTO dto)
    {
        return wareHouseRepository.findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(dto.searchText(),1L, EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
    }

    public WareHouse findByIdAndAuthId(Long id)
    {

        return wareHouseRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.WAREHOUSE_NOT_FOUND));
    }

}
