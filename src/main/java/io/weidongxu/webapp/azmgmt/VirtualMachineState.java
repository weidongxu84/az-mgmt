package io.weidongxu.webapp.azmgmt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.management.compute.PowerState;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class VirtualMachineState {

    @JsonProperty("powerState")
    private final PowerState powerState;
}
