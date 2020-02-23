package io.weidongxu.webapp.azmgmt;

import com.microsoft.azure.management.compute.PowerState;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VirtualMachineState {

    private final PowerState powerState;
}
