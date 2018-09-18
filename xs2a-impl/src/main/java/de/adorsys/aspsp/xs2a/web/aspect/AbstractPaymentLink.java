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

package de.adorsys.aspsp.xs2a.web.aspect;

import de.adorsys.aspsp.xs2a.domain.Links;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.aspsp.ScaApproach;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentType;

import java.util.Base64;
import java.util.EnumSet;
import java.util.List;

import static de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus.RJCT;
import static de.adorsys.aspsp.xs2a.domain.consent.Xs2aAuthorisationStartType.EXPLICIT;
import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.PERIODIC;
import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.SINGLE;

public abstract class AbstractPaymentLink<T> extends AbstractLinkAspect<T> {
    @SuppressWarnings("unchecked")
    protected ResponseObject<?> enrichLink(ResponseObject<?> result, PaymentType paymentType) {
        Object body = result.getBody();
        if (EnumSet.of(SINGLE, PERIODIC).contains(paymentType)) {
            doEnrichLink(paymentType, (PaymentInitialisationResponse) body);
        } else {
            ((List<PaymentInitialisationResponse>) body)
                .forEach(r -> doEnrichLink(paymentType, r));
        }
        return result;
    }

    private void doEnrichLink(PaymentType paymentType, PaymentInitialisationResponse body) {
        body.setLinks(buildPaymentLinks(body, paymentType.getValue()));
    }

    private Links buildPaymentLinks(PaymentInitialisationResponse body, String paymentService) {
        if (RJCT == body.getTransactionStatus()) {
            return null;
        }
        String encodedPaymentId = Base64.getEncoder()
                                      .encodeToString(body.getPaymentId().getBytes());
        Links links = new Links();
        links.setSelf(buildPath("/v1/{paymentService}/{paymentId}", paymentService, encodedPaymentId));
        links.setStatus(buildPath("/v1/{paymentService}/{paymentId}/status", paymentService, encodedPaymentId));
        if (ScaApproach.EMBEDDED == aspspProfileService.getScaApproach()) {
            return addEmbeddedRelatedLinks(links, paymentService, encodedPaymentId, body.getAuthorizationId());
        }
        if (ScaApproach.REDIRECT == aspspProfileService.getScaApproach()) {
            links.setScaRedirect(aspspProfileService.getPisRedirectUrlToAspsp() + body.getPisConsentId() + "/" + encodedPaymentId);
        }
        return links;
    }

    private Links addEmbeddedRelatedLinks(Links links, String paymentService, String paymentId, String authorisationId) {
        if (EXPLICIT == aspspProfileService.getAuthorisationStartType()) {
            links.setStartAuthorisation(buildPath("/v1/{payment-service}/{paymentId}/authorisations", paymentService, paymentId));
        } else {
            links.setScaStatus(
                buildPath("/v1/{paymentService}/{paymentId}/authorisations/{authorisationId}", paymentService, paymentId, authorisationId));
            links.setStartAuthorisationWithPsuAuthentication(
                buildPath("/v1/{paymentService}/{paymentId}/authorisations/{authorizationId}", paymentService, paymentId, authorisationId));
        }

        return links;
    }
}


