package cloudledger.requisition_system.service;

import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.models.dto.request.UpdateUserDTO;
import cloudledger.requisition_system.models.dto.response.GetUserDTO;

import java.util.List;

public interface UserService {
    GetUserDTO getUserByUserName(String username);
    List<GetUserDTO> getAllUsers();
    String deleteUser(Integer id);
    User assignUserToProject(Integer userId, Long projectId);
    String updateUser(UpdateUserDTO request, Integer userId);
}
