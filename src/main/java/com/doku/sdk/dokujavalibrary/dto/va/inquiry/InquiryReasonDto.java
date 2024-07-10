package com.doku.sdk.dokujavalibrary.dto.va.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryReasonDto {
    private String english;
    private String indonesia;
}
