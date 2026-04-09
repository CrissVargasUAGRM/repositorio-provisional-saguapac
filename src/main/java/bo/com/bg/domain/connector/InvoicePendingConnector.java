package bo.com.bg.domain.connector;

import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;

public interface InvoicePendingConnector {

    InvoicePendingResponseDTO getPendingInvoices(String codigoSocio) throws ProviderException;
}
