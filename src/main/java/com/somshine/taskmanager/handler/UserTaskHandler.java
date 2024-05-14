package com.somshine.taskmanager.handler;

import com.somshine.taskmanager.config.Constant;
import com.somshine.taskmanager.config.TaskStatuses;
import com.somshine.taskmanager.entity.TaskStatue;
import com.somshine.taskmanager.entity.User;
import com.somshine.taskmanager.entity.UserTask;
import com.somshine.taskmanager.model.request.AssignTaskRequest;
import com.somshine.taskmanager.model.request.UserTaskRequest;
import com.somshine.taskmanager.model.response.BaseResponse;
import com.somshine.taskmanager.model.response.UserTaskSaveOrUpdateResponse;
import com.somshine.taskmanager.repository.TaskStatusRepository;
import com.somshine.taskmanager.repository.UserRepository;
import com.somshine.taskmanager.repository.UserTaskRepository;
import com.somshine.taskmanager.service.UserTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserTaskHandler {
    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Handler method to do the validation, request and response mapping
     * @param request
     * @return
     */
    public UserTaskSaveOrUpdateResponse handlerSaveOrUpdateValidation(UserTaskRequest request) {
        UserTaskSaveOrUpdateResponse response = null;
        if (request != null) {
            if (StringUtils.isEmpty(request.getTitle())) {
                response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_TITLE_REQUIRED);
            } else {
                if (request.getId() != null && request.getId() > 0) {
                    Optional<UserTask> userTask = userTaskRepository.findById(request.getId());
                    if (!userTask.isPresent()) {
                        response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_USER_TASK_NOT_FOUND);
                    } else if (StringUtils.isEmpty(request.getStatus())) {
                        response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_USER_TASK_STATUS_REQUIRED);
                    } else if (StringUtils.isNotEmpty(request.getStatus())) {
                        Optional<TaskStatue> taskStatue = taskStatusRepository.findByNameIgnoreCase(request.getStatus());
                        if (!taskStatue.isPresent()) {
                            response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_USER_TASK_STATUS_NOT_VALID);
                        }
                    }
                }
            }
        } else {
            response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_EMPTY_REQUEST_BODY);
        }

        return response;
    }

    public BaseResponse handlerAssignValidation(AssignTaskRequest request) {
        BaseResponse response = null;

        if (request != null) {
            if (request.getUserId() == null || request.getUserId() <= 0) {
                response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_USER_ID_REQUIRED);
            } else if (request.getTaskId() == null || request.getTaskId() <= 0) {
                response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_TASK_ID_REQUIRED);
            } else {
                Optional<User> userOptional = userRepository.findById(request.getUserId());
                if (!userOptional.isPresent()) {
                    response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_USER_NOT_FOUND);
                } else {
                    Optional<UserTask> userTaskOptional = userTaskRepository.findById(request.getTaskId());
                    if (!userTaskOptional.isPresent()) {
                        response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_USER_NOT_FOUND);
                    }
                }

            }
        } else {
            response = new UserTaskSaveOrUpdateResponse(null, false, Constant.ERROR_EMPTY_REQUEST_BODY);
        }
        return response;
    }
}
