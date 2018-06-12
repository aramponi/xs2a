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

package de.adorsys.aspsp.xs2a.web;

import de.adorsys.aspsp.xs2a.service.AspspProfileService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "aspsp-profile")
@Api(value = "Aspsp profile", tags = "Aspsp profile", description = "Provides access to aspsp profile")
public class AspspProfileController {
    private final AspspProfileService aspspProfileService;

    @GetMapping(path = "/frequency-per-day")
    @ApiOperation(value = "Reads frequency per day", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = String.class),
        @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<Integer> getFrequencyPerDay() {
        return new ResponseEntity<>(aspspProfileService.getFrequencyPerDay(), HttpStatus.OK);
    }

    @GetMapping(path = "/combined-service-indicator")
    @ApiOperation(value = "Reads combined service indicator value", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = String.class),
        @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<Boolean> getCombinedServiceIndicator() {
        return new ResponseEntity<>(aspspProfileService.isCombinedServiceIndicator(), HttpStatus.OK);
    }

    @GetMapping(path = "/available-payment-products")
    @ApiOperation(value = "Reads list of available payment products", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = String.class),
        @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<List<String>> getAvailablePaymentProducts() {
        return new ResponseEntity<>(aspspProfileService.getAvailablePaymentProducts(), HttpStatus.OK);
    }

    @GetMapping(path = "/available-payment-types")
    @ApiOperation(value = "Reads list of available payment types ", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = String.class),
        @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<List<String>> getAvailablePaymentTypes() {
        return new ResponseEntity<>(aspspProfileService.getAvailablePaymentTypes(), HttpStatus.OK);
    }

    @GetMapping(path = "/sca-approach")
    @ApiOperation(value = "Reads sca approach value", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = String.class),
        @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<String> getScaApproach() {
        return new ResponseEntity<>(aspspProfileService.getScaApproach(), HttpStatus.OK);
    }
}