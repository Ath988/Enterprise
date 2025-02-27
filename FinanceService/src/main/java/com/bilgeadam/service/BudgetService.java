package com.bilgeadam.service;

import com.bilgeadam.dto.request.BudgetSaveRequestDTO;
import com.bilgeadam.dto.request.BudgetUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.dto.response.BudgetByDepartmentResponseDTO;
import com.bilgeadam.dto.response.DepartmentResponseDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.Budget;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.manager.IDepartmentClient;
import com.bilgeadam.repository.BudgetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final IDepartmentClient departmentClient;
    private final AccountService accountService;

    @Transactional
    public Boolean save(BudgetSaveRequestDTO dto) {
        // FeignClient kullanarak OrganizationManagementMicroservice'den department bilgisi alınıyor.
        DepartmentResponseDTO department = departmentClient.getDepartmentById(dto.departmentId());
        if (department == null) {
            throw new FinanceServiceException(ErrorType.DEPARTMENT_NOT_FOUND);
        }

        Budget budget = Budget.builder()
                .year(dto.year())
                .month(dto.month())
                .allocatedAmount(dto.allocatedAmount())
                .spentAmount(BigDecimal.ZERO)
                .budgetCategory(dto.budgetCategory())
                .description(dto.description())

                .departmentId(department.id()) // Department ID de setleniyor
                .build();

        budgetRepository.save(budget);
        return true;
    }

    @Transactional
    public Boolean update(BudgetUpdateRequestDTO dto) {
        // FeignClient ile OrganizationManagementMicroservice'den department bilgisi alınıyor.
        DepartmentResponseDTO department = departmentClient.getDepartmentById(dto.departmentId());
        if (department == null) {
            throw new FinanceServiceException(ErrorType.DEPARTMENT_NOT_FOUND);
        }

        // Budget veritabanından çekiliyor
        Budget budget = budgetRepository.findById(dto.id())
                .orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));

        // Veriler güncelleniyor
        budget.setYear(dto.year());
        budget.setMonth(dto.month());
        budget.setDepartmentId(department.id()); // Department ID güncelleniyor
        budget.setAllocatedAmount(dto.allocatedAmount());
        budget.setBudgetCategory(dto.budgetCategory());
        budget.setDescription(dto.description());

        // Harcanan miktar DTO'da varsa güncelleniyor
        if (dto.spentAmount() != null) {
            budget.setSpentAmount(dto.spentAmount());
        }

        // Budget kaydediliyor
        budgetRepository.save(budget);
        return true;
    }

    @Transactional
    public Boolean delete(Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
        budget.setStatus(EStatus.DELETED);
        budgetRepository.save(budget);
        return true;
    }

    public List<BudgetByDepartmentResponseDTO> findAllByDepartmentName(String departmentName) {
        // FeignClient ile departmentName'e göre ID çekiyoruz
        DepartmentResponseDTO department = departmentClient.getDepartmentByName(departmentName);

        if (department == null) {
            throw new FinanceServiceException(ErrorType.DEPARTMENT_NOT_FOUND);
        }

        // departmentId'ye göre bütçeleri çekiyoruz
        List<Budget> allBudgetsByDepartment = budgetRepository.findAllByDepartmentId(department.id());
        allBudgetsByDepartment.removeIf(budget -> budget.getStatus().equals(EStatus.DELETED));

        // Budget nesnelerini DTO'ya çeviriyoruz
        List<BudgetByDepartmentResponseDTO> budgetByDepartmentResponseDTOS = new ArrayList<>();
        for (Budget budget : allBudgetsByDepartment) {
            budgetByDepartmentResponseDTOS.add(new BudgetByDepartmentResponseDTO(
                    budget.getId(),
                    budget.getBudgetCategory(),
                    budget.getSpentAmount(),
                    budget.getDescription()
            ));
        }
        return budgetByDepartmentResponseDTOS;
    }

    public List<Budget> findAll(PageRequestDTO dto) {
        return budgetRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
    }

    @Transactional
    public void updateSpentAmount(Budget budget) {
        budgetRepository.save(budget);
    }

}
