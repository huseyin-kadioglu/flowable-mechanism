package com.linktera.demo.api;

import com.linktera.demo.service.CurrencyDefinitionDto;
import com.linktera.demo.service.TwoValidatorsService;
import lombok.AllArgsConstructor;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TwoValidatorsAPI {

    private TwoValidatorsService twoValidatorsService;

    //TODO: Şu an deploy ettikten sonra process'i başlatıyor. Bu kısım ayrılacak.
    @PostMapping("/deploy")
    public ProcessInstanceResponse deploy(@RequestBody CurrencyDefinitionDto dto) {
        return twoValidatorsService.deployXML(dto);
    }

    // TODO: Sadece process'i başlatan ayrı bir servis yazılacak
    @PostMapping("/startProcess")
    public ProcessInstanceResponse startProcess(@RequestBody CurrencyDefinitionDto dto) {
        return twoValidatorsService.startProces(dto);
    }

    @GetMapping("/getCurrencyDefinition/{id}")
    public CurrencyDefinitionDto getCurrencyDefinition(@PathVariable("id") String execId) {
        return twoValidatorsService.getVariables(execId);
    }

    @PostMapping("acceptFirstApproval/{id}")
    public void acceptFirstApproval(@PathVariable("id") String execId) {
        twoValidatorsService.acceptFirstApproval(execId);
    }

    @PostMapping("rejectFirstApproval/{id}")
    public void rejectFirstApproval(@PathVariable("id") String execId) {
        twoValidatorsService.rejectFirstApproval(execId);
    }

    // TODO: task list indexinden dolayı secondlar çalışmayabilir.
    @PostMapping("acceptSecondApproval/{id}")
    public void acceptSecondApproval(@PathVariable("id") String execId) {
        twoValidatorsService.acceptSecondApproval(execId);
    }

    @PostMapping("rejectSecondApproval/{id}")
    public void rejectSecondApproval(@PathVariable("id") String execId) {
        twoValidatorsService.rejectSecondApproval(execId);
    }

}
