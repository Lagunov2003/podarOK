package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.AddGroupDto;
import ru.uniyar.podarok.repositories.GroupRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    @Deprecated
    public void GroupService_AddGroup_VerifiesGroupAdded() {
        AddGroupDto addGroupDto = new AddGroupDto();
        addGroupDto.setId(1L);

        groupService.addGroup(addGroupDto);

        verify(groupRepository, times(1)).addGroup(1L);
    }
}
