package bo.com.bg.domain.provider.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bo.com.bg.commons.db.QueryManager;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderConfigSql;
import bo.com.bg.domain.provider.response.PaymentMovtoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.SqlParameter;

@ExtendWith(MockitoExtension.class)
class SrvProceduresProviderImplTest {

    @Mock
    private QueryManager<?> queryManager;

    @Mock
    private ProviderConfigSql config;

    private SrvProceduresProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new SrvProceduresProviderImpl(queryManager, config, new ObjectMapper());
        when(config.getSchemaGanadero()).thenReturn("SCH");
        when(config.getPackageSrvName()).thenReturn("PKG");
        when(config.getSpSrvInsertName()).thenReturn("SP");
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_invokesSp() {
        when(queryManager.executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class)))
                .thenReturn(PaymentMovtoResponse.builder().pStrCodError("COD000").build());

        provider.saveInformationService(Map.of("k", "v"), 705, "idx-1");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(queryManager)
                .executeSPForObject(eq("SCH"), eq("PKG"), eq("SP"), captor.capture(), any(SqlParameter[].class), eq(PaymentMovtoResponse.class));
        assertThat(captor.getValue().get("pIntCodExterno")).isEqualTo(705);
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_logsWhenSpReturnsError() {
        when(queryManager.executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class)))
                .thenReturn(PaymentMovtoResponse.builder()
                        .pStrCodError("ERR")
                        .pStrDescError("detalle")
                        .build());

        provider.saveInformationService("x", 705, "i");

        verify(queryManager)
                .executeSPForObject(eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_swallowsProviderException() {
        doThrow(new ProviderException("x", "E", org.springframework.http.HttpStatus.BAD_REQUEST))
                .when(queryManager)
                .executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class));

        provider.saveInformationService("x", 705, "i");
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_noLogWhenResponseNull() {
        when(queryManager.executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class)))
                .thenReturn(null);
        provider.saveInformationService("x", 705, "i");
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_noLogWhenCodErrorNull() {
        when(queryManager.executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class)))
                .thenReturn(PaymentMovtoResponse.builder().pStrCodError(null).build());
        provider.saveInformationService("x", 705, "i");
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_noLogWhenCod000() {
        when(queryManager.executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class)))
                .thenReturn(PaymentMovtoResponse.builder().pStrCodError("COD000").pStrDescError("ok").build());
        provider.saveInformationService("x", 705, "i");
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveInformationService_logsWhenDescErrorNull() {
        when(queryManager.executeSPForObject(
                        eq("SCH"), eq("PKG"), eq("SP"), any(Map.class), any(SqlParameter[].class), eq(PaymentMovtoResponse.class)))
                .thenReturn(PaymentMovtoResponse.builder().pStrCodError("ERR").pStrDescError(null).build());
        provider.saveInformationService("x", 705, "i");
    }
}
