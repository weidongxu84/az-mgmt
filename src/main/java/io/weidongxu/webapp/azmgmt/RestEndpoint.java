package io.weidongxu.webapp.azmgmt;

import com.azure.core.util.Context;
import com.azure.resourcemanager.compute.fluent.VirtualMachinesClient;
import com.azure.resourcemanager.compute.models.VirtualMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.weidongxu.webapp.azmgmt.AzureResource.*;

@Slf4j
@RestController
public class RestEndpoint {

    @Autowired
    private AzureManagement azureManagement;

    @GetMapping(
            value = "/",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String ok() {
        return "";
    }

    @GetMapping(
            value = "/vm/state",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public VirtualMachineState state() {
        VirtualMachine vm = azureManagement.getClient().virtualMachines().getByResourceGroup(VM_RG, VM_NAME);
        return VirtualMachineState.builder()
                .powerState(vm.powerState().toString())
                .build();
    }

    @GetMapping(
            value = "/vm/power",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public VirtualMachineState power(@RequestParam String op) {
        VirtualMachinesClient clientVmInner = azureManagement.getClient().virtualMachines().manager().serviceClient().getVirtualMachines();
        switch (op) {
            case "on":
                clientVmInner.beginStart(VM_RG, VM_NAME);
                break;
            case "off":
                clientVmInner.beginPowerOff(VM_RG, VM_NAME, false, Context.NONE);
                break;
            case "reset":
                clientVmInner.beginRestart(VM_RG, VM_NAME);
                break;
            default:
                log.warn("Invalid operation `{}`.", op);
                break;
        }
        return state();
    }

    public static class StringAsJson {

    }
}
