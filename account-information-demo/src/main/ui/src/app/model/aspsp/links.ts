/**
 * XS2A REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0
 * Contact: fpo@adorsys.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


/**
 * Links 
 */
export interface Links {
    /**
     * download: link to a resource, where the transaction report might be downloaded when is requested which has a huge size
     */
    download?: string;
    /**
     * Navigation link for paginated account reports.
     */
    first?: string;
    /**
     * Navigation link for paginated account reports.
     */
    last?: string;
    /**
     * Navigation link for paginated account reports.
     */
    next?: string;
    /**
     * Navigation link for paginated account reports.
     */
    previous?: string;
    /**
     * The link refers to a JSON document specifying the OAuth details of the ASPSP’s authorisation server.
     */
    scaOAuth?: string;
    /**
     * The link to an ASPSP site where SCA is performed within the Redirect SCA approach.
     */
    scaRedirect?: string;
    /**
     * This is a link to a resource, where the TPP can select the applicable second factor authentication methods for the PSU, if there were several available authentication methods.
     */
    selectAuthenticationMethod?: string;
    /**
     * Self: The link to the payment initiation resource created by the request itself. This link can be used later to retrieve the transaction status of the payment initiation.
     */
    self?: string;
    /**
     * Link for check the status of a transaction
     */
    status?: string;
    /**
     * The link to the payment initiation or account information resource, which needs to be updated by the proprietary data.
     */
    updateProprietaryData?: string;
    /**
     * The link to the payment initiation or account information resource, which needs to be updated by a PSU password and eventually the PSU identification if not delivered yet.
     */
    updatePsuAuthentication?: string;
    /**
     * The link to the payment initiation or account information resource, which needs to be updated by the PSU identification if not delivered yet.
     */
    updatePsuIdentification?: string;
    /**
     * account link
     */
    viewAccount?: string;
    /**
     * balances: A link to the resource providing the balance of a dedicated account.
     */
    viewBalances?: string;
    /**
     * TransactionsCreditorResponse: A link to the resource providing the transaction history of a dediated amount.
     */
    viewTransactions?: string;
}
