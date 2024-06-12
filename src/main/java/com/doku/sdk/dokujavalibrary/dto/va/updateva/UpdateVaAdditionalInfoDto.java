package com.doku.sdk.dokujavalibrary.dto.va.updateva;

import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVaAdditionalInfoDto {

    @SafeString(groups = SafeStringValidation.class)
    private String channel;
    private UpdateVaVirtualAccountConfigDto updateVaVirtualAccountConfig;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateVaVirtualAccountConfigDto {

        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String status;
    }
}
