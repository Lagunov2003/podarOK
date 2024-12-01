package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.AddGroupDto;
import ru.uniyar.podarok.repositories.GroupRepository;

@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository groupRepository;

    @Transactional
    public void addGroup(AddGroupDto addGroupDto) {
        groupRepository.addGroup(addGroupDto.getId());
    }
}
