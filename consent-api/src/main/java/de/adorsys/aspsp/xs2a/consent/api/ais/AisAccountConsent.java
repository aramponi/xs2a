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

package de.adorsys.aspsp.xs2a.consent.api.ais;

import de.adorsys.aspsp.xs2a.consent.api.AisConsentRequestType;
import de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus;
import lombok.Value;

import java.time.LocalDate;

@Value
public class AisAccountConsent {
    private String id;
    private AisAccountAccess access;
    private boolean recurringIndicator;
    private LocalDate validUntil;
    private int frequencyPerDay;
    private LocalDate lastActionDate;
    private CmsConsentStatus consentStatus;
    private boolean withBalance;
    private boolean tppRedirectPreferred;
    private byte[] aspspConsentData;
    private AisConsentRequestType aisConsentRequestType;
    private String psuId;
    private String tppId;
}
