package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.User;
import com.projectmanagementservice.entities.enums.EStatus;
import com.projectmanagementservice.exceptions.ErrorType;
import com.projectmanagementservice.exceptions.ProjectManagementException;
import com.projectmanagementservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    //TODO Gelen istekten hangi kullanıcının olduğu bilgisi gelince metodlardaki sabit "1L" değerleri değiştirilecek.
    public Boolean save(UserSaveRequestDTO dto)
    {
        if (userRepository.findByEmailIgnoreCaseAndAuthIdAndStatusIsNot(dto.email(), 1L, EStatus.DELETED).isPresent())
        {
            throw new ProjectManagementException(ErrorType.EMAIL_ALREADY_TAKEN);
        }
        userRepository.save(User
                .builder()
                .name(dto.name())
                        .authId(1L)
                        .surname(dto.surname())
                        .email(dto.email())
                .build());
        return true;
    }

    public Boolean delete(Long id)
    {
        User user = userRepository.findByIdAndAuthId(id,1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        user.setStatus(EStatus.DELETED);
        userRepository.save(user);
        return true;
    }

    public Boolean update(UserUpdateRequestDTO dto)
    {
        User user = userRepository.findByIdAndAuthId(dto.id(),1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        if (userRepository.findByEmailIgnoreCaseAndAuthIdAndStatusIsNot(dto.email(), 1L, EStatus.DELETED).isPresent())
        {
            throw new ProjectManagementException(ErrorType.EMAIL_ALREADY_TAKEN);
        }

        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        userRepository.save(user);
        return true;
    }


    public List<User> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(PageRequestDTO dto)
    {
        return userRepository.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(dto.searchText(), EStatus.DELETED,1L, PageRequest.of(dto.page(), dto.size()));
    }

    public User findByIdAndAuthId(Long id)
    {
       return  userRepository.findByIdAndAuthId(id,1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
    }

}

