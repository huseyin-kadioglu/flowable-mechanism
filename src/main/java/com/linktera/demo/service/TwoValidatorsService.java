package com.linktera.demo.service;

import com.linktera.demo.exception.TaskNotFoundException;
import com.linktera.demo.mapper.ProcessMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TwoValidatorsService {

    private final ProcessMapper mapper;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public ProcessInstanceResponse deployXML(CurrencyDefinitionDto dto) {

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("two-validators.bpmn20.xml")
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
                runtimeService.startProcessInstanceByKey("two-validators", variables);


        Map<String, Object> variableList = runtimeService.getVariables(processInstance.getId());
        variableList.get("dto");

        return mapper.createProcessResponse(processInstance);
    }


    public CurrencyDefinitionDto getVariables(String execId) {

        Map<String, Object> varList = runtimeService.getVariables(execId);
        return (CurrencyDefinitionDto) varList.get("dto");
    }


    public void acceptFirstApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("first_approval", true);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("accept first -Task not found");
        }
    }

    public void rejectFirstApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("first_approval", false);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("reject first- Task not found");
        }
    }

    public void acceptSecondApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("second_approval", true);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("accept second- Task  not found");
        }
    }

    public void rejectSecondApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("second_approval", false);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("reject second - Task not found");
        }
    }


    public ProcessInstanceResponse startProces(CurrencyDefinitionDto dto) {
    return null;
    }
}
