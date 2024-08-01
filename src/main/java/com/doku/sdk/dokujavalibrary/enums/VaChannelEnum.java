package com.doku.sdk.dokujavalibrary.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum VaChannelEnum {
    VIRTUAL_ACCOUNT_BCA("29", "VIRTUAL_ACCOUNT_BCA"),
    VIRTUAL_ACCOUNT_BANK_MANDIRI("08", "VIRTUAL_ACCOUNT_BANK_MANDIRI"),
    VIRTUAL_ACCOUNT_BANK_BRI("34", "VIRTUAL_ACCOUNT_BRI"),
    VIRTUAL_ACCOUNT_BANK_BNI("38", "VIRTUAL_ACCOUNT_BNI"),
    VIRTUAL_ACCOUNT_BANK_DANAMON("33", "VIRTUAL_ACCOUNT_BANK_DANAMON"),
    VIRTUAL_ACCOUNT_BANK_PERMATA("05", "VIRTUAL_ACCOUNT_BANK_PERMATA"),
    VIRTUAL_ACCOUNT_MAYBANK("44", "VIRTUAL_ACCOUNT_MAYBANK"),
    VIRTUAL_ACCOUNT_BNC("", "VIRTUAL_ACCOUNT_BNC"),
    VIRTUAL_ACCOUNT_BTN("43", "VIRTUAL_ACCOUNT_BTN"),
    VIRTUAL_ACCOUNT_BSI("", "VIRTUAL_ACCOUNT_BSI"),
    VIRTUAL_ACCOUNT_BANK_CIMB("32", "VIRTUAL_ACCOUNT_BANK_CIMB"),
    VIRTUAL_ACCOUNT_SINARMAS("22", "VIRTUAL_ACCOUNT_SINARMAS"),
    VIRTUAL_ACCOUNT_SINARMAS_FULL("21", "VIRTUAL_ACCOUNT_SINARMAS_FULL"),
    VIRTUAL_ACCOUNT_DOKU("47", "VIRTUAL_ACCOUNT_DOKU"),
    VIRTUAL_ACCOUNT_BSS("", "VIRTUAL_ACCOUNT_BSS"),
    VIRTUAL_ACCOUNT_QNB("42", "VIRTUAL_ACCOUNT_QNB");

    final String ocoChannelId;
    final String v2Channel;

    @Getter
    private static final Map<String, VaChannelEnum> lookup = new HashMap<>();

    static {
        for (VaChannelEnum s : EnumSet.allOf(VaChannelEnum.class)) {
            getLookup().put(s.v2Channel, s);
        }
    }

    public static VaChannelEnum findByV2Channel(String v2Channel){
        Predicate<VaChannelEnum> isEqual  = s -> Optional.ofNullable(s)
                .map(VaChannelEnum::getV2Channel)
                .map(v2Channel::equalsIgnoreCase)
                .orElse(false);

        return Stream.of(values())
                .filter(isEqual)
                .findAny()
                .orElse(null);
    }

}
