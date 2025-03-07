# DOKU JAVA SDK Documentation

## Introduction
Welcome to the DOKU JAVA SDK! This SDK simplifies access to the DOKU API for your server-side PHP applications, enabling seamless integration with payment and virtual account services.

If your looking for another language  [Node.js](https://github.com/PTNUSASATUINTIARTHA-DOKU/doku-nodejs-library), [Go](https://github.com/PTNUSASATUINTIARTHA-DOKU/doku-golang-library), [Python](https://github.com/PTNUSASATUINTIARTHA-DOKU/doku-python-library), [PHP](https://github.com/PTNUSASATUINTIARTHA-DOKU/doku-php-library)

## Table of Contents
- [DOKU JAVA SDK Documentation](#doku-php-sdk-documentation)
  - [1. Getting Started](#1-getting-started)
  - [2. Usage](#2-usage)
    - [Virtual Account](#virtual-account)
      - [I. Virtual Account (DGPC \& MGPC)](#i-virtual-account-dgpc--mgpc)
      - [II. Virtual Account (DIPC)](#ii-virtual-account-dipc)
      - [III. Check Virtual Account Status](#iii-check-virtual-account-status)
    - [B. Binding / Registration Operations](#b-binding--registration-operations)
      - [I. Account Binding](#i-account-binding)
      - [II. Card Registration](#ii-card-registration)
    - [C. Direct Debit and E-Wallet](#c-direct-debit-and-e-wallet)
      - [I. Request Payment](#i-request-payment)
      - [II. Request Payment Jump APP ](#ii-request-payment-jump-app)
  - [3. Other Operation](#3-other-operation)
    - [Check Transaction Status](#a-check-transaction-status)
    - [Refund](#b-refund)
    - [Balance Inquiry](#c-balance-inquiry)
  - [4. Error Handling and Troubleshooting](#4-error-handling-and-troubleshooting)




## 1. Getting Started

### Requirements
- Java 11 or higher
- Composer installed

### Installation
Get started by installing the library:
### Maven
Put the following dependency to your pom.xml:
```
<!--SDK dependency-->
<dependency>
    <groupId>com.doku.sdk</groupId>
    <artifactId>doku-java-library</artifactId>
    <version>1.0.5-SNAPSHOT</version>
</dependency>
```
### Configuration
Before using the Doku Snap SDK, you need to initialize it with your credentials:
1. **Client ID**, **Secret Key** and **DOKU Public Key**: Retrieve these from the Integration menu in your Doku Dashboard
2. **Private Key** and **Public Key** : Generate your Private Key and Public Key
   
How to generate Merchant privateKey and publicKey:
1. generate private key RSA : openssl genrsa -out private.key 2048
2. set passphrase your private key RSA : openssl pkcs8 -topk8 -inform PEM -outform PEM -in private.key -out pkcs8.key -v1 PBE-SHA1-3DES
3. generate public key RSA : openssl rsa -in private.key -outform PEM -pubout -out public.pem

The encryption model applied to messages involves both asymmetric and symmetric encryption, utilizing a combination of Private Key and Public Key, adhering to the following standards:

  1. Standard Asymmetric Encryption Signature: SHA256withRSA dengan Private Key ( Kpriv ) dan Public Key ( Kpub ) (256 bits)
  2. Standard Symmetric Encryption Signature HMAC_SHA512 (512 bits)
  3. Standard Symmetric Encryption AES-256 dengan client secret sebagai encryption key.

| **Parameter**       | **Description**                                    | **Required** |
|-----------------|----------------------------------------------------|--------------|
| `privateKey`    | The private key for the partner service.           | ✅          |
| `publicKey`     | The public key for the partner service.            | ✅           |
| `dokuPublicKey` | Key that merchants use to verify DOKU request      | ✅           |
| `clientId`      | The client ID associated with the service.         | ✅           |
| `secretKey`     | The secret key for the partner service.            | ✅           |
| `isProduction`  | Set to true for production environment             | ✅           |
| `issuer`        | Optional issuer for advanced configurations.       | ❌           |
| `authCode`      | Optional authorization code for advanced use.      | ❌           |


```java
private final DokuSnap dokuSnap;

private final Boolean isProduction = false;

@Value("${merchant.private-key}") // your private key
private String privateKey;
@Value("${merchant.public-key}") // your public key
private String publicKey;
@Value("${doku.public-key}") // doku public key
private String dokuPublicKey;
@Value("${merchant.client-id}") // your client id
private String clientId;
@Value("${merchant.secret-key}") // your secret key
private String secretKey;
```

## 2. Usage

### Virtual Account
#### I. Virtual Account (DGPC & MGPC)
##### DGPC
- **Description:** A pre-generated virtual account provided by DOKU.
- **Use Case:** Recommended for one-time transactions.
##### MGPC
- **Description:** Merchant generated virtual account.
- **Use Case:** Recommended for top up business model.

Parameters for **createVA** and **updateVA**
<table>
  <thead>
    <tr>
      <th><strong>Parameter</strong></th>
      <th colspan="2"><strong>Description</strong></th>
      <th><strong>Data Type</strong></th>
      <th><strong>Required</strong></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>partnerServiceId</code></td>
      <td colspan="2">The unique identifier for the partner service.</td>
      <td>String(20)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>customerNo</code></td>
      <td colspan="2">The customer's identification number.</td>
      <td>String(20)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>virtualAccountNo</code></td>
      <td colspan="2">The virtual account number associated with the customer.</td>
      <td>String(20)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>virtualAccountName</code></td>
      <td colspan="2">The name of the virtual account associated with the customer.</td>
      <td>String(255)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>virtualAccountEmail</code></td>
      <td colspan="2">The email address associated with the virtual account.</td>
      <td>String(255)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td><code>virtualAccountPhone</code></td>
      <td colspan="2">The phone number associated with the virtual account.</td>
      <td>String(9-30)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td><code>trxId</code></td>
      <td colspan="2">Invoice number in Merchants system.</td>
      <td>String(64)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="2"><code>totalAmount</code></td>
      <td colspan="2"><code>value</code>: Transaction Amount (ISO 4217) <br> <small>Example: "11500.00"</small></td>
      <td>String(16.2)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>Currency</code>: Currency <br> <small>Example: "IDR"</small></td>
      <td>String(3)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="4"><code>additionalInfo</code></td>
      <td colspan="2"><code>channel</code>: Channel that will be applied for this VA <br> <small>Example: VIRTUAL_ACCOUNT_BANK_CIMB</small></td>
      <td>String(20)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="3"><code>virtualAccountConfig</code></td>
      <td><code>reusableStatus</code>: Reusable Status For Virtual Account Transaction <br><small>value TRUE or FALSE</small></td>
      <td>Boolean</td>
      <td>❌</td>
    </tr>
    <tr>
      <td><code>minAmount</code>: Minimum Amount can be used only if <code>virtualAccountTrxType</code> is Open Amount (O). <br><small>Example: "10000.00"</small></td>
      <td>String(16.2)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td><code>maxAmount</code>: Maximum Amount can be used only if <code>virtualAccountTrxType</code> is Open Amount (O). <br><small>Example: "5000000.00"</small></td>
      <td>String(16.2)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td><code>virtualAccountTrxType</code></td>
      <td colspan="2">Transaction type for this transaction. C (Closed Amount), O (Open Amount)</td>
      <td>String(1)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>expiredDate</code></td>
      <td colspan="2">Expiration date for Virtual Account. ISO-8601 <br><small>Example: "2023-01-01T10:55:00+07:00"</small></td>
      <td>String</td>
      <td>❌</td>
    </tr>
  </tbody>
</table>


1. **Create Virtual Account**
    - **Function:** `createVa`
    ```java
    import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;

   public CreateVaResponseDto doCreateVa(CreateVaRequestDto createVaRequestDto) {
        return dokuSnap.createVa(createVaRequestDto, privateKey, clientId, secretKey, false);
   }
    ```

2. **Update Virtual Account**
    - **Function:** `updateVa`

    ```java
    import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
    
    public UpdateVaResponseDto doUpdateVa(UpdateVaRequestDto updateVaRequestDto) {
        return dokuSnap.updateVa(updateVaRequestDto, privateKey, clientId, secretKey, false);
    }
    ```

3. **Delete Virtual Account**

    | **Parameter**        | **Description**                                                             | **Data Type**       | **Required** |
    |-----------------------|----------------------------------------------------------------------------|---------------------|--------------|
    | `partnerServiceId`    | The unique identifier for the partner service.                             | String(8)        | ✅           |
    | `customerNo`          | The customer's identification number.                                      | String(20)       | ✅           |
    | `virtualAccountNo`    | The virtual account number associated with the customer.                   | String(20)       | ✅           |
    | `trxId`               | Invoice number in Merchant's system.                                       | String(64)       | ✅           |
    | `additionalInfo`      | `channel`: Channel applied for this VA.<br><small>Example: VIRTUAL_ACCOUNT_BANK_CIMB</small> | String(30)       | ✅    |

    
  - **Function:** `deletePaymentCode`

    ```java
    import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
    
    public DeleteVaResponseDto doDeleteVa(DeleteVaRequestDto deleteVaRequestDto) {
        return dokuSnap.deletePaymentCode(deleteVaRequestDto, privateKey, clientId, secretKey, false);
    }
    ```


#### II. Virtual Account (DIPC)
- **Description:** The VA number is registered on merchant side and DOKU will forward Acquirer inquiry request to merchant side when the customer make payment at the acquirer channel

- **Function:** `directInquiryVa`

    ```java
    import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseBodyDto;
    import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseVirtualAccountDataDto;
    import com.doku.sdk.dokujavalibrary.dto.va.inquiry.request.InquiryRequestDto;
    
       @SneakyThrows
        public InquiryResponseBodyDto doDirectInquiry(InquiryRequestDto inquiryRequestDto) {
        String tokenB2b = inquiryRequestDto.getHeader().getAuthorization();

        if (dokuSnap.validateToken(tokenB2b, publicKey)) {
            //example validate vaNumber to db 
            String vaNumber = inquiryRequestDto.getBody().getVirtualAccountNo();
            var directInquiryVa = directInquiryVaService.findByVaNumber(vaNumber);

            if (directInquiryVa != null) {
                log.debug("va number found {}", directInquiryVa.getVaNumber());
                if (directInquiryVa.getStatusVa().equalsIgnoreCase("pending")) {
                    String timestamp = dateTimeUtils.getTimestamp();
                    directInquiryVaService.updateVaStatus(directInquiryVa, timestamp);
                }
                var inquiryResult = directInquiryService.findByInquiryRequestId(inquiryRequestDto.getBody().getVirtualAccountNo()).orElseThrow(() -> new GeneralException("4002400", "Inquiry not found"));
                var inquiryResultVirtualAccountData = mapper.treeToValue(inquiryResult.getInquiryJsonObject(), InquiryResponseVirtualAccountDataDto.class);


                return InquiryResponseBodyDto.builder()
                        .responseCode("2002400")
                        .responseMessage("Successful")
                        .virtualAccountData(inquiryResultVirtualAccountData)
                        .build();
            }
            return InquiryResponseBodyDto.builder()
                    .responseCode("4042400")
                    .responseMessage("VA number is invalid or not found")
                    .build();

        }

        return InquiryResponseBodyDto.builder()
                .responseCode("4002401")
                .responseMessage("Failed to validate token")
                .build();
    }
    ```

#### III. Check Virtual Account Status
 | **Parameter**        | **Description**                                                             | **Data Type**       | **Required** |
|-----------------------|----------------------------------------------------------------------------|---------------------|--------------|
| `partnerServiceId`    | The unique identifier for the partner service.                             | String(8)        | ✅           |
| `customerNo`          | The customer's identification number.                                      | String(20)       | ✅           |
| `virtualAccountNo`    | The virtual account number associated with the customer.                   | String(20)       | ✅           |
| `inquiryRequestId`    | The customer's identification number.                                      | String(128)       | ❌           |
| `paymentRequestId`    | The virtual account number associated with the customer.                   | String(128)       | ❌           |
| `additionalInfo`      | The virtual account number associated with the customer.                   | String      | ❌           |

  - **Function:** `checkStatusVa`
    ```java
    import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;

    public CheckStatusVaResponseDto doCheckStatusVa(CheckStatusVaRequestDto checkStatusVaRequestDto) {
        return dokuSnap.checkStatusVa(checkStatusVaRequestDto, privateKey, clientId, secretKey, false);
    }
    ```

### B. Binding / Registration Operations
The card registration/account binding process must be completed before payment can be processed. The merchant will send the card registration request from the customer to DOKU.

Each card/account can only registered/bind to one customer on one merchant. Customer needs to verify OTP and input PIN.

| **Services**     | **Binding Type**      | **Details**                        |
|-------------------|-----------------------|-----------------------------------|
| Direct Debit      | Account Binding       | Supports **Allo Bank** and **CIMB** |
| Direct Debit      | Card Registration     | Supports **BRI**                    |
| E-Wallet          | Account Binding       | Supports **OVO**                    |

#### I. Account Binding 
1. **Binding**

<table>
  <thead>
    <tr>
      <th><strong>Parameter</strong></th>
      <th colspan="2"><strong>Description</strong></th>
      <th><strong>Data Type</strong></th>
      <th><strong>Required</strong></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>phoneNo</code></td>
      <td colspan="2">Phone Number Customer. <br> <small>Format: 628238748728423</small> </td>
      <td>String(9-16)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="13"><code>additionalInfo</code></td>
      <td colspan="2"><code>channel</code>: Payment Channel<br></td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>custIdMerchant</code>: Customer id from merchant</td>
      <td>String(64)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>customerName</code>: Customer name from merchant</td>
      <td>String(70)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td colspan="2"><code>email</code>: Customer email from merchant </td>
      <td>String(64)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td colspan="2"><code>idCard</code>: Customer id card from merchant</td>
      <td>String(20)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td colspan="2"><code>country</code>: Customer country </td>
      <td>String</td>
      <td>❌</td>
    </tr>
    <tr>
      <td colspan="2"><code>address</code>: Customer Address</td>
      <td>String(255)</td>
      <td>❌</td>
    </tr>
        <tr>
      <td colspan="2"><code>dateOfBirth</code> </td>
      <td>String(YYYYMMDD)</td>
      <td>❌</td>
    </tr>
    <tr>
      <td colspan="2"><code>successRegistrationUrl</code>: Redirect URL when binding is success </td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>failedRegistrationUrl</code>: Redirect URL when binding is success fail</td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>deviceModel</code>: Device Model customer </td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>osType</code>: Format: ios/android </td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>channelId</code>: Format: app/web </td>
      <td>String</td>
      <td>✅</td>
    </tr>
    </tbody>
  </table> 

  - **Function:** `doAccountBinding`

    ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
    
    public AccountBindingResponseDto doAccountBinding(AccountBindingRequestDto accountBindingRequestDto) {
        return dokuSnap.doAccountBinding(accountBindingRequestDto, privateKey, secretKey, clientId, isProduction, deviceId, ipAddress);
    }
    ```

1. **Unbinding**
     - **Function:** `getTokenB2B2C`
    ```java
    import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2B2CResponseDto;
    
    public TokenB2B2CResponseDto getTokenB2b2c(String authCode) {
        return dokuSnap.getB2b2cToken(authCode, privateKey, clientId, false);
    }
   ```
    - **Function:** `doAccountUnbinding`
    ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request.AccountUnbindingRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response.AccountUnbindingResponseDto;

    public AccountUnbindingResponseDto doAccountUnbinding(AccountUnbindingRequestDto accountUnbindingRequestDto) {
        return dokuSnap.doAccountUnbinding(accountUnbindingRequestDto, privateKey, secretKey, clientId, isProduction, ipAddress);
    }
    ```

#### II. Card Registration
1. **Registration**
    - **Function:** `doCardRegistration`

    ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request.CardRegistrationRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.response.CardRegistrationResponseDto;
    
    public CardRegistrationResponseDto doCardRegistration(CardRegistrationRequestDto cardRegistrationRequestDto) {
        return dokuSnap.doCardRegistration(cardRegistrationRequestDto, privateKey, secretKey, clientId, "DH", isProduction);
    }
    ```

2. **UnRegistration**
    - **Function:** `getTokenB2B2C`
     ```java
    import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2B2CResponseDto;
    
    public TokenB2B2CResponseDto getTokenB2b2c(String authCode) {
        return dokuSnap.getB2b2cToken(authCode, privateKey, clientId, false);
    }
   ```
    - **Function:** `doCardUnbinding`

    ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.request.CardUnbindingRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.response.CardUnbindingResponseDto;

    public CardUnbindingResponseDto doCardUnbinding(CardUnbindingRequestDto cardUnbindingRequestDto){
        return dokuSnap.doCardUnbinding(cardUnbindingRequestDto, privateKey, clientId, isProduction);
    }
    ```

### C. Direct Debit and E-Wallet 

#### I. Request Payment
  Once a customer’s account or card is successfully register/bind, the merchant can send a payment request. This section describes how to send a unified request that works for both Direct Debit and E-Wallet channels.

| **Acquirer**       | **Channel Name**         | 
|-------------------|--------------------------|
| Allo Bank         | DIRECT_DEBIT_ALLO_SNAP   | 
| BRI               | DIRECT_DEBIT_BRI_SNAP    | 
| CIMB              | DIRECT_DEBIT_CIMB_SNAP   |
| OVO               | EMONEY_OVO_SNAP   | 

##### Common parameter
<table>
  <thead>
    <tr>
      <th><strong>Parameter</strong></th>
      <th colspan="2"><strong>Description</strong></th>
      <th><strong>Data Type</strong></th>
      <th><strong>Required</strong></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>partnerReferenceNo</code></td>
      <td colspan="2"> Reference No From Partner <br> <small>Format: 628238748728423</small> </td>
      <td>String(9-16)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="2"><code>amount</code></td>
      <td colspan="2"><code>value</code>: Transaction Amount (ISO 4217) <br> <small>Example: "11500.00"</small></td>
      <td>String(16.2)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>Currency</code>: Currency <br> <small>Example: "IDR"</small></td>
      <td>String(3)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="4"><code>additionalInfo</code> </td>
      <td colspan = "2" ><code>channel</code>: payment channel</td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>remarks</code>:Remarks from Partner</td>
      <td>String(40)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>successPaymentUrl</code>: Redirect Url if payment success</td>
      <td>String</td>
      <td>✅</td>
    </tr>
        <tr>
      <td colspan="2"><code>failedPaymentUrl</code>: Redirect Url if payment fail
      </td>
      <td>String</td>
      <td>✅</td>
    </tr>
    </tbody>
  </table> 

 ##### Allo Bank Specific Parameters

| **Parameter**                        | **Description**                                               | **Required** |
|--------------------------------------|---------------------------------------------------------------|--------------|
| `additionalInfo.remarks`             | Remarks from the partner                                      | ✅           |
| `additionalInfo.lineItems.name`      | Item name (String)                                            | ✅           |
| `additionalInfo.lineItems.price`     | Item price (ISO 4217)                                         | ✅           |
| `additionalInfo.lineItems.quantity`  | Item quantity (Integer)                                      | ✅           |
| `payOptionDetails.payMethod`         | Balance type (options: BALANCE/POINT/PAYLATER)                | ✅           |
| `payOptionDetails.transAmount.value` | Transaction amount                                            | ✅           |
| `payOptionDetails.transAmount.currency` | Currency (ISO 4217, e.g., "IDR")                             | ✅           |


#####  CIMB Specific Parameters

| **Parameter**                        | **Description**                                               | **Required** |
|--------------------------------------|---------------------------------------------------------------|--------------|
| `additionalInfo.remarks`             | Remarks from the partner                                      | ✅           |


#####  OVO Specific Parameters

| **Parameter**                           | **Description**                                                | **Required** |
|------------------------------------------|---------------------------------------------------------------|--------------|
| `feeType`                                | Fee type from partner (values: OUR, BEN, SHA)                  | ❌           |
| `payOptionDetails.payMethod`             | Payment method format: CASH, POINTS                            | ✅           |
| `payOptionDetails.transAmount.value`    | Transaction amount (ISO 4217)                                  | ✅           |
| `payOptionDetails.transAmount.currency` | Currency (ISO 4217, e.g., "IDR")                               | ✅           |
| `payOptionDetails.feeAmount.value`      | Fee amount (if applicable)                                     | ✅           |
| `payOptionDetails.feeAmount.currency`   | Currency for the fee                                          | ✅           |
| `additionalInfo.paymentType`            | Transaction type (values: SALE, RECURRING)                     | ✅           |

  
Here’s how you can use the `doPayment` function for both payment types:
  - **Function:** `doPayment`
    
    ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.response.PaymentResponseDto;

    public PaymentResponseDto doPaymentHostToHost(PaymentRequestDto paymentRequestDto, String authCode) {
        return dokuSnap.doPayment(paymentRequestDto, privateKey, secretKey, clientId, ipAddress, "DH", authCode, isProduction);
    }
      ```

#### II. Request Payment Jump APP
| **Acquirer**       | **Channel Name**        | 
|-------------------|--------------------------|
| DANA              | EMONEY_DANA_SNAP   |  
| ShopeePay         | EMONEY_SHOPEE_PAY_SNAP  |

The following fields are common across **DANA and ShopeePay** requests:
<table>
  <thead>
    <tr>
      <th><strong>Parameter</strong></th>
      <th colspan="2"><strong>Description</strong></th>
      <th><strong>Data Type</strong></th>
      <th><strong>Required</strong></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>partnerReferenceNo</code></td>
      <td colspan="2"> Reference No From Partner <br> <small>Examplae : INV-0001</small> </td>
      <td>String(9-16)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>validUpto</code></td>
      <td colspan = "2" >Expired time payment url </td>
      <td>String</td>
      <td>❌</td>
    </tr>
    <tr>
      <td><code>pointOfInitiation</code></td>
      <td colspan = "2" >Point of initiation from partner,<br> value: app/pc/mweb </td>
      <td>String</td>
      <td>❌</td>
    </tr>
    <tr>
      <td rowspan = "3" > <code>urlParam</code></td>
      <td colspan = "2"><code>url</code>: URL after payment sucess </td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>type</code>: Pay Return<br> <small>always PAY_RETURN </small></td>
      <td>String</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>isDeepLink</code>: Is Merchant use deep link or not<br> <small>Example: "Y/N"</small></td>
      <td>String(1)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td rowspan="2"><code>amount</code></td>
      <td colspan="2"><code>value</code>: Transaction Amount (ISO 4217) <br> <small>Example: "11500.00"</small></td>
      <td>String(16.2)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td colspan="2"><code>Currency</code>: Currency <br> <small>Example: "IDR"</small></td>
      <td>String(3)</td>
      <td>✅</td>
    </tr>
    <tr>
      <td><code>additionalInfo</code> </td>
      <td colspan = "2" ><code>channel</code>: payment channel</td>
      <td>String</td>
      <td>✅</td>
    </tr>
    </tbody>
  </table> 

##### DANA

DANA spesific parameters
<table>
    <thead>
    <tr>
      <th><strong>Parameter</strong></th>
      <th colspan="2"><strong>Description</strong></th>
      <th><strong>Data Type</strong></th>
      <th><strong>Required</strong></th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td rowspan = "2" ><code>additionalInfo</code></td>
      <td colspan = "2" ><code>orderTitle</code>: Order title from merchant</td>
      <td>String</td>
      <td>❌</td>
    </tr>
    <tr>
      <td colspan = "2" ><code>supportDeepLinkCheckoutUrl</code> : Value 'true' for Jumpapp behaviour, 'false' for webview, false by default</td>
      <td>String</td>
      <td>❌</td>
    </tr>
    </tbody>
  </table> 
For Shopeepay and Dana you can use the `doPaymentJumpApp` function for for Jumpapp behaviour

- **Function:** `doPaymentJumpApp`

```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request.PaymentJumpAppRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response.PaymentJumpAppResponseDto;
    
    public PaymentJumpAppResponseDto doPaymentJumpApp(PaymentJumpAppRequestDto paymentJumpAppRequestDto) {
        return dokuSnap.doPaymentJumpApp(paymentJumpAppRequestDto, privateKey, secretKey, clientId, deviceId, ipAddress, isProduction);
    }
```

  
      
## 3. Other Operation

### A. Check Transaction Status

  ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.request.CheckStatusRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.response.CheckStatusResponseDto;
    
    public CheckStatusResponseDto doCheckStatus(CheckStatusRequestDto checkStatusRequestDto){
        return dokuSnap.doCheckStatus(checkStatusRequestDto, privateKey, secretKey, clientId, isProduction);
    }
  ```

### B. Refund

  ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request.RefundRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response.RefundResponseDto;
    
    public RefundResponseDto doRefund(RefundRequestDto refundRequestDto, String authCode){
        return dokuSnap.doRefund(refundRequestDto, privateKey, secretKey, clientId, ipAddress, authCode, isProduction);
    }
  ```

### C. Balance Inquiry

  ```java
    import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.request.BalanceInquiryRequestDto;
    import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.response.BalanceInquiryResponseDto;
    
    public BalanceInquiryResponseDto doBalanceInquiry(BalanceInquiryRequestDto balanceInquiryRequestDto, String authCode) {
        return dokuSnap.doBalanceInquiry(balanceInquiryRequestDto, privateKey, secretKey, clientId, ipAddress, authCode, isProduction);
    }
  ```

## 4. Error Handling and Troubleshooting

The SDK throws exceptions for various error conditions. throw status and body like this:
 ```java
    @PostMapping(value = "/create")
    public ResponseEntity<CreateVaResponseDto> createVa(@RequestBody CreateVaRequestDto createVaRequestDto) {
        var response = dokuSnap.createVa(createVaRequestDto, privateKey, clientId, secretKey, false);

        String httpStatus = response.getResponseCode().substring(0,3);
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(httpStatus));
        return ResponseEntity.status(status).body(response);
    }
 ```

This section provides common errors and solutions:

| Error Code | Description                           | Solution                                     |
|------------|---------------------------------------|----------------------------------------------|
| `4010000`  | Unauthorized                          | Check if Client ID and Secret Key are valid. |
| `4012400`  | Virtual Account Not Found             | Verify the virtual account number provided.  |
| `2002400`  | Successful                            | Transaction completed successfully.          |


