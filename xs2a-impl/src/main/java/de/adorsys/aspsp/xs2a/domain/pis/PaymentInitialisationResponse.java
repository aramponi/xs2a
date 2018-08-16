/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.xs2a.domain.pis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.adorsys.aspsp.xs2a.domain.Amount;
import de.adorsys.aspsp.xs2a.domain.Links;
import de.adorsys.aspsp.xs2a.domain.TransactionStatus;
import de.adorsys.psd2.model.AuthenticationObject;
import de.adorsys.psd2.model.TppMessageGeneric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Response Body generated by the Payment Service")
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class PaymentInitialisationResponse {

    @JsonUnwrapped
    @ApiModelProperty(value = "The transaction status is filled with value of the ISO20022 data table", required = true, example = "ACCP")
    private TransactionStatus transactionStatus;

    @ApiModelProperty(value = "Resource identification of the generated payment initiation resource.", required = true, example = "qwer3456tzui7890")
    private String paymentId;

    @ApiModelProperty(value = "Can be used by the ASPSP to transport transaction fees relevant for the underlying payments.")
    private Amount transactionFees;

    @ApiModelProperty(value = "If equals true, the transaction will involve specific transaction cost as shown by the ASPSP in their public price list or as agreed between ASPSP and PSU.", example = "false")
    private boolean transactionFeeIndicator;

    @ApiModelProperty(value = "This data element might be contained, if SCA is required and if the PSU has a choice between different authentication methods")
    private AuthenticationObject[] scaMethods;

    @ApiModelProperty(value = "Text to be displayed to the PSU")
    private String psuMessage;

    @ApiModelProperty(value = "Messages to the TPP on operational issues.")
    private TppMessageGeneric[] tppMessages;

    @ApiModelProperty(value = "Links: a list of hyperlinks to be recognised by the TPP.")
    @JsonProperty("_links")
    private Links links;

    @ApiModelProperty(name = "tppRedirectPreferred", value = "If it equals “true”, the TPP prefers a redirect over an embedded SCA approach.")
    private boolean tppRedirectPreferred;

    @JsonIgnore
    private String iban;

    @JsonIgnore
    private String pisConsentId;
}

