package com.linktera.demo.service;

import com.linktera.demo.exception.TaskNotFoundException;
import com.linktera.demo.model.CurrencyDefinitionDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.RuntimeServiceImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Event;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class HolidayRequestService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final RuntimeServiceImpl service;
    private final TaskService taskService;
    private final IdentityService identityService;
    private final HistoryService historyService;

    public Boolean deploy(String bpmnFileName) {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(bpmnFileName)
                .deploy();
        return true;
    }




    public ProcessInstanceResponse deployXML(CurrencyDefinitionDto dto) {

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        log.info("Found process definition : " + processDefinition.getName());

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entryUser", dto.getEntryUser());
        variables.put("roleList", dto.getRoleList());
        variables.put("dto", dto);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);


        Map<String, Object> variableList = runtimeService.getVariables(processInstance.getId());
        variableList.get("dto");

        ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();
        instanceResponse.setId(processInstance.getId());
        instanceResponse.setActivityId(processInstance.getActivityId());
        instanceResponse.setName(processInstance.getName());
        instanceResponse.setProcessDefinitionDescription(processInstance.getDescription());
        instanceResponse.setBusinessKey(processInstance.getBusinessKey());
        instanceResponse.setCallbackId(processInstance.getCallbackId());
        instanceResponse.setStartUserId(processInstance.getStartUserId());
        instanceResponse.setCompleted(processInstance.isEnded());
        instanceResponse.setEnded(processInstance.isEnded());
        instanceResponse.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        instanceResponse.setProcessDefinitionDescription(processInstance.getProcessDefinitionName());
        instanceResponse.setTenantId(processInstance.getTenantId());
        instanceResponse.setUrl(instanceResponse.getProcessDefinitionUrl());

        return instanceResponse;
    }

    public ProcessInstanceResponse startProcess(CurrencyDefinitionDto dto) {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entryUser", dto.getEntryUser());
        variables.put("roleList", dto.getRoleList());
        variables.put("dto", dto);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);


        Map<String, Object> variableList = runtimeService.getVariables(processInstance.getId());
        variableList.get("dto");

        ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();
        instanceResponse.setId(processInstance.getId());
        instanceResponse.setActivityId(processInstance.getActivityId());
        instanceResponse.setName(processInstance.getName());
        instanceResponse.setProcessDefinitionDescription(processInstance.getDescription());
        instanceResponse.setBusinessKey(processInstance.getBusinessKey());
        instanceResponse.setCallbackId(processInstance.getCallbackId());
        instanceResponse.setStartUserId(processInstance.getStartUserId());
        instanceResponse.setCompleted(processInstance.isEnded());
        instanceResponse.setEnded(processInstance.isEnded());
        instanceResponse.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        instanceResponse.setProcessDefinitionDescription(processInstance.getProcessDefinitionName());
        instanceResponse.setTenantId(processInstance.getTenantId());
        instanceResponse.setUrl(instanceResponse.getProcessDefinitionUrl());

        return instanceResponse;
    }


    public CurrencyDefinitionDto getVariables(String execId) {

        Map<String, Object> varList = runtimeService.getVariables(execId);
        return (CurrencyDefinitionDto) varList.get("dto");
    }

    public void approvedCurrencyDefiniton(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("approved", true);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("Task not found");
        }
    }

    public void rejectCurrencyDefinition(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("approved", false);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("Task not found");
        }
    }

    public List<HistoricTaskInstance> getTasks(String execId) {
        return
                historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(execId)
                        .unfinished().list();

    }

    public List<HistoricProcessInstance> ser(String execId) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(execId).list();
    }

    public List<Execution> pointerProcess(String execId) {
        List<Execution> executionQuery = runtimeService.createExecutionQuery()
                .processInstanceId(execId)
                .list();

        return executionQuery;
    }


    public List<Task> getTasksByProcessInstanceId(String execId) {
        return taskService.createTaskQuery()
                .processInstanceId(execId).list();
    }

    public List<HistoricTaskInstance> getAllUnfinishedTasks() {
        return historyService.createHistoricTaskInstanceQuery()
                .unfinished().list();
    }

    public List<HistoricTaskInstance> getAllFinishedTasks() {
        return historyService.createHistoricTaskInstanceQuery()
                .finished().list();
    }

    public List<HistoricTaskInstance> getTasksWithUserInvolved(String userId) {
        return historyService.createHistoricTaskInstanceQuery().taskInvolvedUser(userId).list();
    }

    public List<ProcessInstanceResponse> getActiveProcesses() {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().active().list();
        List<ProcessInstanceResponse> response = new ArrayList<>();
        ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();

        for (ProcessInstance instance : instances) {
            instanceResponse.setId(instance.getId());
            instanceResponse.setActivityId(instance.getActivityId());
            instanceResponse.setName(instance.getName());
            instanceResponse.setProcessDefinitionDescription(instance.getDescription());
            instanceResponse.setBusinessKey(instance.getBusinessKey());
            instanceResponse.setCallbackId(instance.getCallbackId());
            instanceResponse.setStartUserId(instance.getStartUserId());
            instanceResponse.setCompleted(instance.isEnded());
            instanceResponse.setEnded(instance.isEnded());
            instanceResponse.setProcessDefinitionId(instance.getProcessDefinitionId());
            instanceResponse.setProcessDefinitionDescription(instance.getProcessDefinitionName());
            instanceResponse.setTenantId(instance.getTenantId());
            instanceResponse.setUrl(instanceResponse.getProcessDefinitionUrl());
            response.add(instanceResponse);

        }

        return response;
    }

    public List<ProcessInstanceResponse> getProcessStateById(String execId) {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .processInstanceId(execId)
                .list();

        List<ProcessInstanceResponse> response = new ArrayList<>();
        ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();

        for (ProcessInstance instance : instances) {
            instanceResponse.setId(instance.getId());
            instanceResponse.setActivityId(instance.getActivityId());
            instanceResponse.setName(instance.getName());
            instanceResponse.setProcessDefinitionDescription(instance.getDescription());
            instanceResponse.setBusinessKey(instance.getBusinessKey());
            instanceResponse.setCallbackId(instance.getCallbackId());
            instanceResponse.setStartUserId(instance.getStartUserId());
            instanceResponse.setCompleted(instance.isEnded());
            instanceResponse.setEnded(instance.isEnded());
            instanceResponse.setProcessDefinitionId(instance.getProcessDefinitionId());
            instanceResponse.setProcessDefinitionDescription(instance.getProcessDefinitionName());
            instanceResponse.setTenantId(instance.getTenantId());
            instanceResponse.setUrl(instanceResponse.getProcessDefinitionUrl());
            response.add(instanceResponse);

        }

        return response;
    }


    public void suspendProcess(String execId) {
        runtimeService.suspendProcessInstanceById(execId);
    }

    public List<Event> getEvents(String execId) {
        /*
         *//**
         * The all events related to the given Process Instance.
         *//*
        List<Event> getProcessInstanceEvents(String processInstanceId);*/

        return runtimeService.getProcessInstanceEvents(execId);

    }


    public void deleteAllTasks(String execId) {
        runtimeService.deleteProcessInstance(execId, "just because i want it");
    }

    public void delete() {


    }


}





















