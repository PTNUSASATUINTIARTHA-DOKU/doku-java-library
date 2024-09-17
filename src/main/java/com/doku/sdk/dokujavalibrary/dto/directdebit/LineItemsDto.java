package com.doku.sdk.dokujavalibrary.dto.directdebit;

import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.PatternValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItemsDto {
    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 32, groups = SizeValidation.class)
    private String name;
    @SafeString(groups = SafeStringValidation.class)
    @Pattern(regexp = "[0-9]{1,16}\\.[0-9]{2}", groups = PatternValidation.class, message = "invalid pattern")
    private String price;
    @SafeString(groups = SafeStringValidation.class)
    private String quantity;
}
