package com.linktera.demo.service;


import com.linktera.demo.exception.TaskNotFoundException;
import com.linktera.demo.mapper.ProcessMapper;
import com.linktera.demo.model.CurrencyDefinitionDto;
import com.linktera.demo.model.ProcessStarterModel;
import com.linktera.demo.model.ValidatorLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
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
public class ServiceLayer {

    static Map<String, String> validatorMap = new HashMap<>();

    static {
        validatorMap.put("validator1", "1");
        validatorMap.put("validator2", "2");
        validatorMap.put("validator3", "3");
        validatorMap.put("validator4", "4");
        validatorMap.put("validator5", "5");

    }

    private final ProcessMapper mapper;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;

    public void deployProcessDefinition(String bpmnFile) {
        repositoryService.createDeployment()
                .addClasspathResource("processes/" + bpmnFile)
                .deploy();
    }

    public ProcessInstanceResponse startProcess(ProcessStarterModel starterModel) {
        try {
            String processDefinitionKey = null;
            if (starterModel.getLevel().equals("1")) {
                processDefinitionKey = ValidatorLevel.LEVEL1.getLevel();
            } else if (starterModel.getLevel().equals("2")) {
                processDefinitionKey = ValidatorLevel.LEVEL2.getLevel();
            } else if (starterModel.getLevel().equals("3")) {
                processDefinitionKey = ValidatorLevel.LEVEL3.getLevel();
            } else if (starterModel.getLevel().equals("4")) {
                processDefinitionKey = ValidatorLevel.LEVEL4.getLevel();
            } else if (starterModel.getLevel().equals("5")) {
                processDefinitionKey = ValidatorLevel.LEVEL5.getLevel();
            }

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("type", starterModel.getModel().getType());
            variables.put("payload", starterModel.getModel().getPayload());
            variables.put("whole", starterModel.getModel());
            variables.put("screen-url", null);
            variables.put("approverList", new ArrayList<String>());
            ProcessInstance processInstance =
                    runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
            return mapper.createProcessResponse(processInstance);
        } catch (Exception e) {
            throw new TaskNotFoundException("Process Definition key not found");
        }
    }


    public Object getObject(String execId) {
        Map<String, Object> varList = runtimeService.getVariables(execId);
        Object var = varList.get("whole");
        return var;
    }

    public Object getHistoricObject(String execId) {
        List<HistoricTaskInstance> historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .processInstanceId(execId).list();

        HistoricTaskInstance historicTaskInstance = historicTaskInstanceQuery.get(0);
        Map<String, Object> processVariables = historicTaskInstance.getProcessVariables();
        return processVariables.get("whole");
    }

    public void checkAuthorization(String execId) {

        String approver = "ApproverName"; // todo: auth'tan alınacak.
        String currentState = getCurrentState(execId);// Step'i gösteriyor.
        Map<String, Object> varList = runtimeService.getVariables(execId);
        Object var = varList.get("Screen_url");
        // screen-url ve currentState ile rol listesi getirecek.
        List<String> roleList = new ArrayList<>(); // todo: Burada liste luap db'den gelecek.
        if (!roleList.contains(approver)) {
            // todo: approver onaylayamaz UNAUTHORIZED EXCEPTION FIRLAT.
        }

        // todo: onaylayabilme yetkisi var; devam edecek.
    }

    public CurrencyDefinitionDto getPayload(String execId) {
        Map<String, Object> varList = runtimeService.getVariables(execId);
        return (CurrencyDefinitionDto) varList.get("payload");
    }


    //role list gelsin
    public void validatorService(String execId, String approveStep, Boolean isApproved) {


        String processVariable = null;
        if (approveStep.equals("1")) {
            processVariable = ValidatorLevel.LEVEL1.getProcessVariableName();
        } else if (approveStep.equals("2")) {
            processVariable = ValidatorLevel.LEVEL2.getProcessVariableName();
        } else if (approveStep.equals("3")) {
            processVariable = ValidatorLevel.LEVEL3.getProcessVariableName();
        } else if (approveStep.equals("4")) {
            processVariable = ValidatorLevel.LEVEL4.getProcessVariableName();
        } else if (approveStep.equals("5")) {
            processVariable = ValidatorLevel.LEVEL5.getProcessVariableName();
        }

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
        Map<String, Object> variables = new HashMap<>();
        variables.put(processVariable, isApproved);
        taskService.complete(tasks.get(0).getId(), variables);
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

    public void acceptThirdApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("third_approval", true);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("accept third -Task not found");
        }
    }

    public void rejectThirdApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("third_approval", false);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("reject third -Task not found");
        }
    }

    public void acceptFourthApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("fourth_approval", true);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("accept fourth_approval -Task not found");
        }
    }

    public void rejectFourthApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("fourth_approval", false);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("reject fourth_approval- Task not found");
        }
    }

    public void acceptFifthApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("fifth_approval", true);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("accept third -Task not found");
        }
    }

    public void rejectFifthApproval(String execId) {
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("fifth_approval", false);
            taskService.complete(tasks.get(0).getId(), variables);
        } catch (Exception e) {
            log.error(" Task not found  :" + e);
            throw new TaskNotFoundException("reject fifth- Task not found");
        }
    }

    public String getCurrentState(String execId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(execId).list();
        Task task = tasks.get(0);
        String response = validatorMap.get(task.getTaskDefinitionKey());
        return response;
    }

}
