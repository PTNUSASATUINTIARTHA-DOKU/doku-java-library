package com.doku.sdk.dokujavalibrary.validation.custom;

import com.doku.sdk.dokujavalibrary.validation.annotation.VirtualAccountNo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class VirtualAccountNoValidator implements ConstraintValidator<VirtualAccountNo, Object> {

    private static final String CUSTOMER_NO_IS_NULL = "customerNo is null";
    private static final String PARTNER_SERVICE_ID_IS_NULL = "partnerServiceId is null";
    private static final String VIRTUAL_ACCOUNT_NO_IS_NULL = "virtualAccountNo is null";

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        String customerNo = getCustomerNoFromObject(object);
        String partnerServiceId = getPartnerServiceIdFromObject(object);
        String virtualAccountNo = getVirtualAccountNoFromObject(object);

        if (!StringUtils.isEmpty(customerNo) && !StringUtils.isEmpty(partnerServiceId)) {
            String target = partnerServiceId + customerNo;
            if (!target.matches(virtualAccountNo)) {
                valid = false;
            }
        }

        return valid;
    }

    private String getCustomerNoFromObject(Object object) {
        String customerNo = "";
        try {
            customerNo = BeanUtils.getProperty(object, "customerId");
        } catch (Exception e) {
            log.warn(CUSTOMER_NO_IS_NULL);
        }
        return customerNo;
    }

    private String getPartnerServiceIdFromObject(Object object) {
        String partnerServiceId = "";
        try {
            partnerServiceId = BeanUtils.getProperty(object, "partnerServiceId");
        } catch (Exception e) {
            log.warn(PARTNER_SERVICE_ID_IS_NULL);
        }
        return partnerServiceId;
    }

    private String getVirtualAccountNoFromObject(Object object) {
        String virtualAccountNo = "";
        try {
            virtualAccountNo = BeanUtils.getProperty(object, "virtualAccountNo");
        } catch (Exception e) {
            log.warn(VIRTUAL_ACCOUNT_NO_IS_NULL);
        }
        return virtualAccountNo;
    }
}
