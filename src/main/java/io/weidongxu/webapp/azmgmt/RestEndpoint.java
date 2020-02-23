package io.weidongxu.webapp.azmgmt;

import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.compute.implementation.VirtualMachinesInner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static io.weidongxu.webapp.azmgmt.AzureResource.*;

@RestController
public class RestEndpoint {

    @Autowired
    private AzureManagement azureManagement;

    @GetMapping(
            value = "/vm/state",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String state(@RequestParam String secret) {
        checkAuthorization(secret);

        VirtualMachine vm = azureManagement.getClient().virtualMachines().getByResourceGroup(VM_RG, VM_NAME);
        return vm.powerState().toString();
    }

    @GetMapping(
            value = "/vm/power",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String power(@RequestParam String op, @RequestParam String secret) {
        checkAuthorization(secret);

        VirtualMachinesInner clientVmInner = azureManagement.getClient().virtualMachines().manager().inner().virtualMachines();
        switch (op) {
            case "on":
                clientVmInner.beginStart(VM_RG, VM_NAME);
                break;
            case "off":
                clientVmInner.beginPowerOff(VM_RG, VM_NAME);
                break;
            case "reset":
                clientVmInner.beginRestart(VM_RG, VM_NAME);
                break;
        }
        return state(secret);
    }

    private void checkAuthorization(String secret) {
        if (!azureManagement.getSecret().equals(secret)) {
            throw new ForbiddenException("Not authorized.");
        }
    }
}
