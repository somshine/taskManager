package com.somshine.taskmanager.service.impl;

import com.somshine.taskmanager.config.Constant;
import com.somshine.taskmanager.config.TaskPriorities;
import com.somshine.taskmanager.config.TaskStatuses;
import com.somshine.taskmanager.dto.UserTaskDto;
import com.somshine.taskmanager.entity.TaskStatue;
import com.somshine.taskmanager.entity.UserTask;
import com.somshine.taskmanager.handler.UserTaskHandler;
import com.somshine.taskmanager.model.PageModel;
import com.somshine.taskmanager.model.request.AssignTaskRequest;
import com.somshine.taskmanager.model.request.ProgressTaskRequest;
import com.somshine.taskmanager.model.request.UserTaskRequest;
import com.somshine.taskmanager.model.response.BaseResponse;
import com.somshine.taskmanager.model.response.TaskStatisticResponse;
import com.somshine.taskmanager.model.response.UserTaskSaveOrUpdateResponse;
import com.somshine.taskmanager.repository.TaskPriorityRepository;
import com.somshine.taskmanager.repository.TaskStatusRepository;
import com.somshine.taskmanager.repository.UserRepository;
import com.somshine.taskmanager.repository.UserTaskRepository;
import com.somshine.taskmanager.service.UserTaskService;
import com.somshine.taskmanager.utils.DateUtil;
import com.somshine.taskmanager.utils.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements UserTaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserTaskHandler userTaskHandler;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    /**
     * Save and Update User Task functionality
     * @param request
     * @return
     */
    @Transactional
    @Override
    public UserTaskSaveOrUpdateResponse save(UserTaskRequest request) {
        UserTaskSaveOrUpdateResponse response = userTaskHandler.handlerSaveOrUpdateValidation(request);

        if (response != null) {
            return response;
        }

        UserTask userTask = null;

        if (request.getId() != null && request.getId() > 0) {
            userTask = userTaskRepository.findById(request.getId()).orElse(null);
            userTask.setTaskStatus(taskStatusRepository.findByNameIgnoreCase(request.getStatus()).get());

            if (StringUtils.isNotEmpty(request.getPriority())) {
                userTask.setTaskPriority(taskPriorityRepository.findByNameIgnoreCase(request.getPriority()).get());
            }

            if (request.getStatus().equalsIgnoreCase(TaskStatuses.COMPLETED)) {
                userTask.setCompletedOn(LocalDateTime.now());
            }

        } else {
            userTask = new UserTask();
            userTask.setCreatedOn(LocalDateTime.now());
            userTask.setTaskStatus(taskStatusRepository.findByNameIgnoreCase(TaskStatuses.NEW).get());
            userTask.setTaskPriority(taskPriorityRepository.findByNameIgnoreCase(TaskPriorities.LOW).get());
        }

        userTask.setTitle(request.getTitle());
        userTask.setDescription(request.getDescription());
        userTask.setDueDate(request.getDueDate());

        UserTask userTaskSaved = userTaskRepository.save(userTask);
        return new UserTaskSaveOrUpdateResponse(convertUserTaskToDto(userTaskSaved), true, Constant.SUCCESS_MESSAGE);
    }

    /**
     * Delete user task
     * @param taskId
     */
    @Transactional
    @Override
    public void delete(Long taskId) {
        userTaskRepository.deleteById(taskId);
    }

    /**
     * Get User Task by Id
     * @param id
     * @return
     */
    @Override
    public UserTask getTaskById(Long id) {
        return userTaskRepository.findById(id).orElse(null);
    }

    /**
     * Get all Task with pagination
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserTask> getAllTask(Integer pageNo, Integer pageSize) {
        PageModel pageModel = PaginationUtil.getPaginationDetail(pageNo, pageSize);
        Pageable pageWith = PageRequest.of(pageModel.getPageNo(), pageModel.getPageSize());

        return userTaskRepository.findUserTaskByTitleIsNotNull(pageWith);
    }

    /**
     * Assign task to user
     * @param request
     * @return
     */
    @Transactional
    @Override
    public BaseResponse assignTaskToUser(AssignTaskRequest request) {
        BaseResponse response = userTaskHandler.handlerAssignValidation(request);

        if (response != null) {
            return response;
        }

        response = new BaseResponse();

        Optional<UserTask> userTaskOptional = userTaskRepository.findById(request.getTaskId());
        if (userTaskOptional.isPresent()) {
            UserTask userTask = userTaskOptional.get();

            userTask.setUser(userRepository.findById(request.getUserId()).get());
            userTaskRepository.save(userTask);

            response.setSuccess(true);
            response.setMessage(Constant.SUCCESS_MESSAGE);
        } else {
            response.setSuccess(false);
            response.setMessage(Constant.ERROR_USER_NOT_FOUND);
        }

        return response;
    }

    /**
     * Get Assigned user tasks
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserTask> getUserAssignedTasks(Long userId, Integer pageNo, Integer pageSize) {
        PageModel pageModel = PaginationUtil.getPaginationDetail(pageNo, pageSize);
        Pageable pageWith = PageRequest.of(pageModel.getPageNo(), pageModel.getPageSize());

        return userTaskRepository.findUserTaskByUser(userRepository.findById(userId).get(), pageWith);
    }

    /**
     * Add Progress on task
     * @param request
     * @return
     */
    @Override
    @Transactional
    public BaseResponse progressTask(ProgressTaskRequest request) {
        BaseResponse response = new BaseResponse();

        if (request.getTaskId() == null || request.getTaskId() == 0) {
            response.setSuccess(false);
            response.setMessage(Constant.ERROR_TASK_ID_REQUIRED);

            return response;
        }

        if (request.getProgress() == null || request.getProgress() < 0 || request.getProgress() > 100) {
            response.setSuccess(false);
            response.setMessage(Constant.ERROR_PROGRESS_RANGE);

            return response;
        }

        Optional<UserTask> userTaskOptional = userTaskRepository.findById(request.getTaskId());
        if (userTaskOptional.isPresent()) {
            UserTask userTask = userTaskOptional.get();

            userTask.setProgress(request.getProgress());
            userTaskRepository.save(userTask);

            response.setSuccess(true);
            response.setMessage(Constant.SUCCESS_MESSAGE);
        } else {
            response.setSuccess(false);
            response.setMessage(Constant.ERROR_USER_NOT_FOUND);
        }

        return response;
    }

    /**
     * Get all tasks with overdue
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserTask> getOverdueTasks(Integer pageNo, Integer pageSize) {
        PageModel pageModel = PaginationUtil.getPaginationDetail(pageNo, pageSize);
        Pageable pageWith = PageRequest.of(pageModel.getPageNo(), pageModel.getPageSize());

        return userTaskRepository.findUserTaskByDueDateIsLessThan(LocalDateTime.now(), pageWith);
    }

    @Override
    public Page<UserTask> getTaskByStatus(String status, Integer pageNo, Integer pageSize) {
        PageModel pageModel = PaginationUtil.getPaginationDetail(pageNo, pageSize);
        Pageable pageWith = PageRequest.of(pageModel.getPageNo(), pageModel.getPageSize());
        return userTaskRepository.findUserTaskByTaskStatus(taskStatusRepository.findByNameIgnoreCase(status).get(), pageWith);
    }

    /**
     * Get the completed task with Date range as Start Date and End Date
     * Date in query parameters with LocalDateTime format as "yyyy-MM-dd'T'HH:mm:ss" ex: 2024-05-14T00:00:00
     * @param status
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserTask> getTasksCompletedByDateRange(String status, String startDate, String endDate, Integer pageNo, Integer pageSize) {

        try {
            LocalDateTime startDateTime = DateUtil.getLocalDateTimeFromString(startDate);
            LocalDateTime endDateTime = DateUtil.getLocalDateTimeFromString(endDate);

            log.info("Start : {} end : {}", startDateTime, endDateTime);

            PageModel pageModel = PaginationUtil.getPaginationDetail(pageNo, pageSize);
            Pageable pageWith = PageRequest.of(pageModel.getPageNo(), pageModel.getPageSize());

            return userTaskRepository.findUserTaskByTaskStatusAndCreatedOnBetween(taskStatusRepository.findByNameIgnoreCase(status).get(), startDateTime, endDateTime, pageWith);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Page.empty();
    }

    /**
     * Get statistics of the task with criteria:
     * Get statistics on the total number of tasks, the number of completed tasks, and the percentage of completed tasks.
     * @return
     */
    @Override
    public TaskStatisticResponse getTaskStatistics() {
        TaskStatisticResponse taskStatisticResponse = new TaskStatisticResponse();
        taskStatisticResponse.setTotalTasks(userTaskRepository.count());
        taskStatisticResponse.setCompletedTasks(userTaskRepository.countByTaskStatus(taskStatusRepository.findByNameIgnoreCase(TaskStatuses.COMPLETED).get()));
        taskStatisticResponse.setCompletedTasks(userTaskRepository.countByTaskStatus(taskStatusRepository.findByNameIgnoreCase(TaskStatuses.COMPLETED).get()));

        if (taskStatisticResponse.getTotalTasks() > 0 && taskStatisticResponse.getCompletedTasks() > 0) {
            taskStatisticResponse.setPercentCompleted((double) (((double) taskStatisticResponse.getCompletedTasks() / (double) taskStatisticResponse.getTotalTasks()) * 100));
        }

        taskStatisticResponse.setSuccess(true);
        taskStatisticResponse.setMessage(Constant.SUCCESS_MESSAGE);

        return taskStatisticResponse;
    }

    /**
     * For last point "Priority-based Task Queue:" I am having confusion so create the API method to return the priority tasks
     * @return
     */
    @Override
    public List<UserTask> getPriorityQueue() {
        List<Sort.Order> orders = new ArrayList<>();

        Sort.Order StartTimeOrder = new Sort.Order(Sort.Direction.DESC, "dueDate");
        orders.add(StartTimeOrder);
        Sort.Order progDateOrder = new Sort.Order(Sort.Direction.DESC, "taskPriority");
        orders.add(progDateOrder);

        return userTaskRepository.findAll(Sort.by(orders));
    }

    /**
     * We can use the ord.modelmapper here instead of manual conversion
     * @param userTask
     * @return
     */
    private UserTaskDto convertUserTaskToDto(UserTask userTask) {
        if (userTask == null) {
            return null;
        }
        UserTaskDto userTaskDto = new UserTaskDto();
        userTaskDto.setId(userTask.getId());
        userTaskDto.setTitle(userTask.getTitle());
        userTaskDto.setDescription(userTask.getDescription());
        userTaskDto.setStatus(userTask.getTaskStatus().getName());
        userTaskDto.setTaskPriority(userTask.getTaskPriority().getName());

        if (userTask.getUser() != null) {
            userTaskDto.setAssignedUser(userTask.getUser().getFullName());
        }
        userTaskDto.setDueDate(userTask.getDueDate());
        userTaskDto.setProgress(userTask.getProgress());
        userTaskDto.setCompletedOn(userTask.getCompletedOn());
        userTaskDto.setCreatedOn(userTask.getCreatedOn());

        return userTaskDto;
    }
}
