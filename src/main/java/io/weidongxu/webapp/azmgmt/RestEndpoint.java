package io.weidongxu.webapp.azmgmt;

import com.microsoft.azure.management.compute.PowerState;
import com.microsoft.azure.management.compute.VirtualMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestEndpoint {

    @Autowired
    private AzureManagement azureManagement;

    @GetMapping(
            value = "/vm/state",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    PowerState state(@RequestParam String secret) {
        checkAuthorization(secret);
        VirtualMachine vm = azureManagement.getClient().virtualMachines().getByResourceGroup("vmess", "vmess");
        return vm.powerState();
    }

    @GetMapping(
            value = "/vm/power",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    PowerState power(@RequestParam String op, @RequestParam String secret) {
        checkAuthorization(secret);
        VirtualMachine vm = azureManagement.getClient().virtualMachines().getByResourceGroup("vmess", "vmess");
        switch (op) {
            case "start":
                vm.start();
                break;
            case "stop":
                vm.powerOff();
                break;
            case "restart":
                vm.restart();
                break;
        }
        vm.refresh();
        return vm.powerState();
    }

    private void checkAuthorization(String secret) {
        if (!azureManagement.getSecret().equals(secret)) {
            throw new ForbiddenException();
        }
    }
}
