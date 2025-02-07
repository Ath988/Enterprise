package com.inventoryservice.services;



import com.inventoryservice.dto.request.PageRequestDTO;
import com.inventoryservice.dto.request.SupplierSaveRequestDTO;
import com.inventoryservice.dto.request.SupplierUpdateRequestDTO;
import com.inventoryservice.entities.Supplier;
import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.exceptions.InventoryServiceException;
import com.inventoryservice.repositories.SupplierRepository;
import com.inventoryservice.exceptions.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService
{
    private final SupplierRepository supplierRepository;
    private final BuyOrderService orderService;
    private final ProductService productService;


    //1L OLAN BÜTÜN DEĞERLER DAHA SONRA İSTEK ATILAN HESABIN AUTH ID'SİNDEN ALINACAKTIR.
    
    public Boolean save(SupplierSaveRequestDTO dto)
    {
        supplierRepository.findByEmailAndAuthId(dto.email(),1L).ifPresent(supplier -> {
            throw new InventoryServiceException(ErrorType.SUPPLIER_EMAIL_ALREADY_EXISTS);
        });
        
        supplierRepository.save(Supplier
                .builder()
                .name(dto.name())
                .surname(dto.surname())
                .authId(1L)
                .email(dto.email())
                .contactInfo(dto.contactInfo())
                .address(dto.address())
                .notes(dto.notes())
                .build());

        return true;
    }
    

    public Boolean delete(Long id)
    {
        Supplier supplier = supplierRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.SUPPLIER_NOT_FOUND));
        supplier.setStatus(EStatus.DELETED);
        supplierRepository.save(supplier);
        return true;
    }

    public Boolean update(SupplierUpdateRequestDTO dto)
    {
        Supplier supplier = supplierRepository.findByIdAndAuthId(dto.id(), 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.SUPPLIER_NOT_FOUND));
        if (dto.name() != null)
        {
            supplier.setName(dto.name());
        }
        if (dto.contactInfo() != null)
        {
            supplier.setContactInfo(dto.contactInfo());
        }
        if (dto.address() != null)
        {
            supplier.setAddress(dto.address());
        }
        if (dto.notes() != null)
        {
            supplier.setNotes(dto.notes());
        }
        if (dto.surname() != null)
        {
            supplier.setSurname(dto.surname());
        }
        supplierRepository.save(supplier);
        return true;
    }

    public List<Supplier> findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(PageRequestDTO dto)
    {
        return supplierRepository.findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(dto.searchText(),1L, EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
    }

    public Supplier findByIdAndAuthId(Long id)
    {
        return supplierRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.SUPPLIER_NOT_FOUND));
    }

    public Supplier findById(Long id)
    {
        return supplierRepository.findById(id).orElseThrow(() -> new InventoryServiceException(ErrorType.SUPPLIER_NOT_FOUND));
    }


}
