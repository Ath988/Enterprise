package com.inventoryservice.util;

import com.inventoryservice.dto.request.BuyOrderSaveRequestDTO;
import com.inventoryservice.dto.request.ProductSaveRequestDTO;
import com.inventoryservice.dto.request.SupplierSaveRequestDTO;
import com.inventoryservice.dto.request.WareHouseSaveRequestDTO;
import com.inventoryservice.services.BuyOrderService;
import com.inventoryservice.services.ProductService;
import com.inventoryservice.services.SupplierService;
import com.inventoryservice.services.WareHouseService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DemoDataGenerator
{
    private final WareHouseService wareHouseService;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final BuyOrderService buyOrderService;

    @PostConstruct
    public void generator(){
        addWarehouses();
        addSuppliers();
        addProducts();
        addBuyOrders();
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

    public void addSuppliers(){
        supplierService.save(new SupplierSaveRequestDTO("Ali", "Kaya", "ali@gmail.com", "05335242332", "Istanbul", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Mehmet", "Yılmaz", "mehmet@gmail.com", "05432659874", "Ankara", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Zeynep", "Demir", "zeynep@gmail.com", "05539874521", "Izmir", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Ahmet", "Çelik", "ahmet@gmail.com", "05325698741", "Bursa", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Elif", "Kurt", "elif@gmail.com", "05412547896", "Adana", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Hakan", "Koç", "hakan@gmail.com", "05534789632", "Trabzon", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Merve", "Şahin", "merve@gmail.com", "05329874561", "Antalya", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Burak", "Öztürk", "burak@gmail.com", "05436985214", "Eskişehir", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Cem", "Erdem", "cem@gmail.com", "05547896321", "Samsun", "Tedarikci"));
        supplierService.save(new SupplierSaveRequestDTO("Gözde", "Aydın", "gozde@gmail.com", "05321478965", "Gaziantep", "Tedarikci"));
    }

    public void addProducts(){
        productService.save(new ProductSaveRequestDTO(1L, 1L, "Masa", "Dört ayaklı masa", BigDecimal.valueOf(2500), 100, 50));
        productService.save(new ProductSaveRequestDTO(2L, 1L, "Sandalye", "Ahşap sandalye", BigDecimal.valueOf(750), 200, 30));
        productService.save(new ProductSaveRequestDTO(3L, 2L, "Dolap", "İki kapaklı dolap", BigDecimal.valueOf(4200), 50, 20));
        productService.save(new ProductSaveRequestDTO(4L, 2L, "Kitaplık", "Beş raflı kitaplık", BigDecimal.valueOf(1800), 80, 40));
        productService.save(new ProductSaveRequestDTO(5L, 3L, "Sehpa", "Cam yüzeyli sehpa", BigDecimal.valueOf(950), 120, 25));
        productService.save(new ProductSaveRequestDTO(6L, 5L, "TV Ünitesi", "Modern TV ünitesi", BigDecimal.valueOf(3700), 60, 15));
        productService.save(new ProductSaveRequestDTO(7L, 4L, "Çalışma Masası", "Geniş çalışma masası", BigDecimal.valueOf(3200), 70, 35));
        productService.save(new ProductSaveRequestDTO(8L, 8L, "Ofis Koltuğu", "Ergonomik ofis koltuğu", BigDecimal.valueOf(2800), 90, 45));
        productService.save(new ProductSaveRequestDTO(9L, 9L, "Yatak", "Çift kişilik yatak", BigDecimal.valueOf(5600), 40, 50));
        productService.save(new ProductSaveRequestDTO(10L, 7L, "Komodin", "İki çekmeceli komodin", BigDecimal.valueOf(1100), 150, 60));

    }

    public void addBuyOrders(){
        buyOrderService.save(new BuyOrderSaveRequestDTO(1L,1L,10));
        buyOrderService.save(new BuyOrderSaveRequestDTO(2L,2L,20));
        buyOrderService.save(new BuyOrderSaveRequestDTO(3L,3L,30));
        buyOrderService.save(new BuyOrderSaveRequestDTO(4L,4L,40));
        buyOrderService.save(new BuyOrderSaveRequestDTO(5L,5L,50));
        buyOrderService.save(new BuyOrderSaveRequestDTO(6L,6L,66));
        buyOrderService.save(new BuyOrderSaveRequestDTO(7L,7L,77));
        buyOrderService.save(new BuyOrderSaveRequestDTO(8L,8L,88));
        buyOrderService.save(new BuyOrderSaveRequestDTO(9L,9L,99));
        buyOrderService.save(new BuyOrderSaveRequestDTO(10L,10L,100));

    }
}
