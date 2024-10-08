package com.doku.sdk.dokujavalibrary.module;

import com.doku.sdk.dokujavalibrary.controller.DirectDebitController;
import com.doku.sdk.dokujavalibrary.controller.NotificationController;
import com.doku.sdk.dokujavalibrary.controller.TokenController;
import com.doku.sdk.dokujavalibrary.controller.VaController;
import com.doku.sdk.dokujavalibrary.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        assertEquals("4002702", response.getResponseCode());
    }

    @Test
    void createVa_PartnerIdLengthIsNot8Digits() {
        var request = getCreateVaRequestDto();
        request.setPartnerServiceId("123456789");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_PartnerIdLengthIsNotNumerical() {
        var request = getCreateVaRequestDto();
        request.setPartnerServiceId("1234567z");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_CustomerNoIsMoreThan20() {
        var request = getCreateVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_CustomerNoIsNotNumerical() {
        var request = getCreateVaRequestDto();
        request.setCustomerNo("123456789z");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNoIsNull() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002702", response.getResponseCode());
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

        assertEquals("4002702", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNameIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountName("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountNameIsMoreThan255() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountName("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountEmailIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountEmail("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountEmailIsMoreThan255() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountEmail("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountEmailIsInvalid() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountEmail("sdk@emailcom");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountPhoneIsLessThan9() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountPhone("12345678");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountPhoneIsMoreThan30() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountPhone("1234567890123456789012345678901");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_TrxIdIsNull() {
        var request = getCreateVaRequestDto();
        request.setTrxId(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002702", response.getResponseCode());
    }

    @Test
    void createVa_TrxIdIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.setTrxId("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_TrxIdIsMoreThan64() {
        var request = getCreateVaRequestDto();
        request.setTrxId("FcGcsrqYNNQotmv7b2dSFdVbUmiexl0s1wE7H23gpXsFzcXUHXXnRLBUuREMuWxVx");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_ValueIsNull() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setValue(null);
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002702", response.getResponseCode());
    }

    @Test
    void createVa_ValueIsLessThan4() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setValue("100");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_ValueIsMoreThan19() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setValue("12345678901234567890");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_CurrencyIsNot3Characters() {
        var request = getCreateVaRequestDto();
        request.getTotalAmount().setCurrency("ID");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
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

        assertEquals("4002702", response.getResponseCode());
    }

    @Test
    void createVa_ChannelIsLessThan1() {
        var request = getCreateVaRequestDto();
        request.getAdditionalInfo().setChannel("");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
    }

    @Test
    void createVa_ChannelIsMoreThan30() {
        var request = getCreateVaRequestDto();
        request.getAdditionalInfo().setChannel("VIRTUAL_ACCOUNT_BANK_MANDIRI_TEST");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
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

        assertEquals("4002702", response.getResponseCode());
    }

    @Test
    void createVa_VirtualAccountTrxIsNot1Digit() {
        var request = getCreateVaRequestDto();
        request.setVirtualAccountTrxType("CC");
        var response = dokuSnap.createVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002701", response.getResponseCode());
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

        assertEquals("4002701", response.getResponseCode());
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

        assertEquals("4002802", response.getResponseCode());
    }

    @Test
    void updateVa_PartnerServiceIdIsNot8Digits() {
        var request = getUpdateVaRequestDto();
        request.setPartnerServiceId("123456789");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_PartnerServiceIdIsNotNumerical() {
        var request = getUpdateVaRequestDto();
        request.setPartnerServiceId("1234567z");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_CustomerNoIsMoreThan20() {
        var request = getUpdateVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_CustomerNoIsNotNumerical() {
        var request = getUpdateVaRequestDto();
        request.setCustomerNo("123456789z");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountNoIsNull() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002802", response.getResponseCode());
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

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountEmailIsMoreThan255() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountEmail("ImCyDjTlTqJu9Rrq1uSuKxNNqcNdcD8EuXigmUMZsge3fvkSOyZ8FwMfyDGeOXxaDENzXzHrnXTfHIqXaKLz5Uq7zaGkjNL0DiTRn7vnBEigFFkJlhftfqiT2ml82pYI1ZUmuuR3N1zaAQNYZvg3asANmoDVGmJYnMdGTyWtD3PPb2t8Nwm57Qd1BfSZIiC7A4cGFSyzYZNp2ObxP4zUeMoa0TPV2WbnLKJ761qP594vMXt9Om4pzdcwK3aAWHQd@email.com");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountEmailIsInvalidFormat() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountEmail("sdk@emailcom");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountPhoneIsLessThan9() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountPhone("12345678");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountPhoneIsMoreThan30() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountPhone("1234567890123456789012345678901");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_TrxIdIsNull() {
        var request = getUpdateVaRequestDto();
        request.setTrxId(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002802", response.getResponseCode());
    }

    @Test
    void updateVa_TrxIdIsLessThan1() {
        var request = getUpdateVaRequestDto();
        request.setTrxId("");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_TrxIdIsMoreThan64() {
        var request = getUpdateVaRequestDto();
        request.setTrxId("PXXT0awNv2lE3Jq6dYT1Vc4cNcdEMxbrtxDtAIzFAzieO7K9kRL2Wpenh6mBxu1Sn");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_ValueIsNull() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setValue(null);
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002802", response.getResponseCode());
    }

    @Test
    void updateVa_ValueIsLessThan4() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setValue("100");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_ValueIsMoreThan19() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setValue("12345678901234567890");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_CurrencyIsNot3Characters() {
        var request = getUpdateVaRequestDto();
        request.getTotalAmount().setCurrency("ID");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
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

        assertEquals("4002802", response.getResponseCode());
    }

    @Test
    void updateVa_ChannelIsLessThan1() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().setChannel("");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_ChannelIsMoreThan30() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().setChannel("VIRTUAL_ACCOUNT_BANK_MANDIRI_TEST");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
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

        assertEquals("4002802", response.getResponseCode());
    }

    @Test
    void updateVa_StatusIsLessThan1() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().getVirtualAccountConfig().setStatus("");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
    }

    @Test
    void updateVa_StatusIsMoreThan20() {
        var request = getUpdateVaRequestDto();
        request.getAdditionalInfo().getVirtualAccountConfig().setStatus("9KtgA3YWn1aDEEGE1gWZp");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
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

        assertEquals("4002802", response.getResponseCode());
    }

    @Test
    void updateVa_VirtualAccountTrxTypeIsNot1Digit() {
        var request = getUpdateVaRequestDto();
        request.setVirtualAccountTrxType("12");
        var response = dokuSnap.updateVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002801", response.getResponseCode());
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

        assertEquals("4002801", response.getResponseCode());
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

        assertEquals("4003102", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_PartnerServiceIdIsNot8Digits() {
        var request = getDeleteVaRequestDto();
        request.setPartnerServiceId("123456789");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_CustomerNoIsMoreThan20() {
        var request = getDeleteVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_CustomerNoIsNotNumeric() {
        var request = getDeleteVaRequestDto();
        request.setCustomerNo("1234567z");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_VirtualAccountNoIsNull() {
        var request = getDeleteVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003102", response.getResponseCode());
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

        assertEquals("4003102", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_TrxIdIsLessThan1() {
        var request = getDeleteVaRequestDto();
        request.setTrxId("");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_TrxIdIsMoreThan128() {
        var request = getDeleteVaRequestDto();
        request.setTrxId("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_ChannelIsNull() {
        var request = getDeleteVaRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003102", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_ChannelIsLessThan1() {
        var request = getDeleteVaRequestDto();
        request.getAdditionalInfo().setChannel("");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
    }

    @Test
    void deletePaymentCode_ChannelIsMoreThan128() {
        var request = getDeleteVaRequestDto();
        request.getAdditionalInfo().setChannel("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.deletePaymentCode(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4003101", response.getResponseCode());
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

        assertEquals("4002602", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PartnerServiceIdIsNot8Digits() {
        var request = getCheckStatusVaRequestDto();
        request.setPartnerServiceId("1234567");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002601", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PartnerServiceIdIsNotNumeric() {
        var request = getCheckStatusVaRequestDto();
        request.setPartnerServiceId("1234567z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002601", response.getResponseCode());
    }

    @Test
    void checkStatusVa_CustomerNoIsMoreThan20() {
        var request = getCheckStatusVaRequestDto();
        request.setCustomerNo("123456789012345678901");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002601", response.getResponseCode());
    }

    @Test
    void checkStatusVa_CustomerNoIsNotNumeric() {
        var request = getCheckStatusVaRequestDto();
        request.setCustomerNo("1234567z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002601", response.getResponseCode());
    }

    @Test
    void checkStatusVa_VirtualAccountNoIsNull() {
        var request = getCheckStatusVaRequestDto();
        request.setVirtualAccountNo(null);
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002602", response.getResponseCode());
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

        assertEquals("4002601", response.getResponseCode());
    }

    @Test
    void checkStatusVa_PaymentRequestIdIsMoreThan128() {
        var request = getCheckStatusVaRequestDto();
        request.setPaymentRequestId("CIwxu2v0XgURbX2RYclSfsw4N6fd29YIgvgv1LJpkmSPItG7jrC8ARlKyRhfkgiVnSJvKWRBAu8u0wPyGg0N8mWA8vcSCEvcYsVWut7NNctBkNLT6Le2rBRiEMchWfv4z");
        var response = dokuSnap.checkStatusVa(request, PRIVATE_KEY, CLIENT_ID, SECRET_KEY, false);

        assertEquals("4002601", response.getResponseCode());
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

        assertEquals("4000702", response.getResponseCode());
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

        assertEquals("4000902", response.getResponseCode());
    }

    @Test
    void directDebitCardRegistration_Success() {
        when(directDebitController.doCardRegistration(any(), any(), any(), any(), any(), any())).thenReturn(getCardRegistrationResponseDto());
        var response = dokuSnap.doCardRegistration(getCardRegistrationRequestDto(), PRIVATE_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);

        assertEquals("2000100", response.getResponseCode());
    }

    @Test
    void directDebitCardRegistration_Failed() {
        var request = getCardRegistrationRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doCardRegistration(request, PRIVATE_KEY, CLIENT_ID, "DIRECT_DEBIT_BRI_SNAP", false);

        assertEquals("4000102", response.getResponseCode());
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

        assertEquals("4000502", response.getResponseCode());
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

        assertEquals("4005402", response.getResponseCode());
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

        assertEquals("4005402", response.getResponseCode());
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

        assertEquals("4001102", response.getResponseCode());
    }

    @Test
    void directDebitRefund_Success() {
        when(directDebitController.doRefund(any(), any(), any(), any(), any(), any(), any())).thenReturn(getRefundResponseDto());
        var response = dokuSnap.doRefund(getRefundRequestDto(), PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);

        assertEquals("2005800", response.getResponseCode());
    }

    @Test
    void directDebitRefund_Failed() {
        var request = getRefundRequestDto();
        request.getAdditionalInfo().setChannel(null);
        var response = dokuSnap.doRefund(request, PRIVATE_KEY, SECRET_KEY, CLIENT_ID, IP_ADDRESS, "EMONEY_OVO_SNAP", false);

        assertEquals("4005802", response.getResponseCode());
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

        assertEquals("4005502", response.getResponseCode());
    }
}