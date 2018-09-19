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

package de.adorsys.aspsp.xs2a.service.authorization.ais;

import de.adorsys.aspsp.xs2a.domain.consent.*;
import de.adorsys.aspsp.xs2a.service.consent.AisConsentService;
import de.adorsys.aspsp.xs2a.service.mapper.consent.Xs2aAisConsentMapper;
import de.adorsys.aspsp.xs2a.spi.domain.SpiResponse;
import de.adorsys.aspsp.xs2a.spi.domain.psu.SpiScaMethod;
import de.adorsys.aspsp.xs2a.spi.service.AccountSpi;
import de.adorsys.psd2.model.ScaStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.adorsys.aspsp.xs2a.domain.consent.ConsentAuthorizationResponseLinkType.*;

@Service
@RequiredArgsConstructor
public class EmbeddedAisAuthorizationService implements AisAuthorizationService {
    private final AccountSpi accountSpi;
    private final AisConsentService aisConsentService;
    private final Xs2aAisConsentMapper aisConsentMapper;

    @Override
    public Optional<CreateConsentAuthorizationResponse> createConsentAuthorization(String psuId, String consentId) {
        return StringUtils.isBlank(psuId)
                   ? createConsentAuthorizationAndGetResponse(ScaStatus.RECEIVED, START_AUTHORISATION_WITH_PSU_IDENTIFICATION, consentId)
                   : createConsentAuthorizationAndGetResponse(ScaStatus.PSUAUTHENTICATED, START_AUTHORISATION_WITH_PSU_AUTHENTICATION, consentId);
    }

    @Override
    public AccountConsentAuthorization getAccountConsentAuthorizationById(String authorizationId, String consentId) {
        return aisConsentService.getAccountConsentAuthorizationById(authorizationId, consentId);
    }

    @Override
    public UpdateConsentPsuDataResponse updateConsentPsuData(UpdateConsentPsuDataReq updatePsuData, AccountConsentAuthorization consentAuthorization) {

        UpdateConsentPsuDataResponse response = new UpdateConsentPsuDataResponse();

        if (checkPsuIdentification(updatePsuData, response)) {
            return response;
        }

        if (checkPsuAuthentication(updatePsuData, response, consentAuthorization)) {
            return response;
        }

        if (checkScaMethod(updatePsuData, response, consentAuthorization)) {
            return response;
        }

        if (checkScaAuthenticationData(updatePsuData, response)) {
            return response;
        }

        aisConsentService.updateConsentAuthorization(aisConsentMapper.mapToSpiUpdateConsentPsuDataReq(response));

        return response;
    }

    private Optional<CreateConsentAuthorizationResponse> createConsentAuthorizationAndGetResponse(ScaStatus scaStatus, ConsentAuthorizationResponseLinkType linkType, String consentId) {
        return aisConsentService.createAisConsentAuthorization(consentId, Xs2aScaStatus.valueOf(scaStatus.name()))
                   .map(authId -> {
                       CreateConsentAuthorizationResponse resp = new CreateConsentAuthorizationResponse();
                       resp.setConsentId(consentId);
                       resp.setAuthorizationId(authId);
                       resp.setScaStatus(scaStatus);
                       resp.setResponseLinkType(linkType);

                       return resp;
                   });
    }

    private boolean checkPsuIdentification(UpdateConsentPsuDataReq updatePsuData, UpdateConsentPsuDataResponse response) {
        if (updatePsuData.isUpdatePsuIdentification()) {
            response.setPsuId(updatePsuData.getPsuId());
            response.setScaStatus(Xs2aScaStatus.PSUIDENTIFIED);
            response.setResponseLinkType(START_AUTHORISATION_WITH_PSU_AUTHENTICATION);
            return true;
        }
        return false;
    }

    private boolean checkPsuAuthentication(UpdateConsentPsuDataReq updatePsuData, UpdateConsentPsuDataResponse response, AccountConsentAuthorization spiAuthorization) {
        if (spiAuthorization.getPassword() == null && updatePsuData.getPassword() != null) {
            response.setPassword(updatePsuData.getPassword());

            SpiResponse<List<SpiScaMethod>> spiResponse = accountSpi.readAvailableScaMethods(spiAuthorization.getPsuId(), spiAuthorization.getPassword());
            if (spiResponse.getPayload().size() > 1) {
                response.setScaStatus(Xs2aScaStatus.PSUAUTHENTICATED);
                response.setResponseLinkType(START_AUTHORISATION_WITH_AUTHENTICATION_METHOD_SELECTION);
                return true;
            } else {
                response.setAuthenticationMethodId(spiResponse.getPayload().get(0).name());
                response.setScaStatus(Xs2aScaStatus.SCAMETHODSELECTED);
                response.setResponseLinkType(START_AUTHORISATION_WITH_TRANSACTION_AUTHORISATION);
                return true;
            }
        }
        return false;
    }

    private boolean checkScaMethod(UpdateConsentPsuDataReq updatePsuData, UpdateConsentPsuDataResponse response, AccountConsentAuthorization spiAuthorization) {
        if (spiAuthorization.getAuthenticationMethodId() == null && updatePsuData.getAuthenticationMethodId() != null) {
            response.setAuthenticationMethodId(updatePsuData.getAuthenticationMethodId());
            response.setScaStatus(Xs2aScaStatus.SCAMETHODSELECTED);
            response.setResponseLinkType(START_AUTHORISATION_WITH_TRANSACTION_AUTHORISATION);
            return true;
        }
        return false;
    }

    private boolean checkScaAuthenticationData(UpdateConsentPsuDataReq updatePsuData, UpdateConsentPsuDataResponse response) {
        if (updatePsuData.getScaAuthenticationData() != null) {
            response.setScaAuthenticationData(updatePsuData.getScaAuthenticationData());
            response.setScaStatus(Xs2aScaStatus.STARTED);
            response.setResponseLinkType(START_AUTHORISATION_WITH_AUTHENTICATION_METHOD_SELECTION);
            return true;
        }
        return false;
    }
}
