package com.inventoryservice.util;

import com.inventoryservice.dto.request.NotificationMessageRequestDto;
import com.inventoryservice.entities.Product;
import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.manager.NotificationManager;
import com.inventoryservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoNotificationScheduler
{
    private final ProductService productService;
    private final NotificationManager notificationManager;

    /**
     * Every minute database will be checked and if product is below minimum stock level it will be auto ordered
     */
    //TODO CHANGE 1 MIN SCHEDULER TO 1 HOUR LATER
    @Scheduled(cron = "0 * * * * ?")
    public void AutoOrderByStockLevel() {

        List<Product> productList = productService.findAllByMinimumStockLevelAndStatus(EStatus.ACTIVE);
        productList.forEach(product ->
        {
            if (!product.getIsNotified())
            {
                System.out.println(product.getName() + " adlı ürüne notification gitti.");
                notificationManager.notificationSender(new NotificationMessageRequestDto("Düşük Stok",product.getName() + " adlı ürünün kalan stok miktarı: " + product.getStockCount(),true));
                product.setIsNotified(true);
                productService.save(product);
            }
        });


    }
}
