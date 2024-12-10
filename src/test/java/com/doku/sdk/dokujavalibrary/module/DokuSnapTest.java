package com.doku.sdk.dokujavalibrary.module;

import java.util.Collections;

import com.doku.sdk.dokujavalibrary.controller.DirectDebitController;
import com.doku.sdk.dokujavalibrary.controller.NotificationController;
import com.doku.sdk.dokujavalibrary.controller.TokenController;
import com.doku.sdk.dokujavalibrary.controller.VaController;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;

@ExtendWith(MockitoExtension.class)
class DokuSnapTest extends TestUtil {

    @InjectMocks
    private DokuSnap dokuSnap;
    @Mock
    private TokenController tokenController;
    @Mock
    private VaController vaController;
    @Mock
    private DirectDebitController directDebitController;
    @Mock
    private NotificationController notificationController;

    private static final Logger logger = LoggerFactory.getLogger(DokuSnapTest.class);

    @BeforeEach
    void setup() {
        dokuSnap = new DokuSnap(tokenController, vaController, directDebitController, notificationController);
        ReflectionTestUtils.setField(dokuSnap, "tokenB2bExpiresIn", 900);
        ReflectionTestUtils.setField(dokuSnap, "tokenB2bGeneratedTimestamp", 900);
        ReflectionTestUtils.setField(dokuSnap, "tokenB2b2cExpiresIn", 900);
        ReflectionTestUtils.setField(dokuSnap, "tokenB2b2cGeneratedTimestamp", 900);
    }

    @Test
    void getB2bToken_Success() {
        when(tokenController.getTokenB2B(any(), any(), any())).thenReturn(getTokenB2BResponseDto("2007300"));
        var response = dokuSnap.getB2bToken(PRIVATE_KEY, CLIENT_ID, false);

        assertEquals("2007300", response.getResponseCode());
    }

    @Test
    void getB2bToken_ClientIdInvalid() {
        when(tokenController.getTokenB2B(any(), any(), any())).thenReturn(getTokenB2BResponseDto("5007300"));
        var response = dokuSnap.getB2bToken(PRIVATE_KEY, null, false);

        assertEquals("5007300", response.getResponseCode());
    }

    @Test
    void createVa_Success() {
        when(vaController.createVa(any(), any(), any(), any(), any())).thenReturn(getCreateVaResponseDto());
        var response = dokuSnap.createVa(getCreateVaRequestDto(), PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("2002700", response.getResponseCode());
    }

    @Test
    void createVa_PartnerIdNull() {
        var request = getCreateVaRequestDto();
        request.setPartnerServiceId(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_PartnerIdLengthIsNot8Digits() {
        var request = getCreateVaRequestDto();
        request.setPartnerServiceId("123456789");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_PartnerIdLengthIsNotNumerical() {
        var request = getCreateVaRequestDto();
        request.setPartnerServiceId("1234567z");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_CustomerNoIsMoreThan20() {
        var request = getCreateVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_CustomerNoIsNotNumerical() {
        var request = getCreateVaRequestDto();
        request.setCustomerNo("123456789z");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNoIsNull() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNoIsNotValid() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountNo("    189920240704002");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNameIsNull() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountName(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNameIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountName("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNameIsMoreThan255() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountName("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountEmailIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountEmail("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountEmailIsMoreThan255() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountEmail("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountEmailIsInvalid() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountEmail("sdk@emailcom");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountPhoneIsLessThan9() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountPhone("12345678");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountPhoneIsMoreThan30() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountPhone("1234567890123456789012345678901");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_TrxIdIsNull() {
        var request = getCreateVaRequestDto();
        request.setTrxId(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_TrxIdIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.setTrxId("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_TrxIdIsMoreThan64() {
        var request = getCreateVaRequestDto();
        request.setTrxId("FcGcsrqYNNQotmv7b2dSFdVbUmiexl0s1wE7H23gpXsFzcXUHXXnRLBUuREMuWxVx");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_ValueIsNull() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setValue(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_ValueIsLessThan4() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setValue("100");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_ValueIsMoreThan19() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setValue("12345678901234567890");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_CurrencyIsNot3Characters() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setCurrency("ID");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_CurrencyIsNotIDR() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setCurrency("USD");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_ChannelIsNull() {
        var request = getCreateVaRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_ChannelIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.getAdditionalInfo().setChannel("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_ChannelIsMoreThan30() {
        var request = getCreateVaRequestDto();
        request.getAdditionalInfo().setChannel("VIRTUAL_ACCOUNT_BANK_MANDIRI_TEST");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_ChannelIsNotValidChannel() {
        var request = getCreateVaRequestDto();
        request.getAdditionalInfo().setChannel("5Vl3mjMJpA6NuUNHWrucSymfjlWPCb");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountTrxIsNull() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountTrxType(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountTrxIsNot1Digit() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountTrxType("CC");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountTrxIsInvalid() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountTrxType("A");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_ExpiredDateInvalidFormat() {
        var request = getCreateVaRequestDto();
        request.setExpiredDate("2024-07-11");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002700", response.getResponseCode());
    }

    @Test
    void updateVa_Success() {
        when(vaController.doUpdateVa(any(), any(), any(), any(), any())).thenReturn(getUpdateVaResponseDto());
        var response = dokuSnap.updateVa(getUpdateVaRequestDto(), PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("2002800", response.getResponseCode());
    }

    @Test
    void updateVa_PartnerServiceIdNull() {
        var request = getUpdateVaRequestDto();
        request.setPartnerServiceId(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_PartnerServiceIdIsNot8Digits() {
        var request = getUpdateVaRequestDto();
        request.setPartnerServiceId("123456789");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_PartnerServiceIdIsNotNumerical() {
        var request = getUpdateVaRequestDto();
        request.setPartnerServiceId("1234567z");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_CustomerNoIsMoreThan20() {
        var request = getUpdateVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_CustomerNoIsNotNumerical() {
        var request = getUpdateVaRequestDto();
        request.setCustomerNo("123456789z");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountNoIsNull() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountNoIsNotValid() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountNo("    1899000000000651");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountNameIsMoreThan255() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountName("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountEmailIsMoreThan255() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountEmail("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd@email.com");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountEmailIsInvalidFormat() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountEmail("sdk@emailcom");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountPhoneIsLessThan9() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountPhone("12345678");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountPhoneIsMoreThan30() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountPhone("1234567890123456789012345678901");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_TrxIdIsNull() {
        var request = getUpdateVaRequestDto();
        request.setTrxId(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_TrxIdIsLessThan1() {
        var request = getUpdateVaRequestDto();
        request.setTrxId("");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_TrxIdIsMoreThan64() {
        var request = getUpdateVaRequestDto();
        request.setTrxId("PXXT0awNv2lE3Jq6dYT1Vc4cNcdEMxbrtxDtAIzFAzieO7K9kRL2Wpenh6mBxu1Sn");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_ValueIsNull() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setValue(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_ValueIsLessThan4() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setValue("100");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_ValueIsMoreThan19() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setValue("12345678901234567890");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_CurrencyIsNot3Characters() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setCurrency("ID");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_CurrencyIsNotIDR() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setCurrency("USD");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_ChannelIsNull() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_ChannelIsLessThan1() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().setChannel("");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_ChannelIsMoreThan30() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().setChannel("VIRTUAL_ACCOUNT_BANK_MANDIRI_TEST");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_ChannelIsInvalid() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().setChannel("VIRTUAL_ACCOUNT_BANK");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_StatusIsNull() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().getVirtualAccountConfig().setStatus(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_StatusIsLessThan1() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().getVirtualAccountConfig().setStatus("");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_StatusIsMoreThan20() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().getVirtualAccountConfig().setStatus("9KtgA3YWn1aDEEGE1gWZp");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_StatusIsInvalid() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().getVirtualAccountConfig().setStatus("CLOSED");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountTrxTypeIsNull() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountTrxType(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountTrxTypeIsNot1Digit() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountTrxType("12");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountTrxTypeIsInvalid() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountTrxType("A");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_ExpiredDateIsInvalid() {
        var request = getUpdateVaRequestDto();
        request.setExpiredDate("2024-07-11");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002800", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_Success() {
        when(vaController.doDeletePaymentCode(any(), any(), any(), any(), any())).thenReturn(getDeleteVaResponseDto());
        var response = dokuSnap.deletePaymentCode(getDeleteVaRequestDto(), PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("2003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_PartnerServiceIdIsNull() {
        var request = getDeleteVaRequestDto();
        request.setPartnerServiceId(null);
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_PartnerServiceIdIsNot8Digits() {
        var request = getDeleteVaRequestDto();
        request.setPartnerServiceId("123456789");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_CustomerNoIsMoreThan20() {
        var request = getDeleteVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_CustomerNoIsNotNumeric() {
        var request = getDeleteVaRequestDto();
        request.setCustomerNo("1234567z");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_VirtualAccountNoIsNull() {
        var request = getDeleteVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_VirtualAccountNoIsInvalid() {
        var request = getDeleteVaRequestDto();
        request.setVirtualAccountNo("    189920240704000");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_TrxIdIsNull() {
        var request = getDeleteVaRequestDto();
        request.setTrxId(null);
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_TrxIdIsLessThan1() {
        var request = getDeleteVaRequestDto();
        request.setTrxId("");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_TrxIdIsMoreThan128() {
        var request = getDeleteVaRequestDto();
        request.setTrxId("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_ChannelIsNull() {
        var request = getDeleteVaRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_ChannelIsLessThan1() {
        var request = getDeleteVaRequestDto();
        request.getAdditionalInfo().setChannel("");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_ChannelIsMoreThan128() {
        var request = getDeleteVaRequestDto();
        request.getAdditionalInfo().setChannel("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5003100", response.getResponseCode());
    }


    @Test
    void checkStatusVa_Success() {
        when(vaController.doCheckStatusVa(any(), any(), any(), any(), any())).thenReturn(getCheckStatusVaResponseDto());
        var response = dokuSnap.checkStatusVa(getCheckStatusVaRequestDto(), PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("2002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PartnerServiceIdIsNull() {
        var request = getCheckStatusVaRequestDto();
        request.setPartnerServiceId(null);
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PartnerServiceIdIsNot8Digits() {
        var request = getCheckStatusVaRequestDto();
        request.setPartnerServiceId("1234567");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PartnerServiceIdIsNotNumeric() {
        var request = getCheckStatusVaRequestDto();
        request.setPartnerServiceId("1234567z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_CustomerNoIsMoreThan20() {
        var request = getCheckStatusVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_CustomerNoIsNotNumeric() {
        var request = getCheckStatusVaRequestDto();
        request.setCustomerNo("1234567z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_VirtualAccountNoIsNull() {
        var request = getCheckStatusVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_VirtualAccountNoIsInvalid() {
        var request = getCheckStatusVaRequestDto();
        request.setVirtualAccountNo("    1899000000000660");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002601", response.getResponseCode());
    }

    @Test
    void checkStatusVa_InquiryRequestIdIsMoreThan128() {
        var request = getCheckStatusVaRequestDto();
        request.setInquiryRequestId("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PaymentRequestIdIsMoreThan128() {
        var request = getCheckStatusVaRequestDto();
        request.setPaymentRequestId("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("5002600", response.getResponseCode());
    }

    @Test
    void directDebitAccountBinding_Success() {
        when(directDebitController.doAccountBinding(any(), any(), any(), any(), any(), any(), any())).thenReturn(getAccountBindingResponseDto());
        var response = dokuSnap.doAccountBinding(getAccountBindingRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);

        assertEquals("2000700", response.getResponseCode());
    }

    @Test
    void directDebitAccountBinding_Failed() {
        var request = getAccountBindingRequestDto();
        request.setPhoneNo(null);
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);

        assertEquals("5000700", response.getResponseCode());
    }

    @Test
    void directDebitAccountUnbinding_Success() {
        when(directDebitController.doAccountUnbinding(any(), any(), any(), any(), any(), any())).thenReturn(getAccountUnbindingResponseDto());
        var response = dokuSnap.doAccountUnbinding(getAccountUnbindingRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, IP_ADDRESS);

        assertEquals("2000900", response.getResponseCode());
    }

    @Test
    void directDebitAccountUnbinding_Failed() {
        var request = getAccountUnbindingRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doAccountUnbinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, IP_ADDRESS);

        assertEquals("5000900", response.getResponseCode());
    }

    @Test
    void directDebitCardRegistration_Success() {
        when(directDebitController.doCardRegistration(any(), any(), any(), any(), any(), any())).thenReturn(getCardRegistrationResponseDto());
        var response = dokuSnap.doCardRegistration(getCardRegistrationRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP",false);

        assertEquals("2000100", response.getResponseCode());
    }

    @Test
    void directDebitCardRegistration_Failed() {
        var request = getCardRegistrationRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);

        assertEquals("5000100", response.getResponseCode());
    }

    @Test
    void directDebitCardUnbinding_Success() {
        when(directDebitController.doCardUnbinding(any(), any(), any(), any(), any())).thenReturn(getCardUnbindingResponseDto());
        var response = dokuSnap.doCardUnbinding(getCardUnbindingRequestDto(), PRIVATE_KEY, CLIENT_ID, false);

        assertEquals("2000500", response.getResponseCode());
    }

    @Test
    void directDebitCardUnbinding_Failed() {
        var request = getCardUnbindingRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doCardUnbinding(request, PRIVATE_KEY, CLIENT_ID, false);

        assertEquals("5000500", response.getResponseCode());
    }

    @Test
    void directDebitPayment_Success() {
        when(directDebitController.doPayment(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(getPaymentResponseDto());
        var response = dokuSnap.doPayment(getPaymentRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", "authCode", false);

        assertEquals("2005400", response.getResponseCode());
    }

    @Test
    void directDebitPayment_Failed() {
        var request = getPaymentRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_BRI_SNAP", "authCode", false);

        assertEquals("5005400", response.getResponseCode());
    }

    @Test
    void directDebitPaymentJumpApp_Success() {
        when(directDebitController.doPaymentJumpApp(any(), any(), any(), any(), any(), any(), any())).thenReturn(getPaymentJumpAppResponseDto());
        var response = dokuSnap.doPaymentJumpApp(getPaymentJumpAppRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);

        assertEquals("2005400", response.getResponseCode());
    }

    @Test
    void directDebitPaymentJumpApp_Failed() {
        var request = getPaymentJumpAppRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doPaymentJumpApp(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);

        assertEquals("5005400", response.getResponseCode());
    }

    @Test
    void directDebitBalanceInquiry_Success() {
        when(directDebitController.doBalanceInquiry(any(), any(), any(), any(), any(), any(), any())).thenReturn(getBalanceInquiryResponseDto());
        var response = dokuSnap.doBalanceInquiry(getBalanceInquiryRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);

        assertEquals("2001100", response.getResponseCode());
    }

    @Test
    void directDebitBalanceInquiry_Failed() {
        var request = getBalanceInquiryRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doBalanceInquiry(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);

        assertEquals("5001100", response.getResponseCode());
    }

    @Test
    void directDebitRefund_Success() {
        when(directDebitController.doRefund(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(getRefundResponseDto());
        var response = dokuSnap.doRefund(getRefundRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false,"test");

        assertEquals("2005800", response.getResponseCode());
    }

    @Test
    void directDebitRefund_Failed() {
        var request = getRefundRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false, "test");

        assertEquals("5000700", response.getResponseCode());
    }

    @Test
    void directDebitCheckStatus_Success() {
        when(directDebitController.doCheckStatus(any(), any(), any(), any(), any())).thenReturn(getCheckStatusResponseDto());
        var response = dokuSnap.doCheckStatus(getCheckStatusRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false);

        assertEquals("2005500", response.getResponseCode());
    }

    @Test
    void directDebitCheckStatus_Failed() {
        var request = getCheckStatusRequestDto();
        request.setServiceCode(null);
        var response = dokuSnap.doCheckStatus(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false);

        assertEquals("5005500", response.getResponseCode());
    }

    @Test
    void directDebitAccountBinding_PhoneNoCannotBeNull() {
    

        var request = getAccountBindingRequestDto();
        request.setPhoneNo(null);
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("phoneNo cannot be null. Please provide a phoneNo. Example: '62813941306101'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_PhoneNoCannotLessThan9() {
    

        var request = getAccountBindingRequestDto();
        request.setPhoneNo("121");
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("phoneNo must be at least 9 digits. Ensure that phoneNo is not empty. Example: '62813941306101'.", response.getResponseMessage());
    }
    @Test
    void directDebitAccountBinding_PhoneNoCannotMoreThan16() {
    

        var request = getAccountBindingRequestDto();
        request.setPhoneNo("6281223948928374983749328");
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("phoneNo must be 16 characters or fewer. Ensure that phoneNo is no longer than 16 characters. Example: '62813941306101'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_CustomerIdMerchantCannotBeNull() {
    

        var request = getAccountBindingRequestDto();
        request.getAdditionalInfo().setCustIdMerchant(null);
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("additionalInfo.custIdMerchant cannot be null or empty. Please provide a additionalInfo.custIdMerchant. Example: 'cust-001'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_CustomerIdMerchantCannotMoreThan16() {
    

        var request = getAccountBindingRequestDto();
        request.getAdditionalInfo().setCustIdMerchant("18726387298374982739487398475983479587394857938475984576984579687459867498576987239487298374982739482793847928344682736487163847234234");
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("additionalInfo.custIdMerchant must be 64 characters or fewer. Ensure that additionalInfo.custIdMerchant is no longer than 64 characters. Example: 'cust-001'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_SuccessRegistrationUrlCannotBeNull() {
        var request = getAccountBindingRequestDto();
        request.getAdditionalInfo().setSuccessRegistrationUrl(null); // Set successRegistrationUrl to null
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("additionalInfo.successRegistrationUrl cannot be null. Please provide a additionalInfo.successRegistrationUrl. Example: 'https://www.doku.com'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_FailedRegistrationUrlCannotBeNull() {
        var request = getAccountBindingRequestDto();
        request.getAdditionalInfo().setFailedRegistrationUrl(null); // Set failedRegistrationUrl to null
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("additionalInfo.failedRegistrationUrl cannot be null. Please provide a additionalInfo.failedRegistrationUrl. Example: 'https://www.doku.com'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_ChannelCannotBeNull() {
        var request = getAccountBindingRequestDto();
        request.getAdditionalInfo().setChannel(null); // Set channel to null
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("additionalInfo.channel cannot be null. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.", response.getResponseMessage());
    }

    @Test
    void directDebitAccountBinding_ChannelCannotOutsideEnum() {
        var request = getAccountBindingRequestDto();
        request.getAdditionalInfo().setChannel("bri"); // Set channel to null
        var response = dokuSnap.doAccountBinding(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false, DEVICE_ID, IP_ADDRESS);
        assertEquals("additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.", response.getResponseMessage());
    }

    @Test
    void directDebitRefund_ChannelCannotBeNull() {
        var request = getRefundRequestDto();
        request.getAdditionalInfo().setChannel(null); // Set channel to null
        var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false,"test");
        assertEquals("Invalid Mandatory Field additionalInfo.channel", response.getResponseMessage());
    }

    @Test
    void directDebitRefund_ChannelNotInEnum() {
        var request = getRefundRequestDto();
        request.getAdditionalInfo().setChannel("bri"); // Set channel to null
        var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false,"test");
        assertEquals("additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.", response.getResponseMessage());
    }

    @Test
    void directDebitRefund_originalPartnerReferenceNoCannotBeNuLL() {
        var request = getRefundRequestDto();
        request.setOriginalPartnerReferenceNo(null); // Set channel to null
        var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false,"test");
        assertEquals("Invalid Mandatory Field originalPartnerReferenceNo", response.getResponseMessage());
    }

    @Test
void directDebitRefund_originalPartnerReferenceNoMustBeBetween32And64Characters_ForAlloSnap() {
    var request = getRefundRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name());
    request.setOriginalPartnerReferenceNo("12345"); // Less than 32 characters

    var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_ALLO_SNAP", false,"test");
    assertEquals("originalPartnerReferenceNo must be 64 characters and at least 32 characters. Ensure that originalPartnerReferenceNo is no longer than 64 characters and at least 32 characters. Example: 'INV-REF-001'.", response.getResponseMessage());
}

@Test
void directDebitRefund_originalPartnerReferenceNoMustBeMax12Characters_ForCimbSnapOrBriSnap() {
    var request = getRefundRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name());
    request.setOriginalPartnerReferenceNo("1234567890123"); // More than 12 characters

    var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_CIMB_SNAP", false,"test");
    assertEquals("originalPartnerReferenceNo max 12 characters. Ensure that originalPartnerReferenceNo is no longer than 12 characters. Example: 'INV-001'.", response.getResponseMessage());
}

@Test
void directDebitRefund_originalPartnerReferenceNoMustBeMax64Characters_ForEmoneyShopeeOrDana() {
    var request = getRefundRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.EMONEY_SHOPEE_PAY_SNAP.name());
    request.setOriginalPartnerReferenceNo("1234567890123456789012345678901234567890123456789012345678901234567890"); // More than 64 characters

    var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_SHOPEE_PAY_SNAP", false,"test");
    assertEquals("originalPartnerReferenceNo must be 64 characters or fewer. Ensure that originalPartnerReferenceNo is no longer than 64 characters. Example: 'INV-001'.", response.getResponseMessage());
}

@Test
void directDebitRefund_partnerRefundNoMustBeMax64Characters_ForEmoneyChannels() {
    var request = getRefundRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.EMONEY_SHOPEE_PAY_SNAP.name());
    request.setPartnerRefundNo("1234567890123456789012345678901234567890123456789012345678901234567890"); // More than 64 characters

    var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_SHOPEE_PAY_SNAP", false,"test");
    assertEquals("partnerRefundNo must be 64 characters or fewer. Ensure that partnerRefundNo is no longer than 64 characters. Example: 'INV-REF-001'.", response.getResponseMessage());
}

@Test
void directDebitRefund_partnerRefundNoMustBeMax12Characters_ForCimbBriOrAlloSnap() {
    var request = getRefundRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name());
    request.setPartnerRefundNo("1234567890123"); // More than 12 characters

    var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_CIMB_SNAP", false,"test");
    assertEquals("partnerRefundNo must be 12 characters or fewer. Ensure that partnerRefundNo is no longer than 12 characters. Example: 'INV-REF-001'.", response.getResponseMessage());
}

@Test
void directDebitPayment_ChannelCannotBeNull() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(null);

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_BRI_SNAP", "authCode", false);
    assertEquals("Invalid Mandatory Field additionalInfo.channel", response.getResponseMessage());
}

@Test
void paymentRequest_ChannelMustBeValid() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel("INVALID_CHANNEL");

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "INVALID_CHANNEL", "authCode", false);
    assertEquals("additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.", response.getResponseMessage());
}

@Test
void paymentRequest_FeeTypeMustBeValid_ForEmoneyOvoSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.EMONEY_OVO_SNAP.name());
    request.setFeeType("INVALID_FEE_TYPE"); // Invalid fee type

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", "authCode", false);
    assertEquals("Value can only be OUR/BEN/SHA for EMONEY_OVO_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_PayOptionDetailsCannotBeEmpty_ForEmoneyOvoSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.EMONEY_OVO_SNAP.name());
    request.setPayOptionDetails(Collections.emptyList()); // Empty pay option details

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", "authCode", false);
    assertEquals("Pay Option Details cannot be empty for EMONEY_OVO_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_PaymentTypeMustBeValid_ForEmoneyOvoSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.EMONEY_OVO_SNAP.name());
    request.getAdditionalInfo().setPaymentType("INVALID_TYPE"); // Invalid payment type

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", "authCode", false);
    assertEquals("additionalInfo.paymentType cannot be empty for EMONEY_OVO_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_LineItemsCannotBeEmpty_ForDirectDebitAlloSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name());
    request.getAdditionalInfo().setLineItems(Collections.emptyList()); // Empty line items

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_ALLO_SNAP", "authCode", false);
    assertEquals("additionalInfo.lineItems cannot be empty for DIRECT_DEBIT_ALLO_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_RemarksCannotBeEmpty_ForDirectDebitAlloSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name());
    request.getAdditionalInfo().setRemarks(""); // Empty remarks

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_ALLO_SNAP", "authCode", false);
    assertEquals("additionalInfo.remarks cannot be empty for DIRECT_DEBIT_ALLO_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_RemarksCannotBeEmpty_ForDirectDebitCimbSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name());
    request.getAdditionalInfo().setRemarks(""); // Empty remarks

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_CIMB_SNAP", "authCode", false);
    assertEquals("additionalInfo.remarks cannot be empty for DIRECT_DEBIT_CIMB_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_PaymentTypeMustBeValid_ForDirectDebitBriSnap() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setChannel(DirectDebitChannelEnum.DIRECT_DEBIT_BRI_SNAP.name());
    request.getAdditionalInfo().setPaymentType("INVALID_TYPE"); // Invalid payment type

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_BRI_SNAP", "authCode", false);
    assertEquals("additionalInfo.paymentType cannot be empty for DIRECT_DEBIT_BRI_SNAP", response.getResponseMessage());
}

@Test
void paymentRequest_SuccessPaymentUrlCannotBeNull() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setSuccessPaymentUrl(null); // Set successPaymentUrl to null

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_BRI_SNAP", "authCode", false);
    assertEquals("Invalid Mandatory Field additionalInfo.successPaymentUrl", response.getResponseMessage());
}

@Test
void paymentRequest_FailedPaymentUrlCannotBeNull() {
    var request = getPaymentRequestDto();
    request.getAdditionalInfo().setFailedPaymentUrl(null); // Set failedPaymentUrl to null

    var response = dokuSnap.doPayment(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "DIRECT_DEBIT_BRI_SNAP", "authCode", false);
    assertEquals("Invalid Mandatory Field additionalInfo.failedPaymentUrl", response.getResponseMessage());
}

@Test
void cardRegistration_ChannelCannotBeNull() {
    var request = getCardRegistrationRequestDto();
    request.getAdditionalInfo().setChannel(null); // Set channel to null

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Mandatory Field additionalInfo.channel", response.getResponseMessage());
}

@Test
void cardRegistration_CustIdMerchantMustNotBeNull() {
    var request = getCardRegistrationRequestDto();
    request.setCustIdMerchant(null); // Set custIdMerchant to null

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Mandatory Field custIdMerchant", response.getResponseMessage());
}

@Test
void cardRegistration_CustIdMerchantMustNotExceedMaxLength() {
    var request = getCardRegistrationRequestDto();
    request.setCustIdMerchant("ThisIsAReallyLongCustIdMerchantThatExceedsTheMaxLength8jskdhfishbdfjsgidfgisjdhfisdjhfisjdfkjsdbfsd"); // Exceeding max length

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Field Format custIdMerchant", response.getResponseMessage());
}

@Test
void cardRegistration_CustIdMerchantMustMatchPattern() {
    var request = getCardRegistrationRequestDto();
    request.setCustIdMerchant("Invalid#MerchantID"); // Invalid format

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Field Format custIdMerchant", response.getResponseMessage());
}

@Test
void cardRegistration_SuccessRegistrationUrlCannotBeNull() {
    var request = getCardRegistrationRequestDto();
    request.getAdditionalInfo().setSuccessRegistrationUrl(null); // Set successRegistrationUrl to null

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Mandatory Field additionalInfo.successRegistrationUrl", response.getResponseMessage());
}


@Test
void cardRegistration_FailedRegistrationUrlCannotBeNull() {
    var request = getCardRegistrationRequestDto();
    request.getAdditionalInfo().setFailedRegistrationUrl(null); // Set failedRegistrationUrl to null

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Mandatory Field additionalInfo.failedRegistrationUrl", response.getResponseMessage());
}

@Test
void cardRegistration_CardDataCannotBeNull() {
    var request = getCardRegistrationRequestDto();
    request.setCardData(null); // Set cardData to null

    var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);
    assertEquals("Invalid Mandatory Field cardData", response.getResponseMessage());
}

@Test
void cardUnregistration_tokenIdCannotBeNull() {
    var request = getCardUnbindingRequestDto();
    request.setTokenId(null); // Set tokenId to null

    var response = dokuSnap.doCardUnbinding(request, PRIVATE_KEY, CLIENT_ID, false);
    assertEquals("Invalid Mandatory Field tokenId", response.getResponseMessage());
}
@Test
void balanceInquiry_ChannelCannotBeNull() {
    var request = getBalanceInquiryRequestDto();
    request.getAdditionalInfo().setChannel(null); // Set channel to null

    var response = dokuSnap.doBalanceInquiry(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);
    assertEquals("Invalid Mandatory Field additionalInfo.channel", response.getResponseMessage());
}

@Test
void checkstatuslanceInquiry_ServiceCodeMust55() {
    var request = getCheckStatusRequestDto();
    request.setServiceCode("22"); // Set channel to null

    var response = dokuSnap.doCheckStatus(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, false);
     assertEquals("serviceCode must be '55'", response.getResponseMessage());
}
    

}