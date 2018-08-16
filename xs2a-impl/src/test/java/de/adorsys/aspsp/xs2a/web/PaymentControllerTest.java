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

import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import de.adorsys.aspsp.xs2a.service.PaymentService;
import de.adorsys.aspsp.xs2a.service.mapper.ResponseMapper;
import de.adorsys.aspsp.xs2a.web12.PaymentController;
import de.adorsys.psd2.model.TppMessageCategory;
import de.adorsys.psd2.model.TppMessageGENERICRESOURCEUNKNOWN404403400;
import de.adorsys.psd2.model.TppMessageGeneric;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {

    private static final String CORRECT_PAYMENT_ID = "33333-444444-55555-55555";
    private static final String WRONG_PAYMENT_ID = "wrong_payment_id";
    private static final String PAYMENT_PRODUCT = "33333-444444-55555-55555";

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;
    @Mock
    private ResponseMapper responseMapper;

    @Before
    public void setUp() {
        when(paymentService.getPaymentById(SINGLE, PAYMENT_PRODUCT, CORRECT_PAYMENT_ID))
            .thenReturn(ResponseObject.builder().body(getPayment()).build());
        when(paymentService.getPaymentById(SINGLE, PAYMENT_PRODUCT, WRONG_PAYMENT_ID))
            .thenReturn(ResponseObject.builder().fail(Arrays.asList(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICRESOURCEUNKNOWN404403400.CodeEnum.UNKNOWN))
            ).build());

}

    @Test
    public void getPaymentById() {
        when(responseMapper.ok(any())).thenReturn(new ResponseEntity<>(getPayment(), OK));
        //When
        ResponseEntity response = paymentController.getPaymentInformation(SINGLE.getValue(), CORRECT_PAYMENT_ID,
            null, null, null, null, null, null, null,
            null, null, null, null, null, null, null);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualToComparingFieldByField(getPayment());
    }

    @Test
    public void getPaymentById_Failure() {
        when(responseMapper.ok(any()))
            .thenReturn(new ResponseEntity<>(Arrays.asList(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICRESOURCEUNKNOWN404403400.CodeEnum.UNKNOWN)), HttpStatus.FORBIDDEN));

        //When
        ResponseEntity response = paymentController.getPaymentInformation(SINGLE.getValue(), WRONG_PAYMENT_ID,
            null, null, null, null, null, null, null,
            null, null, null, null, null, null, null);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private Object getPayment() {
        SinglePayments payment = new SinglePayments();
        payment.setEndToEndIdentification(CORRECT_PAYMENT_ID);

        return payment;
    }
}
