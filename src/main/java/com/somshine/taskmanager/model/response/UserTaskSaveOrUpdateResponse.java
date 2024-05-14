package com.somshine.taskmanager.model.response;

import com.somshine.taskmanager.dto.UserTaskDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTaskSaveOrUpdateResponse extends BaseResponse {
    UserTaskDto userTask;

    public UserTaskSaveOrUpdateResponse(UserTaskDto userTask, Boolean success, String message) {
        this.userTask = userTask;
        this.setSuccess(success);
        this.setMessage(message);
    }
}
