package com.linktera.demo.api;

import com.linktera.demo.service.CurrencyDefinitionDto;
import com.linktera.demo.service.HolidayRequestService;
import lombok.AllArgsConstructor;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Event;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class HolidayRequestAPI {

    private final HolidayRequestService holidayRequestService;


    @PostMapping("/deployDefinition")
    public ResponseEntity deploy(@RequestParam String bpmnFileName){
        return ResponseEntity.ok(holidayRequestService.deploy(bpmnFileName));
    }


    @PostMapping("/deploy")
    public ProcessInstanceResponse deploy(@RequestBody CurrencyDefinitionDto dto) {
        return holidayRequestService.deployXML(dto);
    }

    @PostMapping("/startProcess")
    public ProcessInstanceResponse startProcess(@RequestBody CurrencyDefinitionDto dto) {
        return holidayRequestService.startProcess(dto);
    }

    @GetMapping("/historic/{execId}")
    public List<HistoricProcessInstance> serv(@PathVariable String execId) {
        return holidayRequestService.ser(execId);
    }

    // çalışmıyor Json write.
    @GetMapping("/getTaskById/{execId}")
    public List<Task> getTasksByProcessInstanceId(@PathVariable String execId) {
        return holidayRequestService.getTasksByProcessInstanceId(execId);
    }

    @GetMapping("/getCurrencyDefinition/{id}")
    public CurrencyDefinitionDto getCurrencyDefinition(@PathVariable("id") String execId) {
        return holidayRequestService.getVariables(execId);
    }

    @GetMapping("/approvedCurrencyDefiniton/{id}")
    public void approvedCurrencyDefinition(@PathVariable("id") String execId) {
        holidayRequestService.approvedCurrencyDefiniton(execId);
    }


    @GetMapping("/rejectCurrencyDefiniton/{id}")
    public void rejectCurrencyDefinition(@PathVariable("id") String execId) {
        holidayRequestService.rejectCurrencyDefinition(execId);
    }

    @GetMapping("/getTask/{id}")
    public List<HistoricTaskInstance> getTaskWithId(@PathVariable("id") String execId) {
        return holidayRequestService.getTasks(execId);
    }

    @GetMapping("/getAllUnfinishedTasks")
    public List<HistoricTaskInstance> getAllUnfinishedTasks() {
        return holidayRequestService.getAllUnfinishedTasks();
    }

    @GetMapping("/getAllFinishedTasks")
    public List<HistoricTaskInstance> getAllFinishedTasks() {
        return holidayRequestService.getAllFinishedTasks();
    }

    @DeleteMapping("/{execId}")
    public void deleteTaskWithId(@PathVariable String execId){
        holidayRequestService.deleteAllTasks(execId);
    }

    @GetMapping("/userInvoledTasks")
    public List<HistoricTaskInstance> userInvolved(@RequestBody String userId) {
        return holidayRequestService.getTasksWithUserInvolved(userId);
    }

    @GetMapping("/getActiveProcesses")
    public List<ProcessInstanceResponse> ssss() {
        return holidayRequestService.getActiveProcesses();
    }


    // ACTIVE PROCESS LISTESINDEN CIKARIYOR.
    @GetMapping("/suspend/{execId}")
    public void suspendProcess(@PathVariable("execId") String execId) {
        holidayRequestService.suspendProcess(execId);
    }

    @GetMapping("event/{execId}")
    public List<Event> getEventsById(@PathVariable("execId") String execId) {
        return holidayRequestService.getEvents(execId);
    }



    // Getting process state
    @GetMapping("/getActiveProcesses/{execId}")
    public List<ProcessInstanceResponse> getProcessStateById(@PathVariable("execId") String execId) {
        return holidayRequestService.getProcessStateById(execId);
    }

    @GetMapping("/currentState/{execId}")
    public List<Execution> getCurrentState(@PathVariable("execId") String execId) {
        return holidayRequestService.pointerProcess(execId);
    }


}
