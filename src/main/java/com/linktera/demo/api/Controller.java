package com.linktera.demo.api;


import com.linktera.demo.model.CurrencyDefinitionDto;
import com.linktera.demo.model.GenericApprovalModel;
import com.linktera.demo.model.ProcessStarterModel;
import com.linktera.demo.service.ServiceLayer;
import lombok.AllArgsConstructor;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class Controller {

    @Autowired
    private ServiceLayer service;


    @GetMapping("/test")
    public Object test(@RequestBody GenericApprovalModel model) {
        return model;
    }

    @PostMapping("/deploy")
    public void deployBpmn20file(@RequestParam String bpmnFile) {
        service.deployProcessDefinition(bpmnFile);
    }

    @PostMapping("/startProcess")
    public ProcessInstanceResponse startProcessInstance(@RequestBody ProcessStarterModel starterModel) {
        return service.startProcess(starterModel);
    }

    @GetMapping("/getHistoricVar/{execId}")
    public Object getHistoricVars(@PathVariable("execId") String execId) {
        return service.getHistoricObject(execId);
    }

    @GetMapping("/getObject/{execId}")
    public Object getGenericObj(@PathVariable("execId") String execId) {
        return service.getObject(execId);
    }

    @GetMapping("/getCurrentState/{execId}")
    public String getCurrentState(@PathVariable("execId") String execId) {
        return service.getCurrentState(execId);
    }

    @PostMapping("/approve")
    public void approve(@RequestParam String execId,
                        @RequestParam String approveStep,
                        @RequestParam Boolean isApproved) {
        service.validatorService(execId, approveStep, isApproved);

    }

    @GetMapping("/getCurrencyDefinition/{id}")
    public CurrencyDefinitionDto getCurrencyDefinition(@PathVariable("id") String execId) {
        return service.getPayload(execId);
    }

}
