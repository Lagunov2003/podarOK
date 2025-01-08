package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.AddGroupDto;
import ru.uniyar.podarok.repositories.GroupRepository;

/**
 * Сервис для управления группами.
 */
@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository groupRepository;

    /**
     * Добавляет новую группу в систему.
     *
     * @param addGroupDto объект с данными для создания новой группы
     */
    @Deprecated
    @Transactional
    public void addGroup(AddGroupDto addGroupDto) {
        groupRepository.addGroup(addGroupDto.getId());
    }
}
