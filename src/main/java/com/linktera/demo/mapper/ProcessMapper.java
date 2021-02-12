package com.linktera.demo.mapper;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.stereotype.Component;

@Component
public class ProcessMapper {

    public ProcessInstanceResponse createProcessResponse(ProcessInstance processInstance){

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
}
