package io.weidongxu.webapp.azmgmt;

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
            value = "/vm",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    List<VirtualMachine> listVirtualMachines(@RequestParam String secret) {
        if (azureManagement.getSecret().equals(secret)) {
            return azureManagement.getClient().virtualMachines().listByResourceGroup("vmess");
        } else {
            throw new ForbiddenException();
        }
    }
}
