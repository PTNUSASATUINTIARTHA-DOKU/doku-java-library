
# DOKU Java Library
Welcome to the DOKU Java library! This powerful tool simplifies access to the DOKU API for your server-side Java Spring Boot applications.

## Documentation
For detailed information, visit the full [DOKU API Docs](https://developers.doku.com/accept-payment/direct-api/snap).

## Requirements
- Java 11 or higher.

## Installation
Get started by installing the library:

### Maven
Put the following dependency to your `pom.xml`:

```xml
<!--SDK dependency-->
<dependency>
    <groupId>com.doku.sdk</groupId>
    <artifactId>doku-java-library</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### SpringBoot Configuration
If you use Spring Boot, you might want to add these package into your main Apps:

`@SpringBootApplication(scanBasePackages = {"your-project-package","com.doku.sdk.dokujavalibrary"})`


## Usage
This section will guide you through setting up the DOKU Java library, creating payment requests, and handling notifications. Let’s get started!

### 1. Configuration
To configure the library, you'll need your account's Client ID, Secret Key, and Private Key. Here’s how:

1. **Client ID and Secret Key:** Retrieve these from the Integration menu in your [DOKU Dashboard](https://dashboard.doku.com/bo/login).
2. **Private Key:** Generate your Private Key following DOKU’s guide and insert the corresponding Public Key into the same menu.

> Your private key will not be transmitted or shared with DOKU. It remains on your server and is only used to sign the requests you send to DOKU.

```java
private final DokuSnap dokuSnap;

@Value("${merchant.private-key}") // your private key
private String privateKey;
@Value("${merchant.client-id}") // your client id
private String clientId;
@Value("${merchant.secret-key}") // your secret key
private String secretKey;
```

### 2. Payment Flow
This section guides you through the steps to process payments using the DOKU Java library. You'll learn how to create a payment request and call the payment function.
#### a. Virtual Account
DOKU offers three ways to use a virtual account: DOKU-Generated Payment Code (DGPC), Merchant-Generated Payment Code (MGPC), and Direct Inquiry Payment Code (DIPC). You can find the full details [here](https://developers.doku.com/accept-payment/direct-api/snap/integration-guide/virtual-account).

> [!Important!]
>Each transaction can use only one feature at a time, but you can use multiple features across different transactions.

##### Create VA DGPC and MGPC
###### CreateVaRequestDto Model
Create the request object to generate a VA number. Specify the acquirer in the request object. This function is applicable for DGPC and MGPC.

```java
CreateVaRequestDto createVaRequestDto = CreateVaRequestDto.builder()
        .partnerServiceId("    1899")
        .customerNo("20240704001")
        .virtualAccountNo("    189920240704001")
        .virtualAccountName("SDK TEST")
        .virtualAccountEmail("sdk@email.com")
        .virtualAccountPhone("6281288932399")
        .trxId("INV_20240711001")
        .totalAmount(TotalAmountDto.builder()
                .value("10000.00")
                .currency("IDR")
                .build())
        .additionalInfo(AdditionalInfoDto.builder()
                .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                .virtualAccountConfig(VirtualAccountConfigDto.builder()
                        .reusableStatus(false)
                        .build())
                .build())
        .virtualAccountTrxType("C")
        .expiredDate("2024-07-29T09:54:04+07:00")
        .build();
```

###### createVa Function
Call the `createVa` function to request the paycode from DOKU. You’ll receive the paycode and payment instructions to display to your customers. This function is applicable for DGPC and MGPC.

```java
/**
 * CreateVaRequestDto -> object
 * privateKey -> string
 * clientId -> string
 * isProduction -> boolean
 */
dokuSnap.createVa(CreateVaRequestDto, privateKey, clientId, isProduction);
```

##### Create VA DIPC
###### #coming-soon inquiryResponse Function
If you use the DIPC feature, you can generate your own paycode and allow your customers to pay without direct communication with DOKU. After customers initiate the payment via the acquirer's channel, DOKU sends an inquiry request to you for validation. This function is applicable for DIPC.

> [!Important!]
>Before sending the inquiry, DOKU sends a token request. Use the `generateToken` function found in the Handling Payment Notification section.


##### Update VA
###### UpdateVaRequestDto Model
Create the request object to update VA. Specify the acquirer in the request object.

```java
UpdateVaRequestDto updateVaRequestDto = UpdateVaRequestDto.builder()
        .partnerServiceId("    1899")
        .customerNo("000000000650")
        .virtualAccountNo("    1899000000000650")
        .virtualAccountName("SDK TEST")
        .virtualAccountEmail("sdk@email.com")
        .virtualAccountPhone("6281288932399")
        .trxId("INV_20240710001")
        .totalAmount(TotalAmountDto.builder()
                .value("10000.00")
                .currency("IDR")
                .build())
        .additionalInfo(UpdateVaAdditionalInfoDto.builder()
                .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                .virtualAccountConfig(UpdateVaAdditionalInfoDto.UpdateVaVirtualAccountConfigDto.builder()
                        .status("ACTIVE")
                        .build())
                .build())
        .virtualAccountTrxType("C")
        .expiredDate("2024-07-29T09:54:04+07:00")
        .build();
```

###### updateVa Function
Call the `updateVa` function to update VA. It will return the updated VA.

```java
/**
 * UpdateVaRequestDto -> object
 * privateKey -> string
 * clientId -> string
 * secretKey -> string
 * isProduction -> boolean
 */
dokuSnap.updateVa(UpdateVaRequestDto, privateKey, clientId, secretKey, isProduction);
```

##### Delete VA
###### DeleteVaRequestDto Model
Create the request object to delete VA. Specify the acquirer in the request object.

```java
DeleteVaRequestDto deleteVaRequestDto = DeleteVaRequestDto.builder()
        .partnerServiceId("    1899")
        .customerNo("000000000661")
        .virtualAccountNo("    1899000000000661")
        .trxId("INV_20240715001")
        .additionalInfo(DeleteVaRequestAdditionalInfoDto.builder()
                .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                .build())
        .build();
```

###### deletePaymentCode Function
Call the `deletePaymentCode` function to delete VA.

```java
/**
 * DeleteVaRequestDto -> object
 * privateKey -> string
 * clientId -> string
 * secretKey -> string
 * isProduction -> boolean
 */
dokuSnap.deletePaymentCode(DeleteVaRequestDto, privateKey, clientId, secretKey, isProduction);
```

##### Check Status VA
###### CheckStatusVaRequestDto Model
Create the request object to check status of your VA. Specify the acquirer in the request object.

```java
CheckStatusVaRequestDto checkStatusVaRequestDto = CheckStatusVaRequestDto.builder()
        .partnerServiceId("    1899")
        .customerNo("000000000661")
        .virtualAccountNo("    1899000000000661")
        .build();
```

###### checkStatusVa Function
Call the `checkStatusVa` function to check the status of your VA.

```java
/**
 * CheckStatusVaRequestDto -> object
 * privateKey -> string
 * clientId -> string
 * secretKey -> string
 * isProduction -> boolean
 */
dokuSnap.checkStatusVa(CheckStatusRequestDto, privateKey, clientId, secretKey, isProduction);
```


### Handling Payment Notification
After your customers make a payment, you’ll receive a notification from DOKU to update the payment status on your end. DOKU first sends a token request (as with DIPC), then uses that token to send the payment notification.
##### validateAsymmetricSignatureAndGenerateToken function
Generate the response to DOKU, including the required token, by calling this function.

```java
/**
 * signature -> string
 * timestamp -> string
 * privateKey -> string
 * clientId -> string
 */
dokuSnap.validateAsymmetricSignatureAndGenerateToken(signature, timestamp, privateKey, clientId);
```

##### validateTokenAndGenerateNotificationReponse function
Deserialize the raw notification data into a structured object using a Data Transfer Object (DTO). This allows you to update the order status, notify customers, or perform other necessary actions based on the notification details.

```java
/**
 * tokenB2b -> string
 * NotifyPaymentRequestDto -> object
 * publicKey -> string
 */
dokuSnap.validateTokenAndGenerateNotificationResponse(tokenB2b, NotifyPaymentRequestDto, publicKey);
```

##### generateNotificationResponse function
DOKU requires a response to the notification. Use this function to serialize the response data to match DOKU’s format.
You will need to validate the token first and provide the PaymentNotificationRequestBodyDto (you can use the model included in the SDK).

```java
/**
 * isTokenValid -> boolean
 * PaymentNotificationRequestBodyDto -> object
 */
dokuSnap.generateNotificationResponse(isTokenValid, PaymentNotificationRequestBodyDto);
```

### 4. Additional Features
Need to use our functions independently? No problem! Here’s how:
#### - v1 to SNAP converter
If you're one of our earliest users, you might still use our v1 APIs. In order to simplify your re-integration process to DOKU's SNAP API specification, DOKU provides you with a helper tools to directly convert v1 APIs to SNAP APIs specification.

##### a. convertRequestV1
Convert DOKU's inquiry and notification from SNAP format (JSON) to v1 format (XML). Feed the inquiry and notification directly to your app without manually mapping parameters or converting file formats.
This function expects an XML string request and return a SNAP format of the request.

```java
/**
 * header -> HttpServletRequest
 * InquiryRequestBodyDto -> object
 */
dokuSnap.directInquiryRequestMapping(header, InquiryRequestBodyDto);
```

##### b. convertResponseV1
Convert your inquiry response to DOKU from v1 format (XML) to SNAP format (Form data). Our library handles response code mapping, allowing you to directly use the converted response and send it to DOKU.
This function will return the response in form data format.

```java
/**
 * xmlString -> String
 */
dokuSnap.directInquiryResponseMapping(xmlString);
```

#### b. Direct Debit
#### Direct Debit Account Binding
> You need to save the authCode from account binding response to access the other functions
```java
/**
 * AccountBindingRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * isProduction -> boolean
 * deviceId -> String
 * ipAddress -> String
 */
dokuSnap.doAccountBinding(AccountBindingRequestDto, privateKey, clientId, isProduction, deviceId, ipAddress);
```

#### Direct Debit Account Unbinding
```java
/**
 * AccountUnbindingRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * isProduction -> boolean
 * ipAddress -> String
 */
dokuSnap.doAccountUnbinding(AccountUnbindingRequestDto, privateKey, clientId, isProduction, ipAddress);
```

#### Direct Debit Card Registration
> You need to save the authCode from account binding response to access the other functions
```java
/**
 * CardRegistrationRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * channelId -> String
 * isProduction -> boolean
 */
dokuSnap.doCardRegistration(CardRegistrationRequestDto, privateKey, clientId, channelId, isProduction);
```

#### Direct Debit Card Unbinding
```java
/**
 * CardUnbindingRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * isProduction -> boolean
 */
dokuSnap.doCardUnbinding(CardUnbindingRequestDto, privateKey, clientId, isProduction);
```

#### Direct Debit Payment
> You need to save the authCode from account binding/card registration
```java
/**
 * PaymentRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * ipAddress -> String
 * channelId -> String
 * authCode -> String
 * isProduction -> boolean
 */
dokuSnap.doPayment(PaymentRequestDto, privateKey, clientId, channelid, authCode, isProduction);
```

#### Direct Debit Payment Jump App
> This function is only applicable for DANA & Shopee Pay acquirer
```java
/**
 * PaymentJumpAppRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * deviceId -> String
 * ipAddress -> String
 * isProduction -> boolean
 */
dokuSnap.doPaymentJumpApp(PaymentJumpAppRequestDto, privateKey, clientId, deviceId, ipAddress, isProduction);
```

#### Direct Debit Refund
> You need to save the authCode from account binding/card registration
```java
/**
 * RefundRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * ipAddress -> String
 * authCode -> String
 * isProduction -> boolean
 */
dokuSnap.doRefund(RefundRequestDto, privateKey, clientId, ipAddress, authCode, isProduction);
```

#### Direct Debit Balance Inquiry (Check Balance)
> You need to save the authCode from account binding/card registration
```java
/**
 * BalanceInquiryRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * ipAddress -> String
 * authCode -> String
 * isProduction -> boolean
 */
dokuSnap.doBalanceInquiry(BalanceInquiryRequestDto, privateKey, clientId, ipAddress, authCode, isProduction);
```

#### Direct Debit Check Status
```java
/**
 * CheckStatusRequestDto -> object
 * privateKey -> String
 * clientId -> String
 * isProduction -> boolean
 */
dokuSnap.doCheckStatus(CheckStatusRequestDto, privateKey, clientId, isProduction);
```