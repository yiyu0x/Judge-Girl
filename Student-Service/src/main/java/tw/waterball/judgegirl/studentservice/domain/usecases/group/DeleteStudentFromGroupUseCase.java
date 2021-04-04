package tw.waterball.judgegirl.studentservice.domain.usecases.group;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tw.waterball.judgegirl.commons.exceptions.NotFoundException;
import tw.waterball.judgegirl.entities.Group;
import tw.waterball.judgegirl.studentservice.domain.repositories.GroupRepository;

import javax.inject.Named;

/**
 * @author - wally55077@gmail.com
 */
@Named
@AllArgsConstructor
public class DeleteStudentFromGroupUseCase {

    private final GroupRepository groupRepository;

    public void execute(Request request)
            throws NotFoundException {
        // TODO: improve performance, should only perform one SQL query
        int groupId = request.groupId;
        int studentId = request.studentId;
        Group group = groupRepository.findGroupById(groupId)
                .orElseThrow(NotFoundException::new);
        group.deleteStudentById(studentId);
        groupRepository.save(group);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        public int groupId;
        public int studentId;
    }

}